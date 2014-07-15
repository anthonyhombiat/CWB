package lig.steamer.cwb.io;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.CWBProperties;
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

		LOGGER.log(Level.INFO, "Saving project to " + CWBProperties.CWB_TMP_DIR
				+ "...");

		CWBOwlRendererVisitor visitor = new CWBOwlRendererVisitor(
				CWBProperties.CWB_TMP_DIR);

		model.acceptCWBVisitor(visitor);

		File zip = new File(CWBProperties.CWB_OUTPUT_DIR + File.separatorChar
				+ CWBProperties.DEFAULT_PROJECT_NAME
				+ CWBProperties.CWB_PROJECT_FORMAT);
		
		ZipUtility ziputil = new ZipUtility();
		ziputil.zipDirectory(new File(CWBProperties.CWB_TMP_DIR
					+ File.separatorChar + CWBProperties.DEFAULT_PROJECT_NAME), zip.getAbsolutePath());

		LOGGER.log(Level.INFO, "Project saved.");

		return zip;

	}
}
