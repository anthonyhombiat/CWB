/**
 * 
 */
package yamSS.main.oaei.run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Formatter;
import java.util.Iterator;

import main.YAMOptions;
import yamSS.constraints.ConceptRules;
import yamSS.constraints.PrefixRules;
import yamSS.constraints.PropertyRules;
import yamSS.datatypes.mapping.GMapping;
import yamSS.datatypes.mapping.GMappingScore;
import yamSS.datatypes.mapping.GMappingTable;
import yamSS.datatypes.scenario.Scenario;
import yamSS.engine.IMatcher;
import yamSS.engine.level1.AnnotationLabelsMatcher;
import yamSS.loader.TranslateAllLabels;
import yamSS.loader.alignment.AlignmentParserFactory;
import yamSS.loader.alignment.IAlignmentParser;
import yamSS.loader.ontology.OntoBuffer;
import yamSS.main.oaei.sys.YAMSetting;
import yamSS.selector.IFilter;
import yamSS.selector.SelectNaiveDescending;
import yamSS.selector.SelectWithPriority;
import yamSS.simlib.ext.ASimFunction;
import yamSS.simlib.linguistic.LanguageBased;
import yamSS.simlib.linguistic.atoms.WordMatchingStoilois;
import yamSS.simlib.wn.WNPreScoreTable;
import yamSS.system.Configs;
import yamSS.tools.AlignmentHelper;
import yamSS.tools.Evaluation;
import yamSS.tools.PrintHelper;
import yamSS.tools.Supports;
import yamSS.tools.wordnet.WordNetHelper;

/**
 * @author ngoduyhoa
 *
 */
public class YAM {

	public static boolean DEBUG = true; // default: false
	public static boolean PRINT = true; // default: false

	public static float THRESHOLD = 0.1f; // default: 0.1
	public static boolean MAPPING_EXTRACTION = true;

	public static boolean USING_ML = false;

	boolean initstatus = YAMSetting.init();
	private static YAM instance = null;

	// element matcher
	// private EMatcher ematcher;
	private NewEMatcher ematcher;

	// structure matcher
	private SMatcher smatcher;

	// SIAmatcher
	private SIAMatcher siamatcher;

	// InstanceMatcher
	private InstanceMatcher instanceMatcher;

	private YAM() {
		// this.ematcher = EMatcher.getInstance();

		// if(!USING_ML)
		// {
		// Without ML
		// this.ematcher = NewEMatcher.getInstance(false,
		// true,true,true,YAMOptions.IB_METHOD);
		// }
		// else
		// {
		// with ML and profile
		// this.ematcher = NewEMatcher.getInstance(true, true,true,true,true);

		// with ML and No profile
		// this.ematcher = NewEMatcher.getInstance(true, true,true,false,true);
		//
//		 this.ematcher = NewEMatcher.getInstance(false, false, false, true,
//		 YAMOptions.IB_METHOD);
		//
		// }
		this.ematcher = NewEMatcher.getInstance(
				YAMOptions.ML_METHOD, // Machine Learning (= Machine Learning Method ?)
				false, // SIA
				YAMOptions.IR_METHOD, // Label (= Information Retrieval Method ?)
				false, // Profile
				YAMOptions.IB_METHOD); // Instance (= Instance-Based Method ?)
		this.siamatcher = new SIAMatcher();
		this.smatcher = new SMatcher();
	}

	public static YAM getInstance() {

		if (instance == null) {
			instance = new YAM();
		}

		return instance;
	}

