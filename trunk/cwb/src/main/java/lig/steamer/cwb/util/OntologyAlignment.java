package lig.steamer.cwb.util;

import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.Cell;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.tudarmstadt.ke.sw.matching.wikimatch.matcher.OaeiWikiMatch;
import fr.inrialpes.exmo.align.impl.URIAlignment;

public class OntologyAlignment extends URIAlignment implements AlignmentProcess {

	private static Logger LOGGER = Logger
			.getLogger("lig.steamer.cwb.util.ontologyalignment");

	public OntologyAlignment() {

	}

	public void align(Alignment alignment, Properties param)
			throws AlignmentException {

		OntModel onto1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		onto1.read(getOntology1URI().toString());

		OntModel onto2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		onto2.read(getOntology2URI().toString());

		OaeiWikiMatch matcher = new OaeiWikiMatch();
		Alignment result = matcher.align(onto1, onto2, new URIAlignment());

		Enumeration<Cell> cells = result.getElements();
		while (cells.hasMoreElements()) {
			
			Cell c = cells.nextElement();
			this.addAlignCell(c.getObject1(), c.getObject2(), c.getRelation()
					.getRelation().toString(), c.getStrength());
			
			LOGGER.log(Level.INFO, c.getObject1AsURI().getFragment().toString()
					+ " " + c.getRelation().getRelation() + " "
					+ c.getObject2AsURI().getFragment().toString() + " ("
					+ String.format("%.2f", c.getStrength()) + ") ");
		}

	}
}
