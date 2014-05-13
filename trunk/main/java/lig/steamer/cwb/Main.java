package lig.steamer.cwb;

import lig.steamer.cwb.tagging.model.ITagSet;
import lig.steamer.cwb.util.OntologyMatcher;
import lig.steamer.cwb.util.Tag2OwlParser;
import lig.steamer.cwb.util.TagInfoClient;

public class Main {

	public static void main(String[] args) {

		TagInfoClient tagInfoClient = new TagInfoClient();
		String [] keys = {"amenity","shop","leisure"}; 
		ITagSet tagSet = tagInfoClient.getTagsByKeys(keys, "fr");

		Tag2OwlParser tag2owlParser = new Tag2OwlParser(TagInfoClient.OSM_TAG_INFO_URI);
		tag2owlParser.addTagSet(tagSet);
		
		tag2owlParser.printTagOntology("osm_taginfo_ontology.owl");

		OntologyMatcher matcher = new OntologyMatcher();
		matcher.match(
				"file:///d:/anthony_docs/workspace_kepler/cwb/src/resources/ontologies/bpe.owl",
				"file:///d:/anthony_docs/workspace_kepler/cwb/src/resources/ontologies/osm_taginfo_ontology.owl");
	}

}
