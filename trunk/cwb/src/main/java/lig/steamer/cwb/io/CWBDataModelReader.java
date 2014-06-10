package lig.steamer.cwb.io;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.vaadin.ui.Html5File;

import lig.steamer.cwb.io.exception.OntologyFormatException;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;

public class CWBDataModelReader {

	private static Logger LOGGER = Logger
			.getLogger(CWBDataModelReader.class.getName());

	private OWLDataFactory factory;
	private OWLOntologyManager manager;

	public CWBDataModelReader() {
		this.manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();
	}
	
	public CWBDataModel read(OWLOntology ontology) throws OntologyFormatException {

		LOGGER.log(Level.INFO, "Parsing ontology...");
		
		CWBDataModel dataModel = new CWBDataModel(ontology.getOntologyID().getOntologyIRI());

		dataModel = createDataModel(ontology, dataModel, findRootClasses(ontology), null);
		
		LOGGER.log(Level.INFO, "Parsing done");
		
		return dataModel;

	}

	public CWBDataModel read(File file) throws OntologyFormatException {
		
		try {
			OWLOntology ontology = manager
					.loadOntologyFromOntologyDocument(file);
			
			return read(ontology);

		} catch (OWLOntologyCreationException e) {
			throw new OntologyFormatException(e);
		}

	}
	
	public CWBDataModel read(InputStream inputStream) throws OntologyFormatException {
		
		try {
			OWLOntology ontology = manager
					.loadOntologyFromOntologyDocument(inputStream);
			
			return read(ontology);

		} catch (OWLOntologyCreationException e) {
			throw new OntologyFormatException(e);
		}

	}
	
	public CWBDataModel read(Html5File file) throws OntologyFormatException {

		return read(new File(file.getFileName()));

	}

	public CWBDataModel createDataModel(OWLOntology ontology,
			CWBDataModel dataModel, Set<? extends OWLClassExpression> classes,
			CWBConcept parent) {

		for (OWLClassExpression classExpression : classes) {

			OWLClass clazz = classExpression.asOWLClass();

			CWBConcept concept = getConceptFromOwlClass(clazz, ontology);

			if (!dataModel.getConcepts().contains(concept)) {

				if (parent != null) {
					concept.setParent(parent);
				}

				dataModel.addConcept(concept);

				LOGGER.log(Level.INFO, "Concept " + concept.getFragment()
						+ " added to data model.");

				createDataModel(ontology, dataModel,
						clazz.getSubClasses(ontology), concept);
			}
		}

		return dataModel;

	}

	public CWBConcept getConceptFromOwlClass(OWLClass clazz,
			OWLOntology ontology) {

		CWBConcept concept = new CWBConcept(clazz.getIRI());

		Set<OWLAnnotation> labels = clazz.getAnnotations(ontology,
				factory.getRDFSLabel());

		for (OWLAnnotation label : labels) {
			OWLLiteral literal = (OWLLiteral) label.getValue();
			concept.addName(literal.getLiteral().toString(), new Locale(literal.getLang()));
		}

		Set<OWLAnnotation> comments = clazz.getAnnotations(ontology,
				factory.getRDFSComment());

		for (OWLAnnotation comment : comments) {
			OWLLiteral literal = (OWLLiteral) comment.getValue();
			concept.addDescription(literal.getLiteral().toString(), new Locale(literal.getLang()));
		}

		return concept;
	}

	private Set<OWLClass> findRootClasses(OWLOntology ontology) {
		
		LOGGER.log(Level.INFO, "Searching for root classes...");
		
		Set<OWLClass> rootClasses = new HashSet<OWLClass>();

		for (OWLClass clazz : ontology.getClassesInSignature()) {
			if (clazz.getSuperClasses(ontology).size() == 0) {
				rootClasses.add(clazz);
				LOGGER.log(Level.INFO, "> " + clazz.getIRI().getFragment());
			}
		}

		LOGGER.log(Level.INFO, "Searching done.");
		
		return rootClasses;

	}
}
