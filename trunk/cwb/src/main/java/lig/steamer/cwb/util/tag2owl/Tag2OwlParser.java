package lig.steamer.cwb.util.tag2owl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import lig.steamer.cwb.model.tagging.ITag;
import lig.steamer.cwb.model.tagging.ITagSet;

/**
 * @author Anthony Hombiat
 *  Parser that provides methods for converting a set of tags into a set of OWL classes in an ontology.
 */
public class Tag2OwlParser {

	private static Logger LOGGER = Logger
			.getLogger("lig.steamer.cwb.io.tag2owlparser");

	public static String DEFAULT_OUTPUT_DIRNAME = "src/resources/ontologies/osm/taginfo/";
	public static String DEFAULT_FILENAME = "taginfo.owl";

	private String outputFileDirName;

	private OWLDataFactory factory;
	private OWLOntologyManager manager;
	private OWLOntology tagOntology;
	
	private String ontologyUri;

	public Tag2OwlParser(String ontologyUri) {
		this.ontologyUri = ontologyUri;
		this.setOutputFileDirName(DEFAULT_OUTPUT_DIRNAME);
		this.manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();
		try {
			tagOntology = manager.createOntology(IRI.create(ontologyUri));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the given TagSet to the tag ontology.
	 * @param tagSet, the TagSet
	 */
	public void addTagSet(ITagSet tagSet) {

		LOGGER.log(Level.INFO, "Adding OWL classes corresponding to the tags ("
				+ tagSet.getTags().size() + ") in the tag ontology...");

		List<OWLOntologyChange> ontologyChanges = new ArrayList<OWLOntologyChange>();

		for (ITag tag : tagSet.getTags()) {
			List<OWLAxiom> axioms = getAxiomsFromTag(tag);
			for (OWLAxiom axiom : axioms) {
				AddAxiom addAxiom = new AddAxiom(tagOntology, axiom);
				ontologyChanges.add(addAxiom);
			}
		}

		manager.applyChanges(ontologyChanges);

		LOGGER.log(Level.INFO, "Classes added.");

	}

	/**
	 * Returns the list of OWLAxioms (RDFS_LABEL, RDFS_COMMENT, OWL_CLASS)
	 * 		built from the given Tag. 
	 * @param tag, the Tag
	 * @return the list of OWLAxioms
	 */
	public List<OWLAxiom> getAxiomsFromTag(ITag tag) {

		LOGGER.log(Level.INFO, "Adding tag " + tag.toString());

		List<OWLAxiom> axioms = new ArrayList<OWLAxiom>();

		OWLAnnotationProperty labelProperty = factory
				.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		
		OWLAnnotationProperty commentProperty = factory
				.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT
						.getIRI());
		
		// Decalaring the OWLClass
		OWLClass tagClass = factory.getOWLClass(IRI.create(ontologyUri + "#"
				+ tag.getValue().getString()));
		
		axioms.add(factory.getOWLDeclarationAxiom(tagClass));

		// Adding the label
		OWLLiteral tagLabelLiteral = factory.getOWLLiteral(tag.getValue()
				.getString(), tag.getValue().getLanguage());
		OWLAnnotation tagLabelAnnotation = factory.getOWLAnnotation(
				labelProperty, tagLabelLiteral);

		axioms.add(factory.getOWLAnnotationAssertionAxiom(tagClass.getIRI(),
				tagLabelAnnotation));

		// Adding the comment
		OWLLiteral tagCommentLiteral = factory
				.getOWLLiteral(tag.getDescription().getString(), tag
						.getDescription().getLanguage());
		OWLAnnotation tagCommentAnnotation = factory.getOWLAnnotation(
				commentProperty, tagCommentLiteral);

		axioms.add(factory.getOWLAnnotationAssertionAxiom(tagClass.getIRI(),
				tagCommentAnnotation));

		return axioms;

	}

	public void printTagOntology(String outputFileName) {
		try {
			File output = new File(DEFAULT_OUTPUT_DIRNAME, outputFileName);
			SimpleIRIMapper iriMapper = new SimpleIRIMapper(
					IRI.create(ontologyUri), IRI.create(output.getPath()));
			manager.addIRIMapper(iriMapper);
			manager.saveOntology(tagOntology, IRI.create(output));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	public OWLOntology getTagOntology() {
		return tagOntology;
	}

	public String getOutputFileDirName() {
		return outputFileDirName;
	}

	public void setOutputFileDirName(String outputFileDirName) {
		this.outputFileDirName = outputFileDirName;
	}

}
