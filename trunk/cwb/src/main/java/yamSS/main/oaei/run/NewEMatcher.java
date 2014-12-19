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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Formatter;
import java.util.Iterator;



import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import yamSS.constraints.PropertyRules;
import yamSS.constraints.SimpleSemanticConstraint;
import yamSS.datatypes.mapping.GMapping;
import yamSS.datatypes.mapping.GMappingScore;
import yamSS.datatypes.mapping.GMappingTable;
import yamSS.datatypes.scenario.Scenario;
import yamSS.engine.IMatcher;
import yamSS.engine.level1.FullDescriptionMatcher;
import yamSS.engine.level1.LabelMatcher2;
import yamSS.engine.level1.LabelMatcher3;
import yamSS.engine.level1.StringMatcher;
import yamSS.learner.PrepareRawData;
import yamSS.loader.alignment.AlignmentParserFactory;
import yamSS.loader.alignment.IAlignmentParser;
import yamSS.loader.ontology.OntoBuffer;
import yamSS.main.oaei.sys.YAMSetting;
import yamSS.selector.IFilter;
import yamSS.selector.MaxWeightAssignment;
import yamSS.selector.SelectNaiveDescending;
import yamSS.selector.SelectThreshold;
import yamSS.selector.SelectWithPriority;
import yamSS.simlib.ext.SoundexAndAbbreviation;
import yamSS.simlib.linguistic.Stoilois;
import yamSS.simlib.linguistic.WordApproximate;
import yamSS.simlib.linguistic.atoms.LinStoilois;
import yamSS.simlib.linguistic.factory.QGrams;
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
public class NewEMatcher 
{
	public	static	boolean	initWN	=	false;
	
	public static boolean MAPPING_EXTRACTION	=	false;
	
	public	static	boolean	usingLabelMatcher	=	true;
	public	static	boolean	usingSIAMatcher		=	true;
	public	static	boolean	usingProfileMatcher		=	true;
	public	static	boolean	usingMLModel	=	true;	
	public	static	boolean	usingInstance	=	true;
	
	public	static	double	LABEL3_THRESHOLD	=	0.75; // default: 0.75
	public	static	double	PROFILE_THRESHOLD	=	0.75; // default: 0.75
	
	//boolean	initstatus	=	YAMSetting.init();
	private	static	NewEMatcher		instance	=	null;
	
	private MLModel				mlmatcher;
	
	IMatcher	labelmatcher;
	
	SIAMatcher		siamatcher;
	
	FullDescriptionMatcher	profileMatcher;
	
	private	IdenticalMatcher	idmatcher;
	
	private NewEMatcher() 
	{
		
		if(usingMLModel)
			mlmatcher	=	new MLModel(new J48());
		
		if(usingLabelMatcher)
			labelmatcher	=	new LabelMatcher3(new WordApproximate(), 0.9, false); // default: 0.9
		
		if(usingSIAMatcher)
			siamatcher	=	new SIAMatcher();
		
		if(usingProfileMatcher)
			profileMatcher	=	new FullDescriptionMatcher(null, true);
	}
	
		
	public static NewEMatcher getInstance(boolean mlmodel, boolean siamodel, boolean labelmodel, boolean profilemodel, boolean instancemodel) 
	{
		usingMLModel		=	mlmodel;
		usingSIAMatcher		=	siamodel;
		usingLabelMatcher	=	labelmodel;
		usingProfileMatcher	=	profilemodel;
		usingInstance		=	instancemodel;
		
		if(instance == null)
		{
			instance	=	new NewEMatcher();
		}
		
		return instance;
	}



	public static NewEMatcher getInstance()
	{
		if(instance == null)
		{
			instance	=	new NewEMatcher();
		}
		
		return instance;
	}
	
