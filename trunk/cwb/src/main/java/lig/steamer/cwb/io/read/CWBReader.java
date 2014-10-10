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

		String dataModelsDir = destinationPath + File.separatorChar
				+ Prop.DIRNAME_DATAMODELS;
		
		model.setFolksonomy(readFolkso(dataModelsDir).iterator().next());
		model.setNomenclature(readNomen(dataModelsDir).iterator().next());
//		model.addEquivalences(equivalences);

		new File(projectPath).delete();
		try {
			FileUtils.deleteDirectory(new File(destinationPath));
		} catch (IOException e) {
			throw new CWBReaderException(e);
		}

		LOGGER.log(Level.INFO, "CWB Project loaded.");

		return model;

	}

	private Collection<CWBDataModelNomen> readNomen(
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

	private Collection<CWBDataModelFolkso> readFolkso(
			String dataModelsDirPath) throws CWBReaderException {

		File nomenDir = new File(dataModelsDirPath
				+ File.separatorChar + Prop.DIRNAME_FOLKSO);

		Collection<CWBDataModelFolkso> folksos = new ArrayList<CWBDataModelFolkso>();

		if (nomenDir.isDirectory()) {
			for (File file : nomenDir.listFiles()) {
				CWBDataModelReader dataModelReader = new CWBDataModelFolksoReader();
				try {
					folksos.add((CWBDataModelFolkso) dataModelReader
							.read(file));
				} catch (CWBDataModelReaderException e) {
					throw new CWBReaderException(e);
				}
			}
		}

		return folksos;
	}

	private Collection<CWBDataModelMatched> readEquivalences(
			String dataModelsDirPath) throws CWBReaderException {

		File equivalencesDir = new File(dataModelsDirPath
				+ File.separatorChar + Prop.DIRNAME_ALIGN);

		Collection<CWBDataModelMatched> equivalences = new ArrayList<CWBDataModelMatched>();

		if (equivalencesDir.isDirectory()) {
			for (File file : equivalencesDir.listFiles()) {
				CWBDataModelReader dataModelReader = new CWBDataModelMatchedReader();
				try {
					equivalences.add((CWBDataModelMatched) dataModelReader
							.read(file));
				} catch (CWBDataModelReaderException e) {
					throw new CWBReaderException(e);
				}
			}
		}

		return equivalences;
	}

}
