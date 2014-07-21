package lig.steamer.cwb.io.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;

import org.coode.owlapi.rdf.rdfxml.RDFXMLOntologyStorer;
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
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

public class CWBDataModelOwlRenderer implements CWBDataModelVisitor {

	private static Logger LOGGER = Logger.getLogger(CWBDataModelOwlRenderer.class
			.getName());

	private OWLDataFactory factory;
	private OWLOntologyManager manager;

	private OWLOntology dataModelOntology;
	
	private IRI iri;

	public CWBDataModelOwlRenderer(IRI iri) {

		this.iri = iri;
		this.manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();

	}

	@Override
	public void visitDataModel(CWBDataModel dataModel) {
		
		try {
			dataModelOntology = manager.createOntology(dataModel.getNamespace());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
		for (CWBConcept concept : dataModel.getConcepts()) {
			concept.acceptCWBDataModelVisitor(this);
		}

		for (CWBEquivalence equivalence : dataModel.getEquivalences()) {
			equivalence.acceptCWBDataModelVisitor(this);
		}
		
		RDFXMLOntologyStorer storer = new RDFXMLOntologyStorer();
		try {
			storer.storeOntology(dataModelOntology, iri,
					 new PrefixOWLOntologyFormat());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void visitConcept(CWBConcept concept) {

		List<OWLOntologyChange> ontologyChanges = new ArrayList<OWLOntologyChange>();

		List<OWLAxiom> axioms = new ArrayList<OWLAxiom>();

		axioms.add(getOWLClassFromCWBConcept(concept));
		axioms.addAll(getRDFSLabelsFromCWBConcept(concept));
		axioms.addAll(getRDFSCommentsFromCWBConcept(concept));

		if (concept.getParent() != null) {
			axioms.add(getOWLSuperClassFromCWBConcept(concept));
		}

		for (OWLAxiom axiom : axioms) {
			AddAxiom addAxiom = new AddAxiom(dataModelOntology, axiom);
			ontologyChanges.add(addAxiom);
		}

		manager.applyChanges(ontologyChanges);

	}

	@Override
	public void visitEquivalence(CWBEquivalence equivalence) {

		List<OWLOntologyChange> ontologyChanges = new ArrayList<OWLOntologyChange>();

		ontologyChanges.add(new AddAxiom(dataModelOntology,
				getOWLEquivalentClassFromCWBEquivalence(equivalence)));

		manager.applyChanges(ontologyChanges);

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
				factory.getOWLClass(equivalence.getConcept1().getIri()),
				factory.getOWLClass(equivalence.getConcept2().getIri()));

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
		OWLClass conceptClass = factory.getOWLClass(concept.getIri());
		return factory.getOWLDeclarationAxiom(conceptClass);
	}

	/**
	 * Returns the OWLAxiom declaring the super OWL class corresponding to the
	 * given CWBConcept.
	 * @param concept, the CWBConcept
	 * @return the OWLAxiom
	 */
	private OWLAxiom getOWLSuperClassFromCWBConcept(CWBConcept concept) {
		LOGGER.log(Level.INFO, "Declaring super OWL class for concept "
				+ concept.getFragment());
		OWLClass subClass = factory.getOWLClass(concept.getIri());
		OWLClass superClass = factory.getOWLClass(concept.getParent().getIri());
		return factory.getOWLSubClassOfAxiom(subClass, superClass);
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

			axioms.add(factory.getOWLAnnotationAssertionAxiom(concept.getIri(),
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

			axioms.add(factory.getOWLAnnotationAssertionAxiom(concept.getIri(),
					labelAnnotation));
		}

		LOGGER.log(Level.INFO, "RDFS comments added.");

		return axioms;
	}

}
