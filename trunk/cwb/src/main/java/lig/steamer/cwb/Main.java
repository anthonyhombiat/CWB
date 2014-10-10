package lig.steamer.cwb;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import lig.steamer.cwb.io.read.exception.CWBDataModelReaderException;
import lig.steamer.cwb.io.write.CWBWriter;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.util.matching.CWBOntologyMatcher;
import lig.steamer.cwb.util.matching.impl.YamOntologyMatcher;
import lig.steamer.cwb.util.wsclient.taginfo.TaginfoWSClient;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoClientException;
import yamSS.main.oaei.run.YAM;

public class Main {

	public static void main(String[] args) throws CWBDataModelReaderException {

		URI sourceURI = URI
				.create("file:///d:/anthony_docs/workspace_kepler/cwb/src/resources/ontologies/bpe/bpe.owl");
		URI targetURI = URI
				.create("file:///d:/anthony_docs/workspace_kepler/cwb/src/resources/ontologies/osm/taginfo/taginfo.owl");

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
				sourceURI.toString(), targetURI.toString());
		for (CWBEquivalence equivalence : equivalences) {
			System.out.println(equivalence.getConcept1().getFragment() + " = "
					+ equivalence.getConcept2().getFragment() + " ("
					+ equivalence.getConfidence() + ")");
		}

		YAM yamMatcher = YAM.getInstance();
		System.out.println(yamMatcher.align(
				"src/resources/ontologies/bpe/bpe.owl",
				"src/resources/ontologies/osm/taginfo/taginfo.owl"));

	}

}
