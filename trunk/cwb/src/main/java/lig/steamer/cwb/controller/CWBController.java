package lig.steamer.cwb.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.read.CWBAlignmentReader;
import lig.steamer.cwb.io.read.CWBFolksoReader;
import lig.steamer.cwb.io.read.CWBNomenReader;
import lig.steamer.cwb.io.read.impl.CWBAlignmentRDFReader;
import lig.steamer.cwb.io.read.impl.CWBFolksoOWLReader;
import lig.steamer.cwb.io.read.impl.CWBNomenOWLReader;
import lig.steamer.cwb.io.read.impl.exception.CWBAlignmentReaderException;
import lig.steamer.cwb.io.read.impl.exception.CWBFolksoReaderException;
import lig.steamer.cwb.io.read.impl.exception.CWBNomenReaderException;
import lig.steamer.cwb.io.write.CWBFolksoWriter;
import lig.steamer.cwb.io.write.CWBNomenWriter;
import lig.steamer.cwb.io.write.impl.CWBAlignmentRDFWriter;
import lig.steamer.cwb.io.write.impl.CWBFolksoOWLWriter;
import lig.steamer.cwb.io.write.impl.CWBNomenOWLWriter;
import lig.steamer.cwb.io.write.impl.exception.CWBAlignmentWriterException;
import lig.steamer.cwb.io.write.impl.exception.CWBFolksoWriterException;
import lig.steamer.cwb.io.write.impl.exception.CWBNomenWriterException;
import lig.steamer.cwb.model.CWBAlignment;
import lig.steamer.cwb.model.CWBBuffer;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.model.CWBStudyArea;
import lig.steamer.cwb.ui.AppUI;
import lig.steamer.cwb.ui.window.CWBAboutWindow;
import lig.steamer.cwb.util.archive.ZipUtility;
import lig.steamer.cwb.util.browser.BrowserHomepageProvider;
import lig.steamer.cwb.util.browser.UnsupportedBrowserException;
import lig.steamer.cwb.util.matching.impl.YamOntologyMatcher;
import lig.steamer.cwb.util.wsclient.DataModelFolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.DataModelNomenProviderWSClient;
import lig.steamer.cwb.util.wsclient.WSDataModelFolksoProvider;
import lig.steamer.cwb.util.wsclient.WSDataModelNomenProvider;
import lig.steamer.cwb.util.wsclient.WSDatasetFolksoProvider;
import lig.steamer.cwb.util.wsclient.WSDatasetNomenProvider;
import lig.steamer.cwb.util.wsclient.bdtopo.BDTopoWSClient;
import lig.steamer.cwb.util.wsclient.exception.WSDataModelFolksoClientException;
import lig.steamer.cwb.util.wsclient.exception.WSDataModelNomenClientException;
import lig.steamer.cwb.util.wsclient.exception.WSDatasetFolksoClientException;
import lig.steamer.cwb.util.wsclient.exception.WSDatasetNomenClientException;
import lig.steamer.cwb.util.wsclient.overpass.OverpassWSClient;
import lig.steamer.cwb.util.wsclient.taginfo.TaginfoWSClient;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class CWBController implements Serializable {

	private static final long serialVersionUID = 1L;

	private CWBModel model;
	private AppUI view;

	public CWBController(CWBModel model, AppUI view) {

		this.model = model;
		this.view = view;

		/** Menu item listeners */

		// File
		view.addOpenMenuItemCommand(new CWBOpenMenuItemCommand());
		view.addSaveMenuItemCommand(new CWBSaveMenuItemCommand());
		view.addCloseMenuItemCommand(new CWBCloseMenuItemCommand());
		view.addLogoutMenuItemCommand(new CWBLogoutMenuItemCommand());

		// Data model
		view.addLoadFolksoFromWSMenuItemCommand(new CWBLoadFolksoFromWSMenuItemCommand());
		view.addLoadNomenFromWSMenuItemCommand(new CWBLoadNomenFromWSMenuItemCommand());
		view.addLoadAlignFromWSMenuItemCommand(new CWBLoadAlignFromWSMenuItemCommand());
		view.addLoadFolksoFromFileMenuItemCommand(new CWBLoadFolksoFromFileMenuItemCommand());
		view.addLoadNomenFromFileMenuItemCommand(new CWBLoadNomenFromFileMenuItemCommand());
		view.addLoadAlignFromFileMenuItemCommand(new CWBLoadAlignFromFileMenuItemCommand());

		// Map
		view.addStudyAreaMenuItemCommand(new CWBStudyAreaItemCommand());
		view.addDataProviderMenuItemCommand(new CWBDataProviderMenuItemCommand());
		view.addBufferOptionsMenuItemCommand(new CWBBufferOptionsMenuItemCommand());

		// Window
		view.addMapMenuItemCommand(new CWBMapMenuItemCommand());
		view.addModelMenuItemCommand(new CWBModelMenuItemCommand());

		// About
		view.addDocMenuItemCommand(new CWBDocMenuItemCommand());
		view.addAboutMenuItemCommand(new CWBAboutMenuItemCommand());

		/** Window listeners */

		// Data model windows listeners
		view.addLoadFolksoFromWSWindowButtonListener(new CWBLoadFolksoButtonListener());
		view.addLoadNomenFromWSWindowButtonListener(new CWBLoadNomenButtonListener());
		view.addFolksoWSComboBoxListener(new CWBFolksoWSComboBoxListener());
		view.addNomenWSComboBoxListener(new CWBNomenWSComboBoxListener());

		// Map
		view.addDataProviderWindowButtonListener(new CWBDataProviderWindowButtonListener());
		view.addStudyAreaWindowButtonListener(new CWBStudyAreaWindowButtonListener());
		view.addBufferOptionsWindowButtonListener(new CWBBufferOptionsWindowButtonListener());
		view.addStudyAreaComboboxValueChangedListener(new CWBStudyAreaComboBoxListener());
		view.addDataProviderFolksoComboboxValueChangedListener(new CWBDataProviderFolksoComboBoxListener());
		view.addDataProviderNomenComboboxValueChangedListener(new CWBDataProviderNomenComboBoxListener());

		// Upload receivers
		view.addOpenProjectUploadReceiver(new CWBOpenProjectUploader());
		view.addOpenProjectUploadSucceededListener(new CWBOpenProjectUploader());
		view.addLoadNomenFromFileUploadReceiver(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadSucceededListener(new CWBLoadNomenFromFileUploader());
		view.addLoadFolksoFromFileUploadReceiver(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadSucceededListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadAlignFromFileUploadReceiver(new CWBLoadAlignFromFileUploader());
		view.addLoadAlignFromFileUploadSucceededListener(new CWBLoadAlignFromFileUploader());

		/** Matching */
		view.addMatchButtonListener(new CWBMatchButtonListener());
		view.addMatchingResultsTableValueChangedListener(new CWBMatchingResultsTableValueChangeListener());
		view.addMatchingResultsTableCheckboxesListener(new CWBMatchingResultsTableCheckboxListener());

		/** Map listeners */
		view.addMapMoveEndListener(new CWBMoveEndListener());

	}

	/*********************/
	/*** MENU COMMANDS ***/
	/*********************/

	class CWBOpenMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {

			if (!model.isEmpty()) {
				ConfirmDialog.show(view, Msg.get("confirm.close.capt"),
						Msg.get("confirm.close.txt"),
						Msg.get("confirm.close.ok"),
						Msg.get("confirm.close.ko"),
						new ConfirmDialog.Listener() {

							private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									view.getSaveMenuItem()
											.getCommand()
											.menuSelected(
													view.getSaveMenuItem());
								}
								UI.getCurrent().addWindow(
										view.getOpenProjectWindow());
							}
						});
			} else {
				UI.getCurrent().addWindow(view.getOpenProjectWindow());
			}
		}

	}

	class CWBSaveMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("deprecation")
		@Override
		public void menuSelected(MenuItem selectedItem) {

			if (!model.isEmpty()) {

				try {

					File projectRootDir = new File(Prop.DIR_TMP
							+ File.separatorChar + Prop.DEFAULT_PROJECT_NAME);
					projectRootDir.mkdir();

					File zipFile = new File(Prop.DIR_OUTPUT
							+ File.separatorChar + Prop.DEFAULT_PROJECT_NAME
							+ Prop.FMT_CWB);

					File dataModelsDir = new File(projectRootDir.getAbsolutePath()
							+ File.separatorChar + Prop.DIRNAME_DATAMODELS);
					dataModelsDir.mkdir();
						
					// Writing nomenclature
					File nomenDir = new File(dataModelsDir.getAbsolutePath() + File.separatorChar
							+ Prop.DIRNAME_NOMEN);
					nomenDir.mkdir();

					CWBNomenWriter nomenWriter = new CWBNomenOWLWriter();
					nomenWriter.write(model.getNomenclature(), nomenDir.getAbsolutePath());

					// Writing folksonomy
					File folksoDir = new File(dataModelsDir.getAbsolutePath() + File.separatorChar
							+ Prop.DIRNAME_FOLKSO);
					folksoDir.mkdir();
					
					CWBFolksoWriter folksoWriter = new CWBFolksoOWLWriter();
					folksoWriter.write(model.getFolksonomy(), folksoDir.getAbsolutePath());

					// Writing alignment
					File alignmentDir = new File(dataModelsDir.getAbsolutePath()
							+ File.separatorChar + Prop.DIRNAME_ALIGN);
					alignmentDir.mkdir();

					CWBAlignmentRDFWriter alignementWriter = new CWBAlignmentRDFWriter();
					alignementWriter.write(model.getAlignment(), alignmentDir.getAbsolutePath());

					ZipUtility zipUtil = new ZipUtility();
					zipUtil.zipDirectory(projectRootDir,
							zipFile.getAbsolutePath());

					FileUtils.deleteDirectory(projectRootDir);

				} catch (CWBNomenWriterException e) {
					e.printStackTrace();
					view.err(Msg.get("notif.err.nomen.write.capt"),
							Msg.get("notif.err.nomen.write.txt"));
				} catch (CWBFolksoWriterException e) {
					e.printStackTrace();
					view.err(Msg.get("notif.err.folkso.write.capt"),
							Msg.get("notif.err.folkso.write.txt"));
				} catch (CWBAlignmentWriterException e) {
					e.printStackTrace();
					view.err(Msg.get("notif.err.align.write.capt"),
							Msg.get("notif.err.align.write.txt"));
				} catch (IOException e) {
					e.printStackTrace();
					view.err(Msg.get("notif.err.save.capt"),
							Msg.get("notif.err.save.txt"));
				}
				
				FileResource resource = new FileResource(new File(
						Prop.DIR_OUTPUT + File.separatorChar
								+ Prop.DEFAULT_PROJECT_NAME + Prop.FMT_CWB));

				Page.getCurrent().open(resource, "http://cwb.imag.fr/download",
						false);

				view.info(Msg.get("notif.info.save.capt"),
						Msg.get("notif.info.save.txt"));
			} else {
				view.info(Msg.get("notif.err.empty.capt"),
						Msg.get("notif.err.empty.txt"));
			}

		}

	}

	class CWBCloseMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {

			if (!model.isEmpty()) {
				ConfirmDialog.show(view, Msg.get("confirm.close.capt"),
						Msg.get("confirm.close.txt"),
						Msg.get("confirm.close.ok"),
						Msg.get("confirm.close.ko"),
						new ConfirmDialog.Listener() {

							private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									view.getSaveMenuItem()
											.getCommand()
											.menuSelected(
													view.getSaveMenuItem());
								}

								model.init();

								view.info(Msg.get("notif.info.close.capt"),
										Msg.get("notif.info.close.txt"));
							}
						});
			}

		}

	}

	class CWBLogoutMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {

			if (!model.isEmpty()) {
				ConfirmDialog.show(view, Msg.get("confirm.close.capt"),
						Msg.get("confirm.close.txt"),
						Msg.get("confirm.close.ok"),
						Msg.get("confirm.close.ko"),
						new ConfirmDialog.Listener() {

							private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									view.getSaveMenuItem()
											.getCommand()
											.menuSelected(
													view.getSaveMenuItem());
								} else {
									view.getUI().getSession().close();
									try {
										Page.getCurrent().setLocation(
												BrowserHomepageProvider
														.getUrl());
									} catch (UnsupportedBrowserException e) {
										view.err(
												Msg.get("notif.err.browser.capt"),
												Msg.get("notif.err.browser.txt"));
									}
								}
							}
						});
			} else {
				view.getUI().getSession().close();
				try {
					Page.getCurrent().setLocation(
							BrowserHomepageProvider.getUrl());
				} catch (UnsupportedBrowserException e) {
					view.err(Msg.get("notif.err.browser.capt"),
							Msg.get("notif.err.browser.txt"));
				}
			}

		}
	}

	class CWBLoadFolksoFromWSMenuItemCommand implements Command, ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadFolksoFromWSWindow());
		}

		@Override
		public void buttonClick(ClickEvent event) {
			UI.getCurrent().addWindow(view.getLoadFolksoFromWSWindow());
		}

	}

	class CWBLoadNomenFromWSMenuItemCommand implements Command, ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadNomenFromWSWindow());
		}

		@Override
		public void buttonClick(ClickEvent event) {
			UI.getCurrent().addWindow(view.getLoadNomenFromWSWindow());
		}

	}

	class CWBLoadAlignFromWSMenuItemCommand implements Command, ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadAlignFromWSWindow());
		}

		@Override
		public void buttonClick(ClickEvent event) {
			UI.getCurrent().addWindow(view.getLoadAlignFromWSWindow());
		}

	}

	class CWBLoadFolksoFromFileMenuItemCommand implements Command,
			ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadFolksoFromFileWindow());
		}

		@Override
		public void buttonClick(ClickEvent event) {
			UI.getCurrent().addWindow(view.getLoadFolksoFromFileWindow());
		}

	}

	class CWBLoadNomenFromFileMenuItemCommand implements Command, ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadNomenFromFileWindow());
		}

		@Override
		public void buttonClick(ClickEvent event) {
			UI.getCurrent().addWindow(view.getLoadNomenFromFileWindow());
		}

	}

	class CWBLoadAlignFromFileMenuItemCommand implements Command, ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadAlignFromFileWindow());
		}

		@Override
		public void buttonClick(ClickEvent event) {
			UI.getCurrent().addWindow(view.getLoadAlignFromFileWindow());
		}

	}

	class CWBStudyAreaItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getSelectStudyAreaWindow());
		}

	}

	class CWBDataProviderMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getSelectDataProviderWindow());
		}

	}

	class CWBBufferOptionsMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getBufferOptionsWindow());
		}

	}

	class CWBMapMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			view.getMapPanel().setVisible(selectedItem.isChecked());
		}

	}
	
	class CWBModelMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			view.getModelLayout().setVisible(selectedItem.isChecked());
		}

	}

	class CWBDocMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {

			JavaScript.getCurrent().execute(
					"window.open('" + Msg.get("doc.url") + "', '_blank')");
		}

	}

	class CWBAboutMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(new CWBAboutWindow());
		}

	}

	class CWBOpenProjectUploader implements Receiver, SucceededListener {

		private static final long serialVersionUID = 1L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			File file = null;
			FileOutputStream fos = null;

			try {

				file = new File(Prop.DIR_TMP + File.separator
						+ Prop.FILENAME_TMP + Prop.FMT_CWB);
				fos = new FileOutputStream(file);

			} catch (FileNotFoundException e) {
				view.err(Msg.get("notif.err.file.read.capt"), MessageFormat
						.format(Msg.get("notif.err.file.read.txt"),
								file.getAbsolutePath()));
				return null;
			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			File file = new File(Prop.DIR_TMP + File.separator
					+ Prop.FILENAME_TMP + Prop.FMT_CWB);

			try {

				String destinationPath = Prop.DIR_TMP + File.separatorChar
						+ Prop.DEFAULT_PROJECT_NAME;

				// Unziping CWB project
				ZipUtility zipUtil = new ZipUtility();
				zipUtil.unzip(file.getAbsolutePath(), destinationPath);

				String dataModelsDir = destinationPath + File.separatorChar
						+ Prop.DIRNAME_DATAMODELS;

				// Adding nomenclature to the CWB model
				File nomen = new File(dataModelsDir + File.separatorChar
						+ Prop.DIRNAME_NOMEN + File.separatorChar
						+ Prop.FILENAME_NOMEN + Prop.FMT_OWL);

				CWBNomenReader nomenReader = new CWBNomenOWLReader();
				model.setNomenclature(nomenReader.read(nomen));

				// Adding folksonomy to the CWB model
				File folkso = new File(dataModelsDir + File.separatorChar
						+ Prop.DIRNAME_FOLKSO + File.separatorChar
						+ Prop.FILENAME_FOLKSO + Prop.FMT_OWL);

				CWBFolksoReader folksoReader = new CWBFolksoOWLReader();
				model.setFolksonomy(folksoReader.read(folkso));

				// Adding alignment to the CWB model
				File alignment = new File(dataModelsDir + File.separatorChar
						+ Prop.DIRNAME_ALIGN + File.separatorChar
						+ Prop.FILENAME_ALIGNMENT + Prop.FMT_RDF);

				CWBAlignmentReader alignmentReader = new CWBAlignmentRDFReader();
				model.setAlignment(alignmentReader.read(alignment));

				// Removing temp file
				file.delete();
				FileUtils.deleteDirectory(new File(destinationPath));

			} catch (IOException e) {
				view.err(Msg.get("notif.err.open.capt"),
						Msg.get("notif.err.open.txt"));
			} catch (CWBNomenReaderException e) {
				view.err(Msg.get("notif.err.nomen.read.capt"),
						Msg.get("notif.err.nomen.read.txt"));
			} catch (CWBFolksoReaderException e) {
				view.err(Msg.get("notif.err.folkso.read.capt"),
						Msg.get("notif.err.folkso.read.txt"));
				e.printStackTrace();
			} catch (CWBAlignmentReaderException e) {
				view.err(Msg.get("notif.err.align.read.capt"),
						Msg.get("notif.err.align.read.txt"));
			}

			view.info(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"));

			// Close pop-up window
			view.getOpenProjectWindow().close();

		}
	}

	class CWBLoadNomenFromFileUploader implements Receiver, SucceededListener {

		private static final long serialVersionUID = 1L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			File file = null;
			FileOutputStream fos = null;

			try {

				file = new File(Prop.DIR_TMP + File.separator
						+ Prop.FILENAME_TMP + Prop.FMT_OWL);
				fos = new FileOutputStream(file);

			} catch (FileNotFoundException e) {
				view.err(Msg.get("notif.err.file.read.capt"), MessageFormat
						.format(Msg.get("notif.err.file.read.txt"),
								file.getAbsolutePath()));
				return null;
			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			File file = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_TMP + Prop.FMT_OWL);

			CWBNomenReader reader = new CWBNomenOWLReader();
			CWBDataModelNomen nomen = null;

			try {
				nomen = reader.read(file);
			} catch (CWBNomenReaderException e) {
				view.err(Msg.get("notif.err.nomen.read.capt"),
						Msg.get("notif.err.nomen.read.txt"));
			}

			// Update model
			model.setNomenclature(nomen);

			// Notify
			view.info(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"));

			// Close pop-up window
			view.getLoadNomenFromFileWindow().close();

		}
	}

	class CWBLoadFolksoFromFileUploader implements Receiver, SucceededListener {

		private static final long serialVersionUID = 1L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			File file = null;
			FileOutputStream fos = null;

			try {

				file = new File(Prop.DIR_TMP + File.separator
						+ Prop.FILENAME_TMP + Prop.FMT_OWL);
				fos = new FileOutputStream(file);

			} catch (FileNotFoundException e) {
				view.err(Msg.get("notif.err.file.read.capt"), MessageFormat
						.format(Msg.get("notif.err.file.read.txt"),
								file.getAbsolutePath()));
				return null;
			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			File file = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_TMP + Prop.FMT_OWL);

			CWBFolksoReader reader = new CWBFolksoOWLReader();
			CWBDataModelFolkso folkso = null;
			try {
				folkso = reader.read(file);
			} catch (CWBFolksoReaderException e) {
				view.err(Msg.get("notif.err.folkso.read.capt"),
						Msg.get("notif.err.folkso.read.txt"));
			}

			// Update model
			model.setFolksonomy(folkso);

			// Notify
			view.info(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"));

			// Close pop-up window
			view.getLoadFolksoFromFileWindow().close();

		}
	}

	class CWBLoadAlignFromFileUploader implements Receiver, SucceededListener {

		private static final long serialVersionUID = 1L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			File file = null;
			FileOutputStream fos = null;

			try {

				file = new File(Prop.DIR_TMP + File.separator
						+ Prop.FILENAME_TMP + Prop.FMT_RDF);
				fos = new FileOutputStream(file);

			} catch (FileNotFoundException e) {
				view.err(Msg.get("notif.err.file.read.capt"), MessageFormat
						.format(Msg.get("notif.err.file.read.txt"),
								file.getAbsolutePath()));
				return null;
			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			File file = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_TMP + Prop.FMT_RDF);

			CWBAlignmentReader reader = new CWBAlignmentRDFReader();
			CWBAlignment alignment = null;
			try {
				alignment = reader.read(file);
			} catch (CWBAlignmentReaderException e) {
				view.err(Msg.get("notif.err.nomen.read.capt"),
						Msg.get("notif.err.nomen.read.txt"));
			}

			// Update model
			model.setAlignment(alignment);

			// Notify
			view.info(Msg.get("notif.info.align.done.capt"),
					Msg.get("notif.info.align.done.txt"));

			// Close pop-up window
			view.getLoadAlignFromFileWindow().close();

		}
	}

	/***********************/
	/*** LOAD TAG WINDOW ***/
	/***********************/

	class CWBLoadFolksoButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			WSDataModelFolksoProvider selectedWS = (WSDataModelFolksoProvider) view
					.getFolksoWSCombobox().getValue();

			DataModelFolksoProviderWSClient client = null;
			CWBDataModelFolkso folksonomy = null;

			switch (selectedWS) {
			case TAGINFO:
				client = new TaginfoWSClient();
				break;
			default:
				return;
			}

			try {
				folksonomy = client.getDataModelFolkso();
			} catch (WSDataModelFolksoClientException e) {
				view.err(Msg.get("notif.err.ws.folkso.model.capt"),
						MessageFormat.format(
								Msg.get("notif.err.ws.folkso.model.txt"),
								selectedWS.toString()));
			}

			// Update model
			model.setFolksonomy(folksonomy);

			// Notify
			view.info(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"));

			// Close pop-up window
			view.getLoadFolksoFromWSWindow().close();

		}

	}

	class CWBLoadNomenButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			WSDataModelNomenProvider selectedWS = (WSDataModelNomenProvider) view
					.getNomenWSCombobox().getValue();

			DataModelNomenProviderWSClient client = null;
			CWBDataModelNomen nomen = null;

			try {

				switch (selectedWS) {
				case BDTOPO:
					client = new BDTopoWSClient();
					break;
				default:
					return;
				}

				nomen = client.getDataModelNomen();

			} catch (WSDataModelNomenClientException e) {
				view.err(Msg.get("notif.err.ws.nomen.model.capt"),
						MessageFormat.format(
								Msg.get("notif.err.ws.nomen.model.txt"),
								selectedWS.toString()));
			}

			// Update model
			model.setNomenclature(nomen);

			// Notify
			view.info(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"));

			// Close pop-up window
			view.getLoadNomenFromWSWindow().close();

		}

	}

	class CWBFolksoWSComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			view.getLoadFolksoFromWSButton().setEnabled(
					event.getProperty().getValue() != null);
		}

	}

	class CWBNomenWSComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			view.getLoadNomenFromWSButton().setEnabled(
					event.getProperty().getValue() != null);
		}

	}

	class CWBStudyAreaComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			view.getStudyAreaOkButton().setEnabled(
					event.getProperty().getValue() != null);
		}

	}

	class CWBDataProviderNomenComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {

			view.getDataProviderOkButton()
					.setEnabled(
							event.getProperty().getValue() != null
									&& view.getDataProviderFolksoComboBox()
											.getValue() != null);
		}

	}

	class CWBDataProviderFolksoComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {

			view.getDataProviderOkButton()
					.setEnabled(
							event.getProperty().getValue() != null
									&& view.getDataProviderNomenComboBox()
											.getValue() != null);
		}

	}

	class CWBDataProviderWindowButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			WSDatasetNomenProvider nomenProvider = (WSDatasetNomenProvider) view
					.getDataProviderNomenComboBox().getValue();
			WSDatasetFolksoProvider folksoProvider = (WSDatasetFolksoProvider) view
					.getDataProviderFolksoComboBox().getValue();

			switch (nomenProvider) {
			case BDTOPO:
				model.setDatasetNomenProvider(new BDTopoWSClient());
			}

			switch (folksoProvider) {
			case OVERPASS:
				model.setDatasetFolksoProvider(new OverpassWSClient());
			}

			// TODO reload instances

			view.getSelectDataProviderWindow().close();

			view.info(Msg.get("notif.info.map.provider.capt"),
					Msg.get("notif.info.map.provider.txt"));
		}

	}

	class CWBStudyAreaWindowButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			CWBStudyArea studyArea = (CWBStudyArea) view.getStudyAreaComboBox()
					.getValue();

			model.setStudyArea(studyArea);

			Object o = view.getAlignPanel().getTable().getValue();
			if (o != null && o instanceof CWBEquivalence) {
				// CWBEquivalence equivId =
				// view.getAlignPanel().getDataModelContainer().getBeanIdResolver().getIdForBean((CWBEquivalence)
				// o);
				view.getAlignPanel().getTable().select((CWBEquivalence) o);
			}

			view.getSelectStudyAreaWindow().close();

			view.info(Msg.get("notif.info.map.area.capt"), MessageFormat
					.format(Msg.get("notif.info.map.area.txt"),
							studyArea.toString()));
		}

	}

	class CWBBufferOptionsWindowButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			CWBBuffer buffer = new CWBBuffer();
			buffer.setSize(view.getBufferSizeSlider().getValue());
			buffer.setVisible(view.getBufferDisplayCheckBox().getValue());

			model.setBuffer(buffer);

			view.getBufferOptionsWindow().close();

			view.info(Msg.get("notif.info.map.buffer.capt"),
					Msg.get("notif.info.map.buffer.txt"));
		}

	}

	class CWBMoveEndListener implements LeafletMoveEndListener {

		@Override
		public void onMoveEnd(LeafletMoveEndEvent event) {

//			if (view.getMap().getZoomLevel() < CWBMapPanel.DEFAULT_ZOOM_LEVEL) {
//				view.getMap().setZoomLevel(CWBMapPanel.DEFAULT_ZOOM_LEVEL);
//			}

		}

	}

	/***********************/
	/*** MATCHING WINDOW ***/
	/***********************/

	class CWBMatchButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			CWBDataModelNomen nomen = model.getNomenclature();
			CWBDataModelFolkso folkso = model.getFolksonomy();

			CWBNomenWriter nomenWriter = new CWBNomenOWLWriter();
			CWBFolksoWriter folksoWriter = new CWBFolksoOWLWriter();

			File nomenFile = null;
			File folksoFile = null;

			try {
				nomenFile = nomenWriter.write(nomen);
				folksoFile = folksoWriter.write(folkso);
			} catch (CWBNomenWriterException e) {
				view.err(Msg.get("notif.err.nomen.write.capt"),
						Msg.get("notif.err.nomen.write.txt"));
			} catch (CWBFolksoWriterException e) {
				view.err(Msg.get("notif.err.folkso.write.capt"),
						Msg.get("notif.err.folkso.write.txt"));
			}

			// WikimatchOntologyMatcher matcher = new
			// WikimatchOntologyMatcher();
			// matcher.getEquivalences(
			// writer2.getFile().toURI().toString(), writer1.getFile()
			// .toURI().toString());

			YamOntologyMatcher matcher = new YamOntologyMatcher();
			Collection<CWBEquivalence> equivalences = matcher.getEquivalences(
					nomenFile.getAbsolutePath(), folksoFile.getAbsolutePath());

			// matcher.printAlignment();
			model.addEquivalences(equivalences);

			// Notify
			view.info(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"));

		}
	}

	class CWBMatchingResultsTableValueChangeListener implements
			ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {

			System.out.println("value change listener called !");

			Object item = event.getProperty().getValue();

			model.removeAllInstancesFolkso();
			model.removeAllInstancesNomen();

			if (item instanceof CWBEquivalence) {

				CWBEquivalence equivalence = (CWBEquivalence) item;

				try {

					CWBConcept conceptNomen = model.getNomenclature()
							.getConceptFromIRI(
									equivalence.getConcept1().getIri());

					CWBConcept conceptFolkso = model.getFolksonomy()
							.getConceptFromIRI(
									equivalence.getConcept2().getIri());

					if (conceptNomen != null) {
						model.addInstancesNomen(model.getDatasetNomenProvider()
								.getNomenInstances(conceptNomen,
										model.getStudyArea().getBBox()));
						view.info(Msg.get("notif.info.nomen.data.capt"),
								MessageFormat.format(
										Msg.get("notif.info.nomen.data.txt"),
										model.getInstancesNomen().size()));
					} else {
						view.err(
								Msg.get("notif.err.align.nomen.capt"),
								MessageFormat.format(
										Msg.get("notif.err.align.nomen.txt"),
										equivalence.getConcept1().getFragment()));
					}

					if (conceptFolkso != null) {
						model.addInstancesFolkso(model
								.getDatasetFolksoProvider().getFolksoInstances(
										conceptFolkso,
										model.getStudyArea().getBBox()));
						view.info(Msg.get("notif.info.folkso.data.capt"),
								MessageFormat.format(
										Msg.get("notif.info.folkso.data.txt"),
										model.getInstancesFolkso().size()));
					} else {
						view.err(
								Msg.get("notif.err.align.folkso.capt"),
								MessageFormat.format(
										Msg.get("notif.err.align.folkso.txt"),
										equivalence.getConcept2().getFragment()));
					}

				} catch (WSDatasetFolksoClientException e) {
					view.err(
							Msg.get("notif.err.ws.folkso.data.capt"),
							MessageFormat.format(
									Msg.get("notif.err.ws.folkso.data.txt"),
									model.getDatasetFolksoProvider().toString()));
				} catch (WSDatasetNomenClientException e) {
					view.err(Msg.get("notif.err.ws.nomen.data.capt"),
							MessageFormat.format(
									Msg.get("notif.err.ws.nomen.data.txt"),
									model.getDatasetNomenProvider().toString()));
				}

				// select equivalence 1st concept in nomenclature
				Iterator<?> itNomen = view.getNomenPanel().getTable()
						.getItemIds().iterator();
				while (itNomen.hasNext()) {
					CWBConcept currentConcept = (CWBConcept) itNomen.next();
					if (((CWBConcept) currentConcept).equals(equivalence
							.getConcept1())) {
						view.getNomenPanel().getTable().select(currentConcept);
						break;
					}
				}

				// select equivalence 1st concept in folksonomy
				Iterator<?> itFolkso = view.getFolksoPanel().getTable()
						.getItemIds().iterator();
				while (itFolkso.hasNext()) {
					CWBConcept currentConcept = (CWBConcept) itFolkso.next();
					if (((CWBConcept) currentConcept).equals(equivalence
							.getConcept2())) {
						view.getFolksoPanel().getTable().select(currentConcept);
						break;
					}
				}

			}
		}
	}

	class CWBMatchingResultsTableCheckboxListener implements
			ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {

			Object property = event.getProperty();

			if (property instanceof CheckBox) {

				CheckBox checkbox = (CheckBox) property;

				Iterator<?> it = view.getAlignPanel().getTable().getItemIds()
						.iterator();

				while (it.hasNext()) {

					Object currentElement = it.next();

					if (currentElement instanceof CWBEquivalence) {
						if (currentElement.toString().equals(
								checkbox.getStyleName())) {
							if (checkbox.getValue()) {
								model.addSelectedEquivalence((CWBEquivalence) currentElement);
							} else {
								model.removeSelectedEquivalence((CWBEquivalence) currentElement);
							}
							System.out.println("equivalences selected:");
							for (CWBEquivalence selectedEquiv : model
									.getSelectedEquivalences()) {
								System.out.println(selectedEquiv);
							}
							break;
						}
					}
				}
			}
		}
	}

}
