package lig.steamer.cwb.io.read.impl;

import java.net.URL;

import junit.framework.TestCase;
import lig.steamer.cwb.io.read.CWBFolksoReader;
import lig.steamer.cwb.io.read.impl.CWBFolksoOWLReader;
import lig.steamer.cwb.io.read.impl.exception.CWBFolksoReaderException;
import lig.steamer.cwb.model.CWBDataModelFolkso;

public class OWLFolksoReaderTest extends TestCase {

	private URL MOCK_FOLKSO_PATH = this.getClass().getResource("/lig/steamer/cwb/io/test/taginfo_amenity_100.owl");
	
	public OWLFolksoReaderTest(){
		
	}
	
	public void testRead() throws CWBFolksoReaderException{
		CWBFolksoReader reader = new CWBFolksoOWLReader();
		CWBDataModelFolkso folkso = reader.read(MOCK_FOLKSO_PATH.getPath());
	}
	
}
