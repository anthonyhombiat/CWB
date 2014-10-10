package lig.steamer.cwb.util.parser;

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
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.core.tagging.ITag;
import lig.steamer.cwb.core.tagging.ITagSet;

/**
 * @author Anthony Hombiat Parser that provides methods for converting a set of
 * tags into a set of OWL classes in an ontology.
 */
public class Tag2OwlParser {

	private static Logger LOGGER = Logger.getLogger(Tag2OwlParser.class
			.getName());

	private OWLDataFactory factory;
	private OWLOntologyManager manager;
	private OWLOntology tagOntology;

	private IFolksonomy folksonomy;

	public Tag2OwlParser(IFolksonomy folksonomy) {

		this.folksonomy = folksonomy;
		this.manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();
		try {
			tagOntology = manager.createOntology(folksonomy.getSource()
					.getIRI());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds the given TagSet to the tag ontology.
	 * @return the tag ontology
	 */
	public OWLOntology parse() {

		ITagSet tagset = folksonomy.getTagSet();

		LOGGER.log(Level.INFO, "Adding OWL classes corresponding to the tags ("
				+ tagset.getTags().size() + ") in the tag ontology...");

		List<OWLOntologyChange> ontologyChanges = new ArrayList<OWLOntologyChange>();

		for (ITag tag : tagset.getTags()) {
			List<OWLAxiom> axioms = getAxiomsFromTag(tag);
			for (OWLAxiom axiom : axioms) {
				AddAxiom addAxiom = new AddAxiom(tagOntology, axiom);
				ontologyChanges.add(addAxiom);
			}
		}

		manager.applyChanges(ontologyChanges);

		LOGGER.log(Level.INFO, "Classes added.");

		return tagOntology;

	}

	/**
	 * Returns the list of OWLAxioms (RDFS_LABEL, RDFS_COMMENT, OWL_CLASS) built
	 * from the given Tag.
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

		// Removing special chars from tag value
		String value = tag.getValue().getString()
				.replaceAll("[^a-zA-Z0-9]+", "_");

		// Decalaring the OWLClass
		OWLClass tagClass = factory.getOWLClass(IRI.create(folksonomy
				.getSource().getIRI() + "#" + value));

		axioms.add(factory.getOWLDeclarationAxiom(tagClass));

		// Adding the label
		OWLLiteral tagLabelLiteral = factory.getOWLLiteral(value, tag
				.getValue().getLanguage());
		OWLAnnotation tagLabelAnnotation = factory.getOWLAnnotation(
				labelProperty, tagLabelLiteral);

		axioms.add(factory.getOWLAnnotationAssertionAxiom(tagClass.getIRI(),
				tagLabelAnnotation));

		// Adding the comment
		if (tag.getDescription() != null) {
			OWLLiteral tagCommentLiteral = factory.getOWLLiteral(tag
					.getDescription().getString(), tag.getDescription()
					.getLanguage());
			OWLAnnotation tagCommentAnnotation = factory.getOWLAnnotation(
					commentProperty, tagCommentLiteral);

			axioms.add(factory.getOWLAnnotationAssertionAxiom(
					tagClass.getIRI(), tagCommentAnnotation));
		}

		return axioms;

	}

}
