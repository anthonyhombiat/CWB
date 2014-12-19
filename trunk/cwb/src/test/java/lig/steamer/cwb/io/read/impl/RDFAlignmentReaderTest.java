package lig.steamer.cwb.io.read.impl;

import java.net.URL;

import junit.framework.TestCase;
import lig.steamer.cwb.io.read.CWBAlignmentReader;
import lig.steamer.cwb.io.read.impl.CWBAlignmentRDFReader;
import lig.steamer.cwb.io.read.impl.exception.CWBAlignmentReaderException;
import lig.steamer.cwb.model.CWBAlignment;

public class RDFAlignmentReaderTest extends TestCase {

	private URL MOCK_ALIGNMENT_PATH = this.getClass().getResource("/lig/steamer/cwb/io/test/ign-topo-za_vs_taginfo-amenity-100.rdf");
	
	public RDFAlignmentReaderTest(){
		
	}
	
	public void testRead() throws CWBAlignmentReaderException{
		CWBAlignmentReader reader = new CWBAlignmentRDFReader();
		CWBAlignment alignment = reader.read(MOCK_ALIGNMENT_PATH.getPath());
		alignment.getEquivalences();
	}
	
}
