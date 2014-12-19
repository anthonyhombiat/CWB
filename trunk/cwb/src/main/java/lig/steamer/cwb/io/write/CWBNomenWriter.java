package lig.steamer.cwb.io.write;

import java.io.File;

import lig.steamer.cwb.io.write.impl.exception.CWBNomenWriterException;
import lig.steamer.cwb.model.CWBDataModelNomen;

public interface CWBNomenWriter {

	/**
	 * Writes the given nomenclature to the given directory, with the given file
	 * name, in the given output format and with the given charset.
	 * @param nomenclature
	 * @param outputDir
	 * @param outputFilename
	 * @param outputFileFormat
	 * @param outputFileCharset
	 * @return file, the output file
	 * @throws CWBNomenWriterException
	 */
	public File write(CWBDataModelNomen nomenclature, String outputDir,
			String outputFilename, String outputFileFormat,
			String outputFileCharset) throws CWBNomenWriterException;

	/**
	 * Writes the given nomenclature to the given directory, with the given file
	 * name, in the given output format.
	 * @param nomenclature
	 * @param outputDir
	 * @param outputFilename
	 * @param outputFileFormat
	 * @return file, the output file
	 * @throws CWBNomenWriterException
	 */
	public File write(CWBDataModelNomen nomenclature, String outputDir,
			String outputFilename, String outputFileFormat)
			throws CWBNomenWriterException;

	/**
	 * Writes the given nomenclature to the given directory, with the given file
	 * name.
	 * @param nomenclature
	 * @param outputDir
	 * @param outputFilename
	 * @return file, the output file
	 * @throws CWBNomenWriterException
	 */
	public File write(CWBDataModelNomen nomenclature, String outputDir,
			String outputFilename) throws CWBNomenWriterException;

	/**
	 * Writes the given nomenclature to the given directory.
	 * @param nomenclature
	 * @param outputDir
	 * @return file, the output file
	 * @throws CWBNomenWriterException
	 */
	public File write(CWBDataModelNomen nomenclature, String outputDir)
			throws CWBNomenWriterException;

	/**
	 * Writes the given nomenclature.
	 * @param nomenclature
	 * @return file, the output file
	 * @throws CWBNomenWriterException
	 */
	public File write(CWBDataModelNomen nomenclature)
			throws CWBNomenWriterException;
}
