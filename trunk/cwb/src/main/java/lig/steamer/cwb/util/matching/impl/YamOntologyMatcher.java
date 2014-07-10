package lig.steamer.cwb.util.matching.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.util.matching.CWBOntologyAlignmentVisitor;
import lig.steamer.cwb.util.matching.CWBOntologyMatcher;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;

import yamSS.main.oaei.run.YAM;
import fr.inrialpes.exmo.align.parser.AlignmentParser;

public class YamOntologyMatcher implements CWBOntologyMatcher {

	private static Logger LOGGER = Logger.getLogger(YamOntologyMatcher.class
			.getName());

	public YamOntologyMatcher() {

	}

	@Override
	public Collection<CWBEquivalence> getEquivalences(String sourceURI,
			String targetURI) {
		Collection<CWBEquivalence> equivalences = new ArrayList<CWBEquivalence>();

		YAM matcher = YAM.getInstance();
		LOGGER.log(Level.INFO, "Matching " + sourceURI + " with "
				+ targetURI + "...");

		try {

			URL url = matcher.createTmpAlignmentFromSingleScenario(new File(
					sourceURI).toURI().toURL(), new File(targetURI).toURI()
					.toURL());
			System.out.println(url);
			AlignmentParser parser = new AlignmentParser(0);
			Alignment alignment = parser.parse(url.toURI());

			Enumeration<Cell> cells = alignment.getElements();

			while (cells.hasMoreElements()) {

				Cell c = cells.nextElement();

				LOGGER.log(Level.INFO, c.getObject1AsURI().getFragment()
						.toString()
						+ " "
						+ c.getRelation().getRelation()
						+ " "
						+ c.getObject2AsURI().getFragment().toString()
						+ " ("
						+ String.format("%.2f", c.getStrength()) + ") ");
			}

			CWBOntologyAlignmentVisitor visitor = new CWBOntologyAlignmentVisitor();
			alignment.accept(visitor);

			equivalences = visitor.getEquivalences();

		} catch (AlignmentException | MalformedURLException
				| URISyntaxException e) {
			e.printStackTrace();
		}
		
		LOGGER.log(Level.INFO, "Matching done.");

		return equivalences;
	}

}
