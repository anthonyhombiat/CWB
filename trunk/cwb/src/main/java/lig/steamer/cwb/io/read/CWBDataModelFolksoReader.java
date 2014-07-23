package lig.steamer.cwb.io.read;

import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelFolkso;

import org.semanticweb.owlapi.model.OWLOntology;

public class CWBDataModelFolksoReader extends CWBDataModelReader {

	private static Logger LOGGER = Logger
			.getLogger(CWBDataModelFolksoReader.class.getName());

	@Override
	public CWBDataModel read(OWLOntology ontology) {
		LOGGER.log(Level.INFO, "Parsing ontology...");

		CWBDataModel dataModel = populateDataModel(ontology,
				findRootClasses(ontology), new CWBDataModelFolkso(ontology
						.getOntologyID().getOntologyIRI()), null);

		LOGGER.log(Level.INFO, "Parsing done");

		return dataModel;
	}

}
