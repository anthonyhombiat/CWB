package lig.steamer.cwb.io.read.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.io.read.CWBFolksoReader;
import lig.steamer.cwb.io.read.impl.exception.CWBFolksoReaderException;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModelFolkso;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class CWBFolksoOWLReader implements CWBFolksoReader {

	private static Logger LOGGER = Logger.getLogger(CWBFolksoOWLReader.class
			.getName());

	private OWLOntologyManager manager;
	private OWL2CWB owl2cwb;

	public CWBFolksoOWLReader() {
		manager = OWLManager.createOWLOntologyManager();
		owl2cwb = new OWL2CWB(manager.getOWLDataFactory());
	}

	@Override
	public CWBDataModelFolkso read(File file) throws CWBFolksoReaderException {

		try {
			OWLOntology ontology = manager
					.loadOntologyFromOntologyDocument(file);

			return read(ontology);

		} catch (OWLOntologyCreationException e) {
			throw new CWBFolksoReaderException(e);
		}

	}

	@Override
	public CWBDataModelFolkso read(InputStream inputStream)
			throws CWBFolksoReaderException {

		try {
			OWLOntology ontology = manager
					.loadOntologyFromOntologyDocument(inputStream);

			return read(ontology);

		} catch (OWLOntologyCreationException e) {
			throw new CWBFolksoReaderException(e);
		}

	}

	@Override
	public CWBDataModelFolkso read(String filename)
			throws CWBFolksoReaderException {

		return read(new File(filename));

	}
	
	private CWBDataModelFolkso read(OWLOntology ontology) {
		LOGGER.log(Level.INFO, "Reading folksonomy " + ontology.getOntologyID().getOntologyIRI() + "...");

		CWBDataModelFolkso folkso = populateDataModel(new CWBDataModelFolkso(
				ontology.getOntologyID().getOntologyIRI()), ontology,
				owl2cwb.findRootClasses(ontology), null);

		LOGGER.log(Level.INFO, "Folksonomy read (" + folkso.getConcepts().size() + " concepts).");

		return folkso;
	}

	private CWBDataModelFolkso populateDataModel(CWBDataModelFolkso dataModel,
			OWLOntology ontology, Set<? extends OWLClassExpression> classes,
			CWBConcept parent) {

		for (OWLClassExpression classExpression : classes) {

			OWLClass clazz = classExpression.asOWLClass();

			CWBConcept concept = owl2cwb.getCWBConceptFromOwlClass(clazz, ontology);

			if (!dataModel.getConcepts().contains(concept)) {

				if (parent != null) {
					concept.setParent(parent);
				}

				dataModel.addConcept(concept);

				LOGGER.log(Level.FINE, "Concept " + concept.getFragment()
						+ " added to data model.");

				populateDataModel(dataModel, ontology,
						clazz.getSubClasses(ontology), concept);

			}
		}

		return dataModel;
	}

}
