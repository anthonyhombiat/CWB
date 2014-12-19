package lig.steamer.cwb.io.read;

import java.io.File;
import java.io.InputStream;

import lig.steamer.cwb.io.read.impl.exception.CWBNomenReaderException;
import lig.steamer.cwb.model.CWBDataModelNomen;

public interface CWBNomenReader {

	/**
	 * @param file, the file to read
	 * @return the nomenclature read from the given file
	 * @throws CWBNomenReaderException
	 */
	public CWBDataModelNomen read(File file) throws CWBNomenReaderException;
	
	/**
	 * @param inputStream, the inputStream to read
	 * @return the nomenclature read from the given file
	 * @throws CWBNomenReaderException
	 */
	public CWBDataModelNomen read(InputStream inputStream) throws CWBNomenReaderException;
	
	/**
	 * @param filename, the filename corresponding to the file to read
	 * @return the nomenclature read from the given file
	 * @throws CWBNomenReaderException
	 */
	public CWBDataModelNomen read(String filename)
			throws CWBNomenReaderException;
}
