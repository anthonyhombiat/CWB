package lig.steamer.cwb.util.parser;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owlapi.model.IRI;

import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.core.tagging.ITag;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataProvider;

public class Tag2ConceptParser {
	
	private static Logger LOGGER = Logger
			.getLogger("lig.steamer.cwb.util.parser.tag2conceptparser");

	public static final String DEFAULT_FRAGMENT_SEPARATOR = "#";
	private String namespace;

	public Tag2ConceptParser(String namespace) {
		this.namespace = namespace;
	}

	public CWBDataModel parse(IFolksonomy folksonomy) {

		LOGGER.log(Level.INFO, "Parsing tags to concepts...");
		
		CWBDataProvider dataProvider = new CWBDataProvider(folksonomy.getSource().getName());
		
		CWBDataModel dataModel = new CWBDataModel(dataProvider);

		for (ITag tag : folksonomy.getTagSet().getTags()) {
			
			CWBConcept concept = new CWBConcept(IRI.create(namespace
					+ DEFAULT_FRAGMENT_SEPARATOR + tag.getValue().getString()));
			
			concept.addName(tag.getValue().getString(), new Locale(tag
					.getValue().getLanguage()));
			
			concept.addDescription(tag.getValue().getString(), new Locale(tag
					.getValue().getLanguage()));
			
			dataModel.addConcept(concept);
		}

		LOGGER.log(Level.INFO, "Parsing done.");
		
		return dataModel;
	}

}
