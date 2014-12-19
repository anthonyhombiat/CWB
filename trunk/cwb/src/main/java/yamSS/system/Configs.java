package yamSS.system;

import java.io.File;

import lig.steamer.cwb.Prop;

public class Configs {
	
	/**
	 * MODIFS ANTHONY 13/10/2014 !!!
	 */
	public static String BASEDIR = "D:\\anthony_docs\\workspace_kepler\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\cwb\\WEB-INF\\classes"+
			File.separatorChar + "yam" + File.separatorChar + "resources" + File.separatorChar;
	 
//	public static String BASEDIR = Prop.DIR_CLASSES + File.separatorChar
//			+ Prop.PACK_YAM_RESOURCES + File.separatorChar;

	public static boolean RUNFAST = false;
	public static boolean USING_ML = false;

	public static boolean VERB_ADV_ADJ_SYNONYM = false;

	public static boolean USING_INFERENCE_IN_SF = true;

	public static double LOW_INST_THRESHOLD = 0.3; // default: 0.3
	public static double HIGH_INST_THRESHOLD = 0.85; // default: 0.85

	public static String EQUIVALENT = "=";
	public static String SUBSUMPTION = "<";

	// evaluate by OAEI Alignment
	public static boolean ALIGNMENT_EVAL = false;

	public static boolean EQUIVALENT_MAPPING_ONLY = false;

	public static boolean SEMATIC_CONSTRAINT = true;

	// matching object and data properties
	public static boolean MIX_PROP_MATCHING = false;

	// get aggregate profile by max value (default) or by weighted average
	// function
	public static boolean GET_MAX_PROFILE = true;

	// pre-processing equivalent entities first
	public static boolean EQUIVALENT_PREPROCESS = false;

	public static boolean WN_PREMATCHING = false;

	// debug option
	public static boolean DEBUG = false;
	public static boolean INFO = false;
	public static boolean PRINT_SIMPLE = false;

	public static String REPOSITORY = BASEDIR + "repos" + File.separatorChar;
	public static String DATASETS = BASEDIR + "datasets" + File.separatorChar;

	// default text training data
	public static String TRAIN_DATA = REPOSITORY + "data.txt";

	public static String[] TRAIN_DIRS = { "204", "205", "238", "301", "304" };

	// ARFF storage
	public static String ARFF_STORAGE = REPOSITORY + "arff"
			+ File.separatorChar;
	public static String ARFF_TRAINING = REPOSITORY + "trainARFF"
			+ File.separatorChar;

	public static String TMP_DIR = BASEDIR + "tmp" + File.separatorChar + "txt"
			+ File.separatorChar;
	public static String CVS_DIR = BASEDIR + "tmp" + File.separatorChar + "cvs"
			+ File.separatorChar;
	public static String ALIGN_DIR = BASEDIR + "tmp" + File.separatorChar
			+ "aligns" + File.separatorChar;
	public static String EVAL_DIR = BASEDIR + "tmp" + File.separatorChar
			+ "txt" + File.separatorChar + "eval" + File.separatorChar;

	public static String[] MATCHER_NAMES = { "Levenshtein", "SmithWaterman",
			"ISUB", "QGrams", "Level2ISUB", "LinISUB", "ContextProfile" };

	// address of training ARFF file
	public static String TRAIN_ARFF = REPOSITORY + "training.arff";
	public static String NUM_METRIC_TRAIN_ARFF = REPOSITORY
			+ "num_metric_training.arff";
	public static String NOM_METRIC_TRAIN_ARFF = REPOSITORY
			+ "nom_metric_training.arff";
	public static String NUM_MATCHER_TRAIN_ARFF = REPOSITORY
			+ "num_matcher_training.arff";
	public static String NOM_MATCHER_TRAIN_ARFF = REPOSITORY
			+ "nom_matcher_training.arff";
	public static String NOM_TRAIN_ARFF = REPOSITORY
			+ "nom_matcher_training.arff";

	// repository for saving weka classifier
	public static String WK_CLS_REPOS = REPOSITORY + "models"
			+ File.separatorChar;

	// repository for saving lucene indexes
	public static String INDEX_DIR = REPOSITORY + "lucind";

	// weighting type (TFIDF or ENTROPY)
	public static enum WeightTypes {
		TFIDF, ENTROPY
	};

	// fields's name using for saving/indexing document
	public static String F_URI = "URI";
	public static String F_PROFILE = "PROFILE";
	public static String F_TYPE = "TYPE";
	public static String F_OWNER = "OWNER";

	public static String SOURCE = "SOURCE";
	public static String TARGET = "TARGET";

	// stopword list file
	public static String STOPWORDLIST = REPOSITORY + "stopwords.dat";
	public static String STOPWORDFULL = REPOSITORY + "stopwords_full.dat";
	public static String STOPWORDS = REPOSITORY + "stopwords_full.dat";

	// criteria for selecting best quality classifier scheme
	public static enum QCRITERIA {
		PRECISION, RECALL, F_MEASURE, CORRELATION, ROOT_MEAN, ROOT_RELATIVE
	};

	// for nominal instances, classIndex = 1 because class values = {"0.0",
	// "1.0"}
	public static int CLASS_INDEX = 1;

	// return not existing value
	public static float NO_VALUE = -Float.MAX_VALUE;

	// type of ontology's elements
	public static int E_CLASS = 1;
	public static int E_OBJPROP = 2;
	public static int E_DATAPROP = 3;
	public static int E_INSTANCE = 4;

	// type of mapping
	public static int UNKNOWN = -1;

	public static int TRUE_POSITIVE = 1; // a mapping is true and is recognized
											// as true
	public static int FALSE_NEGATIVE = 2; // a mapping is true but is recognized
											// as false
	public static int FALSE_POSITIVE = 4; // a mapping is false but is
											// recognized as true
	public static int IN_PCGRAPH = 8;

