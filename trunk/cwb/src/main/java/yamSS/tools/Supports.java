/**
 * 
 */
package yamSS.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import yamSS.datatypes.mapping.GMapping;
import yamSS.datatypes.mapping.GMappingScore;
import yamSS.datatypes.mapping.GMappingTable;
import yamSS.datatypes.scenario.Scenario;
import yamSS.simlib.ext.SimpleSpliter;
import yamSS.simlib.ext.StopWords;
import yamSS.system.Configs;

/**
 * @author ngoduyhoa
 * 
 */
public class Supports {
	static Logger logger = Logger.getLogger(Supports.class);

	// filter with threshold
	public static <T extends Comparable<T>> GMappingTable<T> filter(
			GMappingTable<T> intable, float threshold) {
		GMappingTable<T> table = new GMappingTable<T>();

		Iterator<GMapping<T>> it = intable.getIterator();

		while (it.hasNext()) {
			GMapping<T> mapping = (GMapping<T>) it.next();

			if (mapping instanceof GMappingScore) {
				float score = ((GMappingScore) mapping).getSimScore();
				if (score >= threshold)
					table.addMapping(mapping);
			}
		}

		return table;
	}

	// check if entity's uri is a standard
	public static boolean isStandard(String uri) {
		boolean status = false;

		if (uri.equalsIgnoreCase(Configs.XSD))
			status = true;

		if (uri.equalsIgnoreCase(Configs.RDF))
			status = true;

		if (uri.equalsIgnoreCase(Configs.RDFS))
			status = true;

		if (uri.equalsIgnoreCase(Configs.DC))
			status = true;

		if (uri.equalsIgnoreCase(Configs.OWL))
			status = true;

		if (uri.equalsIgnoreCase(Configs.FOAF))
			status = true;

		if (uri.equalsIgnoreCase(Configs.ICAL))
			status = true;

		if (uri.equalsIgnoreCase(Configs.ERROR))
			status = true;

		if (Configs.DEBUG)
			if (status)
				System.out.println("Support : " + uri);

		return status;
	}

	public static boolean isPredifined(String entityUri) {
		boolean status = false;

		String prefix = getPrefix(entityUri);

		status = isStandard(prefix);

		return status;
	}

	// create a property file for initialize wordnet by directory and version
	public static void createWNPropertyFile(String wordnetdir,
			String wordnetversion) throws Exception {

		// ////////////////////////////////////////
		// CHANGE (Anthony Hombiat, 03/07/2014) //
		// ////////////////////////////////////////

		// String wntmp = "configs" + File.separatorChar + "WNTemplate.xml";
		// String wnprop = "configs" + File.separatorChar +
		// "file_properties.xml";
		//
		// System.out
		// .println("Support.createWNPropertyFile() > getResource(Configs.WNTMP) \r\n"
		// + Supports.class.getClassLoader().getResource(
		// wntmp));
		//
		// InputStreamReader reader = new InputStreamReader(Supports.class
		// .getClassLoader().getResourceAsStream(wntmp));
		//
		// System.out
		// .println("Support.createWNPropertyFile() > getResource(Configs.WNPROP) \r\n"
		// + Supports.class.getClassLoader().getResource(
		// wnprop));
		//
		// InputStreamReader reader2 = new InputStreamReader(Supports.class
		// .getClassLoader().getResourceAsStream(wnprop));
		//
		// OutputStreamWriter writer = null;
		// IOUtils.copy(reader2, writer);
		//
		// BufferedReader bufRead = new BufferedReader(reader);
		BufferedReader bufRead = new BufferedReader(new FileReader(
				Configs.WNTMP));
		// BufferedWriter bufWrite = new BufferedWriter(writer);
		BufferedWriter bufWrite = new BufferedWriter(new FileWriter(
				Configs.WNPROP));

		String line = null;

		while ((line = bufRead.readLine()) != null) {
			if (line.startsWith("INSERT")) {
				int ind = line.indexOf("YOUR_WN_Version");

				if (ind > 0) {
					String part1 = line.substring(6, ind);
					String part2 = line.substring(ind + 15);

					line = part1 + wordnetversion + part2;
				} else {
					ind = line.indexOf("YOUR_WN_Path");

					if (ind > 0) {
						String part1 = line.substring(6, ind);
						String part2 = line.substring(ind + 12);

						line = part1 + wordnetdir + part2;
					}
				}
			}

			bufWrite.write(line + "\n");

		}

		bufRead.close();
		bufWrite.flush();
		bufWrite.close();
	}

