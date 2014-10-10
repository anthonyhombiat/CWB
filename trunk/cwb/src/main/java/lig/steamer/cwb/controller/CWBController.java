package lig.steamer.cwb.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Set;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.Prop;
import lig.steamer.cwb.io.read.CWBDataModelFolksoReader;
import lig.steamer.cwb.io.read.CWBDataModelNomenReader;
import lig.steamer.cwb.io.read.CWBDataModelReader;
import lig.steamer.cwb.io.read.CWBReader;
import lig.steamer.cwb.io.read.exception.CWBDataModelReaderException;
import lig.steamer.cwb.io.read.exception.CWBReaderException;
import lig.steamer.cwb.io.write.CWBWriter;
import lig.steamer.cwb.io.write.exception.CWBWriterException;
import lig.steamer.cwb.model.CWBBBox;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.model.LeafletBBox;
import lig.steamer.cwb.ui.AppUI;
import lig.steamer.cwb.ui.window.CWBAboutWindow;
import lig.steamer.cwb.util.browser.BrowserHomepageProvider;
import lig.steamer.cwb.util.browser.UnsupportedBrowserException;
import lig.steamer.cwb.util.matching.impl.YamOntologyMatcher;
import lig.steamer.cwb.util.wsclient.DataModelFolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.DataModelNomenProviderWSClient;
import lig.steamer.cwb.util.wsclient.WSDataModelFolksoProvider;
import lig.steamer.cwb.util.wsclient.WSDataModelNomenProvider;
import lig.steamer.cwb.util.wsclient.bdtopo.BDTopoWSClient;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoWSClientException;
import lig.steamer.cwb.util.wsclient.exception.WSClientException;
import lig.steamer.cwb.util.wsclient.overpass.OverpassWSClient;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSClientException;
import lig.steamer.cwb.util.wsclient.taginfo.TaginfoWSClient;

