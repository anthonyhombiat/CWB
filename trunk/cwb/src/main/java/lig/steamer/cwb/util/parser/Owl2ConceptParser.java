package lig.steamer.cwb.util.parser;

import java.io.File;
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

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataProvider;
import lig.steamer.cwb.util.parser.exception.OntologyFormatException;

public class Owl2ConceptParser {

	private static Logger LOGGER = Logger
			.getLogger("lig.steamer.cwb.util.parser.owl2conceptparser");

	private OWLDataFactory factory;
	private OWLOntologyManager manager;

	public Owl2ConceptParser() {
		this.manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();
	}

	public CWBDataModel parse(Html5File file) throws OntologyFormatException {

		return parse(new File(file.getFileName()));

	}

	public CWBDataModel parse(File file) throws OntologyFormatException {

		CWBDataModel dataModel = new CWBDataModel(new CWBDataProvider("user"));

		LOGGER.log(Level.INFO, "Parsing owl file " + file.getAbsolutePath() + "...");
		
		try {
			OWLOntology ontology = manager
					.loadOntologyFromOntologyDocument(file);

			dataModel = createDataModel(ontology, dataModel, findRootClasses(ontology), null);
			
			LOGGER.log(Level.INFO, "Parsing done");
			
			return dataModel;

		} catch (OWLOntologyCreationException e) {
			throw new OntologyFormatException(e);
		}

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
			concept.addName(literal.toString(), new Locale(literal.getLang()));
		}

		Set<OWLAnnotation> comments = clazz.getAnnotations(ontology,
				factory.getRDFSComment());

		for (OWLAnnotation comment : comments) {
			OWLLiteral literal = (OWLLiteral) comment.getValue();
			concept.addName(literal.toString(), new Locale(literal.getLang()));
		}

		return concept;
	}

	public Set<OWLClass> findRootClasses(OWLOntology ontology) {
		
		LOGGER.log(Level.INFO, "Searching for root classes...");
		
		Set<OWLClass> rootClasses = new HashSet<OWLClass>();

		for (OWLClass clazz : ontology.getClassesInSignature()) {
			if (clazz.getSuperClasses(ontology).size() == 0) {
				rootClasses.add(clazz);
				LOGGER.log(Level.INFO, "> " + clazz.getIRI().getFragment()
						+ " added to data model.");
			}
		}

		LOGGER.log(Level.INFO, "Searching done.");
		
		return rootClasses;

	}
}
