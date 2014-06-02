package lig.steamer.cwb.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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

import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.AppUI;
import lig.steamer.cwb.ui.Messages;
import lig.steamer.cwb.ui.window.CWBAboutWindow;
import lig.steamer.cwb.util.parser.Owl2ConceptParser;
import lig.steamer.cwb.util.parser.Tag2ConceptParser;
import lig.steamer.cwb.util.parser.exception.OntologyFormatException;
import lig.steamer.cwb.util.wsclient.TaggingWebService;
import lig.steamer.cwb.util.wsclient.taginfo.TagInfoClient;

public class CWBController implements Serializable {

	private static final long serialVersionUID = 1L;

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

		view.addLoadTagsetButtonListener(new CWBLoadTagButtonListener());
		view.addTagWebServiceComboBoxListener(new CWBTagWebServiceComboBoxListener());
		view.addLoadNomenclatureFileUploadComponentReceiver(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentStartedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentFinishedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentProgressListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentFailedListener(new CWBLoadNomenclatureFromFileUploader());
		view.addLoadNomenclatureFileUploadComponentSucceededListener(new CWBLoadNomenclatureFromFileUploader());

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

				// Converting tags to CWB data model
				Tag2ConceptParser tag2conceptParser = new Tag2ConceptParser(
						TagInfoClient.OSM_TAG_INFO_URI);
				dataModel = tag2conceptParser.parse(folksonomy);
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
			BrowserWindowOpener opener = new BrowserWindowOpener(
					Messages.getString("doc.url"));

			Button b = new Button();
			opener.extend(b);
			b.click();
		}

	}

	class CWBDataModelsMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			view.getDataModelsPanel().setVisible(selectedItem.isChecked());
		}

	}

	class CWBLoadNomenclatureFromFileUploader implements Receiver,
			ProgressListener, FailedListener, SucceededListener,
			StartedListener, FinishedListener {

		private static final long serialVersionUID = 1L;

		private static final String TMP_DIR = "src/resources/tmp/";

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			File file = null;
			FileOutputStream fos = null;

			try {

				String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
				file = new File(basePath + "\\WEB-INF\\tmp\\local_onto.owl");
				fos = new FileOutputStream(file);

			} catch (FileNotFoundException e) {
				new Notification("Could not open file "
						+ file.getAbsolutePath(), e.getMessage(),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}

			return fos;
		}

		@Override
		public void uploadFinished(FinishedEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			
			String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
			File file = new File(basePath + "\\WEB-INF\\tmp\\local_onto.owl");
			
			Owl2ConceptParser parser = new Owl2ConceptParser();
			CWBDataModel dataModel = null;
			try {
				dataModel = parser.parse(file);
			} catch (OntologyFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Update model
			model.addDataModel(dataModel);

			// Update view
			view.getDataModelsPanel().addDataModelTreeTable(dataModel);

			// Notify
			new Notification(
					Messages.getString("notif.loading.done.title"),
					Messages.getString("notif.loading.done.text"),
					Notification.Type.HUMANIZED_MESSAGE).show(Page
					.getCurrent());

			// Close pop-up window
			view.getLoadNomenclatureFromFileWindow().close();
			
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			// TODO Auto-generated method stub

		}

		@Override
		public void uploadStarted(StartedEvent event) {
			// TODO Auto-generated method stub

		}
	}

}
