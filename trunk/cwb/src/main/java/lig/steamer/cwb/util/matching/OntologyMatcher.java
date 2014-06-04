package lig.steamer.cwb.util.matching;

// Alignment API classes
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;

import com.google.gwt.thirdparty.guava.common.base.Charsets;

// Alignment API implementation classes
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

// Java standard classes
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Anthony Hombiat
 * Ontology matcher that provides methods to generate alignments between ontologies.
 */
public class OntologyMatcher {

	public static String DEFAULT_OUTPUT_DIRNAME = "src/resources/alignments/";
	public static String DEFAULT_OUTPUT_FILENAME = "result.owl";
	public static String DEFAULT_OUTPUT_CHARSET = Charsets.UTF_8.toString();
	public static String DEFAULT_OUTPUT_FORMAT = OntologyFormat.OWL.toString();
	
	private static Logger LOGGER = Logger.getLogger(OntologyMatcher.class.getName());
	
	private AlignmentProcess alignment;
	
	public OntologyMatcher(){
		alignment = new OntologyAlignment();
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

		Properties params = new Properties();

		try {
			// Run alignment
			alignment.init(onto1, onto2);
			alignment.align((Alignment) null, params);
			
			LOGGER.log(Level.INFO, "Matching done.");
			
		} catch (AlignmentException e) { e.printStackTrace();
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
	 * Prints the resulting alignment to the given output file name, 
	 * 		in the given output format and with the given charset.
	 * @param outputFilename
	 * @param outputFileFormat
	 * @param outputFileCharset
	 */
	public void printAlignment(String outputFilename, String outputFileFormat, String outputFileCharset) {
		
		LOGGER.log(Level.INFO, "Printing ontology...");
		
		try {
			
			PrintWriter writer = new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(
									new FileOutputStream(DEFAULT_OUTPUT_DIRNAME + DEFAULT_OUTPUT_FILENAME), outputFileCharset)), true);
			
			// Selecting the right renderer
			AlignmentVisitor renderer = null;
			switch(OntologyFormat.valueOf(outputFileFormat)){
				case OWL : renderer = new OWLAxiomsRendererVisitor(writer); break; 
				default : 
					writer.close();
					throw new Exception("Unsupported ontology format.");
			}
			
			// Displays it as OWL Rules
			alignment.render(renderer);
			
			writer.flush();
			writer.close();
			
		} catch (UnsupportedEncodingException e) { e.printStackTrace();
		} catch (FileNotFoundException e) { e.printStackTrace();
		} catch (AlignmentException e) { e.printStackTrace();
		} catch (Exception e) { e.printStackTrace();
		}
		
		LOGGER.log(Level.INFO, "Printing done in " + DEFAULT_OUTPUT_DIRNAME + DEFAULT_OUTPUT_FILENAME + ".");
		
	}
	
	/**
	 * Prints the resulting alignment to the given output file name.
	 * @param outputFilename
	 */
	public void printAlignment(String outputFilename){
		printAlignment(outputFilename, DEFAULT_OUTPUT_FORMAT, DEFAULT_OUTPUT_CHARSET);
	}
	
	/**
	 * Prints the resulting alignment to the given output file name, 
	 * 		in the given output format.
	 * @param outputFilename
	 * @param outputFileFormat
	 */
	public void printAlignment(String outputFilename, String outputFileFormat){
		printAlignment(outputFilename, outputFileFormat, DEFAULT_OUTPUT_CHARSET);
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
	
}
