package lig.steamer.cwb.util.matching.impl;

// Alignment API classes
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
// Java standard classes
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.util.matching.CWBOntologyAlignmentVisitor;
import lig.steamer.cwb.util.matching.CWBOntologyMatcher;
import lig.steamer.cwb.util.matching.OntologyFormat;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Cell;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.tudarmstadt.ke.sw.matching.wikimatch.matcher.OaeiWikiMatch;
import fr.inrialpes.exmo.align.impl.URIAlignment;
// Alignment API implementation classes
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;

/**
 * @author Anthony Hombiat Ontology matcher that provides methods to generate
 * alignments between ontologies.
 */
public class WikimatchOntologyMatcher implements CWBOntologyMatcher {

	private static Logger LOGGER = Logger
			.getLogger(WikimatchOntologyMatcher.class.getName());

	private AlignmentProcess alignment;

	public WikimatchOntologyMatcher() {
		alignment = new WikimatchOntologyAlignment();
	}

	/**
	 * Matches the two ontologies referenced by the gievn URIs.
	 * @param ontologyUri1, the first ontology URI
	 * @param ontologyUri2, the second ontology URI
	 */
	public void match(URI ontologyUri1, URI ontologyUri2) {

		LOGGER.log(Level.INFO, "Matching the given ontologies...");

		URI onto1 = ontologyUri1;
		URI onto2 = ontologyUri2;

		try {
			// Run alignment
			alignment.init(onto1, onto2);
			alignment.align((Alignment) null, new Properties());

			LOGGER.log(Level.INFO, "Matching done.");

		} catch (AlignmentException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Matches the two ontologies referenced by the gievn URIs.
	 * @param ontologyUri1, the first ontology URI
	 * @param ontologyUri2, the second ontology URI
	 */
	public void match(String ontologyUri1, String ontologyUri2) {

		try {
			match(new URI(ontologyUri1), new URI(ontologyUri2));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

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
				+ Prop.FILENAME_ONTO_ALIGNMENT
				+ Prop.FMT_OWL;

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

	/**
	 * Prints the resulting alignment to the given output file name.
	 * @param outputFilename
	 */
	public void printAlignment(String outputFilename) {
		printAlignment(outputFilename, Prop.DEFAULT_ONTO_FMT,
				Prop.DEFAULT_CHARSET);
	}

	/**
	 * Prints the resulting alignment to the given output file name, in the
	 * given output format.
	 * @param outputFilename
	 * @param outputFileFormat
	 */
	public void printAlignment(String outputFilename, String outputFileFormat) {
		printAlignment(outputFilename, outputFileFormat, Prop.DEFAULT_CHARSET);
	}

	/**
	 * @return the alignment
	 */
	public AlignmentProcess getAlignment() {
		return alignment;
	}

	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(AlignmentProcess alignment) {
		this.alignment = alignment;
	}

	@Override
	public Collection<CWBEquivalence> getEquivalences(String sourceURI,
			String targetURI) {

		match(sourceURI, targetURI);

		CWBOntologyAlignmentVisitor visitor = new CWBOntologyAlignmentVisitor();
		try {
			alignment.accept(visitor);
		} catch (AlignmentException e) {
			e.printStackTrace();
		}

		return visitor.getEquivalences();
	}

	private class WikimatchOntologyAlignment extends URIAlignment implements
			AlignmentProcess {

		public WikimatchOntologyAlignment() {

		}

		/**
		 * Aligns two ontologies with the given reference Alignment and the
		 * given matcher properties.
		 * @param alignment, the reference alignment
		 * @param param, the matcher properties parameters
		 */
		public void align(Alignment alignment, Properties param)
				throws AlignmentException {

			OntModel onto1 = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM);
			onto1.read(getOntology1URI().toString());

			OntModel onto2 = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM);
			onto2.read(getOntology2URI().toString());

			OaeiWikiMatch matcher = new OaeiWikiMatch();
			Alignment result = matcher.align(onto1, onto2, new URIAlignment());

			Enumeration<Cell> cells = result.getElements();
			while (cells.hasMoreElements()) {

				Cell c = cells.nextElement();
				this.addAlignCell(c.getObject1(), c.getObject2(), c
						.getRelation().getRelation().toString(),
						c.getStrength());

				LOGGER.log(Level.INFO, c.getObject1AsURI().getFragment()
						.toString()
						+ " "
						+ c.getRelation().getRelation()
						+ " "
						+ c.getObject2AsURI().getFragment().toString()
						+ " ("
						+ String.format("%.2f", c.getStrength()) + ") ");
			}

		}
	}

}
