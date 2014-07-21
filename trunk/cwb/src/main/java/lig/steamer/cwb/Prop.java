package lig.steamer.cwb;

import java.io.File;

import com.vaadin.server.VaadinService;

public class Prop {

	public static final String DIR_ROOT = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	public static final String DIR_WEBINF = DIR_ROOT + File.separatorChar + "WEB-INF";
	public static final String DIR_CLASSES = DIR_WEBINF + File.separatorChar + "classes";
	
	public static final String PACK_YAM_RESOURCES = "yam" + File.separatorChar + "resources";
	public static final String PACK_MAIN = "lig" + File.separatorChar + "steamer" + File.separatorChar + "cwb";
	public static final String PACK_MESSAGES = PACK_MAIN + File.separatorChar + "messages";
	public static final String PACK_IO = PACK_MAIN + File.separatorChar + "io";
	
	public static final String DIR_OUTPUT = DIR_CLASSES	+ File.separatorChar + PACK_IO + File.separatorChar + "output";
	public static final String DIR_TMP = DIR_CLASSES + File.separatorChar + PACK_IO + File.separatorChar + "tmp";

	public static final String DIRNAME_DATAMODELS = "datamodels";
	public static final String DIRNAME_INDICATORS = "indicators";
	public static final String DIRNAME_MEASURES = "measures";

	public static final String FILENAME_CONFIG = "config.xml";
	public static final String FILENAME_TMP = "tmp";
	public static final String FILENAME_SOURCE_ONTO = "source";
	public static final String FILENAME_TARGET_ONTO = "target";
	public static final String FILENAME_ONTO_ALIGNMENT = "alignment";

	public static final String FMT_OWL = ".owl";
	public static final String FMT_CWB = ".cwb";

	public static final String CWB_NAMESPACE = "http://cwb.imag.fr";

	public static final String DEFAULT_PROJECT_NAME = "cwb-project";
	public static final String DEFAULT_CHARSET = "UTF_8";
	public static final String DEFAULT_ONTO_FMT = "OWL";

	private Prop() {
	}

}