package lig.steamer.cwb.io.write;

import java.io.File;

import lig.steamer.cwb.io.write.impl.exception.CWBAlignmentWriterException;
import lig.steamer.cwb.model.CWBAlignment;

public interface CWBAlignmentWriter {

	/**
	 * Writes the given alignment to the given output file name, in the given
	 * output format and with the given charset.
	 * @param alignment
	 * @param outputDir
	 * @param outputFilename
	 * @param outputFileFormat
	 * @param outputFileCharset
	 * @return file, the output file 
	 * @throws CWBAlignmentWriterException
	 */
	public File write(CWBAlignment alignment, String outputDir,
			String outputFilename, String outputFileFormat,
			String outputFileCharset) throws CWBAlignmentWriterException;

	/**
	 * Writes the given alignment to the given output file name, in the given
	 * output format.
	 * @param alignment
	 * @param outputDir
	 * @param outputFilename
	 * @param outputFileFormat
	 * @return file, the output file 
	 * @throws CWBAlignmentWriterException
	 */
	public File write(CWBAlignment alignment, String outputDir,
			String outputFilename, String outputFileFormat)
			throws CWBAlignmentWriterException;

	/**
	 * Writes the given alignment to the given output file name.
	 * @param alignment
	 * @param outputDir
	 * @param outputFilename
	 * @return file, the output file 
	 * @throws CWBAlignmentWriterException
	 */
	public File write(CWBAlignment alignment, String outputDir,
			String outputFilename) throws CWBAlignmentWriterException;

	/**
	 * Writes the given alignment.
	 * @param alignment
	 * @param outputDir
	 * @return file, the output file 
	 * @throws CWBAlignmentWriterException
	 */
	public File write(CWBAlignment alignment, String outputDir)
			throws CWBAlignmentWriterException;

	/**
	 * Writes the given alignment.
	 * @param alignment
	 * @return file, the output file 
	 * @throws CWBAlignmentWriterException
	 */
	public File write(CWBAlignment alignment) throws CWBAlignmentWriterException;
}
