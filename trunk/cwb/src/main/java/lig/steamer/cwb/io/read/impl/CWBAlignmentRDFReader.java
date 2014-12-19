package lig.steamer.cwb.io.read.impl;

import java.io.File;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.io.read.CWBAlignmentReader;
import lig.steamer.cwb.io.read.impl.exception.CWBAlignmentReaderException;
import lig.steamer.cwb.model.CWBAlignment;
import lig.steamer.cwb.util.matching.CWBOntologyAlignmentVisitor;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;

import fr.inrialpes.exmo.align.parser.AlignmentParser;

public class CWBAlignmentRDFReader implements CWBAlignmentReader {

	private static Logger LOGGER = Logger.getLogger(CWBAlignmentRDFReader.class
			.getName());
	
	public CWBAlignmentRDFReader(){
		
	}
	
	public CWBAlignment read(URI uri) throws CWBAlignmentReaderException{
		
		LOGGER.log(Level.INFO, "Reading alignment " + uri.getPath() + "...");
		
		CWBAlignment alignment = null;
		
		AlignmentParser parser = new AlignmentParser(0);
		Alignment align = null;
		
		try {
			align = parser.parse(uri);
			CWBOntologyAlignmentVisitor visitor = new CWBOntologyAlignmentVisitor();
			align.accept(visitor);
			
			alignment = new CWBAlignment(align.getOntology1URI(), align.getOntology2URI()); 
			alignment.addEquivalences(visitor.getEquivalences());
			
		} catch (AlignmentException e) {
			throw new CWBAlignmentReaderException(e);
		}
		
		LOGGER.log(Level.INFO, "Alignment read (" + alignment.getEquivalences().size() + " equivalences).");
		
		return alignment;
	}
	
	public CWBAlignment read(File file) throws CWBAlignmentReaderException{
		return read(file.toURI());
	}
	
	public CWBAlignment read(String filename) throws CWBAlignmentReaderException{
		return read(new File(filename));
	}
	
}
