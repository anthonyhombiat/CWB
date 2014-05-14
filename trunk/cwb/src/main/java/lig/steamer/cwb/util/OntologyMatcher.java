package lig.steamer.cwb.util;

// Alignment API classes
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Cell;

import de.tudarmstadt.ke.sw.matching.wikimatch.WikiMatch;

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
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OntologyMatcher {

	public static String DEFAULT_OUTPUT_DIRNAME = "src/resources/alignments/";
	public static String DEFAULT_FILENAME = "result.owl";
	public static String DEFAULT_CHARSET = StandardCharsets.UTF_8.toString();
	
	private static Logger LOGGER = Logger.getLogger("lig.steamer.cwb.io.ontologymatcher");
	
	private AlignmentProcess alignment;
	
	public OntologyMatcher(){
		alignment = new WikiMatch();
//		alignment = new StringDistAlignment();
	}
	
	public void match(String ontologyUri1, String ontologyUri2) {
		
		LOGGER.log(Level.INFO, "Matching the given ontologies...");
		
		URI onto1 = null;
		URI onto2 = null;

		Properties params = new Properties();

		try {
			// Loading ontologies
			onto1 = new URI(ontologyUri1);
			onto2 = new URI(ontologyUri2);
			
			// Run alignment
//			params.setProperty("stringFunction", "smoaDistance");
//			params.setProperty("stringFunction", "ngramDistance");
			alignment.init(onto1, onto2);
			alignment.align((Alignment) null, params);
			
			Enumeration<Cell> cells = alignment.getElements();
			while(cells.hasMoreElements()){
				Cell cell = cells.nextElement();
				URI uri1 = cell.getObject1AsURI();
				URI uri2 = cell.getObject2AsURI();
				System.out.println(uri1.getFragment()
						+ " equivalent to " + uri2.getFragment());
			}
			
			LOGGER.log(Level.INFO, "Matching done.");
			
		} catch (AlignmentException e) { e.printStackTrace();
		} catch (URISyntaxException e) { e.printStackTrace();
		} 
		
	}
	
	public void printAlignment(String outputFilename, String charset){
		
		PrintWriter writer;
		try {
			
			writer = new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(
									new FileOutputStream(DEFAULT_OUTPUT_DIRNAME + DEFAULT_FILENAME), charset)), true);
			
			// Displays it as OWL Rules
			AlignmentVisitor renderer = new OWLAxiomsRendererVisitor(writer);
			alignment.render(renderer);
			writer.flush();
			writer.close();
			
		} catch (UnsupportedEncodingException e) { e.printStackTrace();
		} catch (FileNotFoundException e) { e.printStackTrace();
		} catch (AlignmentException e) { e.printStackTrace();
		}
		
	}
	
	public void printAlignment(String outputFilename){
		printAlignment(outputFilename, DEFAULT_CHARSET);
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
