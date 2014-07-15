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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import lig.steamer.cwb.CWBProperties;
import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.io.CWBDataModelReader;
import lig.steamer.cwb.io.CWBDataModelWriter;
import lig.steamer.cwb.io.CWBModelReader;
import lig.steamer.cwb.io.exception.OntologyFormatException;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBMatchedDataModel;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.AppUI;
import lig.steamer.cwb.ui.Msg;
import lig.steamer.cwb.ui.window.CWBAboutWindow;
import lig.steamer.cwb.util.matching.impl.YamOntologyMatcher;
import lig.steamer.cwb.util.parser.Tag2OwlParser;
import lig.steamer.cwb.util.wsclient.TaggingWebService;
import lig.steamer.cwb.util.wsclient.taginfo.TagInfoClient;

import org.semanticweb.owlapi.model.OWLOntology;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
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

		view.addOpenProjectMenuItemCommand(new CWBOpenProjectMenuItemCommand());
		view.addDocMenuItemCommand(new CWBDocMenuItemCommand());
		view.addAboutMenuItemCommand(new CWBAboutMenuItemCommand());
		view.addLoadTagsetFromWSMenuItemCommand(new CWBLoadTagsetMenuItemCommand());
		view.addLoadNomenclatureFromFileMenuItemCommand(new CWBLoadNomenclatureFromFileMenuItemCommand());
		view.addDataModelsMenuItemCommand(new CWBDataModelsMenuItemCommand());
		view.addMatchMenuItemCommand(new CWBMatchMenuItemCommand());

		view.addOpenProjectUploadComponentReceiver(new CWBOpenProjectUploader());
		view.addOpenProjectUploadComponentStartedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadComponentFinishedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadComponentProgressListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadComponentFailedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadComponentSucceededListener(new CWBOpenProjectUploader());
		view.addOpenProjectDropBoxDropHandler(new CWBOpenProjectDropHandler());
		view.addLoadTagsetButtonListener(new CWBLoadTagButtonListener());
		view.addTagWebServiceComboBoxListener(new CWBTagWebServiceComboBoxListener());
		view.addLoadNomenclatureFromFileUploadComponentReceiver(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFromFileUploadComponentStartedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFromFileUploadComponentFinishedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFromFileUploadComponentProgressListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFromFileUploadComponentFailedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFromFileUploadComponentSucceededListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFromFileDropBoxDropHandler(new CWBLoadNomenclatureFromFileDropHandler());
		view.addMatchingWindowTableValueChangeListener(new CWBMatchingTableValueChangeListener());
		view.addMatchingWindowButtonClickListener(new CWBMatchingButtonListener());
		view.addMatchingResultsWindowButtonClickListener(new CWBMatchingResultsButtonListener());
		view.addMatchingResultsWindowTableValueChangeListener(new CWBMatchingResultsTableValueChangeListener());

	}

	class CWBLoadTagButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			TaggingWebService selectedWebService = (TaggingWebService) view
					.getTagWebServiceCombobox().getValue();

			CWBDataModel dataModel = null;

			switch (selectedWebService) {

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
				try {
					dataModel = reader.read(ontology);
				} catch (OntologyFormatException e) {
					e.printStackTrace();
				}
				break;

			default:
				return;

			}

			// Update model
			model.addDataModel(dataModel);

			// Update view
			view.getDataModelsPanel().addDataModelTreeTable(dataModel);

			// Notify
			new Notification(Msg.get("notif.loading.done.title"),
					Msg.get("notif.loading.done.text"),
					Notification.Type.HUMANIZED_MESSAGE)
					.show(Page.getCurrent());

			// Close pop-up window
			view.getLoadTagsetWindow().close();

		}

	}

	class CWBTagWebServiceComboBoxListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			view.getLoadTagsetButton().setEnabled(
					event.getProperty().getValue() != null);
		}

	}

	class CWBLoadTagsetMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadTagsetWindow());
		}

	}

	class CWBLoadNomenclatureFromFileMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getLoadNomenclatureFromFileWindow());
		}

	}

	class CWBOpenProjectMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getOpenProjectWindow());
		}

	}

	class CWBAboutMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(new CWBAboutWindow());
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

	class CWBDataModelsMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			view.getDataModelsPanel().setVisible(selectedItem.isChecked());
		}

	}

	class CWBMatchMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().addWindow(view.getMatchWindow());
			view.getMatchingWindowTableContainer()
					.addAll(model.getDataModels());
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

				file = new File(CWBProperties.CWB_TMP_DIR + File.separator
						+ CWBProperties.TMP_FILE_NAME
						+ CWBProperties.CWB_PROJECT_FORMAT);
				fos = new FileOutputStream(file);

			} catch (FileNotFoundException e) {
				new Notification("Could not open file "
						+ file.getAbsolutePath(), e.getMessage(),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
				return null;
			}

			return fos;
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {

			String path = CWBProperties.CWB_TMP_DIR + File.separator
					+ CWBProperties.TMP_FILE_NAME
					+ CWBProperties.CWB_PROJECT_FORMAT;

			CWBModelReader reader = new CWBModelReader();
			model = reader.read(path);

			for (CWBDataModel dataModel : model.getDataModels()) {
				// Update view
				view.getDataModelsPanel().addDataModelTreeTable(dataModel);
			}

			view.getDataModelsPanelAccordion().setSelectedTab(
					view.getDataModelsPanelAccordion().getComponentCount() - 1);

			// Notify
			new Notification(Msg.get("notif.loading.done.title"),
					Msg.get("notif.loading.done.text"),
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

								String path = CWBProperties.CWB_TMP_DIR
										+ File.separator
										+ CWBProperties.DEFAULT_PROJECT_NAME
										+ CWBProperties.CWB_PROJECT_FORMAT;

								File f = new File(path);
								
								try {
									OutputStream os = new FileOutputStream(f);
									int read = 0;
									byte[] bytes = new byte[1024];

									while ((read = is.read(bytes)) != -1) {

										os.write(bytes, 0, read);

									}
								} catch (IOException e) {
									e.printStackTrace();
								}
								
								CWBModelReader reader = new CWBModelReader();
								model = reader.read(f.getAbsolutePath());

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
										Msg.get("notif.loading.done.title"),
										Msg.get("notif.loading.done.text"),
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

	class CWBLoadNomenclatureFromFileUploader implements Receiver,
			ProgressListener, FailedListener, SucceededListener,
			StartedListener, FinishedListener {

		private static final long serialVersionUID = 1L;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			File file = null;
			FileOutputStream fos = null;

			try {

				file = new File(CWBProperties.CWB_TMP_DIR + File.separator
						+ CWBProperties.TMP_FILE_NAME
						+ CWBProperties.OWL_FILE_FORMAT);
				fos = new FileOutputStream(file);

			} catch (FileNotFoundException e) {
				new Notification("Could not open file "
						+ file.getAbsolutePath(), e.getMessage(),
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

			File file = new File(CWBProperties.CWB_TMP_DIR + File.separatorChar
					+ CWBProperties.TMP_FILE_NAME
					+ CWBProperties.OWL_FILE_FORMAT);

			CWBDataModelReader parser = new CWBDataModelReader();
			CWBDataModel dataModel = null;
			try {
				dataModel = parser.read(file);
			} catch (OntologyFormatException e) {
				new Notification("Could not parse ontology "
						+ file.getAbsolutePath(), e.getMessage(),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
			}

			// Update model
			model.addDataModel(dataModel);

			// Update view
			view.getDataModelsPanel().addDataModelTreeTable(dataModel);

			view.getDataModelsPanelAccordion().setSelectedTab(
					view.getDataModelsPanelAccordion().getComponentCount() - 1);

			// Notify
			new Notification(Msg.get("notif.loading.done.title"),
					Msg.get("notif.loading.done.text"),
					Notification.Type.HUMANIZED_MESSAGE)
					.show(Page.getCurrent());

			// Close pop-up window
			view.getLoadNomenclatureFromFileWindow().close();

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

	class CWBLoadNomenclatureFromFileDropHandler implements DropHandler {

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

								CWBDataModelReader parser = new CWBDataModelReader();
								CWBDataModel dataModel = null;
								try {
									dataModel = parser.read(is);
								} catch (OntologyFormatException e) {
									new Notification(
											"Could not parse ontology.",
											e.getMessage(),
											Notification.Type.ERROR_MESSAGE)
											.show(Page.getCurrent());
								}

								// Update model
								model.addDataModel(dataModel);

								// Update view
								view.getDataModelsPanel()
										.addDataModelTreeTable(dataModel);

								view.getDataModelsPanelAccordion()
										.setSelectedTab(
												view.getDataModelsPanelAccordion()
														.getComponentCount() - 1);

								// Notify
								new Notification(
										Msg.get("notif.loading.done.title"),
										Msg.get("notif.loading.done.text"),
										Notification.Type.HUMANIZED_MESSAGE)
										.show(Page.getCurrent());

								// Close pop-up window
								view.getLoadNomenclatureFromFileWindow()
										.close();

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

			CWBMatchedDataModel dataModel = new CWBMatchedDataModel(
					CWBProperties.CWB_NAMESPACE, dataModel1.getNamespace(),
					dataModel2.getNamespace());

			dataModel.addConcepts(dataModel1.getConcepts());
			dataModel.addConcepts(dataModel2.getConcepts());

			CWBDataModelWriter writer1 = new CWBDataModelWriter(dataModel1,
					CWBProperties.SOURCE_ONTOLOGY_FILE_NAME);
			writer1.write();
			writer1.flush();

			CWBDataModelWriter writer2 = new CWBDataModelWriter(dataModel2,
					CWBProperties.TARGET_ONTOLOGY_FILE_NAME);
			writer2.write();
			writer2.flush();

			// WikimatchOntologyMatcher matcher = new
			// WikimatchOntologyMatcher();
			// matcher.getEquivalences(
			// writer2.getFile().toURI().toString(), writer1.getFile()
			// .toURI().toString());

			YamOntologyMatcher matcher = new YamOntologyMatcher();
			Collection<CWBEquivalence> equivalences = matcher.getEquivalences(
					writer1.getFile().getAbsolutePath(), writer2.getFile()
							.getAbsolutePath());

			matcher.printAlignment();

			dataModel.addConcepts(dataModel1.getConcepts());
			dataModel.addConcepts(dataModel2.getConcepts());
			dataModel.addEquivalences(equivalences);

			CWBDataModelWriter writer = new CWBDataModelWriter(dataModel,
					"super_onto");
			writer.write();
			writer.flush();

			// Notify
			new Notification(Msg.get("notif.loading.done.title"),
					Msg.get("notif.loading.done.text"),
					Notification.Type.HUMANIZED_MESSAGE)
					.show(Page.getCurrent());

			// Close pop-up window
			view.getMatchWindow().close();

			// Open the matching results window
			UI.getCurrent().addWindow(view.getMatchingResultsWindow());

			view.getMatchingResultsWindowTableContainer().removeAllItems();
			view.getMatchingResultsWindowTableContainer().addAll(equivalences);

		}
	}

	class CWBMatchingResultsButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

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
