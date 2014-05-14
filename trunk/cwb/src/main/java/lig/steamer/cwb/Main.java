package lig.steamer.cwb;

import lig.steamer.cwb.tagging.model.ITagSet;
import lig.steamer.cwb.util.OntologyMatcher;
import lig.steamer.cwb.util.Tag2OwlParser;
import lig.steamer.cwb.util.TagInfoClient;

public class Main {

	public static void main(String[] args) {

		// Consumes data from the OSM TagInfo Restful Web Service.
		// This Web Service is called to retrieve OSM tags values by their key.
		TagInfoClient tagInfoClient = new TagInfoClient();
		String [] keys = {"amenity","shop","leisure"}; 
		ITagSet tagSet = tagInfoClient.getTagsByKeys(keys);
//
//		// Parses the Tags previously retrieved into a tag ontology.
		Tag2OwlParser tag2owlParser = new Tag2OwlParser(TagInfoClient.OSM_TAG_INFO_URI);
		tag2owlParser.addTagSet(tagSet);
		tag2owlParser.printTagOntology(Tag2OwlParser.DEFAULT_FILENAME);

		// Matches the tag ontology previously created with the BPE nomenclature.
		OntologyMatcher matcher = new OntologyMatcher();
		matcher.match(
				"file:///d:/anthony_docs/workspace_kepler/cwb/src/resources/ontologies/bpe/bpe_test2.owl",
				"file:///d:/anthony_docs/workspace_kepler/cwb/src/resources/ontologies/osm/taginfo/taginfo_test2.owl");
		matcher.printAlignment(OntologyMatcher.DEFAULT_FILENAME);
	}

}
