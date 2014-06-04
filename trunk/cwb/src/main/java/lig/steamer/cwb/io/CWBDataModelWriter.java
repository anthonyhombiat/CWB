package lig.steamer.cwb.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;

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

/**
 * Class that gathers methods to write CWB data model into OWL format.
 * @author Anthony Hombiat
 */
public class CWBDataModelWriter {
	
	private static Logger LOGGER = Logger
			.getLogger(CWBDataModelWriter.class.getName());

	public static String DEFAULT_OUTPUT_DIR = ""; 
	public static String DEFAULT_FILE_NAME = ""; 
	
	private OWLDataFactory factory;
	private OWLOntologyManager manager;

	public CWBDataModelWriter(){
		this.manager = OWLManager.createOWLOntologyManager();
		this.factory = manager.getOWLDataFactory();
	}
	
	/**
	 * Returns the file containing the CWBDataModel in owl format.
	 * @param dataModel, the CWBDataModel to write
	 * @param filename, the name of the output file
	 * @return the file
	 */
	public File write(CWBDataModel dataModel, String filename){
		
		LOGGER.log(Level.INFO, "Writing CWBDataModel to " + DEFAULT_OUTPUT_DIR + filename + "...");
		
		OWLOntology ontology = null;
		
		try {
			ontology = manager.createOntology(IRI.create(dataModel.getNamespace()));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
		File file = new File(DEFAULT_OUTPUT_DIR + filename);
		
		List<OWLOntologyChange> ontologyChanges = new ArrayList<OWLOntologyChange>();
		
		for(CWBConcept concept : dataModel.getConcepts()){
			List<OWLAxiom> axioms = getAxiomsFromConcept(concept);
			for (OWLAxiom axiom : axioms) {
				AddAxiom addAxiom = new AddAxiom(ontology, axiom);
				ontologyChanges.add(addAxiom);
			}
		}
		
		manager.applyChanges(ontologyChanges);
		
		LOGGER.log(Level.INFO, "Writing done.");
		
		return file;
		
	}
	
	/**
	 * Returns the file containing the CWBDataModel in owl format.
	 * @param dataModel, the CWBDataModel to write
	 * @return the file
	 */
	public File write(CWBDataModel dataModel){
		return write(dataModel, DEFAULT_FILE_NAME);
	}
	
	/**
	 * Returns the list of OWLAxioms (RDFS_LABEL, RDFS_COMMENT, OWL_CLASS)
	 * 		built from the given CWBConcept. 
	 * @param concept, the CWBConcept
	 * @return the list of OWLAxioms
	 */
	public List<OWLAxiom> getAxiomsFromConcept(CWBConcept concept) {

		LOGGER.log(Level.INFO, "Adding tag " + concept.toString());

		List<OWLAxiom> axioms = new ArrayList<OWLAxiom>();

		OWLAnnotationProperty labelProperty = factory
				.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		
		OWLAnnotationProperty commentProperty = factory
				.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT
						.getIRI());
		
		// Decalaring the OWLClass
		OWLClass tagClass = factory.getOWLClass(concept.getUri());
		
		axioms.add(factory.getOWLDeclarationAxiom(tagClass));

		// Adding the label
		for(Entry<Locale, String> entry : concept.getNames().entrySet()){
			OWLLiteral label = factory.getOWLLiteral(entry.getKey().toString(), entry.getValue());
			OWLAnnotation labelAnnotation = factory.getOWLAnnotation(
					labelProperty, label);

			axioms.add(factory.getOWLAnnotationAssertionAxiom(tagClass.getIRI(),
					labelAnnotation));
		}
		
		// Adding the comment
		for(Entry<Locale, String> entry : concept.getDescriptions().entrySet()){
			OWLLiteral comment = factory.getOWLLiteral(entry.getKey().toString(), entry.getValue());
			OWLAnnotation commentAnnotation = factory.getOWLAnnotation(
					commentProperty, comment);

			axioms.add(factory.getOWLAnnotationAssertionAxiom(tagClass.getIRI(),
					commentAnnotation));
		}

		return axioms;

	}

}