	public GMappingTable<String> predict(OntoBuffer	onto1, OntoBuffer	onto2)
	{	
		// run with 3 matchers
		//System.out.print("Identical & Synonym matching ..............");
				
		GMappingTable<String> siafounds	=	null;
		
		if(usingSIAMatcher)
		{
			siafounds	=	siamatcher.predict(onto1, onto2);
		}
		
		GMappingTable<String> lbfounds	=	null;
		if(usingLabelMatcher)
		{
			lbfounds	=	labelmatcher.predict(onto1, onto2);
			
			IFilter	extractor	=	new SelectNaiveDescending(LABEL3_THRESHOLD);
			
			lbfounds	=	extractor.select(lbfounds);			
		}		
		
		//System.out.println(" : DONE.");
		
		GMappingTable<String> mlfounds	=	null;
		
		if(usingMLModel)
		{
			//System.out.print("Classification Model.......................");
			
			mlfounds	=	mlmatcher.predict(PrepareRawData.buildFullTable(YAMSetting.defMatchers, onto1, onto2));
				
			//System.out.println(" : DONE.");
					
		}
		
		GMappingTable<String> sfounds	=	null;
		
		if(usingSIAMatcher && usingMLModel)
			sfounds	=	SelectWithPriority.select(siafounds, mlfounds);
		else if(usingSIAMatcher && !usingMLModel)
			sfounds		=	siafounds;
		else if(!usingSIAMatcher && usingMLModel)
			sfounds	=	mlfounds;
		
		GMappingTable<String> pfounds	=	null;
		
		if(usingProfileMatcher)
		{
			//System.out.print("Context Profile Matcher....................");
			pfounds	=	profileMatcher.predict(onto1, onto2);
			
			IFilter	extractor	=	new SelectNaiveDescending(PROFILE_THRESHOLD);
			
			pfounds	=	extractor.select(pfounds);	
			
			//System.out.println(" : DONE.");
		}
				
		GMappingTable<String> exfounds	=	null;
		
		if(usingInstance)
		{
			//System.out.print("Instance-based Matcher.....................");
			
			InstanceMatcher	exmatcher	=	new InstanceMatcher(onto1, onto2, sfounds);
						
			exfounds	=	exmatcher.predict();
			
			//System.out.println(" : DONE.");			
		}
		
				
		//GMappingTable<String> founds	=	SelectWithPriority.select(exfounds,sfounds);
		GMappingTable<String> founds	=	SelectWithPriority.select(sfounds,exfounds);
				
		if(usingLabelMatcher)
			founds	=	SelectWithPriority.select(founds, lbfounds);		
		
		if(usingProfileMatcher)
			founds	=	SelectWithPriority.select(founds, pfounds);			
		
		return founds;
		
	}
	
	public GMappingTable<String> align(OntoBuffer	onto1, OntoBuffer	onto2)
	{	
		GMappingTable<String> founds	=	predict(onto1, onto2);
		
		if(Configs.SEMATIC_CONSTRAINT)
			founds	=	SimpleSemanticConstraint.select(founds, onto1, onto2);
		
		if(MAPPING_EXTRACTION)
		{
			//IFilter	extractor	=	new MaxWeightAssignment();
			IFilter	extractor	=	new SelectThreshold(0.85); // default: 0.85
			
			founds	=	extractor.select(founds);
		}
		
		return founds;
	}
	
	public String align(String filepathSource, String filepathTarget)
	{
		OntoBuffer	onto1	=	new OntoBuffer(filepathSource);
		OntoBuffer	onto2	=	new OntoBuffer(filepathTarget);
		
		String	onto1uri	=	onto1.getOntologyIRI();
		String	onto2uri	=	onto2.getOntologyIRI();
		
		/*
		GMappingTable<String> idfounds	=	idmatcher.predict(onto1, onto2);
		
		GMappingTable<String> mlfounds	=	mlmatcher.predict(PrepareRawData.buildFullTable(YAMSetting.defMatchers, onto1, onto2));
		
		GMappingTable<String> founds	=	SelectWithPriority.select(idfounds, mlfounds);
		
		if(Configs.SEMATIC_CONSTRAINT)
			founds	=	SimpleSemanticConstraint.select(mlfounds, onto1, onto2);
		
		if(MAPPING_EXTRACTION)
		{
			IFilter	extractor	=	new MaxWeightAssignment();
			
			founds	=	extractor.select(mlfounds);
		}
		
		return AlignmentHelper.convertGMappingTable2RDFAlignmentString(onto1uri, onto2uri, mlfounds);
		*/
		
		GMappingTable<String>	founds	=	align(onto1, onto2);
		
		return AlignmentHelper.convertGMappingTable2RDFAlignmentString(onto1uri, onto2uri, founds);
	}
	