import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
		view.addLoadNomenFromWSMenuItemCommand(new CWBLoadNomenFromWSMenuItemCommand());
		view.addLoadFolksoFromFileMenuItemCommand(new CWBLoadFolksoFromFileMenuItemCommand());
		view.addLoadNomenFromFileMenuItemCommand(new CWBLoadNomenFromFileMenuItemCommand());
		view.addMapMenuItemCommand(new CWBMapMenuItemCommand());

		view.addOpenProjectUploadReceiver(new CWBOpenProjectUploader());
		view.addOpenProjectUploadStartedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadFinishedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadProgressListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadFailedListener(new CWBOpenProjectUploader());
		view.addOpenProjectUploadSucceededListener(new CWBOpenProjectUploader());
		
		view.addLoadFolksoFromWSWindowButtonListener(new CWBLoadFolksoButtonListener());
		view.addLoadNomenFromWSWindowButtonListener(new CWBLoadNomenButtonListener());

		view.addLoadFolksoFromWSButtonListener(new CWBLoadFolksoFromWSMenuItemCommand());
		view.addLoadFolksoFromFileButtonListener(new CWBLoadFolksoFromFileMenuItemCommand());
		
		view.addLoadNomenFromWSButtonListener(new CWBLoadNomenFromWSMenuItemCommand());
		view.addLoadNomenFromFileButtonListener(new CWBLoadNomenFromFileMenuItemCommand());
		
		view.addFolksoWSComboBoxListener(new CWBFolksoWSComboBoxListener());
		view.addNomenWSComboBoxListener(new CWBNomenWSComboBoxListener());
		
		view.addLoadFolksoFromFileUploadReceiver(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadStartedListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadFinishedListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadProgressListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadFailedListener(new CWBLoadFolksoFromFileUploader());
		view.addLoadFolksoFromFileUploadSucceededListener(new CWBLoadFolksoFromFileUploader());
		
		view.addLoadNomenFromFileUploadReceiver(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadStartedListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadFinishedListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadProgressListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadFailedListener(new CWBLoadNomenFromFileUploader());
		view.addLoadNomenFromFileUploadSucceededListener(new CWBLoadNomenFromFileUploader());
		
		view.addMatchButtonListener(new CWBMatchButtonListener());
		view.addMatchingResultsTableValueChangedListener(new CWBMatchingResultsTableValueChangeListener());
		
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

			CWBWriter writer = new CWBWriter();
			try {
				writer.write(model);
			} catch (CWBWriterException e) {
				Notification.show(Msg.get("notif.err.save.capt"),
						Msg.get("notif.err.save.txt"),
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

	class CWBMapMenuItemCommand implements Command {

		private static final long serialVersionUID = 1L;

		@Override
		public void menuSelected(MenuItem selectedItem) {
			view.getMap().setVisible(selectedItem.isChecked());
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
				Notification.show(Msg.get("notif.err.file.read.capt"),
						MessageFormat.format(
								Msg.get("notif.err.file.read.txt"),
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
				Notification.show(Msg.get("notif.err.open.capt"),
						Msg.get("notif.err.open.txt"),
						Notification.Type.ERROR_MESSAGE);
			}

			// Notify
			Notification.show(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"),
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
				Notification.show(Msg.get("notif.err.file.read.capt"),
						MessageFormat.format(
								Msg.get("notif.err.file.read.txt"),
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
				Notification.show(Msg.get("notif.err.nomen.read.capt"),
						Msg.get("notif.err.nomen.read.txt"),
						Notification.Type.ERROR_MESSAGE);
			}

			// Update model
			model.setNomenclature((CWBDataModelNomen) dataModel);

			// Notify
			Notification.show(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"),
					Notification.Type.HUMANIZED_MESSAGE);

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
				Notification.show(Msg.get("notif.err.file.read.capt"),
						MessageFormat.format(
								Msg.get("notif.err.file.read.txt"),
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
				Notification.show(Msg.get("notif.err.folkso.read.capt"),
						Msg.get("notif.err.folkso.read.txt"),
						Notification.Type.ERROR_MESSAGE);
			}

			// Update model
			model.setFolksonomy((CWBDataModelFolkso) dataModel);

			// Notify
			Notification.show(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"),
					Notification.Type.HUMANIZED_MESSAGE);

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

	/***********************/
	/*** LOAD TAG WINDOW ***/
	/***********************/

	class CWBLoadFolksoButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			WSDataModelFolksoProvider selectedWS = (WSDataModelFolksoProvider) view.getFolksoWSCombobox()
					.getValue();

			DataModelFolksoProviderWSClient client = null;
			CWBDataModelFolkso folksonomy = null;

			switch (selectedWS) {
			case TAGINFO:
				client = new TaginfoWSClient();
				break;
			case OVERPASS:
				client = new OverpassWSClient();
				break;

			default:
				return;
			}

			try {
				folksonomy = client.getDataModelFolkso();
			} catch (WSClientException e) {
				Notification.show(Msg.get("notif.err.ws.capt"), e.getMessage(),
						Notification.Type.ERROR_MESSAGE);
			}

			// Update model
			model.setFolksonomy(folksonomy);

			// Notify
			Notification.show(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"),
					Notification.Type.HUMANIZED_MESSAGE);

			// Close pop-up window
			view.getLoadFolksoFromWSWindow().close();

		}

	}
	
	class CWBLoadNomenButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			WSDataModelNomenProvider selectedWS = (WSDataModelNomenProvider) view.getNomenWSCombobox()
					.getValue();

			DataModelNomenProviderWSClient client = null;
			CWBDataModelNomen nomen = null;

			switch (selectedWS) {
			case BDTOPO:
				client = new BDTopoWSClient();
				break;
			default:
				return;
			}

			try {
				nomen = client.getDataModelNomen();
			} catch (WSClientException e) {
				Notification.show(Msg.get("notif.err.ws.capt"), e.getMessage(),
						Notification.Type.ERROR_MESSAGE);
			}

			// Update model
			model.setNomenclature(nomen);

			// Notify
			Notification.show(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"),
					Notification.Type.HUMANIZED_MESSAGE);

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

	class CWBMoveEndListener implements LeafletMoveEndListener {

		@Override
		public void onMoveEnd(LeafletMoveEndEvent event) {
			CWBBBox bbox = new LeafletBBox(event.getBounds());

			model.setBBox(bbox);
			model.removeAllInstancesFolkso();

			for (CWBEquivalence equivalence : model.getSelectedEquivalences()) {

				OverpassWSClient overpassWSClient = new OverpassWSClient();
				BDTopoWSClient bdTopoWSClient = new BDTopoWSClient();

				try {
					model.addInstancesFolkso(overpassWSClient
							.getInstancesFolkso(equivalence.getConcept1()
									.getFragment().toString(), model.getBBox()));
					model.addInstancesNomen(bdTopoWSClient.getNomenInstances(
							equivalence.getConcept2().getFragment().toString(),
							model.getBBox()));
				} catch (OverpassWSClientException e) {
					Notification.show(Msg.get("notif.err.ws.overpass.capt"),
							Msg.get("notif.err.ws.overpass.txt"),
							Notification.Type.ERROR_MESSAGE);
					e.printStackTrace();
				} catch (BDTopoWSClientException e) {
					Notification.show(Msg.get("notif.err.ws.bdtopo.capt"),
							Msg.get("notif.err.ws.bdtopo.txt"),
							Notification.Type.ERROR_MESSAGE);
					e.printStackTrace();
				}

			}
		}

	}

	/***********************/
	/*** MATCHING WINDOW ***/
	/***********************/

	class CWBMatchButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {

			CWBDataModelFolkso folkso = model.getFolksonomy();
			CWBDataModelNomen nomen = model.getNomenclature();

			CWBWriter writer = new CWBWriter();

			File onto1 = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_SOURCE_ONTO + Prop.FMT_OWL);

			File onto2 = new File(Prop.DIR_TMP + File.separatorChar
					+ Prop.FILENAME_TARGET_ONTO + Prop.FMT_OWL);

			writer.writeDataModel(nomen, onto2);
			writer.writeDataModel(folkso, onto1);

			// WikimatchOntologyMatcher matcher = new
			// WikimatchOntologyMatcher();
			// matcher.getEquivalences(
			// writer2.getFile().toURI().toString(), writer1.getFile()
			// .toURI().toString());

			YamOntologyMatcher matcher = new YamOntologyMatcher();
			Collection<CWBEquivalence> equivalences = matcher.getEquivalences(
					onto1.getAbsolutePath(), onto2.getAbsolutePath());

			// matcher.printAlignment();
			model.addEquivalences(equivalences);

			// Notify
			Notification.show(Msg.get("notif.info.load.done.capt"),
					Msg.get("notif.info.load.done.txt"),
					Notification.Type.HUMANIZED_MESSAGE);

		}
	}

	class CWBMatchingResultsTableValueChangeListener implements
			ValueChangeListener {

		private static final long serialVersionUID = 1L;

		@Override
		// TODO clean code !
		public void valueChange(ValueChangeEvent event) {

			Set<Item> items = (Set<Item>) event.getProperty().getValue();

			model.removeAllInstancesFolkso();
			model.removeAllSelectedEquivalences();

			for (Object item : items) {
				if (item instanceof CWBEquivalence) {

					CWBEquivalence equivalence = (CWBEquivalence) item;
					model.addSelectedEquivalence(equivalence);

					OverpassWSClient overpassWSClient = new OverpassWSClient();
					BDTopoWSClient bdTopoWSClient = new BDTopoWSClient();

					try {
						model.addInstancesFolkso(overpassWSClient
								.getInstancesFolkso(equivalence.getConcept1()
										.getFragment().toString(),
										model.getBBox()));
						model.addInstancesNomen(bdTopoWSClient
								.getNomenInstances(equivalence.getConcept2()
										.getFragment().toString(),
										model.getBBox()));
					} catch (OverpassWSClientException e) {
						Notification.show(
								Msg.get("notif.err.ws.overpass.capt"),
								Msg.get("notif.err.ws.overpass.txt"),
								Notification.Type.ERROR_MESSAGE);
						e.printStackTrace();
					} catch (BDTopoWSClientException e) {
						Notification.show(Msg.get("notif.err.ws.bdtopo.capt"),
								Msg.get("notif.err.ws.bdtopo.txt") + e.getMessage(),
								Notification.Type.ERROR_MESSAGE);
						e.printStackTrace();
					}

				}
			}
		}
	}

}
