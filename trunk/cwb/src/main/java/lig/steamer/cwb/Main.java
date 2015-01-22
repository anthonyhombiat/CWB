package lig.steamer.cwb;

import java.net.URI;
import java.util.Collection;

import lig.steamer.cwb.io.write.impl.CWBAlignmentRDFWriter;
import lig.steamer.cwb.io.write.impl.exception.CWBAlignmentWriterException;
import lig.steamer.cwb.model.CWBAlignment;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.util.matching.CWBOntologyMatcher;
import lig.steamer.cwb.util.matching.impl.YamOntologyMatcher;

public class Main {
	
	public static String OUTPUT_DIR = "D:\\anthony_docs\\workspace_kepler\\cwb\\src\\resources\\alignments\\"; 
	
	public static String BPE_URI = "d:\\anthony_docs\\workspace_kepler\\cwb\\src\\main\\resources\\lig\\steamer\\cwb\\io\\input\\ontologies\\bpe\\bpe.owl";
	public static String TOPO_URI = "d:\\anthony_docs\\workspace_kepler\\cwb\\src\\main\\resources\\lig\\steamer\\cwb\\io\\input\\ontologies\\topo\\topo.owl";
	public static String TAGINFO_URI = "d:\\anthony_docs\\workspace_kepler\\cwb\\src\\main\\resources\\lig\\steamer\\cwb\\io\\input\\ontologies\\osm\\taginfo\\taginfo_amenity_100.owl";
	public static String OSMONTO_URI = "d:\\anthony_docs\\workspace_kepler\\cwb\\src\\main\\resources\\lig\\steamer\\cwb\\io\\input\\ontologies\\osm\\OSMonto_amenity.owl";
	public static String LGD_URI = "d:\\anthony_docs\\workspace_kepler\\cwb\\src\\main\\resources\\lig\\steamer\\cwb\\io\\input\\ontologies\\osm\\lgd_amenity_no_individuals_utf8.owl";
	public static String OSN_URI = "d:\\anthony_docs\\workspace_kepler\\cwb\\src\\main\\resources\\lig\\steamer\\cwb\\io\\input\\ontologies\\osm\\osn-k-amenity-only_v.owl";

	public static void main(String[] args)  {

		long startTime = System.currentTimeMillis();
		
//		CWBOntologyMatcher matcher = new WikimatchOntologyMatcher();
		CWBOntologyMatcher matcher = new YamOntologyMatcher();
		
		Collection<CWBEquivalence> equivalences = matcher.getEquivalences(
				TOPO_URI, OSN_URI);
		
		CWBAlignment alignment = new CWBAlignment(URI.create("http://ign.bdtopo.fr"), URI.create("http://spatial.ucd.ie/lod/osn/proposed_term"));
		alignment.addEquivalences(equivalences);
		
		CWBAlignmentRDFWriter writer = new CWBAlignmentRDFWriter();
		try {
			writer.write(alignment, "D:\\anthony_docs\\workspace_kepler\\cwb\\src\\main\\resources\\lig\\steamer\\cwb\\io\\input\\alignments\\yam", "ign-topo-za_vs_osn-amenity_v2");
		} catch (CWBAlignmentWriterException e) {
			e.printStackTrace();
		}
		
		System.out.println(equivalences.size() + " equivalence(s) found");
		for(CWBEquivalence equivalence : equivalences){
			System.out.println(equivalence.toString());
		}
		
		/*
		
		// Consumes data from the OSM TagInfo Restful Web Service.
		// This Web Service is called to retrieve OSM tags values by their key.
		TaginfoWSClient tagInfoClient = new TaginfoWSClient();
		CWBDataModel model = null;
		try {
			model = tagInfoClient.getTagsByKey("amenity");
		} catch (TaginfoClientException e) {
			e.printStackTrace();
		} 

		// Writes the model
		File file = new File("src/resources/ontologies/osm/taginfo/taginfo.owl");
		CWBWriter writer = new CWBWriter();
		writer.writeDataModel(model, file.getAbsolutePath());

		// Matches the tag ontology previously created with the BPE
		// nomenclature.
		CWBOntologyMatcher matcher = new YamOntologyMatcher();
		Collection<CWBEquivalence> equivalences = matcher.getEquivalences(
				bpeURI.toString(), taginfoURI.toString());
		for (CWBEquivalence equivalence : equivalences) {
			System.out.println(equivalence.getConcept1().getFragment() + " = "
					+ equivalence.getConcept2().getFragment() + " ("
					+ equivalence.getConfidence() + ")");
		}

		YAM yamMatcher = YAM.getInstance();
		System.out.println(yamMatcher.align(
				"src/resources/ontologies/bpe/bpe.owl",
				"src/resources/ontologies/osm/taginfo/taginfo.owl"));
		
		*/

		long	endTime	=	System.currentTimeMillis();
		
		System.out.println("Running time = " + (endTime - startTime) + "ms");
		
	}

}
