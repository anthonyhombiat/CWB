package lig.steamer.cwb;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import com.vaadin.server.VaadinService;

public class CWBProperties {

	private static final String CONFIG_FILE_NAME = VaadinService.getCurrent()
			.getBaseDirectory().getAbsolutePath()
			+ "\\WEB-INF\\config.xml";

	public static String TMP_DIR = "tmpDir";
	public static String OUTPUT_DIR = "outputDir";
	public static String TMP_FILE_NAME = "tmpFileName";
	public static String OUTPUT_FILE_NAME = "outputFileName";
	public static String OWL_FILE_FORMAT = "owlFileFormat";
	public static String CWB_NAMESPACE = "namespace";

	private CWBProperties() {

	}

	public static String getProperty(String key) {
		String property = "";
		try {
			XMLConfiguration config = new XMLConfiguration(CONFIG_FILE_NAME);
			property = config.getString(key);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		return property;
	}

}
