package lig.steamer.cwb.io.write;

import java.io.File;

import lig.steamer.cwb.io.write.impl.exception.CWBFolksoWriterException;
import lig.steamer.cwb.model.CWBDataModelFolkso;

public interface CWBFolksoWriter {

	/**
	 * Writes the given folksonomy to the given output file name, in the given
	 * output format and with the given charset.
	 * @param folksonomy
	 * @param outputDir
	 * @param outputFilename
	 * @param outputFileFormat
	 * @param outputFileCharset
	 * @return file, the output file 
	 * @throws CWBFolksoWriterException
	 */
	public File write(CWBDataModelFolkso folksonomy, String outputDir,
			String outputFilename, String outputFileFormat,
			String outputFileCharset) throws CWBFolksoWriterException;

	/**
	 * Writes the given folksonomy to the given output file name, in the given
	 * output format.
	 * @param folksonomy
	 * @param outputDir
	 * @param outputFilename
	 * @param outputFileFormat
	 * @return file, the output file 
	 * @throws CWBFolksoWriterException
	 */
	public File write(CWBDataModelFolkso folksonomy, String outputDir,
			String outputFilename, String outputFileFormat)
			throws CWBFolksoWriterException;

	/**
	 * Writes the given folksonomy to the given output file name.
	 * @param folksonomy
	 * @param outputDir
	 * @param outputFilename
	 * @return file, the output file 
	 * @throws CWBFolksoWriterException
	 */
	public File write(CWBDataModelFolkso folksonomy, String outputDir,
			String outputFilename) throws CWBFolksoWriterException;

	/**
	 * Writes the given folksonomy.
	 * @param folksonomy
	 * @param folksonomy
	 * @return file, the output file 
	 * @throws CWBFolksoWriterException
	 */
	public File write(CWBDataModelFolkso folksonomy, String outputDir)
			throws CWBFolksoWriterException;

	/**
	 * Writes the given folksonomy.
	 * @param folksonomy
	 * @return file, the output file 
	 * @throws CWBFolksoWriterException
	 */
	public File write(CWBDataModelFolkso folksonomy)
			throws CWBFolksoWriterException;

}
