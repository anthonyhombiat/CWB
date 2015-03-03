package lig.steamer.cwb.io.write.impl;

import java.io.File;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.write.CWBNomenWriter;
import lig.steamer.cwb.io.write.impl.exception.CWBNomenWriterException;
import lig.steamer.cwb.model.CWBDataModelNomen;

import org.semanticweb.owlapi.model.IRI;

public class CWBNomenOWLWriter implements CWBNomenWriter {

	@Override
	public File write(CWBDataModelNomen nomenclature, String outputDir,
			String outputFilename, String outputFileFormat,
			String outputFileCharset) throws CWBNomenWriterException {

		File file = new File(outputDir + File.separatorChar + outputFilename
				+ outputFileFormat);

		if (nomenclature != null && !nomenclature.getConcepts().isEmpty()) {
			nomenclature.acceptCWBDataModelVisitor(new CWBDataModelOWLRenderer(
					IRI.create(file)));
		}

		return file;
	}

	@Override
	public File write(CWBDataModelNomen nomenclature, String outputDir,
			String outputFilename, String outputFileFormat)
			throws CWBNomenWriterException {
		return write(nomenclature, outputDir, outputFilename, outputFileFormat,
				Prop.DEFAULT_CHARSET);
	}

	@Override
	public File write(CWBDataModelNomen nomenclature, String outputDir,
			String outputFilename) throws CWBNomenWriterException {
		return write(nomenclature, outputDir, outputFilename, Prop.FMT_OWL,
				Prop.DEFAULT_CHARSET);
	}

	@Override
	public File write(CWBDataModelNomen nomenclature, String outputDir)
			throws CWBNomenWriterException {
		return write(nomenclature, outputDir, Prop.FILENAME_NOMEN,
				Prop.FMT_OWL, Prop.DEFAULT_CHARSET);
	}

	@Override
	public File write(CWBDataModelNomen nomenclature)
			throws CWBNomenWriterException {
		return write(nomenclature, Prop.DIR_OUTPUT, Prop.FILENAME_NOMEN,
				Prop.FMT_OWL, Prop.DEFAULT_CHARSET);
	}

}
