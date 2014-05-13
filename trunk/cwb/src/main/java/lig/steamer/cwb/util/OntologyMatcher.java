package lig.steamer.cwb.util;

// Alignment API classes
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;

// Alignment API implementation classes
import fr.inrialpes.exmo.align.impl.method.StringDistAlignment;
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;

import java.io.FileOutputStream;
// Java standard classes
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OntologyMatcher {

	private static String DEFAULT_OUTPUT_DIRNAME = "src/resources/alignments/";
	
	private static Logger LOGGER = Logger.getLogger("lig.steamer.cwb.io.ontologymatcher");
	
	public OntologyMatcher(){
		
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
			AlignmentProcess alignment = new StringDistAlignment();
			params.setProperty("stringFunction", "smoaDistance");
//			params.setProperty("stringFunction", "ngramDistance");
			alignment.init(onto1, onto2);
			alignment.align((Alignment) null, params);
			
			PrintWriter writer = new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(
									new FileOutputStream(DEFAULT_OUTPUT_DIRNAME + "result.owl"), "UTF-8")), true);

			// Displays it as OWL Rules
			AlignmentVisitor renderer = new OWLAxiomsRendererVisitor(writer);
			alignment.render(renderer);
			writer.flush();
			writer.close();
			
			LOGGER.log(Level.INFO, "Matching done.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
