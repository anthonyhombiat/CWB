package lig.steamer.cwb.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.visitor.CWBOwlRendererVisitor;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.util.archive.ZipUtility;

public class CWBModelWriter {

	private static Logger LOGGER = Logger.getLogger(CWBModelWriter.class
			.getName());

	public CWBModelWriter() {

	}

	/**
	 * @param model
	 * @return
	 */
	public File write(CWBModel model) {

		LOGGER.log(Level.INFO, "Saving project to " + Prop.DIR_OUTPUT
				+ "...");

		File projectRootDir = new File(Prop.DIR_TMP + File.separatorChar
				+ Prop.DEFAULT_PROJECT_NAME);
		
		File zipFile = new File(Prop.DIR_OUTPUT + File.separatorChar
				+ Prop.DEFAULT_PROJECT_NAME
				+ Prop.FMT_CWB);
		
		projectRootDir.mkdir();
		
		CWBOwlRendererVisitor visitor = new CWBOwlRendererVisitor(
				projectRootDir.getAbsolutePath());

		model.acceptCWBVisitor(visitor);

		ZipUtility zipUtil = new ZipUtility();
		zipUtil.zipDirectory(projectRootDir,
				zipFile.getAbsolutePath());

		try {
			FileUtils.deleteDirectory(projectRootDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LOGGER.log(Level.INFO, "Project saved to " + Prop.DIR_OUTPUT
				+ ".");

		return zipFile;

	}
}
