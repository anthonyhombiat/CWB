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
import java.util.Set;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.Prop;
import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.io.exception.CWBDataModelReaderException;
import lig.steamer.cwb.io.exception.CWBModelReaderException;
import lig.steamer.cwb.io.exception.CWBModelWriterException;
import lig.steamer.cwb.io.read.CWBDataModelReader;
import lig.steamer.cwb.io.read.CWBReader;
import lig.steamer.cwb.io.write.CWBWriter;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBMatchedDataModel;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.AppUI;
import lig.steamer.cwb.ui.window.CWBAboutWindow;
import lig.steamer.cwb.util.browser.BrowserHomepageProvider;
import lig.steamer.cwb.util.browser.UnsupportedBrowserException;
import lig.steamer.cwb.util.matching.impl.YamOntologyMatcher;
import lig.steamer.cwb.util.parser.Tag2OwlParser;
import lig.steamer.cwb.util.wsclient.TaggingWS;
import lig.steamer.cwb.util.wsclient.taginfo.TagInfoClient;

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
		view.addLoadTagsetFromWSMenuItemCommand(new CWBLoadTagsetMenuItemCommand());
		view.addLoadTagsetFromFileMenuItemCommand(new CWBLoadTagsetFromFileMenuItemCommand());
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
		view.addLoadTagsetButtonListener(new CWBLoadTagButtonListener());
		view.addTagWSComboBoxListener(new CWBTagWSComboBoxListener());
		view.addLoadTagsetFromFileUploadReceiver(new CWBLoadTagsetFromFileUploader());
		view.addLoadTagsetFromFileUploadStartedListener(new CWBLoadTagsetFromFileUploader());
		view.addLoadTagsetFromFileUploadFinishedListener(new CWBLoadTagsetFromFileUploader());
		view.addLoadTagsetFromFileUploadProgressListener(new CWBLoadTagsetFromFileUploader());
		view.addLoadTagsetFromFileUploadFailedListener(new CWBLoadTagsetFromFileUploader());
		view.addLoadTagsetFromFileUploadSucceededListener(new CWBLoadTagsetFromFileUploader());
		view.addLoadTagsetFromFileDropBoxDropHandler(new CWBLoadTagsetFromFileDropHandler());
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
			} catch (CWBModelWriterException e) {
				new Notification(Msg.get("notif.error.save.title"),
						Msg.get("notif.error.save.text"),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
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

	class CWBLoadTagsetMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadTagsetFromWSWindow());
		}

	}

	class CWBLoadTagsetFromFileMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadTagsetFromFileWindow());
		}

	}

	class CWBLoadNomenFromFileMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
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
				new Notification(Msg.get("notif.error.file.read.title"),
						MessageFormat.format(
								Msg.get("notif.error.file.read.text"),
								file.getAbsolutePath()),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
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
			} catch (CWBModelReaderException e) {
				new Notification(Msg.get("notif.error.open.title"),
						Msg.get("notif.error.open.text"),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

			view.clear();

			for (CWBDataModel dataModel : model.getDataModels()) {
				// Update view
				view.getDataModelsPanel().addDataModelTreeTable(dataModel);
			}

			view.getDataModelsPanelAccordion().setSelectedTab(
					view.getDataModelsPanelAccordion().getComponentCount() - 1);

			// Notify
			new Notification(Msg.get("notif.info.load.done.title"),
					Msg.get("notif.info.load.done.text"),
					Notification.Type.HUMANIZED_MESSAGE)
					.show(Page.getCurrent());

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
								} catch (CWBModelReaderException e) {
									new Notification(
											Msg.get("notif.error.open.title"),
											Msg.get("notif.error.open.text"),
											Notification.Type.ERROR_MESSAGE)
											.show(Page.getCurrent());
								}

								view.clear();

								for (CWBDataModel dataModel : model
										.getDataModels()) {
									// Update view
									view.getDataModelsPanel()
											.addDataModelTreeTable(dataModel);
								}

								view.getDataModelsPanelAccordion()
										.setSelectedTab(
												view.getDataModelsPanelAccordion()
														.getComponentCount() - 1);

								// Notify
								new Notification(
										Msg.get("notif.info.load.done.title"),
										Msg.get("notif.info.load.done.text"),
										Notification.Type.HUMANIZED_MESSAGE)
										.show(Page.getCurrent());

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
				new Notification(Msg.get("notif.error.file.read.title"),
						MessageFormat.format(
								Msg.get("notif.error.file.read.text"),
								file.getAbsolutePath()),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
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

			CWBDataModelReader reader = new CWBDataModelReader();
			CWBDataModel dataModel = null;
			try {
				dataModel = reader.read(file);
			} catch (CWBDataModelReaderException e) {
				new Notification(Msg.get("notif.error.datamodel.read.title"),
						Msg.get("notif.error.datamodel.read.text"),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

			// Update model
			if (model.addDataModel(dataModel)) {

				// Update view
				view.getDataModelsPanel().addDataModelTreeTable(dataModel);

				view.getDataModelsPanelAccordion()
						.setSelectedTab(
								view.getDataModelsPanelAccordion()
										.getComponentCount() - 1);

				// Notify
				new Notification(Msg.get("notif.info.load.done.title"),
						Msg.get("notif.info.load.done.text"),
						Notification.Type.HUMANIZED_MESSAGE).show(Page
						.getCurrent());
			} else {
				new Notification(Msg.get("notif.error.datamodel.add.title"),
						Msg.get("notif.error.datamodel.add.text"),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
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

								CWBDataModelReader reader = new CWBDataModelReader();
								CWBDataModel dataModel = null;
								try {
									dataModel = reader.read(is);
								} catch (CWBDataModelReaderException e) {
									new Notification(
											Msg.get("notif.error.datamodel.read.title"),
											Msg.get("notif.error.datamodel.read.text"),
											Notification.Type.ERROR_MESSAGE)
											.show(Page.getCurrent());
								}

								// Update model
								if (model.addDataModel(dataModel)) {

									// Update view
									view.getDataModelsPanel()
											.addDataModelTreeTable(dataModel);

									view.getDataModelsPanelAccordion()
											.setSelectedTab(
													view.getDataModelsPanelAccordion()
															.getComponentCount() - 1);

									// Notify
									new Notification(
											Msg.get("notif.info.load.done.title"),
											Msg.get("notif.info.load.done.text"),
											Notification.Type.HUMANIZED_MESSAGE)
											.show(Page.getCurrent());
								} else {
									new Notification(
											Msg.get("notif.error.datamodel.add.title"),
											Msg.get("notif.error.datamodel.add.text"),
											Notification.Type.ERROR_MESSAGE)
											.show(Page.getCurrent());
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

	class CWBLoadTagsetFromFileUploader implements Receiver, ProgressListener,
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
				new Notification(Msg.get("notif.error.file.read.title"),
						MessageFormat.format(
								Msg.get("notif.error.file.read.text"),
								file.getAbsolutePath()),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
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

			CWBDataModelReader reader = new CWBDataModelReader();
			CWBDataModel dataModel = null;
			try {
				dataModel = reader.read(file);
			} catch (CWBDataModelReaderException e) {
				new Notification(Msg.get("notif.error.datamodel.read.title"),
						Msg.get("notif.error.datamodel.read.text"),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

			// Update model
			if (model.addDataModel(dataModel)) {

				// Update view
				view.getDataModelsPanel().addDataModelTreeTable(dataModel);

				view.getDataModelsPanelAccordion()
						.setSelectedTab(
								view.getDataModelsPanelAccordion()
										.getComponentCount() - 1);

				// Notify
				new Notification(Msg.get("notif.info.load.done.title"),
						Msg.get("notif.info.load.done.text"),
						Notification.Type.HUMANIZED_MESSAGE).show(Page
						.getCurrent());
			} else {
				new Notification(Msg.get("notif.error.datamodel.add.title"),
						Msg.get("notif.error.datamodel.add.text"),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

			// Close pop-up window
			view.getLoadTagsetFromFileWindow().close();

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

	class CWBLoadTagsetFromFileDropHandler implements DropHandler {

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

								CWBDataModelReader reader = new CWBDataModelReader();
								CWBDataModel dataModel = null;
								try {
									dataModel = reader.read(is);
								} catch (CWBDataModelReaderException e) {
									new Notification(
											Msg.get("notif.error.datamodel.read.title"),
											Msg.get("notif.error.datamodel.read.text"),
											Notification.Type.ERROR_MESSAGE)
											.show(Page.getCurrent());
								}

								// Update model
								if (model.addDataModel(dataModel)) {
									// Update view
									view.getDataModelsPanel()
											.addDataModelTreeTable(dataModel);

									view.getDataModelsPanelAccordion()
											.setSelectedTab(
													view.getDataModelsPanelAccordion()
															.getComponentCount() - 1);

									// Notify
									new Notification(
											Msg.get("notif.info.load.done.title"),
											Msg.get("notif.info.load.done.text"),
											Notification.Type.HUMANIZED_MESSAGE)
											.show(Page.getCurrent());
								} else {
									new Notification(
											Msg.get("notif.error.datamodel.add.title"),
											Msg.get("notif.error.datamodel.add.text"),
											Notification.Type.ERROR_MESSAGE)
											.show(Page.getCurrent());
								}

								// Close pop-up window
								view.getLoadTagsetFromFileWindow().close();

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

			CWBDataModel dataModel = null;

			switch (selectedWS) {

			case TAG_INFO:

				// Requesting tags from OSM TAGINFO Web Service
				TagInfoClient tagInfoClient = new TagInfoClient();
				IFolksonomy folksonomy = tagInfoClient
						.getTagsByKey(TagInfoClient.DEFAULT_TAG_KEY);

				Tag2OwlParser tag2owl = new Tag2OwlParser(
						TagInfoClient.OSM_TAG_INFO_URI);
				tag2owl.addTagSet(folksonomy);
				OWLOntology ontology = tag2owl.getTagOntology();

				CWBDataModelReader reader = new CWBDataModelReader();
				dataModel = reader.read(ontology);
				break;

			default:
				return;

			}

			// Update model
			if (model.addDataModel(dataModel)) {
				// Update view
				view.getDataModelsPanel().addDataModelTreeTable(dataModel);

				// Notify
				new Notification(Msg.get("notif.info.load.done.title"),
						Msg.get("notif.info.load.done.text"),
						Notification.Type.HUMANIZED_MESSAGE).show(Page
						.getCurrent());
			} else {
				new Notification(Msg.get("notif.error.datamodel.add.title"),
						Msg.get("notif.error.datamodel.add.text"),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

			// Close pop-up window
			view.getLoadTagsetFromWSWindow().close();

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
			for (Object itemId : (Set<Object>) view.getMatchingWindowTable()
					.getValue()) {
				view.getMatchingWindowTable()
						.getColumnGenerator(
								Msg.get("match.sources.table.column.select"))
						.generateCell(view.getMatchingWindowTable(), itemId,
								Msg.get("match.sources.table.column.select"));
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
			new Notification(Msg.get("notif.info.load.done.title"),
					Msg.get("notif.info.load.done.text"),
					Notification.Type.HUMANIZED_MESSAGE)
					.show(Page.getCurrent());

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

			CWBMatchedDataModel dataModel = new CWBMatchedDataModel(
					IRI.create(Prop.CWB_NAMESPACE), model.getSourceDataModel()
							.getNamespace(), model.getTargetDataModel()
							.getNamespace());

			dataModel.addConcepts(model.getSourceDataModel().getConcepts());
			dataModel.addConcepts(model.getTargetDataModel().getConcepts());
			dataModel.addEquivalences(equivalences);

			CWBWriter writer = new CWBWriter();

			writer.writeDataModel(dataModel, Prop.DIR_TMP + File.separatorChar
					+ "matched_data_model" + Prop.FMT_OWL);

			if (model.addMatchedDataModel(dataModel)) {
				view.getDataModelsPanel().addDataModelTreeTable(dataModel);
			} else {
				new Notification(Msg.get("notif.error.datamodel.add.title"),
						Msg.get("notif.error.datamodel.add.text"),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
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
