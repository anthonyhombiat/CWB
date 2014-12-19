package lig.steamer.cwb.io.write.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.write.CWBAlignmentWriter;
import lig.steamer.cwb.io.write.impl.exception.CWBAlignmentWriterException;
import lig.steamer.cwb.io.write.impl.exception.UnsupportedCWBAlignmentFormatException;
import lig.steamer.cwb.model.CWBAlignment;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.util.matching.CWBOntologyFormatEnum;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentVisitor;

import fr.inrialpes.exmo.align.impl.URIAlignment;
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;
import fr.inrialpes.exmo.align.impl.renderer.RDFRendererVisitor;

public class CWBAlignmentRDFWriter implements CWBAlignmentWriter {

	private static Logger LOGGER = Logger.getLogger(CWBAlignmentRDFWriter.class
			.getName());

	public static String EQUIVALENCE = "=";

	public CWBAlignmentRDFWriter() {

	}

	@Override
	public File write(CWBAlignment alignment, String outputDir,
			String outputFilename, String outputFileFormat,
			String outputFileCharset) throws CWBAlignmentWriterException {

		String path = outputDir + File.separatorChar + outputFilename + "." + outputFileFormat.toLowerCase();
		
		LOGGER.log(Level.INFO, "Printing alignment to " + path
				+ "...");

		Alignment align = new URIAlignment();
//		Alignment align = new ObjectAlignment();
		
		File file = null;
		
		try {
		
			file = new File(path);
			file.mkdir();
			
			for (CWBEquivalence equivalence : alignment.getEquivalences()) {
				
				align.addAlignCell(equivalence.getConcept1().getIri(),
						equivalence.getConcept2().getIri(), EQUIVALENCE,
						equivalence.getConfidence());
			}
			
			PrintWriter writer = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(path),
							outputFileCharset)), true);

			// Selecting the right renderer
			AlignmentVisitor renderer = null;
			switch (CWBOntologyFormatEnum.valueOf(outputFileFormat)) {
			case OWL:
				renderer = new OWLAxiomsRendererVisitor(writer);
				break;
			case RDF:
				renderer = new RDFRendererVisitor(writer);
				break;
			default:
				writer.close();
				throw new UnsupportedCWBAlignmentFormatException();
			}

			// Displays it as OWL Rules
			align.render(renderer);

			writer.flush();
			writer.close();

		} catch (UnsupportedEncodingException | FileNotFoundException | AlignmentException | UnsupportedCWBAlignmentFormatException e) {
			throw new CWBAlignmentWriterException(e);
		}

		LOGGER.log(Level.INFO, "Alignment printed to " + outputFilename + ".");
		
		return file;

	}

	public File write(CWBAlignment alignment, String outputDir,
			String outputFilename, String outputFileFormat)
			throws CWBAlignmentWriterException {
		return write(alignment, outputDir, outputFilename, outputFileFormat,
				Prop.DEFAULT_CHARSET);
	}

	public File write(CWBAlignment alignment, String outputDir,
			String outputFilename) throws CWBAlignmentWriterException {
		return write(alignment, outputDir, outputFilename,
				Prop.DEFAULT_ONTO_FMT, Prop.DEFAULT_CHARSET);
	}

	public File write(CWBAlignment alignment, String outputDir)
			throws CWBAlignmentWriterException {
		return write(alignment, outputDir, Prop.FILENAME_ALIGNMENT,
				Prop.DEFAULT_ONTO_FMT, Prop.DEFAULT_CHARSET);
	}

	public File write(CWBAlignment alignment) throws CWBAlignmentWriterException {
		return write(alignment, Prop.DIR_OUTPUT + File.separatorChar
				+ Prop.FILENAME_ALIGNMENT + Prop.FMT_OWL,
				Prop.DEFAULT_ONTO_FMT, Prop.DEFAULT_CHARSET);
	}

}