	public GMappingTable<String> predict(OntoBuffer onto1, OntoBuffer onto2) {
		// System.out.print("Preprocessing Ontologies ..................");
		System.out.println("Start loading Source and Target ontologies.....");

		GMappingTable<String> founds = new GMappingTable<String>();

		boolean havingRandomEntities = false;

		try {
			WordNetHelper.getInstance().initializeWN(Configs.WNDIR,
					Configs.WNVER);
			WordNetHelper.getInstance().initializeIC(Configs.WNIC);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * double percentOfHumanEntities1 = onto1.getPercentOfHumanConcepts();
		 * double percentOfHumanEntities2 = onto2.getPercentOfHumanConcepts();
		 * 
		 * if((Math.abs(percentOfHumanEntities1 - percentOfHumanEntities2) >
		 * 0.1)) havingRandomEntities = true;
		 */
		
		// clear all stored exiting pair of word in new scenario
		WNPreScoreTable.getInstance().clear();

		// System.out.println(" : DONE.");

		TranslateAllLabels transTool1 = new TranslateAllLabels(
				onto1.getOntology());
		transTool1.translates();

		TranslateAllLabels transTool2 = new TranslateAllLabels(
				onto2.getOntology());
		transTool2.translates();

		boolean needTranslate = (transTool1.isNeedTranslated() || transTool2
				.isNeedTranslated());

		GMappingTable<String> additions = null;

		if (needTranslate) {
			IMatcher langBased = new AnnotationLabelsMatcher(new LanguageBased(
					new ASimFunction(new WordMatchingStoilois())));

			// IMatcher langBased = new AnnotationLabelsMatcher(new
			// LanguageBased(new ASimFunction(new LinStoilois())));

			additions = langBased.predict(onto1, onto2);

			IFilter extractor = new SelectNaiveDescending(0.7); // default: 0.7

			additions = extractor.select(additions);

			Configs.VERB_ADV_ADJ_SYNONYM = true;

			NewEMatcher.LABEL3_THRESHOLD = 0.75; // default: 0.75
			NewEMatcher.PROFILE_THRESHOLD = 0.7; // default: 0.7
		}

		System.out.println("Start matching at Element level.....");
		GMappingTable<String> efounds = ematcher.predict(onto1, onto2);

		if (needTranslate) {
			efounds = SelectWithPriority.select(efounds, additions);
		}

		// GMappingTable<String> cloneEfounds = GMappingTable.clone(efounds);

		// return efounds;

		if (DEBUG && PRINT) {
			try {
				efounds.printOut(new FileOutputStream(Configs.TMP_DIR
						+ "_EMatcher_"), true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		float threshold = THRESHOLD;

		if (YAMOptions.SP_METHOD) {
			// System.out.print("Similarity Flooding........................");
			System.out.println("Start matching at Structure level.....");

			GMappingTable<String> sfounds = smatcher.predict(onto1, onto2,
					efounds);

			// System.out.println(" : DONE.");

			if (DEBUG && PRINT) {
				try {

					// GMappingTable.sort(sfounds).printOut(true);

					sfounds.printSortedOut(new FileOutputStream(Configs.TMP_DIR
							+ "_SMatcher_sort"), true);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			System.out.println("Start combining and refining mappings.....");

			if (efounds.getSize() > 0)
				threshold = GMappingTable.getMinOfTwoTables(efounds, sfounds);

			if (DEBUG)
				System.out.println("YAM : Predict : minThreshold = "
						+ threshold);

			if (havingRandomEntities && threshold < 0.1) {
				threshold = 0.001f;
			}

			float weight = threshold;

			if (threshold < 0.1) {
				threshold = 0.001f;
			}

			// GMappingTable<String> founds = GMappingTable.weightedAdd(efounds,
			// threshold, sfounds, 1-threshold);
			founds = GMappingTable.weightedAdd(efounds, weight, sfounds,
					1 - weight);

			if (DEBUG && PRINT) {
				try {
					founds.printOut(new FileOutputStream(Configs.TMP_DIR
							+ "_BeforeGreedySelection_"), true);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			/*
			 * if(threshold < 0.1) { threshold = 0.001f; }
			 */
			if (MAPPING_EXTRACTION) {
				founds = SelectWithPriority.select(founds, threshold);
				// founds = (new MaxWeightAssignment(threshold)).select(founds);
			}

			if (!Configs.USING_ML) {
				// Add those mapping from efounds that are not appeared in
				// pcgraph
				Iterator<GMapping<String>> it = efounds.getIterator();
				while (it.hasNext()) {
					GMapping<java.lang.String> mapping = (GMapping<java.lang.String>) it
							.next();

					if (mapping.getType() != Configs.IN_PCGRAPH) {
						float score = ((GMappingScore) mapping).getSimScore();

						if (score >= 0.99)
							founds.addMapping(mapping);
					}
				}
			}

			if (DEBUG && PRINT) {
				try {
					founds.printOut(new FileOutputStream(Configs.TMP_DIR
							+ "_AfterGreedySelection_"), true);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else
			founds.addMappings(efounds);

		if (YAMOptions.SV_METHOD) {
			// System.out.print("Inconsistent Removing......................");

			PropertyRules propRules = new PropertyRules(onto1, onto2, founds);
			founds = propRules.removeInsconsistentByDomainRange();

			if (DEBUG && PRINT) {
				try {
					founds.printOut(new FileOutputStream(Configs.TMP_DIR
							+ "_removeInsconsistentByDomainRange"), true);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!havingRandomEntities && threshold > 0.05) {

				AlcomoSupport alcomo = new AlcomoSupport(onto1.getOntoFN(),
						onto2.getOntoFN(), founds);

				try {
					founds = alcomo.getExactMappings();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (DEBUG && PRINT) {
					try {
						founds.printOut(new FileOutputStream(Configs.TMP_DIR
								+ "_removeInsconsistentByAlcomo"), true);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (!Configs.USING_ML)
					founds = (new SelectNaiveDescending()).select(founds);

				propRules.setInitMaps(founds);
				founds = propRules.addDiscoveredProperty();

				propRules.setInitMaps(founds);
				founds = propRules.addInverseProperty();

				ConceptRules clsRules = new ConceptRules(onto1, onto2, founds);
				founds = clsRules.addByCommonDirectParentChildren();

				if (DEBUG && PRINT) {
					try {
						founds.printOut(new FileOutputStream(Configs.TMP_DIR
								+ "_addingNewMappings"), true);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				propRules.setInitMaps(founds);
				founds = propRules.removeInsconsistentByRestriction();

				propRules.setInitMaps(founds);
				founds = propRules.removeInsconsistentByInverse();

				clsRules.setInitMaps(founds);
				founds = clsRules.removeSubSuperConcepts();

				/*
				 * NeighbourRules neighbourRules = new NeighbourRules(onto1,
				 * onto2, efounds, founds); founds =
				 * neighbourRules.removeNoCommonNeigbour();
				 */

				clsRules.setInitMaps(founds);
				founds = clsRules
						.removeConsistentByPropertyValueInEquivalentAxiom();

				if (DEBUG && PRINT) {
					try {
						founds.printOut(new FileOutputStream(Configs.TMP_DIR
								+ "_removeRestrictionInverseSubSuper"), true);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			PrefixRules prefixRules = new PrefixRules(onto1, onto2, founds);
			founds = prefixRules.removeInsconsistentConcepts();

			if (DEBUG && PRINT) {
				try {
					founds.printOut(new FileOutputStream(Configs.TMP_DIR
							+ "_removeInsconsistentConcepts"), true);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// System.out.println(" : DONE.");

			/*
			 * alcomo.setCandidates(founds);
			 * 
			 * try { founds = alcomo.getExactMappings(); } catch (Exception e1)
			 * { // TODO Auto-generated catch block e1.printStackTrace(); }
			 */
		}

		System.out.println();

		// WordNetHelper.getInstance().uninstall();

		return founds;

	}

	public GMappingTable<String> align(OntoBuffer onto1, OntoBuffer onto2) {
		GMappingTable<String> founds = predict(onto1, onto2);
		/*
		 * if(Configs.SEMATIC_CONSTRAINT) founds =
		 * SimpleSemanticConstraint.select(founds, onto1, onto2);
		 * 
		 * if(MAPPING_EXTRACTION) { IFilter extractor = new
		 * MaxWeightAssignment();
		 * 
		 * founds = extractor.select(founds); }
		 */

		return founds;
	}

	public String align(String filepathSource, String filepathTarget) {
		OntoBuffer onto1 = new OntoBuffer(filepathSource);
		OntoBuffer onto2 = new OntoBuffer(filepathTarget);

		String onto1uri = onto1.getOntologyIRI();
		String onto2uri = onto2.getOntologyIRI();

		GMappingTable<String> founds = align(onto1, onto2);

		return AlignmentHelper.convertGMappingTable2RDFAlignmentString(
				onto1uri, onto2uri, founds);
	}

	public String align(URL filepathSource, URL filepathTarget) {
		OntoBuffer onto1 = new OntoBuffer(filepathSource);
		OntoBuffer onto2 = new OntoBuffer(filepathTarget);

		String onto1uri = onto1.getOntologyIRI();
		String onto2uri = onto2.getOntologyIRI();

		GMappingTable<String> founds = align(onto1, onto2);

		return AlignmentHelper.convertGMappingTable2RDFAlignmentString(
				onto1uri, onto2uri, founds);
	}

	// //////////////////////////////////////////////////////////////////////////////

	public void evaluateModelSingleScenario(String scenarioName, String year) {
		Scenario scenario = Supports.getScenario(scenarioName, year);

		// load ontology into buffer
		OntoBuffer onto1 = new OntoBuffer(scenario.getOntoFN1());
		OntoBuffer onto2 = new OntoBuffer(scenario.getOntoFN2());

		GMappingTable<String> founds = predict(onto1, onto2);
		/*
		 * if(Configs.SEMATIC_CONSTRAINT) founds =
		 * SimpleSemanticConstraint.select(founds, onto1, onto2);
		 * 
		 * if(MAPPING_EXTRACTION) {
		 * 
		 * MaxWeightAssignment.NUMBER_ASSIGNMENT = 1; IFilter extractor = new
		 * MaxWeightAssignment();
		 * 
		 * founds = extractor.select(founds);
		 * 
		 * }
		 */
		/*
		 * PropertyRules propRules = new PropertyRules(onto1, onto2, founds);
		 * 
		 * founds = propRules.removeInsconsistentProperties();
		 * 
		 * //ConceptRules clsRules = new ConceptRules(onto1, onto2, founds);
		 * 
		 * //founds = clsRules.removeInsconsistentConcepts();
		 * 
		 * if(DEBUG) { try { founds.printOut(new
		 * FileOutputStream(Configs.TMP_DIR + "_After_remove"), true); } catch
		 * (FileNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * }
		 */

		// Alignment parser
		IAlignmentParser align = null;

		// evaluate result
		boolean evaluate = scenario.hasAlign();

		if (evaluate) {
			align = AlignmentParserFactory.createParser(scenario.getAlignFN());
			GMappingTable<String> experts = align.getMappings();

			// System.out.println("4. number of experts = " +
			// experts.getSize());

			Evaluation<String> eval = new Evaluation<String>(experts, founds);
			eval.evaluate();

			System.out.println(scenarioName + "\t" + eval.toLine());
		}
	}

	public void evaluateAndPrintModelSingleScenario(String scenarioName,
			String year) {
		Configs.PRINT_SIMPLE = true;

		Scenario scenario = Supports.getScenario(scenarioName, year);

		// load ontology into buffer
		OntoBuffer onto1 = new OntoBuffer(scenario.getOntoFN1());
		OntoBuffer onto2 = new OntoBuffer(scenario.getOntoFN2());

		GMappingTable<String> founds = predict(onto1, onto2);

		// Alignment parser
		IAlignmentParser align = null;

		// evaluate result
		boolean evaluate = scenario.hasAlign();

		if (evaluate) {
			align = AlignmentParserFactory.createParser(scenario.getAlignFN(),
					scenario.getAlignmentType());
			GMappingTable<String> experts = align.getMappings();

			Evaluation<String> eval = new Evaluation<String>(experts, founds);

			String resultFN = Configs.TMP_DIR + scenarioName + "_" + year
					+ "_YAM.txt";

			eval.evaluateAndPrintDetailEvalResults(resultFN);

			System.out.println(scenarioName + "\t\t" + eval.toLine());
		} else {
			try {
				founds.printOut(new FileOutputStream(Configs.TMP_DIR
						+ scenarioName + "_" + year + "_YAM.txt"), true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void evaluateModelMultipleScenarios(String[] scenarioNames,
			String year, String outputFN) {
		String modelName = "";

		modelName = this.getClass().getSimpleName();

		if (DEBUG)
			System.out.println("ModelEvaluation: working with : " + modelName);

		String resultFN = YAMSetting.REPOSITORY + modelName + outputFN + ".txt";

		int[] len = { 30, 10, 10, 10, 10, 10, 10 };

		// String path = Configs.ARFF_STORAGE;

		double averagePrecision = 0;
		double averageRecall = 0;
		double averageFmeasure = 0;

		double sumTruePositive = 0;
		double sumFalsePositive = 0;
		double sumFalseNegative = 0;

		double hmeanPrecision = 0;
		double hmeanRecall = 0;
		double hmeanFmeasure = 0;

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(resultFN));

			for (String scenarioName : scenarioNames) {
				System.out.println();
				System.out.println("YAM : runiing scenario " + scenarioName);
				Scenario scenario = Supports.getScenario(scenarioName, year);

				// load ontology into buffer
				OntoBuffer onto1 = new OntoBuffer(scenario.getOntoFN1());
				OntoBuffer onto2 = new OntoBuffer(scenario.getOntoFN2());

				GMappingTable<String> founds = predict(onto1, onto2);
				/*
				 * if(Configs.SEMATIC_CONSTRAINT) founds =
				 * SimpleSemanticConstraint.select(founds, onto1, onto2);
				 * 
				 * if(MAPPING_EXTRACTION) {
				 * MaxWeightAssignment.NUMBER_ASSIGNMENT = 1; IFilter extractor
				 * = new MaxWeightAssignment();
				 * 
				 * founds = extractor.select(founds); }
				 */
				/*
				 * PropertyRules propRules = new PropertyRules(onto1, onto2,
				 * founds);
				 * 
				 * founds = propRules.removeInsconsistentProperties();
				 * 
				 * //ConceptRules clsRules = new ConceptRules(onto1, onto2,
				 * founds);
				 * 
				 * //founds = clsRules.removeInsconsistentConcepts();
				 * 
				 * if(DEBUG) { try { founds.printOut(new
				 * FileOutputStream(Configs.TMP_DIR + "_After_remove"), true); }
				 * catch (FileNotFoundException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); }
				 * 
				 * }
				 */

				// Alignment parser
				IAlignmentParser align = null;

				// evaluate result
				boolean evaluate = scenario.hasAlign();

				if (evaluate) {
					double[] results = null;
					if (Configs.ALIGNMENT_EVAL) {
						String refalign = scenario.getAlignFN();

						results = AlignmentHelper.evals(founds, refalign);
					} else {
						align = AlignmentParserFactory.createParser(scenario
								.getAlignFN());
						GMappingTable<String> experts = align.getMappings();

						// run classification on testing data
						results = Evaluation.evals(founds, experts);

					}

					Formatter line = PrintHelper.printFormatter(len,
							scenarioName, results[0], results[1], results[2],
							results[3], results[4], results[5]);

					System.out.println("MLModel: " + line.toString());

					averagePrecision += results[0];
					averageRecall += results[1];
					averageFmeasure += results[2];

					sumTruePositive += results[3];
					sumFalsePositive += results[4];
					sumFalseNegative += results[5];

					writer.write(line.toString());
					writer.newLine();
				}
			}

			int numberTests = scenarioNames.length;

			averagePrecision = averagePrecision / numberTests;
			averageRecall = averageRecall / numberTests;
			averageFmeasure = averageFmeasure / numberTests;

			hmeanPrecision = sumTruePositive
					/ (sumTruePositive + sumFalsePositive);
			hmeanRecall = sumTruePositive
					/ (sumTruePositive + sumFalseNegative);
			hmeanFmeasure = 2 * hmeanPrecision * hmeanRecall
					/ (hmeanPrecision + hmeanRecall);

			Formatter line1 = PrintHelper.printFormatter(len, modelName
					+ "-average:", averagePrecision, averageRecall,
					averageFmeasure, sumTruePositive, sumFalsePositive,
					sumFalseNegative);

			writer.write(line1.toString());
			writer.newLine();

			Formatter line2 = PrintHelper.printFormatter(len, modelName
					+ "-Hmean:", hmeanPrecision, hmeanRecall, hmeanFmeasure,
					sumTruePositive, sumFalsePositive, sumFalseNegative);

			writer.write(line2.toString());
			writer.newLine();

			// add 3 empty lines
			writer.newLine();
			writer.newLine();
			writer.newLine();

			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void evaluateAndPrintModelMultipleScenarios(String[] scenarioNames,
			String year) {
		for (String scenarioName : scenarioNames) {
			System.out.println("YAM : evaluate and print : " + scenarioName);
			evaluateAndPrintModelSingleScenario(scenarioName, year);
		}
	}

	public void evaluateAndPrintModelMultipleScenarios(String[] scenarioNames,
			String year, String output) {
		try {
			String modelName = "YAM";

			double averagePrecision = 0;
			double averageRecall = 0;
			double averageFmeasure = 0;

			double sumTruePositive = 0;
			double sumFalsePositive = 0;
			double sumFalseNegative = 0;

			double hmeanPrecision = 0;
			double hmeanRecall = 0;
			double hmeanFmeasure = 0;

			BufferedWriter writer = new BufferedWriter(new FileWriter(output));

			for (String scenarioName : scenarioNames) {
				System.out.println("YAM : task " + scenarioName);
				evaluateAndPrintModelSingleScenario(scenarioName, year);

				System.out
						.println("-----------------------------------------------------------");

				String resultFN = Configs.TMP_DIR + scenarioName + "_" + year
						+ "_" + modelName + ".txt";

				BufferedReader reader = new BufferedReader(new FileReader(
						resultFN));

				writer.write(modelName + " : " + scenarioName);
				writer.newLine();
				writer.newLine();

				// get evaluation from the first line
				String line = reader.readLine();

				if (line != null) {
					writer.write(line);
					writer.newLine();

					String[] results = line.split("\\s+");

					averagePrecision += Double.parseDouble(results[0]);
					averageRecall += Double.parseDouble(results[1]);
					averageFmeasure += Double.parseDouble(results[2]);

					sumTruePositive += Double.parseDouble(results[3]);
					sumFalsePositive += Double.parseDouble(results[4]);
					sumFalseNegative += Double.parseDouble(results[5]);
				}

				while ((line = reader.readLine()) != null) {
					writer.write(line);
					writer.newLine();
				}

				writer.newLine();
				writer.newLine();
				writer.write("--------------------------------------------------------------");
				writer.newLine();
				writer.newLine();

				writer.flush();
			}

			int[] len = { 30, 10, 10, 10, 10, 10, 10 };

			int numberTests = scenarioNames.length;

			averagePrecision = averagePrecision / numberTests;
			averageRecall = averageRecall / numberTests;
			averageFmeasure = averageFmeasure / numberTests;

			hmeanPrecision = sumTruePositive
					/ (sumTruePositive + sumFalsePositive);
			hmeanRecall = sumTruePositive
					/ (sumTruePositive + sumFalseNegative);
			hmeanFmeasure = 2 * hmeanPrecision * hmeanRecall
					/ (hmeanPrecision + hmeanRecall);

			Formatter line1 = PrintHelper.printFormatter(len, modelName
					+ "-average:", averagePrecision, averageRecall,
					averageFmeasure, sumTruePositive, sumFalsePositive,
					sumFalseNegative);

			writer.write(line1.toString());
			writer.newLine();

			Formatter line2 = PrintHelper.printFormatter(len, modelName
					+ "-Hmean:", hmeanPrecision, hmeanRecall, hmeanFmeasure,
					sumTruePositive, sumFalsePositive, sumFalseNegative);

			writer.write(line2.toString());
			writer.newLine();

			// add 3 empty lines
			writer.newLine();
			writer.newLine();
			writer.newLine();

			writer.flush();
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	public URL createTmpAlignmentFromSingleScenario(URL filepathSource,
			URL filepathTarget) {
		String salign = align(filepathSource, filepathTarget);

		try {
			File falign = File.createTempFile("alignment", ".rdf");
			FileWriter fw = new FileWriter(falign);

			fw.write(salign);
			fw.flush();
			fw.close();

			return falign.toURI().toURL();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	// ////////////////////////////////////////////////////////////////////////////////

	public static void testCreateTmpAlignmentFromSingleScenario() {
		String scenarioName = "205";
		String year = "2010";

		Scenario scenario = Supports.getScenario(scenarioName, year);

		try {
			URL filepathSource = (new File(scenario.getOntoFN1())).toURI()
					.toURL();
			URL filepathTarget = (new File(scenario.getOntoFN2())).toURI()
					.toURL();

			System.out.println(filepathSource.toString());
			System.out.println(filepathTarget.toString());

			YAM eMatcher = YAM.getInstance();

			URL url = eMatcher.createTmpAlignmentFromSingleScenario(
					filepathSource, filepathTarget);

			System.out.println(url.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		testCreateTmpAlignmentFromSingleScenario();
	}
}