	public static String createWNPropertyString(String wordnetdir,
			String wordnetversion) throws Exception {

		// ////////////////////////////////////////
		// CHANGE (Anthony Hombiat, 03/07/2014) //
		// ////////////////////////////////////////

		String wntmp = Configs.BASEDIR + "configs" + File.separatorChar
				+ "WNTemplate.xml";

		// InputStreamReader reader = new InputStreamReader(Supports.class
		// .getClassLoader().getResourceAsStream(wntmp));
		//
		// BufferedReader bufRead = new BufferedReader(reader);
		BufferedReader bufRead = new BufferedReader(new FileReader(wntmp));

		// ////////////////////////////////////////////
		// END CHANGE (Anthony Hombiat, 03/07/2014) //
		// ////////////////////////////////////////////

		StringWriter swriter = new StringWriter();
		BufferedWriter bufWrite = new BufferedWriter(swriter);

		String line = null;

		while ((line = bufRead.readLine()) != null) {
			if (line.startsWith("INSERT")) {
				int ind = line.indexOf("YOUR_WN_Version");

				if (ind > 0) {
					String part1 = line.substring(6, ind);
					String part2 = line.substring(ind + 15);

					line = part1 + wordnetversion + part2;
				} else {
					ind = line.indexOf("YOUR_WN_Path");

					if (ind > 0) {
						String part1 = line.substring(6, ind);
						String part2 = line.substring(ind + 12);

						line = part1 + wordnetdir + part2;
					}
				}
			}

			bufWrite.write(line + "\n");

		}

		bufRead.close();
		bufWrite.flush();
		bufWrite.close();

		return swriter.toString();
	}

	public static String getMappingType(int type) {
		if (type == Configs.FALSE_POSITIVE)
			return "FP";
		else if (type == Configs.TRUE_POSITIVE)
			return "TP";
		else if (type == Configs.FALSE_NEGATIVE)
			return "FN";
		else if (type == Configs.UNKNOWN)
			return "UN";
		else {
			if (type % Configs.MARKED == 0) {
				int k = type / Configs.MARKED;
				return "" + k + "xMARK";
			} else
				return "UN";
		}
	}

	public static String getEntityType(int type) {
		if (type == Configs.E_CLASS)
			return "CLASS";
		else if (type == Configs.E_OBJPROP)
			return "OBJ_PROP";
		else if (type == Configs.E_DATAPROP)
			return "DATA_PROP";

		return "UNKNOWN";
	}

	public static String getBaseName(String fullname) {
		int ind = fullname.lastIndexOf(File.separatorChar);

		return fullname.substring(ind + 1);
	}

	public static String getLocalName(String uri) {
		int ind = uri.lastIndexOf('#');

		if (ind == -1) {
			ind = uri.lastIndexOf('/');
			if (ind == -1)
				return uri;
		}

		return uri.substring(ind + 1);
	}

	public static String getPrefix(String uri) {
		int ind = uri.lastIndexOf('#');

		if (ind == -1) {
			ind = uri.lastIndexOf('/');
			if (ind == -1)
				return "";
		}

		return uri.substring(0, ind + 1);
	}

	public static String getNS(String uri) {
		int ind = uri.lastIndexOf('#');

		if (ind == -1) {
			ind = uri.lastIndexOf('/');
			if (ind == -1)
				return "";
		}

		return uri.substring(0, ind);
	}

	// check if uri has not a Name space
	public static boolean isNoNS(String uri) {
		int ind = uri.indexOf('#');

		if (ind == -1)
			ind = uri.indexOf('/');

		if (ind == -1)
			return true;

		return false;
	}

	// remove whitespace and special symbol in string
	// special chars are not letter or digit
	public static String removeSpecialChars(String str) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {
			if (Character.isLetterOrDigit(str.charAt(i)))
				buf.append(str.charAt(i));
		}

