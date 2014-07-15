package lig.steamer.cwb.util.archive;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import lig.steamer.cwb.CWBProperties;

/**
 * Utility that zip or unzip files and directories.
 * @author Federico Paolantoni	
 * http://paolantoni.wordpress.com/2011/03/22/java-zip-and-unzip-function/
 */
public class ZipUtility {

	private static Logger LOGGER = Logger.getLogger(ZipUtility.class
			.getName());

	/**
	 * Create a zip file that contains the files passed trough the argument
	 * List<File>.
	 * @param Stirng destPath : if destination path is not specified (is null)
	 * creates a file in temporary directory.
	 * @param String name : the name of the zip file
	 * @param List<File> files to compress.
	 * @return the path of the file .zip.
	 */
	public static String zip(String destPath, String name, List<File> files) {

		int BUFFER = 2048;
		String fileName = null;

		// if destination path is not specified creates a file in temporary
		// directory
		if (destPath == null) {
			destPath = CWBProperties.CWB_TMP_DIR;
		}

		if (destPath.endsWith("\\") || destPath.endsWith("/")) {
			fileName = String.format("%s%s.%s", destPath, name, "zip");
		} else {
			fileName = String.format("%s%s%s.%s", destPath, File.separator,
					name, "zip");
		}

		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(fileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));

			out.setMethod(ZipOutputStream.DEFLATED);
			byte data[] = new byte[BUFFER];

			if (files != null) {

				for (int i = 0; i < files.size(); i++) {
					LOGGER.log(Level.INFO, "Checking read permission of file "
							+ files.get(i).canRead());
					LOGGER.log(Level.INFO, "Adding: "
							+ files.get(i).getAbsolutePath());

					FileInputStream fi = new FileInputStream(files.get(i)
							.getAbsolutePath());
					origin = new BufferedInputStream(fi);
					ZipEntry entry = new ZipEntry(files.get(i).getName());
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
					out.flush();
				}
			} else {
				LOGGER.log(Level.SEVERE,
						"doZip(String , List<File>) : Cannot create zip from NULL files");

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * Extract zip source to a folder and return the path of the folder
	 * @param File zipSource
	 * @return File the Directory with extracted files.
	 */
	public static File unzip(File zipSource) {

		ZipFile zipfile;
		Enumeration e;
		String tempDirName = null;
		FileOutputStream fos = null;
		File fe = null;
		byte data[] = null;
		ZipEntry entry = null;
		String name = "";
		String baseTempPath = "";

		if (zipSource != null && zipSource.length() != 0) {

			int BUFFER = 2048;
			Calendar cal = Calendar.getInstance();
			try {

				name = String.format("%s", cal.getTimeInMillis());
				baseTempPath = CWBProperties.CWB_TMP_DIR + File.separatorChar;
				tempDirName = String.format("%s%s", baseTempPath, name);
				new File(tempDirName).mkdir();

				zipfile = new ZipFile(zipSource);
				e = zipfile.entries();

				while (e.hasMoreElements()) {

					entry = (ZipEntry) e.nextElement();

					if (entry.isDirectory()) {
						new File(tempDirName, entry.getName()).mkdirs();
					} else {

						BufferedOutputStream dest = null;
						BufferedInputStream is = null;
						System.out.println("Extracting: " + entry);
						is = new BufferedInputStream(
								zipfile.getInputStream(entry));
						int count;
						data = new byte[BUFFER];
						fe = new File(tempDirName, entry.getName());
						fos = new FileOutputStream(fe);
						dest = new BufferedOutputStream(fos, BUFFER);

						while ((count = is.read(data, 0, BUFFER)) != -1) {
							dest.write(data, 0, count);
						}

						dest.flush();
						dest.close();
						is.close();
						fos.close();
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				LOGGER.log(Level.SEVERE, "doUnzip(File) exception " + ex);
				System.err.println("doUnzip(File) exception " + ex);
			}

		}
		LOGGER.log(Level.INFO, "doUnZip(File) return dir " + tempDirName);
		return new File(tempDirName);
	}
}
