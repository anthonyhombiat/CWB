package lig.steamer.cwb.io.write;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.write.exception.CWBWriterException;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelMatched;
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBDataSet;
import lig.steamer.cwb.model.CWBIndicatorMeasureSet;
import lig.steamer.cwb.model.CWBIndicatorModel;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.util.archive.ZipUtility;

import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.model.IRI;

public class CWBWriter {

	private static Logger LOGGER = Logger.getLogger(CWBWriter.class
			.getName());

	public CWBWriter() {

	}

	public File write(CWBModel model) throws CWBWriterException {

		LOGGER.log(Level.INFO, "Saving project to " + Prop.DIR_OUTPUT + "...");

		File projectRootDir = new File(Prop.DIR_TMP + File.separatorChar
				+ Prop.DEFAULT_PROJECT_NAME);

		File zipFile = new File(Prop.DIR_OUTPUT + File.separatorChar
				+ Prop.DEFAULT_PROJECT_NAME + Prop.FMT_CWB);

		projectRootDir.mkdir();

		String dataModelsDirPath = projectRootDir.getAbsolutePath()
				+ File.separatorChar + Prop.DIRNAME_DATAMODELS;

		File indicatorsDir = new File(projectRootDir.getAbsolutePath()
				+ File.separatorChar + Prop.DIRNAME_INDICATORS);
		indicatorsDir.mkdir();

		File measuresDir = new File(projectRootDir.getAbsolutePath()
				+ File.separatorChar + Prop.DIRNAME_MEASURES);
		measuresDir.mkdir();

		writeDataModels(model, dataModelsDirPath);

		for (CWBIndicatorModel indicatorModel : model.getIndicatorModels()) {
			// TODO print indicator models
		}

		for (CWBIndicatorMeasureSet indicatorMeasureSet : model
				.getIndicatorMeasureSets()) {
			// TODO print measures
		}

		for (CWBDataSet dataSet : model.getDataSets()) {
			// TODO print datasets
		}

		ZipUtility zipUtil = new ZipUtility();
		zipUtil.zipDirectory(projectRootDir, zipFile.getAbsolutePath());

		try {
			FileUtils.deleteDirectory(projectRootDir);
		} catch (IOException e) {
			throw new CWBWriterException(e);
		}

		LOGGER.log(Level.INFO, "Project saved to " + Prop.DIR_OUTPUT + ".");

		return zipFile;

	}
	
	public void writeDataModels(CWBModel model, String dataModelsDirPath){
		
		File dataModelsDirNomen = new File(dataModelsDirPath
				+ File.separatorChar + Prop.DIRNAME_NOMEN);
		dataModelsDirNomen.mkdir();
		
		File dataModelsDirFolkso = new File(dataModelsDirPath
				+ File.separatorChar + Prop.DIRNAME_FOLKSO);
		dataModelsDirFolkso.mkdir();
		
		File dataModelsDirMatched = new File(dataModelsDirPath
				+ File.separatorChar + Prop.DIRNAME_MATCHED);
		dataModelsDirMatched.mkdir();
		
		int i = 1;
		for (CWBDataModelNomen dataModelNomen : model.getDataModelsNomen()) {
				
			String path = dataModelsDirNomen.getAbsolutePath()
					 + File.separatorChar + i
					 + Prop.FMT_OWL;
			
			writeDataModel(dataModelNomen, path);
			
			i++;
			
		}
		
		i = 1;
		for (CWBDataModelFolkso dataModelFolkso : model.getDataModelsFolkso()) {
				
			String path = dataModelsDirFolkso.getAbsolutePath()
					 + File.separatorChar + i
					 + Prop.FMT_OWL;
			
			writeDataModel(dataModelFolkso, path);
			
			i++;
			
		}
		
		i = 1;
		for (CWBDataModelMatched dataModelMatched : model.getDataModelsMatched()) {
				
			String path = dataModelsDirMatched.getAbsolutePath()
					 + File.separatorChar + i
					 + Prop.FMT_OWL;
			
			writeDataModel(dataModelMatched, path);
			
			i++;
			
		}
	}

	public File writeDataModel(CWBDataModel dataModel, File file) {
		dataModel.acceptCWBDataModelVisitor(new CWBDataModelOwlRenderer(IRI.create(file)));
		return file;
	}
	
	public File writeDataModel(CWBDataModel dataModel, String path) {
		return writeDataModel(dataModel, new File(path));
	}

}