		return buf.toString();
	}

	// remove whitespace, digit and special symbol in string
	// special chars are not letter
	public static String removeNonLetterChars(String str) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {
			if (Character.isLetter(str.charAt(i)))
				buf.append(str.charAt(i));
		}

		return buf.toString();
	}

	public static String replaceNonLetterByBlank(String str) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {
			if (Character.isLetter(str.charAt(i)))
				buf.append(str.charAt(i));
			else {
				if (buf.charAt(buf.length() - 1) != ' ')
					buf.append(" ");
				else
					continue;
			}
		}

		return buf.toString().trim();
	}

	// insert delimiter (whitespace) between tokens of input string
	public static String insertDelimiter(String str) {
		StringBuffer buf = new StringBuffer();

		// split input string to tokens (without number)
		String[] tokens = SimpleSpliter.split(str, false);

		// put tokens in buffer with delimiter
		for (String token : tokens) {
			if (token.charAt(0) == '*')
				token = token.substring(1);

			if (token.length() > 0) {
				buf.append(token);
				buf.append(' ');
			}
		}

		return buf.toString().trim();
	}

	public static String getEntityLabelFromURI(String uriStr) {
		if (uriStr.indexOf("#") >= 0)
			return uriStr.split("#")[1];
		return uriStr;
	}

	public static String[] splitStringByCapitalLetter(String str) {
		String pattern =

		"(?<=[^\\p{Upper}])(?=\\p{Upper})"
		// either there is anything that is not an uppercase character
		// followed by an uppercase character

				+ "|(?<=[\\p{Lower}])(?=\\d)"
		// or there is a lowercase character followed by a digit

		// + "|(?=\\d)(?<=[\\p{Lower}])"
		;

		return str.split(pattern);
		// return str.split("(?=\\p{Upper})");

	}

	public static List<String> cleanLabel(String label_value) {
		List<String> cleanWords = new ArrayList<String>();

		String[] words;

		label_value = label_value.replace(",", "");

		if (label_value.indexOf("_") > 0) { // NCI and SNOMED
			words = label_value.split("_");
		} else if (label_value.indexOf(" ") > 0) { // FMA
			words = label_value.split(" ");
		}
		// Split capitals...
		else {
			words = splitStringByCapitalLetter(label_value);
		}
		// else {
		// words=new String[1];
		// words[0]=label_value;
		// }

		// To lowercase

		for (int i = 0; i < words.length; i++) {

			words[i] = words[i].toLowerCase(); // to lower case

			if (words[i].length() > 0) {

				if (!StopWords.getSmallSet().contains(words[i])) {
					// if
					// (!LexicalUtilities.getStopwordsSetExtended().contains(words[i])){
					// words[i].length()>2 && Not for exact IF: it may contain
					// important numbers
					cleanWords.add(words[i]);
				}
			}
		}

		return cleanWords;

	}

	// replace all special characters and remove all token has only 1 char
	public static String replaceSpecialChars(String str) {
		String res = str.replaceAll("[^a-zA-Z0-9\\-]", " ");

		// System.out.println(res);

		// res = res.replaceAll(" [a-zA-Z0-9] ", " ");

		return res;
	}

	// select only main tokens (without number, length >= 3)
	// the remains are concatenate in one token with symbol'*' at the begining,
	// which means it is not a word
	public static String[] getWords(String str) {
		// get small set of stop words
		StopWords stopwords = StopWords.getSmallSet();

		// temporary storage saves all tokens belong to string
		List<String> words = new ArrayList<String>();

		// split input string to tokens (without number)
		String[] tokens = SimpleSpliter.split(str, false);

		// buffer is used for save not-word tokens
		StringBuffer buf = new StringBuffer();

		// append symbol '*' at begining
		buf.append('*');

		for (String token : tokens) {
			// if token is a word in stoplist --> remove it
			if (stopwords.contains(token))
				continue;

			if (token.charAt(0) == '*') {
				token = token.substring(1);

				if (stopwords.contains(token))
					continue;

				buf.append(token);
			} else {
				// if token has all symbol are vowel or consonant --> add in
				// buffer
				if (isConsonant(token) || isVowel(token))
					buf.append(token);
				else
					words.add(token.toLowerCase());
			}
		}

		// if buffer is not empty (exclude symbol '*')
		if (buf.length() > 1)
			words.add(buf.toString());

		// convert array list into array
		return words.toArray(new String[words.size()]);
	}

	private static boolean isVowel(char c) {
		if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
				|| c == 'y' || c == 'A' || c == 'E' || c == 'I' || c == 'O'
				|| c == 'U' || c == 'Y')
			return true;

		return false;
	}

	private static boolean isVowel(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!isVowel(str.charAt(i)))
				return false;
		}
		return true;
	}

	private static boolean isConsonant(char c) {
		if (Character.isLetter(c) && !isVowel(c))
			return true;

		return false;
	}

	private static boolean isConsonant(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!isConsonant(str.charAt(i)))
				return false;
		}
		return true;
	}

	public static boolean isMarked(String token) {
		if (token == null || token.length() == 0)
			return false;

		return token.charAt(0) == '*';
	}

	public static String unMarked(String token) {
		if (token != null && token.length() > 0 && token.charAt(0) == '*')
			return token.substring(1);

		return token;
	}

	public static boolean isRandomString(String label) {
		String pattern1 = "MA_[0-9]{3,}+|NCI_[0-9]{3,}+|[a-zA-Z]+-[0-9]{3,}+-[0-9]{3,}+";

		boolean match = false;

		match = label.matches(pattern1);

		return match;
	}

	// ////////////////////////////////////////////////////////////////////////////////

	public static Scenario getScenario(String scenarioName, String year) {
		ScenarioHelper sc_helper = ScenarioHelper.getInstance();

		if (sc_helper.isConferences(scenarioName)) {
			return getConferenceScenario(scenarioName);
		} else if (sc_helper.isOthers(scenarioName)) {
			return getOtherScenario(scenarioName);
		} else if (sc_helper.isI3CON(scenarioName)) {
			return getI3CONScenario(scenarioName);
		}
		if (year.equals("2009")) {
			return getBenchmark2009Scenario(scenarioName);
		}
		if (year.equals("2010")) {
			return getBenchmark2010Scenario(scenarioName);
		}
		if (year.equals("2011")) {
			return getBenchmark2011Scenario(scenarioName);
		}
		if (year.equals("2011_5")) {
			return getBenchmark2011_5Scenario(scenarioName);
		}

		return getGenericScenario(scenarioName);
	}

	public static Scenario getScenario(String scenarioName) {
		ScenarioHelper sc_helper = ScenarioHelper.getInstance();

		if (sc_helper.isConferences(scenarioName)) {
			return getConferenceScenario(scenarioName);
		} else if (sc_helper.isOthers(scenarioName)) {
			return getOtherScenario(scenarioName);
		} else if (sc_helper.isI3CON(scenarioName)) {
			return getI3CONScenario(scenarioName);
		}

		return getGenericScenario(scenarioName);
	}

	public static Scenario getGenericScenario(String scenarioName) {
		String dir = Configs.DATASETS + scenarioName + File.separatorChar;

		String ontName1 = dir + "source.owl";

		if (!(new File(ontName1)).exists()) {
			ontName1 = dir + "source.rdf";
		}

		if (!(new File(ontName1)).exists()) {
			System.err
					.println("The SOURCE ontology must have name: source.owl  OR source.rdf");
			return null;
		}

		String ontName2 = dir + "target.owl";

		if (!(new File(ontName2)).exists()) {
			ontName2 = dir + "target.rdf";
		}

		if (!(new File(ontName2)).exists()) {
			System.err
					.println("The TARGET ontology must have name: target.owl  OR target.rdf");
			return null;
		}

		String refName = dir + "refalign.rdf";

		if ((new File(refName)).exists()) {
			return new Scenario(ontName1, ontName2, refName,
					Configs.INRIA_FORMAT);
		} else {
			refName = dir + "refalign.n3";
			if ((new File(refName)).exists()) {
				return new Scenario(ontName1, ontName2, refName,
						Configs.I3CON_FORMAT);
			} else {
				System.err
						.println("The REFERENCE file must have name: refalign.rdf  OR refalign.n3");

				return new Scenario(ontName1, ontName2, null,
						Configs.I3CON_FORMAT);
			}
		}
	}

	public static Scenario getOtherScenario(String scenarioName) {
		String dir = Configs.OTHER_ROOT + scenarioName + File.separatorChar;

		int ind = scenarioName.indexOf('-');

		if (ind > 0) {
			String ontName1 = dir + scenarioName.substring(0, ind) + ".owl";
			String ontName2 = dir + scenarioName.substring(ind + 1) + ".owl";
			// String refName = dir + scenarioName + ".txt";
			// return new Scenario(ontName1, ontName2, refName,
			// Configs.STEXT_FORMAT);

			String refName = dir + scenarioName + ".txt";
			return new Scenario(ontName1, ontName2, refName,
					Configs.STEXT_FORMAT);
		}

		return null;
	}

	public static Scenario getConferenceScenario(String scenarioName) {
		String dir = Configs.CONFERENCE_ROOT + scenarioName
				+ File.separatorChar;

		int ind = scenarioName.indexOf('-');

		if (ind > 0) {
			String ontName1 = dir + scenarioName.substring(0, ind) + ".owl";
			String ontName2 = dir + scenarioName.substring(ind + 1) + ".owl";
			String refName = dir + scenarioName + ".rdf";

			return new Scenario(ontName1, ontName2, refName,
					Configs.INRIA_FORMAT);
		}

		return null;
	}

	public static Scenario getI3CONScenario(String scenarioName) {
		String dir = Configs.I3CON_ROOT + scenarioName + File.separatorChar;

		int ind = scenarioName.indexOf('-');

		if (ind > 0) {
			String ontName1 = dir + scenarioName.substring(0, ind) + ".owl";

			if (!(new File(ontName1)).exists()) {
				ontName1 = dir + scenarioName.substring(0, ind) + ".rdf";
			}

			String ontName2 = dir + scenarioName.substring(ind + 1) + ".owl";

			if (!(new File(ontName2)).exists()) {
				ontName2 = dir + scenarioName.substring(ind + 1) + ".rdf";
			}

			String refName = dir + scenarioName + ".n3";

			return new Scenario(ontName1, ontName2, refName,
					Configs.I3CON_FORMAT);
		}

		return null;
	}

	public static Scenario getBenchmark2009Scenario(String scenarioIndex) {
		File scenarioFolder = new File(Configs.BENCHMARK2009_ROOT
				+ scenarioIndex);

		String original = Configs.ORIGINAL2009;
		String target = null;
		String alignment = null;

		File[] files = scenarioFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (pathname.isFile()) {
					String name = pathname.getName().toLowerCase();
					if (name.endsWith(".rdf") || name.endsWith(".owl"))
						return true;
				}
				return false;
			}
		});

		if (files != null) {
			for (File file : files) {
				String fname = file.getName().toLowerCase();

				if (fname.startsWith("refalign")) {
					alignment = Configs.BENCHMARK2009_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				} else if (fname.startsWith(scenarioIndex)) {
					target = Configs.BENCHMARK2009_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				}
			}

			// construct scenario
			if (target != null) {
				if (alignment != null) {
					return new Scenario(original, target, alignment,
							Configs.INRIA_FORMAT);
				} else {
					return new Scenario(original, target);
				}
			}
		}

		return null;
	}

	public static Scenario getBenchmark2010Scenario(String scenarioIndex) {
		File scenarioFolder = new File(Configs.BENCHMARK2010_ROOT
				+ scenarioIndex);

		String original = null;
		String target = null;
		String alignment = null;

		File[] files = scenarioFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (pathname.isFile()) {
					String name = pathname.getName().toLowerCase();
					if (name.endsWith(".rdf") || name.endsWith(".owl"))
						return true;
				}
				return false;
			}
		});

		if (files != null) {
			for (File file : files) {
				String fname = file.getName().toLowerCase();

				if (fname.startsWith("refalign")) {
					alignment = Configs.BENCHMARK2010_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				} else if (fname.startsWith(scenarioIndex)) {
					target = Configs.BENCHMARK2010_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				} else if (fname.startsWith("original")) {
					original = Configs.BENCHMARK2010_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				}
			}

			// construct scenario
			if (target != null) {
				if (alignment != null) {
					return new Scenario(original, target, alignment,
							Configs.INRIA_FORMAT);
				} else {
					return new Scenario(original, target);
				}
			}
		}

		return null;
	}

	public static Scenario getBenchmark2011Scenario(String scenarioIndex) {
		File scenarioFolder = new File(Configs.BENCHMARK2011_2_ROOT
				+ scenarioIndex);

		String original = null;
		String target = null;
		String alignment = null;

		File[] files = scenarioFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (pathname.isFile()) {
					String name = pathname.getName().toLowerCase();
					if (name.endsWith(".rdf") || name.endsWith(".owl"))
						return true;
				}
				return false;
			}
		});

		if (files != null) {
			for (File file : files) {
				String fname = file.getName().toLowerCase();

				if (fname.startsWith("refalign")) {
					alignment = Configs.BENCHMARK2011_2_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				} else if (fname.startsWith(scenarioIndex)) {
					target = Configs.BENCHMARK2011_2_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				} else if (fname.startsWith("original")) {
					original = Configs.BENCHMARK2011_2_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				}
			}

			// construct scenario
			if (target != null) {
				if (alignment != null) {
					return new Scenario(original, target, alignment,
							Configs.INRIA_FORMAT);
				} else {
					return new Scenario(original, target);
				}
			}
		}

		return null;
	}

	public static Scenario getBenchmark2011_5Scenario(String scenarioIndex) {
		File scenarioFolder = new File(Configs.BENCHMARK2011_5_ROOT
				+ scenarioIndex);

		String original = null;
		String target = null;
		String alignment = null;

		File[] files = scenarioFolder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (pathname.isFile()) {
					String name = pathname.getName().toLowerCase();
					if (name.endsWith(".rdf") || name.endsWith(".owl"))
						return true;
				}
				return false;
			}
		});

		if (files != null) {
			for (File file : files) {
				String fname = file.getName().toLowerCase();

				if (fname.startsWith("refalign")) {
					alignment = Configs.BENCHMARK2011_5_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				} else if (fname.startsWith(scenarioIndex)) {
					target = Configs.BENCHMARK2011_5_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				} else if (fname.startsWith("original")) {
					original = Configs.BENCHMARK2011_5_ROOT + scenarioIndex
							+ File.separatorChar + fname;
				}
			}

			// construct scenario
			if (target != null) {
				if (alignment != null) {
					return new Scenario(original, target, alignment,
							Configs.INRIA_FORMAT);
				} else {
					return new Scenario(original, target);
				}
			}
		}

		return null;
	}

	// ////////////////////////////////////////////////////////////////////////////

	// given string in dictionary file: word1 : synonym1 : synonym2 :
	// extarct it into hashmap<string, arraylist<string>>
	private static HashMap<String, ArrayList<String>> getDictionary(
			String dictionaryFile) {
		HashMap<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
		try {

			// ////////////////////////////////////////
			// CHANGE (Anthony Hombiat, 03/07/2014) //
			// ////////////////////////////////////////

			// System.out
			// .println("Support.getDictionary() > getResource(dictionaryFile) \r\n"
			// + Supports.class.getClassLoader().getResource(
			// dictionaryFile));
			//
			// InputStreamReader reader = new InputStreamReader(Supports.class
			// .getClassLoader().getResourceAsStream(dictionaryFile));

			// BufferedReader br = new BufferedReader(reader);
			BufferedReader br = new BufferedReader(new FileReader(
					dictionaryFile));

			String line;
			while ((line = br.readLine()) != null) {
				// replace all character after symbol ':' by enpty ""
				String word = line.replaceAll(":.*", "");

				// replace all characters from beginning of the line to first
				// ':'
				// split the remain by character ':'
				String synonyms[] = line.replaceAll("^[^:]+:", "").split(":");

				dictionary.put(word,
						new ArrayList<String>(Arrays.asList(synonyms)));

				logger.debug("full line : " + line);
				logger.debug("main word : " + word);
				for (String item : synonyms) {
					logger.debug("\t synonyms : " + item);
				}
			}
		} catch (java.io.IOException e) {
			System.out.println("(E) ThesaurusMatcher.getDictionary - " + e);
		}
		return dictionary;
	}

	// print evaluation results in a string
	public static String printEvalResults(String scenarioName, double[] results) {
		if (results == null)
			return "";

		int[] len = { 10, 10, 10, 10, 10, 10, 10 }; // easily view in console
		// int[] len = {50,15,15,15,15,15,15}; // easily view in text file

		Formatter line = PrintHelper.printFormatter(len, scenarioName,
				results[0], results[1], results[2], results[3], results[4],
				results[5]);

		return line.toString();
	}

	// print evaluation results in a string
	public static String printEvalResults(String matcherName,
			String scenarioName, double[] results) {
		if (results == null)
			return "";

		int[] len = { 50, 10, 10, 10, 10, 10, 10, 10 }; // easily view in
														// console
		// int[] len = {50,15,15,15,15,15,15}; // easily view in text file

		Formatter line = PrintHelper.printFormatter(len, matcherName,
				scenarioName, results[0], results[1], results[2], results[3],
				results[4], results[5]);

		return line.toString();
	}

	// get core name of metric
	// e.x StringMetric[Jaro] --> return Jaro
	public static String getInternalMetricName(String matcherName) {
		int ind1 = matcherName.indexOf('[');
		int ind2 = matcherName.indexOf(']');

		if (ind1 > 0 && ind2 > 0)
			return matcherName.substring(ind1 + 1, ind2);

		return matcherName;
	}

	// /////////////////////////////////////////////////////////////////////////////

	public static void testGetDictionary() {
		String dicpath = "repos" + File.separatorChar + "test_dic.txt";
		HashMap<String, ArrayList<String>> thesaurus = getDictionary(dicpath);
	}

	public static void testReplaceSpecialChars() {
		String str = "Ph.D_physic U.S.A.B.C 12-10 {California}";

		System.out.println("original : " + str);

		System.out.println("replace special chars : "
				+ replaceSpecialChars(str));

		System.out.println("replace special chars : "
				+ replaceNonLetterByBlank(str));
	}

	public static void testInsertDelimiter() {
		String str = "GoodDD1979ngoDuy>Hoa2810KKKL_-@-1984Vu.ThiMM-nn_THANHMai1210Dd1-*";
		// String str = "ADDRESS";
		System.out.println(insertDelimiter(str));
	}

	public static void testGetWords() {
		// String str =
		// "GoodDD1979ngoDuy>Hoa2810KKKL_-@-1984Vu.ThiMM-nn_THANHMai1210Dd1-*";
		// String str = "Ph.DStudent";
		// String str = "i.s.b.n";
		// String str = "u.s.a_army";
		String str = "Ph.D_physic.U.S.A1210California";
		// String str = "Ph.D_physic.U.S.A.1210California";
		// String str = "Ph.DThesis_physic.at1210California_U.S.A";
		// String str = "PH.D_THESIS_PHYSIC.U.S.A1210California";

		for (String token : getWords(str)) {
			System.out.print(token + " : ");
		}

		System.out.println();
	}

	public static void testGetPrefix() {
		String cls_uri1 = "article";
		String cls_uri2 = "http://www.w3.org/2002/12/cal/ical";
		String cls_uri3 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#List";

		if (getPrefix(cls_uri1).equals(""))
			System.out.println("cls_uri1 does not have prefix!!!");

		System.out.println("Prefix : " + getPrefix(cls_uri1));

		System.out.println("Prefix : " + getPrefix(cls_uri2));
		System.out.println("Prefix : " + getPrefix(cls_uri3));
		System.out.println("Prefix : " + getNS(cls_uri3));
	}

	public static void testGetLocalName() {
		String cls_uri1 = "http://oaei.ontologymatching.org/2009/benchmarks/101/onto.rdf#School";

		System.out.println("Local name : " + getLocalName(cls_uri1));

		String cls_uri2 = "http://www.w3.org/2002/12/cal/ical";
		String cls_uri3 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#List"; // "http://xmlns.com/foaf/0.1/Person";

		System.out.println("Prefix : " + getPrefix(cls_uri2));

		if (isStandard(getPrefix(cls_uri3))) {
			System.out.println(cls_uri3 + " has standard URI");
		} else {
			System.out.println(cls_uri3 + " has normal URI");
		}
	}

	public static void testGetScenario() {
		Scenario scenario = getScenario("104", "2010");
		// Scenario scenario = getConferenceScenario("cmt-conference");

		if (scenario != null)
			System.out.println(scenario);
	}

	public static void testPattern() {
		String label1 = "c-1283-405";// "NCI_0111";

		System.out.println(label1 + " is special : " + isRandomString(label1));
	}

	// //////////////////////////////////////////////////////////////////////////////

	// test static methods above
	public static void main(String[] args) {
		logger.setLevel(Level.DEBUG);

		// testInsertDelimiter();
		// testGetWords();
		// testGetLocalName();

		// testGetPrefix();

		// testPattern();

		// testGetScenario();

		// testGetDictionary();

		testReplaceSpecialChars();
	}
}
