package lig.steamer.cwb.io;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

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

		LOGGER.log(Level.INFO, "Saving project to " + Prop.DIR_TMP
				+ "...");

		File projectRootDir = new File(Prop.DIR_TMP + File.separatorChar
				+ Prop.DEFAULT_PROJECT_NAME);
		projectRootDir.mkdir();
		
		CWBOwlRendererVisitor visitor = new CWBOwlRendererVisitor(
				projectRootDir.getAbsolutePath());

		model.acceptCWBVisitor(visitor);

		File zip = new File(Prop.DIR_OUTPUT + File.separatorChar
				+ Prop.DEFAULT_PROJECT_NAME
				+ Prop.FMT_CWB);

		ZipUtility ziputil = new ZipUtility();
		ziputil.zipDirectory(new File(Prop.DIR_TMP
				+ File.separatorChar + Prop.DEFAULT_PROJECT_NAME),
				zip.getAbsolutePath());

		LOGGER.log(Level.INFO, "Project saved to " + Prop.DIR_TMP
				+ ".");

		return zip;

	}
}