	public static int MARKED = 10; // marked as possibly match
	public static int STEPUP = 10;

	// type of alignment format
	public static int INRIA_FORMAT = 1;
	public static int I3CON_FORMAT = 2;
	public static int STEXT_FORMAT = 3;

	// type of weka classification model or instances
	public static int NUMERICAL = 1;
	public static int NOMINAL = 2;

	// expert field name
	public static String EXPERT = "EXPERT";

	// instances information (title)
	public static String INSTANCES_TITLE = "similarity values";

	// special similarity score
	public static float UN_MATCHED = 0.0f;
	public static float MATCHED = 1.0f; // default: 1.0f
	public static float UN_KNOWN = -Float.MAX_VALUE;
	public static float AVERAGE = 0.5f; // default: 0.5f

	public static int NOT_IS_A = Integer.MAX_VALUE;

	// threshold
	public static float NAME_THRESHOLD = 0.85f; // default: 0.85
	public static float SOFT_THRESHOLD = 0.8f; // default: 0.8
	public static float LABEL_THRESHOLD = 0.8f; // default: 0.8
	public static float VDOC_THRESHOLD = 0.7f; // default: 0.7
	public static float COMMENT_THRESHOLD = 0.7f; // default: 0.7

	public static float E_THRESHOLD = 0.7f; // default: 0.7
	public static float S_THRESHOLD = 0.7f; // default: 0.7

	// priority threshold
	public static float P_THRESHOLD = 0.9f; // default: 0.9

	// special similarity score return by wordnet matcher

	// can not compute score because word 1 does not have meaning
	public static float UN_KNOWN_TYPE1 = -2.0f;

	// can not compute score because word 2 does not have meaning
	public static float UN_KNOWN_TYPE2 = -3.0f;

	// minimum length of word
	public static int MIN_LEN = 3;

	// Main contribution Percentage
	public static float MCP = 0.75f; // default: 0.75
	public static float ADJ_THRESHOLD = 0.7f; // default: 0.7

	// type of structure metric
	public static int S_PATH = 1;
	public static int S_SUP_TREE = 2;
	public static int S_SUB_TREE = 3;
	public static int S_GRAPH = 4;

	public static String DICT = BASEDIR + "WordNet" + File.separatorChar;

	// Wordnet data files
	public static String WNVER = "2.1";
	public static String WNDIR = BASEDIR + "WordNet" + File.separatorChar
			+ WNVER + File.separatorChar + "dict";
	public static String WNIC = BASEDIR + "WordNet" + File.separatorChar
			+ "ic-bnc-resnik-add1_2.1.dat";
	public static String WNTMP = BASEDIR + "configs" + File.separatorChar
			+ "WNTemplate.xml";
	public static String WNPROP = BASEDIR + "configs" + File.separatorChar
			+ "file_properties.xml";

	// abbreviated words list
	public static String ACRONYMS_DICT = BASEDIR + "WordNet"
			+ File.separatorChar + "acronyms.txt";
	public static String SYNONYMS_DICT = BASEDIR + "WordNet"
			+ File.separatorChar + "synonyms.txt";

	public static int SENSE_DEPTH = 3;

	// standard URI welknown from W3C
	public static String XSD = "http://www.w3.org/2001/XMLSchema#";
	public static String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	public static String DC = "http://purl.org/dc/elements/1.1/";
	public static String OWL = "http://www.w3.org/2002/07/owl#";
	public static String ERROR = "http://org.semanticweb.owlapi/error#";
	public static String FOAF = "http://xmlns.com/foaf/0.1/";
	public static String ICAL = "http://www.w3.org/2002/12/cal/ical#";

	public static String NOTHING = "http://www.w3.org/2002/07/owl#Nothing";
	public static String THING = "http://www.w3.org/2002/07/owl#Thing";

	// field name in GMappingRecord
	public static String INITIAL = "INITIAL";
	public static String NONAME = "NONAME";

	// default title in GMappingMatrix
	public static String NOTITLE = "NOTITLE";

	// OAEI folder
	public static String BENCHMARK2009_ROOT = BASEDIR + "data"
			+ File.separatorChar + "ontology" + File.separatorChar
			+ "Benchmark2009" + File.separatorChar;
	public static String BENCHMARK2010_ROOT = BASEDIR + "data"
			+ File.separatorChar + "ontology" + File.separatorChar
			+ "Benchmark2010" + File.separatorChar;
	public static String BENCHMARK2011_2_ROOT = BASEDIR + "data"
			+ File.separatorChar + "ontology" + File.separatorChar
			+ "Benchmark2011_2" + File.separatorChar;
	public static String BENCHMARK2011_5_ROOT = BASEDIR + "data"
			+ File.separatorChar + "ontology" + File.separatorChar
			+ "Benchmark2011_5" + File.separatorChar;

	public static String CONFERENCE_ROOT = BASEDIR + "data"
			+ File.separatorChar + "ontology" + File.separatorChar
			+ "conferences" + File.separatorChar;
	public static String CONFERENCE_FULL = BASEDIR + "data"
			+ File.separatorChar + "target" + File.separatorChar;

	public static String I3CON_ROOT = BASEDIR + "data" + File.separatorChar
			+ "ontology" + File.separatorChar + "i3con" + File.separatorChar;
	public static String OTHER_ROOT = BASEDIR + "data" + File.separatorChar
			+ "ontology" + File.separatorChar + "others" + File.separatorChar;
	public static String ORIGINAL2009 = BENCHMARK2009_ROOT + "original"
			+ File.separatorChar + "original.rdf";

	public static boolean BREAKPOINT = false;
}
