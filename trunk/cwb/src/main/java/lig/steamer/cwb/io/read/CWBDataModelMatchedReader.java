package lig.steamer.cwb.io.read;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelMatched;

import org.semanticweb.owlapi.model.OWLOntology;

public class CWBDataModelMatchedReader extends CWBDataModelReader {

	private static Logger LOGGER = Logger
			.getLogger(CWBDataModelMatchedReader.class.getName());

	@Override
	public CWBDataModel read(OWLOntology ontology) {
		LOGGER.log(Level.INFO, "Parsing ontology...");

		Iterator<OWLOntology> it = ontology.getImports().iterator();

		CWBDataModel dataModel = populateDataModel(new CWBDataModelMatched(ontology
				.getOntologyID().getOntologyIRI(), it.next()
				.getOntologyID().getOntologyIRI(), it.next()
				.getOntologyID().getOntologyIRI()), ontology,
				findRootClasses(ontology), null);

		LOGGER.log(Level.INFO, "Parsing done");

		return dataModel;
	}

}