	public String align(URL filepathSource, URL filepathTarget)
	{
		OntoBuffer	onto1	=	new OntoBuffer(filepathSource);
		OntoBuffer	onto2	=	new OntoBuffer(filepathTarget);
		
		String	onto1uri	=	onto1.getOntologyIRI();
		String	onto2uri	=	onto2.getOntologyIRI();
		
		/*
		GMappingTable<String> idfounds	=	idmatcher.predict(onto1, onto2);
		
		GMappingTable<String> mlfounds	=	mlmatcher.predict(PrepareRawData.buildFullTable(YAMSetting.defMatchers, onto1, onto2));
		
		GMappingTable<String> founds	=	SelectWithPriority.select(idfounds, mlfounds);
		
		if(Configs.SEMATIC_CONSTRAINT)
			founds	=	SimpleSemanticConstraint.select(mlfounds, onto1, onto2);
		
		if(MAPPING_EXTRACTION)
		{
			IFilter	extractor	=	new MaxWeightAssignment();
			
			founds	=	extractor.select(mlfounds);
		}
		
		return AlignmentHelper.convertGMappingTable2RDFAlignmentString(onto1uri, onto2uri, founds);
		*/
		
		GMappingTable<String>	founds	=	align(onto1, onto2);
		
		return AlignmentHelper.convertGMappingTable2RDFAlignmentString(onto1uri, onto2uri, founds);
	}
	
	////////////////////////////////////////////////////////////////////////////////
		
	public void evaluateModelSingleScenario(String scenarioName, String year)
	{			
		Scenario	scenario	=	Supports.getScenario(scenarioName, year);
		
		// load ontology into buffer
		OntoBuffer	onto1	=	new OntoBuffer(scenario.getOntoFN1());
		OntoBuffer	onto2	=	new OntoBuffer(scenario.getOntoFN2());
				
		GMappingTable<String> founds	=	predict(onto1, onto2);
			
		if(Configs.SEMATIC_CONSTRAINT)
			founds	=	SimpleSemanticConstraint.select(founds, onto1, onto2);
		
		if(MAPPING_EXTRACTION)
		{
			IFilter	extractor	=	new MaxWeightAssignment();
			
			founds	=	extractor.select(founds);
		}
		
		// Alignment parser
		IAlignmentParser	align	=	null;
		
		// evaluate result
		boolean	evaluate	=	scenario.hasAlign();
		
		if(evaluate)
		{
			align	=	AlignmentParserFactory.createParser(scenario.getAlignFN());
			GMappingTable<String>	experts	=	align.getMappings();
			
			//System.out.println("4. number of experts = " + experts.getSize());
			
			Evaluation<String>	eval	=	new Evaluation<String>(experts, founds);
			eval.evaluate();
			
			System.out.println(scenarioName + "\t" + eval.toLine());
		}		
	}
	
	public void evaluateAndPrintModelSingleScenario(String scenarioName, String year)
	{		
		Scenario	scenario	=	Supports.getScenario(scenarioName, year);
		
		// load ontology into buffer
		OntoBuffer	onto1	=	new OntoBuffer(scenario.getOntoFN1());
		OntoBuffer	onto2	=	new OntoBuffer(scenario.getOntoFN2());
				
		GMappingTable<String> founds	=	predict(onto1, onto2);
		
		if(Configs.SEMATIC_CONSTRAINT)
			founds	=	SimpleSemanticConstraint.select(founds, onto1, onto2);
		
		if(MAPPING_EXTRACTION)
		{
			IFilter	extractor	=	new MaxWeightAssignment();
			
			founds	=	extractor.select(founds);
		}
		
		
		// Alignment parser
		IAlignmentParser	align	=	null;
		
		// evaluate result
		boolean	evaluate	=	scenario.hasAlign();
		
		if(evaluate)
		{			
			align	=	AlignmentParserFactory.createParser(scenario.getAlignFN());
			GMappingTable<String>	experts	=	align.getMappings();
						
			Evaluation<String>	eval	=	new Evaluation<String>(experts, founds);
			
			String	resultFN	=	Configs.TMP_DIR	+	scenarioName + "_" + year + "_EMatcher.txt";
			
			Configs.PRINT_SIMPLE	=	true;
			eval.evaluateAndPrintDetailEvalResults(resultFN);
			
			System.out.println(scenarioName + "\t\t" + eval.toLine());
		}		
	}
	
