package lig.steamer.cwb;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;

import lig.steamer.cwb.util.matching.OntologyFormat;

import com.google.gwt.thirdparty.guava.common.base.Charsets;
import com.vaadin.server.VaadinService;

public class CWBProperties {

	public static final String BASEDIR = VaadinService.getCurrent()
			.getBaseDirectory().getAbsolutePath();

	public static final String WEB_INF_DIR = BASEDIR + File.separatorChar
			+ "WEB-INF";

	public static final String CLASSES_DIR = WEB_INF_DIR + File.separatorChar
			+ "classes";

	public static final String YAM_RESOURCES_PACKAGE = "yam"
			+ File.separatorChar + "resources";

	public static final String CWB_MAIN_PACKAGE = "lig" + File.separatorChar
			+ "steamer" + File.separatorChar + "cwb";

	public static final String CWB_MESSAGES_PACKAGE = CWB_MAIN_PACKAGE
			+ File.separatorChar + "messages";

	public static final String CWB_IO_PACKAGE = CWB_MAIN_PACKAGE
			+ File.separatorChar + "io";

	public static final String CWB_OUTPUT_DIR = CLASSES_DIR
			+ File.separatorChar + CWB_IO_PACKAGE + File.separatorChar
			+ "output";

	public static final String CWB_TMP_DIR = CLASSES_DIR + File.separatorChar
			+ CWB_IO_PACKAGE + File.separatorChar + "tmp";

	public static final String CONFIG_FILE_NAME = "config.xml";

	public static final String DEFAULT_PROJECT_NAME = "cwb-project";
	
	public static final String DATAMODELS_DIR_NAME = "datamodels";
	public static final String INDICATORS_DIR_NAME = "indicators";
	public static final String MEASURES_DIR_NAME = "measures";

	public static final String TMP_FILE_NAME = "tmp";
	public static final String SOURCE_ONTOLOGY_FILE_NAME = "source";
	public static final String TARGET_ONTOLOGY_FILE_NAME = "target";
	public static final String ONTOLOGY_ALIGNMENT_FILE_NAME = "alignment";

	public static final String OWL_FILE_FORMAT = ".owl";
	public static final String CWB_PROJECT_FORMAT = ".cwb";

	public static final IRI CWB_NAMESPACE = IRI.create("http://cwb.imag.fr");

	public static final String DEFAULT_OUTPUT_CHARSET = Charsets.UTF_8
			.toString();
	public static final String DEFAULT_OUTPUT_FORMAT = OntologyFormat.OWL
			.toString();

	private CWBProperties() {
	}

}
