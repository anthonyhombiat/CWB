package yamgui.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.prefs.BackingStoreException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import yamSS.system.Configs;

import com.google.common.collect.Maps;

public class IniConfig {

	private static Ini ini;

	private static Map<String, Object> mapSettings;

	private static boolean isLoaded = false;

	public IniConfig() {

	}

	private static void init() {

		// ////////////////////////////////////////
		// CHANGE (Anthony Hombiat, 03/07/2014) //
		// ////////////////////////////////////////
		
		String filename = Configs.BASEDIR + "configs/settings.ini";
		
		
		// ////////////////////////////////////////////
		// END CHANGE (Anthony Hombiat, 03/07/2014) //
		// ////////////////////////////////////////////
		
		try {
			ini = new Ini(new File(filename));
			mapSettings = Maps.newHashMap();
			if (!isLoaded)
				loadConfiguration();

		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Object get(String key) {
		if (ini == null) {
			init();
		}

		if (!mapSettings.containsKey(key))
			return null;
		return mapSettings.get(key);
	}

	public static boolean contains(String key) {
		if (ini == null) {
			init();
		}

		return mapSettings.containsKey(key);
	}

	public static void loadConfiguration() {
		if (ini == null) {
			init();
		}

		mapSettings.clear();
		if (ini != null) {
			Ini.Section settings = ini.get("settings");
			String source_ontology = settings.get("source_ontology");
			String target_ontology = settings.get("target_ontology");
			String alignment_output = settings.get("alignment_output");
			boolean is_machine_learning = Boolean.parseBoolean(settings
					.get("machine_learning"));
			boolean is_ir_method = Boolean.parseBoolean(settings
					.get("ir_method"));
			String list_methods = settings.get("list_methods");
			boolean is_instance_based_matching = Boolean.parseBoolean(settings
					.get("instance_based_matching"));
			boolean is_similarity_propagation = Boolean.parseBoolean(settings
					.get("similarity_propagation"));
			boolean is_sematic_verification = Boolean.parseBoolean(settings
					.get("sematic_verification"));

			putToMapSettings(source_ontology, target_ontology,
					alignment_output, is_machine_learning, is_ir_method,
					list_methods, is_instance_based_matching,
					is_similarity_propagation, is_sematic_verification);
		}
	}

	private static void putToMapSettings(String sourceOnto, String targetOnto,
			String alignmentOutput, boolean is_machine_learning,
			boolean is_ir_method, String list_methods,
			boolean is_instance_based_matching,
			boolean is_similarity_propagation, boolean is_sematic_verification) {
		StringTokenizer stk = new StringTokenizer(list_methods, ",");

		while (stk.hasMoreTokens()) {
			mapSettings.put(stk.nextToken(), Boolean.valueOf(true));
		}

		mapSettings.put("source_ontology", sourceOnto);
		mapSettings.put("target_ontology", targetOnto);
		mapSettings.put("alignment_output", alignmentOutput);
		mapSettings.put("machine_learning", is_machine_learning);
		mapSettings.put("ir_method", is_ir_method);

		mapSettings.put("instance_based_matching", is_instance_based_matching);
		mapSettings.put("similarity_propagation", is_similarity_propagation);
		mapSettings.put("sematic_verification", is_sematic_verification);
		mapSettings.put("list_methods", list_methods);
	}

	public static void saveConfiguration(String sourceOnto, String targetOnto,
			String alignmentOutput, boolean is_machine_learning,
			boolean is_ir_method, String list_methods,
			boolean is_instance_based_matching,
			boolean is_similarity_propagation, boolean is_sematic_verification) {
		if (ini == null) {
			init();
		}

		Ini.Section settings = ini.get("settings");
		settings.put("source_ontology", sourceOnto);
		settings.put("target_ontology", targetOnto);
		settings.put("alignment_output", alignmentOutput);
		settings.put("machine_learning", String.valueOf(is_machine_learning));
		settings.put("ir_method", String.valueOf(is_ir_method));
		settings.put("list_methods", list_methods);
		settings.put("instance_based_matching",
				String.valueOf(is_instance_based_matching));
		settings.put("similarity_propagation",
				String.valueOf(is_similarity_propagation));
		settings.put("sematic_verification",
				String.valueOf(is_sematic_verification));

		putToMapSettings(sourceOnto, targetOnto, alignmentOutput,
				is_machine_learning, is_ir_method, list_methods,
				is_instance_based_matching, is_similarity_propagation,
				is_sematic_verification);
		try {
			ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] pstrArgs) throws BackingStoreException,
			InvalidFileFormatException, IOException {

		IniConfig.loadConfiguration();

		// Ini ini = new Ini(new File(""));

		// Ini.Section bashful = ini.get("bashful");
		//
		// // get method doesn't resolve variable references
		// String weightRaw = bashful.get("weight"); // = ${bashful/weight}
		// String heightRaw = bashful.get("height"); // = ${doc/height}
		// System.out.println(weightRaw + "  " + heightRaw);
		//
		// bashful.put("weight", 15);
		// bashful.put("height", 16);
		// ini.store();
	}
}
