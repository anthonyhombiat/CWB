package lig.steamer.cwb.io.write.impl;

import java.io.File;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.write.CWBFolksoWriter;
import lig.steamer.cwb.io.write.impl.exception.CWBFolksoWriterException;
import lig.steamer.cwb.model.CWBDataModelFolkso;

import org.semanticweb.owlapi.model.IRI;

public class CWBFolksoOWLWriter implements CWBFolksoWriter {

	@Override
	public File write(CWBDataModelFolkso folksonomy, String outputDir,
			String outputFilename, String outputFileFormat,
			String outputFileCharset) throws CWBFolksoWriterException {

		File file = new File(outputDir + File.separatorChar + outputFilename
				+ outputFileFormat);

		file.mkdir();
		
		folksonomy.acceptCWBDataModelVisitor(new CWBDataModelOWLRenderer(IRI
				.create(file)));

		return file;

	}

	@Override
	public File write(CWBDataModelFolkso folksonomy, String outputDir,
			String outputFilename, String outputFileFormat)
			throws CWBFolksoWriterException {
		return write(folksonomy, outputDir, outputFilename, outputFileFormat,
				Prop.DEFAULT_CHARSET);
	}

	@Override
	public File write(CWBDataModelFolkso folksonomy, String outputDir,
			String outputFilename) throws CWBFolksoWriterException {
		return write(folksonomy, outputDir, outputFilename, Prop.FMT_OWL,
				Prop.DEFAULT_CHARSET);
	}

	@Override
	public File write(CWBDataModelFolkso folksonomy, String outputDir)
			throws CWBFolksoWriterException {
		return write(folksonomy, outputDir, Prop.FILENAME_FOLKSO, Prop.FMT_OWL,
				Prop.DEFAULT_CHARSET);
	}

	@Override
	public File write(CWBDataModelFolkso folksonomy)
			throws CWBFolksoWriterException {
		return write(folksonomy, Prop.DIR_OUTPUT, Prop.FILENAME_FOLKSO,
				Prop.FMT_OWL, Prop.DEFAULT_CHARSET);
	}

}
