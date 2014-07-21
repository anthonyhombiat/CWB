package lig.steamer.cwb.util.matching.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.util.matching.CWBOntologyAlignmentVisitor;
import lig.steamer.cwb.util.matching.CWBOntologyMatcher;
import lig.steamer.cwb.util.matching.OntologyFormat;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Cell;

import yamSS.main.oaei.run.YAM;
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;
import fr.inrialpes.exmo.align.parser.AlignmentParser;

public class YamOntologyMatcher implements CWBOntologyMatcher {

	private static Logger LOGGER = Logger.getLogger(YamOntologyMatcher.class
			.getName());

	private Alignment alignment;

	public YamOntologyMatcher() {

	}

	@Override
	public Collection<CWBEquivalence> getEquivalences(String sourceURI,
			String targetURI) {
		Collection<CWBEquivalence> equivalences = new ArrayList<CWBEquivalence>();

		YAM matcher = YAM.getInstance();
		LOGGER.log(Level.INFO, "Matching " + sourceURI + " with " + targetURI
				+ "...");

		try {

			URL url1 = new File(sourceURI).toURI().toURL();
			URL url2 = new File(targetURI).toURI().toURL();
			URL url = matcher.createTmpAlignmentFromSingleScenario(url1, url2);

			AlignmentParser parser = new AlignmentParser(0);
			alignment = parser.parse(url.toURI());

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

			alignment.setFile1(url1.toURI());
			alignment.setFile2(url2.toURI());

			equivalences = visitor.getEquivalences();

		} catch (AlignmentException | MalformedURLException
				| URISyntaxException e) {
			e.printStackTrace();
		}

		LOGGER.log(Level.INFO, "Matching done.");

		return equivalences;
	}

	/**
	 * Prints the resulting alignment to the given output file name, in the
	 * given output format and with the given charset.
	 * @param outputFilename
	 * @param outputFileFormat
	 * @param outputFileCharset
	 */
	public void printAlignment(String outputFilename, String outputFileFormat,
			String outputFileCharset) {

		String absolutePath = Prop.DIR_OUTPUT + File.separatorChar
				+ outputFilename + Prop.FMT_OWL;

		LOGGER.log(Level.INFO, "Printing alignment to " + absolutePath + "...");

		try {

			PrintWriter writer = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(absolutePath),
							outputFileCharset)), true);

			// Selecting the right renderer
			AlignmentVisitor renderer = null;
			switch (OntologyFormat.valueOf(outputFileFormat)) {
			case OWL:
				renderer = new OWLAxiomsRendererVisitor(writer);
				break;
			default:
				writer.close();
				throw new Exception("Unsupported ontology format.");
			}

			// Displays it as OWL Rules
			alignment.render(renderer);

			writer.flush();
			writer.close();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (AlignmentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.log(Level.INFO, "Alignment printed to " + absolutePath + ".");

	}

	public void printAlignment() {
		printAlignment(Prop.FILENAME_ONTO_ALIGNMENT,
				Prop.DEFAULT_ONTO_FMT, Prop.DEFAULT_CHARSET);
	}

}
