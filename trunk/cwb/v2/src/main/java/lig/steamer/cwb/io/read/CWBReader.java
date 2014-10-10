package lig.steamer.cwb.io.read;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.read.exception.CWBDataModelReaderException;
import lig.steamer.cwb.io.read.exception.CWBReaderException;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelMatched;
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.util.archive.ZipUtility;

import org.apache.commons.io.FileUtils;

public class CWBReader {

	private static Logger LOGGER = Logger.getLogger(CWBReader.class.getName());

	public CWBReader() {

	}

	public CWBModel read(String projectPath) throws CWBReaderException {

		LOGGER.log(Level.INFO, "Loading CWB project from " + projectPath
				+ "...");

		CWBModel model = new CWBModel();
		String destinationPath = Prop.DIR_TMP + File.separatorChar
				+ Prop.DEFAULT_PROJECT_NAME;

		ZipUtility zipUtil = new ZipUtility();
		zipUtil.unzip(projectPath, destinationPath);

		model.addDataModels(readDataModels(destinationPath));

		new File(projectPath).delete();
		try {
			FileUtils.deleteDirectory(new File(destinationPath));
		} catch (IOException e) {
			throw new CWBReaderException(e);
		}

		LOGGER.log(Level.INFO, "CWB Project loaded.");

		return model;

	}

	private Collection<CWBDataModel> readDataModels(String projectRootDir)
			throws CWBReaderException {

		String dataModelsDir = projectRootDir + File.separatorChar
				+ Prop.DIRNAME_DATAMODELS;

		LOGGER.log(Level.INFO, "Loading data models from " + dataModelsDir
				+ "...");

		Collection<CWBDataModel> dataModels = new ArrayList<CWBDataModel>();

		dataModels.addAll(readDataModelsNomen(dataModelsDir));
		dataModels.addAll(readDataModelsFolkso(dataModelsDir));
		dataModels.addAll(readDataModelsMatched(dataModelsDir));

		LOGGER.log(Level.INFO, "Data models loaded.");

		return dataModels;
	}

	private Collection<CWBDataModelNomen> readDataModelsNomen(
			String dataModelsDirPath) throws CWBReaderException {

		File dataModelsNomenDir = new File(dataModelsDirPath
				+ File.separatorChar + Prop.DIRNAME_NOMEN);

		Collection<CWBDataModelNomen> dataModelsNomen = new ArrayList<CWBDataModelNomen>();

		if (dataModelsNomenDir.isDirectory()) {
			for (File file : dataModelsNomenDir.listFiles()) {
				CWBDataModelReader dataModelReader = new CWBDataModelNomenReader();
				try {
					dataModelsNomen.add((CWBDataModelNomen) dataModelReader
							.read(file));
				} catch (CWBDataModelReaderException e) {
					throw new CWBReaderException(e);
				}
			}
		}

		return dataModelsNomen;
	}

	private Collection<CWBDataModelFolkso> readDataModelsFolkso(
			String dataModelsDirPath) throws CWBReaderException {

		File dataModelsNomenDir = new File(dataModelsDirPath
				+ File.separatorChar + Prop.DIRNAME_FOLKSO);

		Collection<CWBDataModelFolkso> dataModelsFolkso = new ArrayList<CWBDataModelFolkso>();

		if (dataModelsNomenDir.isDirectory()) {
			for (File file : dataModelsNomenDir.listFiles()) {
				CWBDataModelReader dataModelReader = new CWBDataModelFolksoReader();
				try {
					dataModelsFolkso.add((CWBDataModelFolkso) dataModelReader
							.read(file));
				} catch (CWBDataModelReaderException e) {
					throw new CWBReaderException(e);
				}
			}
		}

		return dataModelsFolkso;
	}

	private Collection<CWBDataModelMatched> readDataModelsMatched(
			String dataModelsDirPath) throws CWBReaderException {

		File dataModelsNomenDir = new File(dataModelsDirPath
				+ File.separatorChar + Prop.DIRNAME_MATCHED);

		Collection<CWBDataModelMatched> dataModelsMatched = new ArrayList<CWBDataModelMatched>();

		if (dataModelsNomenDir.isDirectory()) {
			for (File file : dataModelsNomenDir.listFiles()) {
				CWBDataModelReader dataModelReader = new CWBDataModelMatchedReader();
				try {
					dataModelsMatched.add((CWBDataModelMatched) dataModelReader
							.read(file));
				} catch (CWBDataModelReaderException e) {
					throw new CWBReaderException(e);
				}
			}
		}

		return dataModelsMatched;
	}

}
