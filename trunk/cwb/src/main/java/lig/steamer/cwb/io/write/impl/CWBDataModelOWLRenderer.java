package lig.steamer.cwb.io.write.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import lig.steamer.cwb.io.write.CWBDataModelVisitor;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelMatched;
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBEquivalence;

import org.coode.owlapi.rdf.rdfxml.RDFXMLOntologyStorer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

public class CWBDataModelOWLRenderer implements CWBDataModelVisitor {

	private static Logger LOGGER = Logger
			.getLogger(CWBDataModelOWLRenderer.class.getName());

	private OWLDataFactory factory;
	private OWLOntologyManager manager;

	private OWLOntology dataModelOntology;

	private IRI iri;

	public CWBDataModelOWLRenderer(IRI iri) {

		this.iri = iri;
		this.manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();

	}

	@Override
	public void visitNomenclature(CWBDataModelNomen dataModelNomen) {

		try {
			dataModelOntology = manager.createOntology(dataModelNomen
					.getNamespace());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		addMetadata(dataModelNomen);

		visitDataModel(dataModelNomen);

		RDFXMLOntologyStorer storer = new RDFXMLOntologyStorer();
		try {
			storer.storeOntology(dataModelOntology, iri,
					new PrefixOWLOntologyFormat());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void visitFolksonomy(CWBDataModelFolkso dataModelFolkso) {

		try {
			dataModelOntology = manager.createOntology(dataModelFolkso
					.getNamespace());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		addMetadata(dataModelFolkso);

		visitDataModel(dataModelFolkso);

		RDFXMLOntologyStorer storer = new RDFXMLOntologyStorer();
		try {
			storer.storeOntology(dataModelOntology, iri,
					new PrefixOWLOntologyFormat());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void visitMatchedDataModel(CWBDataModelMatched dataModelMatched) {

		try {

			dataModelOntology = manager.createOntology(dataModelMatched
					.getNamespace());

			OWLImportsDeclaration import1 = factory
					.getOWLImportsDeclaration(dataModelMatched.getSource1());
			OWLImportsDeclaration import2 = factory
					.getOWLImportsDeclaration(dataModelMatched.getSource2());

			manager.applyChange(new AddImport(dataModelOntology, import1));
			manager.applyChange(new AddImport(dataModelOntology, import2));

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		addMetadata(dataModelMatched);

		visitDataModel(dataModelMatched);

		RDFXMLOntologyStorer storer = new RDFXMLOntologyStorer();
		try {
			storer.storeOntology(dataModelOntology, iri,
					new PrefixOWLOntologyFormat());
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}

	public void addMetadata(CWBDataModel dataModel) {
		addDCCreator(dataModel.getCreator());
		addDCDate(dataModel.getCreationDate());
		addDCModified();
		addDCTitle(dataModel.getTitle());
		addDCDescription(dataModel.getDescription());
	}

	public void addDCCreator(String creator) {
		if (creator != null) {
			OWLAnnotationProperty creatorProp = factory
					.getOWLAnnotationProperty(DublinCoreVocabulary.CREATOR
							.getIRI());
			OWLLiteral creatorValue = factory.getOWLLiteral(creator);
			OWLAnnotation creatorAnnot = factory.getOWLAnnotation(creatorProp,
					creatorValue);
			OWLAxiom creatorAxiom = factory.getOWLAnnotationAssertionAxiom(
					dataModelOntology.getOntologyID().getOntologyIRI(),
					creatorAnnot);

			manager.applyChange(new AddAxiom(dataModelOntology, creatorAxiom));
		}
	}

	public void addDCDescription(String description) {
		if (description != null) {
			OWLAnnotationProperty descrProp = factory
					.getOWLAnnotationProperty(DublinCoreVocabulary.DESCRIPTION
							.getIRI());
			OWLLiteral descrValue = factory.getOWLLiteral(description);
			OWLAnnotation descrAnnot = factory.getOWLAnnotation(descrProp,
					descrValue);
			OWLAxiom descrAxiom = factory.getOWLAnnotationAssertionAxiom(
					dataModelOntology.getOntologyID().getOntologyIRI(),
					descrAnnot);

			manager.applyChange(new AddAxiom(dataModelOntology, descrAxiom));
		}
	}

	public void addDCTitle(String title) {
		if (title != null) {
			OWLAnnotationProperty titleProp = factory
					.getOWLAnnotationProperty(DublinCoreVocabulary.TITLE
							.getIRI());
			OWLLiteral titleValue = factory.getOWLLiteral(title);
			OWLAnnotation titleAnnot = factory.getOWLAnnotation(titleProp,
					titleValue);
			OWLAxiom descrAxiom = factory.getOWLAnnotationAssertionAxiom(
					dataModelOntology.getOntologyID().getOntologyIRI(),
					titleAnnot);

			manager.applyChange(new AddAxiom(dataModelOntology, descrAxiom));
		}
	}

	public void addDCDate(Date date) {
		if (date != null) {
			OWLAnnotationProperty dateProp = factory
					.getOWLAnnotationProperty(DublinCoreVocabulary.DATE
							.getIRI());
			OWLLiteral dateValue = factory.getOWLLiteral(date.toString(),
					OWL2Datatype.XSD_DATE_TIME);
			OWLAnnotation dateAnnot = factory.getOWLAnnotation(dateProp,
					dateValue);
			OWLAxiom descrAxiom = factory.getOWLAnnotationAssertionAxiom(
					dataModelOntology.getOntologyID().getOntologyIRI(),
					dateAnnot);

			manager.applyChange(new AddAxiom(dataModelOntology, descrAxiom));
		}
	}

	public void addDCModified() {
		OWLAnnotationProperty dateProp = factory.getOWLAnnotationProperty(IRI
				.create("http://purl.org/dc/terms/modified"));
		OWLLiteral dateValue = factory.getOWLLiteral(
				DatatypeConverter.printDateTime(new GregorianCalendar()),
				OWL2Datatype.XSD_DATE_TIME);
		OWLAnnotation dateAnnot = factory.getOWLAnnotation(dateProp, dateValue);
		OWLAxiom descrAxiom = factory.getOWLAnnotationAssertionAxiom(
				dataModelOntology.getOntologyID().getOntologyIRI(), dateAnnot);

		manager.applyChange(new AddAxiom(dataModelOntology, descrAxiom));
	}

	@Override
	public void visitDataModel(CWBDataModel dataModel) {

		for (CWBConcept concept : dataModel.getConcepts()) {
			concept.acceptCWBDataModelVisitor(this);
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

		for (Entry<Locale, String> entry : concept.getLabels().entrySet()) {

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
