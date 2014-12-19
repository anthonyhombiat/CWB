package lig.steamer.cwb.io.read;

import java.io.File;
import java.io.InputStream;

import lig.steamer.cwb.io.read.impl.exception.CWBFolksoReaderException;
import lig.steamer.cwb.model.CWBDataModelFolkso;

public interface CWBFolksoReader {

	/**
	 * @param file, the file to read
	 * @return the folksonomy read from the given file
	 * @throws CWBFolksoReaderException
	 */
	public CWBDataModelFolkso read(File file) throws CWBFolksoReaderException;

	/**
	 * @param inputStream, the inputStream to read
	 * @return the folksonomy read from the given file
	 * @throws CWBFolksoReaderException
	 */
	public CWBDataModelFolkso read(InputStream inputStream)
			throws CWBFolksoReaderException;
	
	/**
	 * @param filename, the filename corresponding to the file to read
	 * @return the folksonomy read from the given file
	 * @throws CWBFolksoReaderException
	 */
	public CWBDataModelFolkso read(String filename)
			throws CWBFolksoReaderException;

}
