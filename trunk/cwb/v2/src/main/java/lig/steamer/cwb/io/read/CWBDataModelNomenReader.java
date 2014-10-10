package lig.steamer.cwb.io.read;

import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelNomen;

import org.semanticweb.owlapi.model.OWLOntology;

public class CWBDataModelNomenReader extends CWBDataModelReader {

	private static Logger LOGGER = Logger.getLogger(CWBDataModelNomenReader.class
			.getName());
	
	@Override
	public CWBDataModel read(OWLOntology ontology) {
		LOGGER.log(Level.INFO, "Parsing ontology...");

		CWBDataModel dataModel = populateDataModel(new CWBDataModelNomen(ontology
				.getOntologyID().getOntologyIRI()), ontology,
				findRootClasses(ontology), null);

		LOGGER.log(Level.INFO, "Parsing done");

		return dataModel;
	}

}
