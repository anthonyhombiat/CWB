package lig.steamer.cwb.io.read.impl;

import java.net.URL;

import junit.framework.TestCase;
import lig.steamer.cwb.io.read.CWBNomenReader;
import lig.steamer.cwb.io.read.impl.CWBNomenOWLReader;
import lig.steamer.cwb.io.read.impl.exception.CWBNomenReaderException;
import lig.steamer.cwb.model.CWBDataModelNomen;

public class OWLNomenReaderTest extends TestCase {

	private URL MOCK_NOMEN_PATH = this.getClass().getResource("/lig/steamer/cwb/io/test/topo_test.owl");
	
	public OWLNomenReaderTest(){
		
	}
	
	public void testRead() throws CWBNomenReaderException{
		CWBNomenReader reader = new CWBNomenOWLReader();
		CWBDataModelNomen nomen = reader.read(MOCK_NOMEN_PATH.getPath());
	}
	
}
