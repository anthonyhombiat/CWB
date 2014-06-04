package lig.steamer.cwb.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.io.CWBDataModelReader;
import lig.steamer.cwb.io.CWBDataModelWriter;
import lig.steamer.cwb.io.exception.OntologyFormatException;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.AppUI;
import lig.steamer.cwb.ui.Messages;
import lig.steamer.cwb.ui.window.CWBAboutWindow;
import lig.steamer.cwb.util.matching.OntologyMatcher;
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
import com.vaadin.server.VaadinService;
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

	private static final String LOCAL_TMP_DIR_PATH = "\\WEB-INF\\tmp\\local_onto.owl";

	private CWBModel model;
	private AppUI view;

	public CWBController(CWBModel model, AppUI view) {

		this.model = model;
		this.view = view;

		view.addDocMenuItemCommand(new CWBDocMenuItemCommand());
		view.addAboutMenuItemCommand(new CWBAboutMenuItemCommand());
		view.addLoadTagsetFromWSMenuItemCommand(new CWBLoadTagsetMenuItemCommand());
		view.addLoadNomenclatureFromFileMenuItemCommand(new CWBLoadNomenclatureFromFileMenuItemCommand());
		view.addDataModelsMenuItemCommand(new CWBDataModelsMenuItemCommand());
		view.addMatchMenuItemCommand(new CWBMatchMenuItemCommand());

		view.addLoadTagsetButtonListener(new CWBLoadTagButtonListener());
		view.addTagWebServiceComboBoxListener(new CWBTagWebServiceComboBoxListener());
		view.addLoadNomenclatureFileUploadComponentReceiver(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentStartedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentFinishedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentProgressListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentFailedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentSucceededListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileDropBoxDropHandler(new CWBLoadNomenclatureFromFileDropHandler());
		view.addMatchingWindowTableValueChangeListener(new CWBDataModelsTableValueChangeListener());
		view.addMatchingWindowButtonClickListener(new CWBMatchingButtonListener());

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
			new Notification(Messages.getString("notif.loading.done.title"),
					Messages.getString("notif.loading.done.text"),
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
					"window.open('" + Messages.getString("doc.url")
							+ "', '_blank')");
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
			view.getMatchWindowTableContainer().addAll(model.getDataModels());
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

				String basePath = VaadinService.getCurrent().getBaseDirectory()
						.getAbsolutePath();
				file = new File(basePath + LOCAL_TMP_DIR_PATH);
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

			String basePath = VaadinService.getCurrent().getBaseDirectory()
					.getAbsolutePath();
			File file = new File(basePath + LOCAL_TMP_DIR_PATH);

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
			new Notification(Messages.getString("notif.loading.done.title"),
					Messages.getString("notif.loading.done.text"),
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

								System.out.println(html5File.getFileName());
								// showFile(fileName, html5File.getType(), bas);
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
										Messages.getString("notif.loading.done.title"),
										Messages.getString("notif.loading.done.text"),
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

	class CWBDataModelsTableValueChangeListener implements ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public void valueChange(ValueChangeEvent event) {
			for (Object itemId : (Set<Object>) view.getMatchWindowTable()
					.getValue()) {
				view.getMatchWindowTable()
						.getColumnGenerator(
								Messages.getString("match.sources.table.column.select"))
						.generateCell(
								view.getMatchWindowTable(),
								itemId,
								Messages.getString("match.sources.table.column.select"));
			}
			view.getMatchWindowTable().refreshRowCache();

		}

	}

	class CWBMatchingButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		public void buttonClick(ClickEvent event) {
			CWBDataModelWriter writer = new CWBDataModelWriter();
			List<File> files = new ArrayList<File>(2);
			for (Object itemId : (Set<Object>) view.getMatchWindowTable()
					.getValue()) {
				files.add(writer.write((CWBDataModel) itemId));
			}
			OntologyMatcher matcher = new OntologyMatcher();
			matcher.match(files.get(0).toURI().toString(), files.get(1).toURI()
					.toString());
		}

	}

}
