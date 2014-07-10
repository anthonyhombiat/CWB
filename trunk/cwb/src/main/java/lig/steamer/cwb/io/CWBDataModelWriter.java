package lig.steamer.cwb.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.CWBProperties;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/**
 * Class that gathers methods to write CWB data model into OWL format.
 * @author Anthony Hombiat
 */
public class CWBDataModelWriter {

	private static Logger LOGGER = Logger.getLogger(CWBDataModelWriter.class
			.getName());

	private OWLDataFactory factory;
	private OWLOntologyManager manager;

	private OWLOntology ontology;
	private CWBDataModel dataModel;

	private File outputFile;

	public CWBDataModelWriter(CWBDataModel dataModel, File ouputFile) {

		this.manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();

		this.dataModel = dataModel;
		this.outputFile = ouputFile;

		try {
			ontology = manager.createOntology(dataModel.getNamespace());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	public CWBDataModelWriter(CWBDataModel dataModel, String filename,
			String outputDir, String outputFormat) {
		this(dataModel, new File(outputDir
				+ File.separatorChar + filename + outputFormat));
	}

	public CWBDataModelWriter(CWBDataModel dataModel, String filename,
			String outputDir) {
		this(dataModel, filename, outputDir, CWBProperties.OWL_FILE_FORMAT);
	}

	public CWBDataModelWriter(CWBDataModel dataModel, String filename) {
		this(dataModel, filename, CWBProperties.CWB_OUTPUT_DIR);
	}

	/**
	 * Returns the file containing the CWBDataModel in owl format.
	 * @param dataModel, the CWBDataModel to write
	 */
	public void write() {

		LOGGER.log(Level.INFO,
				"Writing CWBDataModel " + dataModel.getNamespace() + "...");

		List<OWLOntologyChange> ontologyChanges = new ArrayList<OWLOntologyChange>();

		for (CWBConcept concept : dataModel.getConcepts()) {

			List<OWLAxiom> axioms = new ArrayList<OWLAxiom>();

			axioms.add(getOWLClassFromCWBConcept(concept));
			axioms.addAll(getRDFSLabelsFromCWBConcept(concept));
			axioms.addAll(getRDFSCommentsFromCWBConcept(concept));

			for (OWLAxiom axiom : axioms) {
				AddAxiom addAxiom = new AddAxiom(ontology, axiom);
				ontologyChanges.add(addAxiom);
			}
		}

		for (CWBEquivalence equivalence : dataModel.getEquivalences()) {
			ontologyChanges.add(new AddAxiom(ontology,
					getOWLEquivalentClassFromCWBEquivalence(equivalence)));
		}

		manager.applyChanges(ontologyChanges);

		LOGGER.log(Level.INFO, "Writing done.");

	}

	public void flush() {
		LOGGER.log(Level.INFO,
				"Flushing data to " + outputFile.getAbsolutePath() + "...");

		try {
			SimpleIRIMapper iriMapper = new SimpleIRIMapper(ontology
					.getOntologyID().getOntologyIRI(), IRI.create(outputFile));
			manager.addIRIMapper(iriMapper);
			manager.saveOntology(ontology, IRI.create(outputFile));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

		LOGGER.log(Level.INFO, "Writing done.");
	}

	/**
	 * Returns the OWLAxiom containing the OWL equivalent class from the given
	 * CWBEquivalence.
	 * @param concept, the CWBEquivalence
	 * @return the OWLAxiom
	 */
	private OWLAxiom getOWLEquivalentClassFromCWBEquivalence(
			CWBEquivalence equivalence) {

		LOGGER.log(Level.INFO, "Adding OWL equivalent class between concepts "
				+ equivalence.getConcept1().getFragment() + " and "
				+ equivalence.getConcept2().getFragment());

		OWLEquivalentClassesAxiom axiom = factory.getOWLEquivalentClassesAxiom(
				factory.getOWLClass(equivalence.getConcept1().getUri()),
				factory.getOWLClass(equivalence.getConcept1().getUri()));

		return axiom;
	}

	/**
	 * Returns the OWLAxiom declaring the OWL class corresponding to the given
	 * CWBConcept.
	 * @param concept, the CWBConcept
	 * @return the OWLAxiom
	 */
	private OWLAxiom getOWLClassFromCWBConcept(CWBConcept concept) {
		LOGGER.log(Level.INFO,
				"Declaring OWL class for concept " + concept.getFragment());
		OWLClass conceptClass = factory.getOWLClass(concept.getUri());
		return factory.getOWLDeclarationAxiom(conceptClass);
	}

	/**
	 * Returns the list of OWLAxioms containing RDFS labels from the given
	 * CWBConcept.
	 * @param concept, the CWBConcept
	 * @return the list of OWLAxioms
	 */
	private List<OWLAxiom> getRDFSLabelsFromCWBConcept(CWBConcept concept) {

		LOGGER.log(Level.INFO,
				"Adding RDFS labels from concept " + concept.getFragment());

		List<OWLAxiom> axioms = new ArrayList<OWLAxiom>();

		OWLAnnotationProperty labelProperty = factory
				.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());

		for (Entry<Locale, String> entry : concept.getNames().entrySet()) {

			LOGGER.log(Level.INFO,
					"> \"" + entry.getValue() + "\" (" + entry.getKey() + ")");

			OWLLiteral label = factory.getOWLLiteral(entry.getValue(), entry
					.getKey().toString());
			OWLAnnotation labelAnnotation = factory.getOWLAnnotation(
					labelProperty, label);

			axioms.add(factory.getOWLAnnotationAssertionAxiom(concept.getUri(),
					labelAnnotation));
		}

		LOGGER.log(Level.INFO, "RDFS labels added.");

		return axioms;
	}

	/**
	 * Returns the list of OWLAxioms containing RDFS comments from the given
	 * CWBConcept.
	 * @param concept, the CWBConcept
	 * @return the list of OWLAxioms
	 */
	private List<OWLAxiom> getRDFSCommentsFromCWBConcept(CWBConcept concept) {

		LOGGER.log(Level.INFO,
				"Adding RDFS comments from concept " + concept.getFragment());

		List<OWLAxiom> axioms = new ArrayList<OWLAxiom>();

		OWLAnnotationProperty commentProperty = factory
				.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT
						.getIRI());

		for (Entry<Locale, String> entry : concept.getDescriptions().entrySet()) {

			LOGGER.log(Level.INFO,
					"> \"" + entry.getValue() + "\" (" + entry.getKey() + ")");

			OWLLiteral comment = factory.getOWLLiteral(entry.getValue(), entry
					.getKey().toString());
			OWLAnnotation labelAnnotation = factory.getOWLAnnotation(
					commentProperty, comment);

			axioms.add(factory.getOWLAnnotationAssertionAxiom(concept.getUri(),
					labelAnnotation));
		}

		LOGGER.log(Level.INFO, "RDFS comments added.");

		return axioms;
	}

	public File getFile() {
		return outputFile;
	}

}
