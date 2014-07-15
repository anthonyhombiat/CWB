package lig.steamer.cwb.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import lig.steamer.cwb.CWBProperties;
import lig.steamer.cwb.io.exception.OntologyFormatException;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.util.archive.ZipUtility;

public class CWBModelReader {

	private static Logger LOGGER = Logger.getLogger(CWBModelReader.class
			.getName());

	public CWBModelReader() {

	}

	public CWBModel read(String path) {

		LOGGER.log(Level.INFO, "Loading CWB project from " + path + "...");

		CWBModel model = new CWBModel();

		File tmpFile = ZipUtility.unzip(new File(path));

		model.addDataModels(loadDataModels(tmpFile.getAbsolutePath()));
		
		FileUtils.deleteQuietly(tmpFile);

		LOGGER.log(Level.INFO, "CWB Project loaded.");

		return model;

	}

	private Collection<CWBDataModel> loadDataModels(String basePath) {

		String path = basePath + File.separatorChar
				+ CWBProperties.ROOT_DIR_NAME + File.separatorChar
				+ CWBProperties.DATAMODELS_DIR_NAME;

		LOGGER.log(Level.INFO, "Loading data models from " + path + "...");

		Collection<CWBDataModel> dataModels = new ArrayList<CWBDataModel>();

		File dataModelsDir = new File(path);

		for (File file : dataModelsDir.listFiles()) {
			CWBDataModelReader dataModelReader = new CWBDataModelReader();
			try {
				dataModels.add(dataModelReader.read(file));
			} catch (OntologyFormatException e) {
				e.printStackTrace();
			}
		}

		LOGGER.log(Level.INFO, "Data models loaded.");

		return dataModels;
	}

}
