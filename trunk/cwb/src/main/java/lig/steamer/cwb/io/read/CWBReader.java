package lig.steamer.cwb.io.read;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.exception.OntologyFormatException;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.util.archive.ZipUtility;

public class CWBReader {

	private static Logger LOGGER = Logger.getLogger(CWBReader.class
			.getName());

	public CWBReader() {

	}

	public CWBModel read(String projectPath) {

		LOGGER.log(Level.INFO, "Loading CWB project from " + projectPath
				+ "...");

		CWBModel model = new CWBModel();
		String destinationPath = Prop.DIR_TMP + File.separatorChar
				+ Prop.DEFAULT_PROJECT_NAME;

		ZipUtility zipUtil = new ZipUtility();
		zipUtil.unzip(projectPath, destinationPath);

		model.addDataModels(loadDataModels(destinationPath));

		new File(projectPath).delete();
		try {
			FileUtils.deleteDirectory(new File(destinationPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		LOGGER.log(Level.INFO, "CWB Project loaded.");

		return model;

	}

	private Collection<CWBDataModel> loadDataModels(String projectRootDir) {

		File dataModelsDir = new File(projectRootDir + File.separatorChar
				+ Prop.DIRNAME_DATAMODELS);

		LOGGER.log(Level.INFO,
				"Loading data models from " + dataModelsDir.getAbsolutePath()
						+ "...");

		Collection<CWBDataModel> dataModels = new ArrayList<CWBDataModel>();

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