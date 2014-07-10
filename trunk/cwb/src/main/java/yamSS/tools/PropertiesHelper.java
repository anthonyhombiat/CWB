/**
 * 
 */
package yamSS.tools;

import java.io.File;
import java.util.Properties;

import main.YAMOptions;
import yamSS.system.Configs;
import yamgui.configuration.IniConfig;

/**
 * @author ngoduyhoa Read properties from configuration file
 */
public class PropertiesHelper {
	public static void readProperties() {
		String propFN = Configs.BASEDIR + "configs" + File.separatorChar + "trainings.txt";

		Properties properties = new Properties();
		try {

			// ////////////////////////////////////////
			// CHANGE (Anthony Hombiat, 03/07/2014) //
			// ////////////////////////////////////////
			
			properties.load(PropertiesHelper.class.getClassLoader()
					.getResourceAsStream(propFN));
			// properties.load(new FileInputStream(propFN));

			Configs.WNVER = "2.1";// properties.getProperty("WNVER");
			
			Configs.WNDIR = Configs.BASEDIR + "Wordnet//2.1//dict";// normalize(properties.getProperty("WNDIR"));
			Configs.WNIC = Configs.BASEDIR
					+ "Wordnet//ic-bnc-resnik-add1_2.1.dat";// normalize(properties.getProperty("WNIC"));
			Configs.WNTMP = Configs.BASEDIR + "Wordnet/WNTemplate.xml";// normalize(properties.getProperty("WNTMP"));

			Configs.NOM_TRAIN_ARFF = Configs.BASEDIR + "configs/training.arff";// normalize(properties.getProperty("TRAIN_DATA"));
			
//			Configs.WNDIR = "Wordnet//2.1//dict";// normalize(properties.getProperty("WNDIR"));
//			Configs.WNIC = "Wordnet//ic-bnc-resnik-add1_2.1.dat";// normalize(properties.getProperty("WNIC"));
//			Configs.WNTMP = "Wordnet/WNTemplate.xml";// normalize(properties.getProperty("WNTMP"));
//
//			Configs.NOM_TRAIN_ARFF = Configs.BASEDIR + "configs/training.arff";// normalize(properties.getProperty("TRAIN_DATA"));

			// ////////////////////////////////////////////
			// END CHANGE (Anthony Hombiat, 03/07/2014) //
			// ////////////////////////////////////////////

			// Configs.STOPWORDFULL =
			// normalize(properties.getProperty("STOPWORDS"));

			String listdirs = properties.getProperty("TRAIN_DIRS");

			String[] dirs = listdirs.split(",");

			Configs.TRAIN_DIRS = dirs;

			// String usingml = normalize(properties.getProperty("USING_ML"));
			/*
			 * if(usingml.equalsIgnoreCase("true")) Configs.USING_ML = true;
			 * else if(usingml.equalsIgnoreCase("false")) Configs.USING_ML =
			 * false;
			 */

			if (IniConfig.contains("list_methods")) {
				String methods = IniConfig.get("list_methods").toString();

				String[] items = methods.split(",");

				if (items.length != 0)
					Configs.MATCHER_NAMES = items;
			}

			YAMOptions.SRC_ONTO_PATH = IniConfig.get("source_ontology")
					.toString();
			YAMOptions.TAR_ONTO_PATH = IniConfig.get("target_ontology")
					.toString();
			YAMOptions.ALIGN_OUTPUT_PATH = IniConfig.get("alignment_output")
					.toString();

			YAMOptions.ML_METHOD = ((Boolean) IniConfig.get("machine_learning"))
					.booleanValue();
			YAMOptions.IR_METHOD = ((Boolean) IniConfig.get("ir_method"))
					.booleanValue();
			YAMOptions.IB_METHOD = ((Boolean) IniConfig
					.get("instance_based_matching")).booleanValue();
			YAMOptions.SP_METHOD = ((Boolean) IniConfig
					.get("similarity_propagation")).booleanValue();
			YAMOptions.SV_METHOD = ((Boolean) IniConfig
					.get("sematic_verification")).booleanValue();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String normalize(String stpath) {
		StringBuffer buff = new StringBuffer();

		String[] items = stpath.split("//");

		for (String item : items) {
			buff.append(item);
			buff.append(File.separatorChar);
		}

		buff.deleteCharAt(buff.length() - 1);

		return buff.toString().trim();
	}
}