	public void evaluateModelMultipleScenarios(String[] scenarioNames, String year, String outputFN)
	{
		String	modelName	=	"";
									
		modelName	=	this.getClass().getSimpleName();
		
		System.out.println("ModelEvaluation: working with : " + modelName);
		
		String	resultFN	=	YAMSetting.REPOSITORY + modelName + outputFN + ".txt";
	
		int[]	len	=	{50,15,15,15,15,15,15};
		
		//String	path	=	Configs.ARFF_STORAGE;		
		
		double	averagePrecision	=	0;
		double	averageRecall		=	0;
		double	averageFmeasure		=	0;
		
		double	sumTruePositive		=	0;
		double	sumFalsePositive	=	0;
		double	sumFalseNegative	=	0;
		
		double	hmeanPrecision		=	0;
		double	hmeanRecall			=	0;
		double	hmeanFmeasure		=	0;
		
		try 
		{
			BufferedWriter	writer	=	new BufferedWriter(new FileWriter(resultFN));
			
			for(String scenarioName : scenarioNames)
			{
				Scenario	scenario	=	Supports.getScenario(scenarioName, year);
				
				// load ontology into buffer
				OntoBuffer	onto1	=	new OntoBuffer(scenario.getOntoFN1());
				OntoBuffer	onto2	=	new OntoBuffer(scenario.getOntoFN2());
								
				GMappingTable<String> founds	=	predict(onto1, onto2);
					
				if(Configs.SEMATIC_CONSTRAINT)
					founds	=	SimpleSemanticConstraint.select(founds, onto1, onto2);
				
				if(MAPPING_EXTRACTION)
				{
					IFilter	extractor	=	new MaxWeightAssignment();
					
					founds	=	extractor.select(founds);
				}
				
				
				// Alignment parser
				IAlignmentParser	align	=	null;
				
				// evaluate result
				boolean	evaluate	=	scenario.hasAlign();
				
				if(evaluate)
				{
					double[] results	=	null;
					if(Configs.ALIGNMENT_EVAL)
					{
						String	refalign	=	scenario.getAlignFN();
						
						results	=	AlignmentHelper.evals(founds, refalign);
					}
					else
					{
						align	=	AlignmentParserFactory.createParser(scenario.getAlignFN());
						GMappingTable<String>	experts	=	align.getMappings();
									
						// run classification on testing data
						results	=	Evaluation.evals(founds, experts);	
						
					}				
					
					Formatter line	=	PrintHelper.printFormatter(len, scenarioName, results[0], results[1], results[2], results[3], results[4], results[5]);
					
					System.out.println("MLModel: " + line.toString());
					
					averagePrecision	+=	results[0];
					averageRecall		+=	results[1];
					averageFmeasure		+=	results[2];
					
					sumTruePositive		+=	results[3];
					sumFalsePositive	+=	results[4];
					sumFalseNegative	+=	results[5];
										
					writer.write(line.toString());
					writer.newLine();
				}				
			}
			
			int	numberTests	=	scenarioNames.length;
			
			averagePrecision	=	averagePrecision / numberTests;
			averageRecall		=	averageRecall / numberTests;
			averageFmeasure		=	averageFmeasure / numberTests;
			
			hmeanPrecision	=	sumTruePositive / (sumTruePositive + sumFalsePositive);
			hmeanRecall		=	sumTruePositive / (sumTruePositive + sumFalseNegative);
			hmeanFmeasure	=	2 * hmeanPrecision * hmeanRecall / (hmeanPrecision + hmeanRecall);
			
			Formatter line1	=	PrintHelper.printFormatter(len, modelName + "-average:", averagePrecision, averageRecall, averageFmeasure, sumTruePositive, sumFalsePositive, sumFalseNegative);
			
			writer.write(line1.toString());
			writer.newLine();
			
			Formatter line2	=	PrintHelper.printFormatter(len, modelName + "-Hmean:", hmeanPrecision, hmeanRecall, hmeanFmeasure, sumTruePositive, sumFalsePositive, sumFalseNegative);
			
			writer.write(line2.toString());
			writer.newLine();
			
			// add 3 empty lines
			writer.newLine();
			writer.newLine();
			writer.newLine();
			
			writer.flush();
			writer.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void evaluateAndPrintModelMultipleScenarios(String[] scenarioNames, String year)
	{		
		for(String scenarioName : scenarioNames)
		{			
			evaluateAndPrintModelSingleScenario(scenarioName, year);
		}
	}
	
	public void evaluateAndPrintModelMultipleScenarios(String[] scenarioNames, String year, String output)
	{	
		try 
		{
			String modelName	=	"EMatcher";
			
			
			double	averagePrecision	=	0;
			double	averageRecall		=	0;
			double	averageFmeasure		=	0;
			
			double	sumTruePositive		=	0;
			double	sumFalsePositive	=	0;
			double	sumFalseNegative	=	0;
			
			double	hmeanPrecision		=	0;
			double	hmeanRecall			=	0;
			double	hmeanFmeasure		=	0;
			
			BufferedWriter	writer	=	new BufferedWriter(new FileWriter(output));
			
			for(String scenarioName : scenarioNames)
			{			
				evaluateAndPrintModelSingleScenario(scenarioName, year);
				
				String	resultFN	=	Configs.TMP_DIR	+	scenarioName + "_" + year + "_" + modelName + ".txt";
				
				BufferedReader	reader	=	new BufferedReader(new FileReader(resultFN));
				
				writer.write(modelName + " : " + scenarioName);
				writer.newLine();
				writer.newLine();
				
				// get evaluation from the first line
				String	line	=	reader.readLine();
				
				if(line != null)
				{
					writer.write(line);
					writer.newLine();
					
					String[] results	=	line.split("\\s+");
					
					averagePrecision	+=	Double.parseDouble(results[0]);
					averageRecall		+=	Double.parseDouble(results[1]);
					averageFmeasure		+=	Double.parseDouble(results[2]);
					
					sumTruePositive		+=	Double.parseDouble(results[3]);
					sumFalsePositive	+=	Double.parseDouble(results[4]);
					sumFalseNegative	+=	Double.parseDouble(results[5]);
				}
				
				
				while ((line = reader.readLine()) != null) 
				{
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
			
			int[]	len	=	{30,10,10,10,10,10,10};
			
			int	numberTests	=	scenarioNames.length;
			
			averagePrecision	=	averagePrecision / numberTests;
			averageRecall		=	averageRecall / numberTests;
			averageFmeasure		=	averageFmeasure / numberTests;
			
			hmeanPrecision	=	sumTruePositive / (sumTruePositive + sumFalsePositive);
			hmeanRecall		=	sumTruePositive / (sumTruePositive + sumFalseNegative);
			hmeanFmeasure	=	2 * hmeanPrecision * hmeanRecall / (hmeanPrecision + hmeanRecall);
			
			Formatter line1	=	PrintHelper.printFormatter(len, modelName + "-average:", averagePrecision, averageRecall, averageFmeasure, sumTruePositive, sumFalsePositive, sumFalseNegative);
			
			writer.write(line1.toString());
			writer.newLine();
			
			Formatter line2	=	PrintHelper.printFormatter(len, modelName + "-Hmean:", hmeanPrecision, hmeanRecall, hmeanFmeasure, sumTruePositive, sumFalsePositive, sumFalseNegative);
			
			writer.write(line2.toString());
			writer.newLine();
			
			// add 3 empty lines
			writer.newLine();
			writer.newLine();
			writer.newLine();
			
			writer.flush();
			writer.close();	
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	public URL createTmpAlignmentFromSingleScenario(URL filepathSource, URL filepathTarget)
	{	
		String	salign	=	align(filepathSource, filepathTarget);
		
		try 
		{
			File		falign	=	File.createTempFile("alignment", ".rdf");
			FileWriter	fw		=	new FileWriter(falign);
			
			fw.write(salign);
			fw.flush();
			fw.close();
			
			return falign.toURI().toURL();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
		
	//////////////////////////////////////////////////////////////////////////////////
	
	public static void testCreateTmpAlignmentFromSingleScenario()
	{	
		String	scenarioName	=	"205";
		String	year			=	"2010";
		
		Scenario	scenario	=	Supports.getScenario(scenarioName, year);
		
		try 
		{
			URL	filepathSource	=	(new File(scenario.getOntoFN1())).toURI().toURL();
			URL filepathTarget	=	(new File(scenario.getOntoFN2())).toURI().toURL();
			
			System.out.println(filepathSource.toString());
			System.out.println(filepathTarget.toString());
			
			NewEMatcher	eMatcher	=	NewEMatcher.getInstance();
			
			URL	url	=	eMatcher.createTmpAlignmentFromSingleScenario(filepathSource, filepathTarget);
			
			System.out.println(url.toString());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public static void main(String[] args) 
	{
		testCreateTmpAlignmentFromSingleScenario();
	}
}
