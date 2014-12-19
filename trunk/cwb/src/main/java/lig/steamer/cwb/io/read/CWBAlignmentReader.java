package lig.steamer.cwb.io.read;

import java.io.File;

import lig.steamer.cwb.io.read.impl.exception.CWBAlignmentReaderException;
import lig.steamer.cwb.model.CWBAlignment;

public interface CWBAlignmentReader {
	
	/**
	 * @param file, the file to read
	 * @return the alignment read from the given file
	 * @throws CWBAlignmentReaderException
	 */
	public CWBAlignment read(File file) throws CWBAlignmentReaderException;
	
	/**
	 * @param filename, the filename corresponding to the file to be read
	 * @return the alignment read from the given file
	 * @throws CWBAlignmentReaderException
	 */
	public CWBAlignment read(String filename) throws CWBAlignmentReaderException;
	
}
