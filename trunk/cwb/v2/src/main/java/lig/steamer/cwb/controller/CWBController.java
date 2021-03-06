package lig.steamer.cwb.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.Prop;
import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.io.read.CWBDataModelFolksoReader;
import lig.steamer.cwb.io.read.CWBDataModelNomenReader;
import lig.steamer.cwb.io.read.CWBDataModelReader;
import lig.steamer.cwb.io.read.CWBReader;
import lig.steamer.cwb.io.read.exception.CWBDataModelReaderException;
import lig.steamer.cwb.io.read.exception.CWBReaderException;
import lig.steamer.cwb.io.write.CWBWriter;
import lig.steamer.cwb.io.write.exception.CWBWriterException;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelMatched;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.AppUI;
import lig.steamer.cwb.ui.window.CWBAboutWindow;
import lig.steamer.cwb.util.browser.BrowserHomepageProvider;
import lig.steamer.cwb.util.browser.UnsupportedBrowserException;
import lig.steamer.cwb.util.matching.impl.YamOntologyMatcher;
import lig.steamer.cwb.util.parser.Tag2OwlParser;
import lig.steamer.cwb.util.wsclient.FolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.TaggingWS;
import lig.steamer.cwb.util.wsclient.exception.FolksoProviderWSClientException;
import lig.steamer.cwb.util.wsclient.overpass.OverpassClient;
import lig.steamer.cwb.util.wsclient.taginfo.TaginfoClient;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class CWBController implements Serializable {

	private static final long serialVersionUID = 1L;

	private CWBModel model;
	private AppUI view;

	public CWBController(CWBModel model, AppUI view) {

		this.model = model;
		this.view = view;

		view.addOpenMenuItemCommand(new CWBOpenMenuItemCommand());
		view.addSaveMenuItemCommand(new CWBSaveMenuItemCommand());
		view.addCloseMenuItemCommand(new CWBCloseMenuItemCommand());
		view.addDocMenuItemCommand(new CWBDocMenuItemCommand());
		view.addAboutMenuItemCommand(new CWBAboutMenuItemCommand());
		view.addLogoutMenuItemCommand(new CWBLogoutMenuItemCommand());
		view.addLoadFolksoFromWSMenuItemCommand(new CWBLoadFolksoFromWSMenuItemCommand());
		view.addLoadFolksoFromFileMenuItemCommand(new CWBLoadFolksoFromFileMenuItemCommand());
		view.addLoadNomenFromFileMenuItemCommand(new CWBLoadNomenFromFileMenuItemCommand());
		view.addDataModelsMenuItemCommand(new CWBDataModelsMenuItemCommand());
		view.addIndicatorsMenuItemCommand(new CWBIndicatorsMenuItemCommand());
		view.addMapMenuItemCommand(new CWBMapMenuItemCommand());
		view.addMatchMenuItemCommand(new CWBMatchMenuItemCommand());

		view.addOpenProjectUploadReceiver(new CWBOpenProjectUploader());
		view.addOpenProjectUploadStartedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadFinishedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadProgressListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadFailedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadSucceededListener(new CWBOpenProjectUploader());
		view.addOpenProjectDropBoxDropHandler(new CWBOpenProjectDropHandler());
		view.addLoadFolksoButtonListener(new CWBLoadTagButtonListener());
		view.addTagWSComboBoxListener(new CWBTagWSComboBoxListener());
		view.addLoadFolksoFromFileUploadReceiver(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadStartedListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadFinishedListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadProgressListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadFailedListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadSucceededListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileDropBoxDropHandler(new CWBLoadFolksoFromFileDropHandler());
		view.addLoadNomenFromFileUploadReceiver(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadStartedListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadFinishedListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadProgressListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadFailedListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadSucceededListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileDropBoxDropHandler(new CWBLoadNomenFromFileDropHandler());
		view.addMatchingWindowTableValueChangeListener(new CWBMatchingTableValueChangeListener());
		view.addMatchingWindowButtonClickListener(new CWBMatchingButtonListener());
		view.addMatchingResultsWindowButtonClickListener(new CWBMatchingResultsButtonListener());
		view.addMatchingResultsWindowTableValueChangeListener(new CWBMatchingResultsTableValueChangeListener());
		view.addLoadFolksoFromWSButtonListener(new CWBLoadFolksoFromWSMenuItemCommand());
		view.addLoadFolksoFromFileButtonListener(new CWBLoadFolksoFromFileMenuItemCommand());
		view.addLoadNomenFromFileButtonListener(new CWBLoadNomenFromFileMenuItemCommand());

	}

	/*********************/
	/*** MENU COMMANDS ***/
	/*********************/

	class CWBOpenMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			if (!model.isEmpty()) {
				ConfirmDialog.show(view, Msg.get("confirm.close.caption"),
						Msg.get("confirm.close.text"),
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

			CWBWriter writer = new CWBWriter();
			try {
				writer.write(model);
			} catch (CWBWriterException e) {
				Notification.show(Msg.get("notif.error.save.title"),
						Msg.get("notif.error.save.text"),
						Notification.Type.ERROR_MESSAGE);
			}

			FileResource resource = new FileResource(new File(Prop.DIR_OUTPUT
					+ File.separatorChar + Prop.DEFAULT_PROJECT_NAME
					+ Prop.FMT_CWB));

			Page.getCurrent().open(resource, "http://cwb.imag.fr/download",
					false);

		}

	}

	class CWBCloseMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {

			if (!model.isEmpty()) {
				ConfirmDialog.show(view, Msg.get("confirm.close.caption"),
						Msg.get("confirm.close.text"),
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
								model = new CWBModel();
								view.clear();
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
				ConfirmDialog.show(view, Msg.get("confirm.close.caption"),
						Msg.get("confirm.close.text"),
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
										e.printStackTrace();
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
					e.printStackTrace();
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

	class CWBLoadFolksoFromFileMenuItemCommand implements Command, ClickListener {

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

	class CWBDataModelsMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			view.getDataModelsPanel().setVisible(selectedItem.isChecked());
			view.getLeftLayout().setVisible(
					view.getIndicatorsPanel().isVisible()
							|| view.getDataModelsPanel().isVisible());
		}

	}

	class CWBIndicatorsMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			view.getIndicatorsPanel().setVisible(selectedItem.isChecked());
			view.getLeftLayout().setVisible(
					view.getIndicatorsPanel().isVisible()
							|| view.getDataModelsPanel().isVisible());
		}

	}

	class CWBMapMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			view.getTabSheet().setVisible(selectedItem.isChecked());
		}

	}

	class CWBMatchMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {

			view.getMatchingWindowTableContainer().removeAllItems();
			view.getMatchingWindowTableContainer()
					.addAll(model.getDataModels());

			UI.getCurrent().addWindow(view.getMatchWindow());
		}

	}

	class CWBOpenProjectUploader implements Receiver, ProgressListener,
			FailedListener, SucceededListener, StartedListener,
			FinishedListener {

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
				Notification.show(
						Msg.get("notif.error.file.read.title"),
						MessageFormat.format(
								Msg.get("notif.error.file.read.text"),
								file.getAbsolutePath()),
						Notification.Type.ERROR_MESSAGE);
				return null;
			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			String path = Prop.DIR_TMP + File.separator + Prop.FILENAME_TMP
					+ Prop.FMT_CWB;

			CWBReader reader = new CWBReader();
			try {
				model = reader.read(path);
			} catch (CWBReaderException e) {
				Notification.show(Msg.get("notif.error.open.title"),
						Msg.get("notif.error.open.text"),
						Notification.Type.ERROR_MESSAGE);
			}

			view.clear();

			for (CWBDataModel dataModel : model.getDataModels()) {
				// Update view
				view.getDataModelsPanel().addDataModel(dataModel);
			}

			view.getDataModelsPanelAccordion().setSelectedTab(
					view.getDataModelsPanelAccordion().getComponentCount() - 1);

			// Notify
			Notification.show(Msg.get("notif.info.load.done.title"),
					Msg.get("notif.info.load.done.text"),
					Notification.Type.HUMANIZED_MESSAGE);

			// Close pop-up window
			view.getOpenProjectWindow().close();

		}

		@Override
		public void uploadFinished(FinishedEvent event) {
		}

		@Override
		public void uploadFailed(FailedEvent event) {
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
		}

		@Override
		public void uploadStarted(StartedEvent event) {
		}
	}

	class CWBOpenProjectDropHandler implements DropHandler {

		private static final long serialVersionUID = 1L;

		private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB

		@Override
		public void drop(final DragAndDropEvent dropEvent) {

			// expecting this to be an html5 drag
			final WrapperTransferable tr = (WrapperTransferable) dropEvent
					.getTransferable();
			final Html5File[] files = tr.getFiles();
			if (files != null) {
				for (final Html5File html5File : files) {
					final String fileName = html5File.getFileName();

					if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
						Notification
								.show("File rejected. Max 2Mb files are accepted by Sampler",
										Notification.Type.WARNING_MESSAGE);
					} else {

						final ByteArrayOutputStream bas = new ByteArrayOutputStream();
						final StreamVariable streamVariable = new StreamVariable() {

							private static final long serialVersionUID = 1L;

							@Override
							public OutputStream getOutputStream() {
								return bas;
							}

							@Override
							public boolean listenProgress() {
								return false;
							}

							@Override
							public void onProgress(
									final StreamingProgressEvent event) {
							}

							@Override
							public void streamingStarted(
									final StreamingStartEvent event) {
							}

							@Override
							public void streamingFinished(
									final StreamingEndEvent event) {

								// progress.setVisible(false);

								final StreamSource streamSource = new StreamSource() {
									//
									private static final long serialVersionUID = 1L;

									@SuppressWarnings("unused")
									@Override
									public InputStream getStream() {
										if (bas != null) {
											final byte[] byteArray = bas
													.toByteArray();
											return new ByteArrayInputStream(
													byteArray);
										}
										return null;
									}
								};
								final StreamResource resource = new StreamResource(
										streamSource, fileName);

								InputStream is = resource.getStreamSource()
										.getStream();

								String path = Prop.DIR_TMP + File.separator
										+ Prop.DEFAULT_PROJECT_NAME
										+ Prop.FMT_CWB;

								File f = new File(path);

								try {
									OutputStream os = new FileOutputStream(f);
									int read = 0;
									byte[] bytes = new byte[1024];

									while ((read = is.read(bytes)) != -1) {

										os.write(bytes, 0, read);

									}

									os.close();
								} catch (IOException e) {
									e.printStackTrace();
								}

								CWBReader reader = new CWBReader();
								try {
									model = reader.read(f.getAbsolutePath());
								} catch (CWBReaderException e) {
									Notification.show(
											Msg.get("notif.error.open.title"),
											Msg.get("notif.error.open.text"),
											Notification.Type.ERROR_MESSAGE);
								}

								view.clear();

								for (CWBDataModel dataModel : model
										.getDataModels()) {
									// Update view
									view.getDataModelsPanel().addDataModel(
											dataModel);
								}

								view.getDataModelsPanelAccordion()
										.setSelectedTab(
												view.getDataModelsPanelAccordion()
														.getComponentCount() - 1);

								// Notify
								Notification.show(
										Msg.get("notif.info.load.done.title"),
										Msg.get("notif.info.load.done.text"),
										Notification.Type.HUMANIZED_MESSAGE);

								// Close pop-up window
								view.getOpenProjectWindow().close();

							}

							@Override
							public void streamingFailed(
									final StreamingErrorEvent event) {
								// progress.setVisible(false);
							}

							@Override
							public boolean isInterrupted() {
								return false;
							}
						};
						html5File.setStreamVariable(streamVariable);
						// progress.setVisible(true);
					}
				}

			}
		}

		@Override
		public AcceptCriterion getAcceptCriterion() {
			return AcceptAll.get();
		}
	}

	class CWBLoadNomenFromFileUploader implements Receiver, ProgressListener,
			FailedListener, SucceededListener, StartedListener,
			FinishedListener {

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
				Notification.show(
						Msg.get("notif.error.file.read.title"),
						MessageFormat.format(
								Msg.get("notif.error.file.read.text"),
								file.getAbsolutePath()),
						Notification.Type.ERROR_MESSAGE);
				return null;
			}

			return fos;
		}

		@Override
		public void uploadFinished(FinishedEvent event) {

		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			File file = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_TMP + Prop.FMT_OWL);

			CWBDataModelReader reader = new CWBDataModelNomenReader();
			CWBDataModel dataModel = null;
			try {
				dataModel = reader.read(file);
			} catch (CWBDataModelReaderException e) {
				Notification.show(Msg.get("notif.error.datamodel.read.title"),
						Msg.get("notif.error.datamodel.read.text"),
						Notification.Type.ERROR_MESSAGE);
			}

			// Update model
			if (model.addDataModel(dataModel)) {

				// Update view
				view.getDataModelsPanel().addDataModel(dataModel);

				view.getDataModelsPanelAccordion()
						.setSelectedTab(
								view.getDataModelsPanelAccordion()
										.getComponentCount() - 1);

				// Notify
				Notification.show(Msg.get("notif.info.load.done.title"),
						Msg.get("notif.info.load.done.text"),
						Notification.Type.HUMANIZED_MESSAGE);
			} else {
				Notification.show(Msg.get("notif.error.datamodel.add.title"),
						Msg.get("notif.error.datamodel.add.text"),
						Notification.Type.ERROR_MESSAGE);
			}

			// Close pop-up window
			view.getLoadNomenFromFileWindow().close();

		}

		@Override
		public void uploadFailed(FailedEvent event) {

		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {

		}

		@Override
		public void uploadStarted(StartedEvent event) {

		}
	}

	class CWBLoadNomenFromFileDropHandler implements DropHandler {

		private static final long serialVersionUID = 1L;

		private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB

		@Override
		public void drop(final DragAndDropEvent dropEvent) {

			// expecting this to be an html5 drag
			final WrapperTransferable tr = (WrapperTransferable) dropEvent
					.getTransferable();
			final Html5File[] files = tr.getFiles();
			if (files != null) {
				for (final Html5File html5File : files) {
					final String fileName = html5File.getFileName();

					if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
						Notification
								.show("File rejected. Max 2Mb files are accepted by Sampler",
										Notification.Type.WARNING_MESSAGE);
					} else {

						final ByteArrayOutputStream bas = new ByteArrayOutputStream();
						final StreamVariable streamVariable = new StreamVariable() {

							private static final long serialVersionUID = 1L;

							@Override
							public OutputStream getOutputStream() {
								return bas;
							}

							@Override
							public boolean listenProgress() {
								return false;
							}

							@Override
							public void onProgress(
									final StreamingProgressEvent event) {
							}

							@Override
							public void streamingStarted(
									final StreamingStartEvent event) {
							}

							@Override
							public void streamingFinished(
									final StreamingEndEvent event) {

								// progress.setVisible(false);

								final StreamSource streamSource = new StreamSource() {
									//
									private static final long serialVersionUID = 1L;

									@SuppressWarnings("unused")
									@Override
									public InputStream getStream() {
										if (bas != null) {
											final byte[] byteArray = bas
													.toByteArray();
											return new ByteArrayInputStream(
													byteArray);
										}
										return null;
									}
								};
								final StreamResource resource = new StreamResource(
										streamSource, fileName);

								InputStream is = resource.getStreamSource()
										.getStream();

								CWBDataModelReader reader = new CWBDataModelNomenReader();
								CWBDataModel dataModel = null;
								try {
									dataModel = reader.read(is);
								} catch (CWBDataModelReaderException e) {
									Notification
											.show(Msg
													.get("notif.error.datamodel.read.title"),
													Msg.get("notif.error.datamodel.read.text"),
													Notification.Type.ERROR_MESSAGE);
								}

								// Update model
								if (model.addDataModel(dataModel)) {

									// Update view
									view.getDataModelsPanel().addDataModel(
											dataModel);

									view.getDataModelsPanelAccordion()
											.setSelectedTab(
													view.getDataModelsPanelAccordion()
															.getComponentCount() - 1);

									// Notify
									Notification
											.show(Msg
													.get("notif.info.load.done.title"),
													Msg.get("notif.info.load.done.text"),
													Notification.Type.HUMANIZED_MESSAGE);
								} else {
									Notification
											.show(Msg
													.get("notif.error.datamodel.add.title"),
													Msg.get("notif.error.datamodel.add.text"),
													Notification.Type.ERROR_MESSAGE);
								}

								// Close pop-up window
								view.getLoadNomenFromFileWindow().close();

							}

							@Override
							public void streamingFailed(
									final StreamingErrorEvent event) {
								// progress.setVisible(false);
							}

							@Override
							public boolean isInterrupted() {
								return false;
							}
						};
						html5File.setStreamVariable(streamVariable);
						// progress.setVisible(true);
					}
				}

			}
		}

		@Override
		public AcceptCriterion getAcceptCriterion() {
			return AcceptAll.get();
		}
	}

	class CWBLoadFolksoFromFileUploader implements Receiver, ProgressListener,
			FailedListener, SucceededListener, StartedListener,
			FinishedListener {

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
				Notification.show(
						Msg.get("notif.error.file.read.title"),
						MessageFormat.format(
								Msg.get("notif.error.file.read.text"),
								file.getAbsolutePath()),
						Notification.Type.ERROR_MESSAGE);
				return null;
			}

			return fos;
		}

		@Override
		public void uploadFinished(FinishedEvent event) {

		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			File file = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_TMP + Prop.FMT_OWL);

			CWBDataModelReader reader = new CWBDataModelFolksoReader();
			CWBDataModel dataModel = null;
			try {
				dataModel = reader.read(file);
			} catch (CWBDataModelReaderException e) {
				Notification.show(Msg.get("notif.error.datamodel.read.title"),
						Msg.get("notif.error.datamodel.read.text"),
						Notification.Type.ERROR_MESSAGE);
			}

			// Update model
			if (model.addDataModel(dataModel)) {

				// Update view
				view.getDataModelsPanel().addDataModel(dataModel);

				view.getDataModelsPanelAccordion()
						.setSelectedTab(
								view.getDataModelsPanelAccordion()
										.getComponentCount() - 1);

				// Notify
				Notification.show(Msg.get("notif.info.load.done.title"),
						Msg.get("notif.info.load.done.text"),
						Notification.Type.HUMANIZED_MESSAGE);
			} else {
				Notification.show(Msg.get("notif.error.datamodel.add.title"),
						Msg.get("notif.error.datamodel.add.text"),
						Notification.Type.ERROR_MESSAGE);
			}

			// Close pop-up window
			view.getLoadFolksoFromFileWindow().close();

		}

		@Override
		public void uploadFailed(FailedEvent event) {

		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {

		}

		@Override
		public void uploadStarted(StartedEvent event) {

		}
	}

	class CWBLoadFolksoFromFileDropHandler implements DropHandler {

		private static final long serialVersionUID = 1L;

		private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB

		@Override
		public void drop(final DragAndDropEvent dropEvent) {

			// expecting this to be an html5 drag
			final WrapperTransferable tr = (WrapperTransferable) dropEvent
					.getTransferable();
			final Html5File[] files = tr.getFiles();
			if (files != null) {
				for (final Html5File html5File : files) {
					final String fileName = html5File.getFileName();

					if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
						Notification
								.show("File rejected. Max 2Mb files are accepted by Sampler",
										Notification.Type.WARNING_MESSAGE);
					} else {

						final ByteArrayOutputStream bas = new ByteArrayOutputStream();
						final StreamVariable streamVariable = new StreamVariable() {

							private static final long serialVersionUID = 1L;

							@Override
							public OutputStream getOutputStream() {
								return bas;
							}

							@Override
							public boolean listenProgress() {
								return false;
							}

							@Override
							public void onProgress(
									final StreamingProgressEvent event) {
							}

							@Override
							public void streamingStarted(
									final StreamingStartEvent event) {
							}

							@Override
							public void streamingFinished(
									final StreamingEndEvent event) {

								// progress.setVisible(false);

								final StreamSource streamSource = new StreamSource() {
									//
									private static final long serialVersionUID = 1L;

									@SuppressWarnings("unused")
									@Override
									public InputStream getStream() {
										if (bas != null) {
											final byte[] byteArray = bas
													.toByteArray();
											return new ByteArrayInputStream(
													byteArray);
										}
										return null;
									}
								};
								final StreamResource resource = new StreamResource(
										streamSource, fileName);

								InputStream is = resource.getStreamSource()
										.getStream();

								CWBDataModelReader reader = new CWBDataModelFolksoReader();
								CWBDataModel dataModel = null;
								try {
									dataModel = reader.read(is);
								} catch (CWBDataModelReaderException e) {
									Notification
											.show(Msg
													.get("notif.error.datamodel.read.title"),
													Msg.get("notif.error.datamodel.read.text"),
													Notification.Type.ERROR_MESSAGE);
								}

								// Update model
								if (model.addDataModel(dataModel)) {
									// Update view
									view.getDataModelsPanel().addDataModel(
											dataModel);

									view.getDataModelsPanelAccordion()
											.setSelectedTab(
													view.getDataModelsPanelAccordion()
															.getComponentCount() - 1);

									// Notify
									Notification
											.show(Msg
													.get("notif.info.load.done.title"),
													Msg.get("notif.info.load.done.text"),
													Notification.Type.HUMANIZED_MESSAGE);
								} else {
									Notification
											.show(Msg
													.get("notif.error.datamodel.add.title"),
													Msg.get("notif.error.datamodel.add.text"),
													Notification.Type.ERROR_MESSAGE);
								}

								// Close pop-up window
								view.getLoadFolksoFromFileWindow().close();

							}

							@Override
							public void streamingFailed(
									final StreamingErrorEvent event) {
								// progress.setVisible(false);
							}

							@Override
							public boolean isInterrupted() {
								return false;
							}
						};
						html5File.setStreamVariable(streamVariable);
						// progress.setVisible(true);
					}
				}

			}
		}

		@Override
		public AcceptCriterion getAcceptCriterion() {
			return AcceptAll.get();
		}
	}

	/***********************/
	/*** LOAD TAG WINDOW ***/
	/***********************/

	class CWBLoadTagButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			TaggingWS selectedWS = (TaggingWS) view.getTagWSCombobox()
					.getValue();

			FolksoProviderWSClient client = null;
			CWBDataModel dataModel = null;
			IFolksonomy folksonomy = null;

			switch (selectedWS) {
			case TAGINFO:
				client = new TaginfoClient();
				break;
			case OVERPASS:
				client = new OverpassClient();
				break;

			default:
				return;
			}

			try {
				folksonomy = client.getTags();
			} catch (FolksoProviderWSClientException e) {
				Notification.show(Msg.get("notif.error.ws.title"),
						e.getMessage(), Notification.Type.ERROR_MESSAGE);
			}

			Tag2OwlParser tag2owl = new Tag2OwlParser(folksonomy);
			OWLOntology ontology = tag2owl.parse();

			CWBDataModelReader reader = new CWBDataModelFolksoReader();
			dataModel = reader.read(ontology);

			// Update model
			if (model.addDataModel(dataModel)) {
				// Update view
				view.getDataModelsPanel().addDataModel(dataModel);

				// Notify
				Notification.show(Msg.get("notif.info.load.done.title"),
						Msg.get("notif.info.load.done.text"),
						Notification.Type.HUMANIZED_MESSAGE);
			} else {
				Notification.show(Msg.get("notif.error.datamodel.add.title"),
						Msg.get("notif.error.datamodel.add.text"),
						Notification.Type.ERROR_MESSAGE);
			}

			// Close pop-up window
			view.getLoadFolksoFromWSWindow().close();

		}

	}

	class CWBTagWSComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			view.getLoadTagsetFromWSButton().setEnabled(
					event.getProperty().getValue() != null);
		}

	}

	/***********************/
	/*** MATCHING WINDOW ***/
	/***********************/

	class CWBMatchingTableValueChangeListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public void valueChange(ValueChangeEvent event) {

			Set<Object> selectedItems = (Set<Object>) view
					.getMatchingWindowTable().getValue();

			for (Object itemId : selectedItems) {
				view.getMatchingWindowTable()
						.getColumnGenerator(
								Msg.get("match.sources.table.column.select"))
						.generateCell(view.getMatchingWindowTable(), itemId,
								Msg.get("match.sources.table.column.select"));
			}

			int selectedItemCount = selectedItems.size();
			
			if (selectedItemCount > 2) {
				Iterator<Object> it = selectedItems.iterator();
				it.next();
				Set<Object> newValue = new LinkedHashSet<Object>();
				newValue.add(it.next());
				newValue.add(it.next());
				view.getMatchingWindowTable().setValue(newValue);
			}

			view.getMatchingWindowTable().refreshRowCache();

		}

	}

	class CWBMatchingButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public void buttonClick(ClickEvent event) {

			Set<Object> dataModels = (Set<Object>) view
					.getMatchingWindowTable().getValue();

			Iterator<Object> iterator = dataModels.iterator();

			CWBDataModel dataModel1 = (CWBDataModel) iterator.next();
			CWBDataModel dataModel2 = (CWBDataModel) iterator.next();

			CWBWriter writer = new CWBWriter();

			File onto1 = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_SOURCE_ONTO + Prop.FMT_OWL);

			File onto2 = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_TARGET_ONTO + Prop.FMT_OWL);

			writer.writeDataModel(dataModel2, onto2);
			writer.writeDataModel(dataModel1, onto1);

			// WikimatchOntologyMatcher matcher = new
			// WikimatchOntologyMatcher();
			// matcher.getEquivalences(
			// writer2.getFile().toURI().toString(), writer1.getFile()
			// .toURI().toString());

			YamOntologyMatcher matcher = new YamOntologyMatcher();
			Collection<CWBEquivalence> equivalences = matcher.getEquivalences(
					onto1.getAbsolutePath(), onto2.getAbsolutePath());

			// matcher.printAlignment();

			model.setSourceDataModel(dataModel1);
			model.setTargetDataModel(dataModel2);

			// Notify
			Notification.show(Msg.get("notif.info.load.done.title"),
					Msg.get("notif.info.load.done.text"),
					Notification.Type.HUMANIZED_MESSAGE);

			// Close pop-up window
			view.getMatchWindow().close();

			// update matching results table
			view.getMatchingResultsWindowTableContainer().removeAllItems();
			view.getMatchingResultsWindowTableContainer().addAll(equivalences);

			// Open the matching results window
			UI.getCurrent().addWindow(view.getMatchingResultsWindow());

		}
	}

	class CWBMatchingResultsButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			@SuppressWarnings("unchecked")
			Set<CWBEquivalence> equivalences = (Set<CWBEquivalence>) view
					.getMatchingResultsWindowTable().getValue();

			CWBDataModelMatched dataModel = new CWBDataModelMatched(
					IRI.create(Prop.CWB_NAMESPACE), model.getSourceDataModel()
							.getNamespace(), model.getTargetDataModel()
							.getNamespace());

			dataModel.addConcepts(model.getSourceDataModel().getConcepts());
			dataModel.addConcepts(model.getTargetDataModel().getConcepts());
			dataModel.addEquivalences(equivalences);

			CWBWriter writer = new CWBWriter();

			writer.writeDataModel(dataModel, Prop.DIR_TMP + File.separatorChar
					+ "matched_data_model" + Prop.FMT_OWL);

			if (model.addDataModel(dataModel)) {
				view.getDataModelsPanel().addDataModelMatched(dataModel);
			} else {
				Notification.show(Msg.get("notif.error.datamodel.add.title"),
						Msg.get("notif.error.datamodel.add.text"),
						Notification.Type.ERROR_MESSAGE);
			}

			view.getMatchingResultsWindow().close();

		}
	}

	class CWBMatchingResultsTableValueChangeListener implements
			ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public void valueChange(ValueChangeEvent event) {
			for (Object itemId : (Set<Object>) view
					.getMatchingResultsWindowTable().getValue()) {
				view.getMatchingResultsWindowTable()
						.getColumnGenerator(
								Msg.get("matching.results.table.column.select"))
						.generateCell(view.getMatchingResultsWindowTable(),
								itemId,
								Msg.get("matching.results.table.column.select"));
			}
			view.getMatchingResultsWindowTable().refreshRowCache();

		}

	}

}
