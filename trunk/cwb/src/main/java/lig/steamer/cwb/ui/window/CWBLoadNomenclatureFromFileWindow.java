package lig.steamer.cwb.ui.window;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java_cup.lalr_item;
import lig.steamer.cwb.ui.Messages;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamVariable;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class CWBLoadNomenclatureFromFileWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final ProgressBar progress;
	private final Upload uploadComponent;

	public CWBLoadNomenclatureFromFileWindow() {

		super(Messages.getString("load.nomenclature.file.caption"));

		uploadComponent = new Upload("Upload your local ontology", null);
		
		final VerticalLayout uploadLayout = new VerticalLayout();
		uploadLayout.setSizeFull();
		uploadLayout.setMargin(true);
		uploadLayout.addComponent(uploadComponent);
		uploadLayout.setComponentAlignment(uploadComponent, Alignment.MIDDLE_CENTER);
		
		Panel uploadPanel = new Panel(Messages.getString("load.nomenclature.file.explorer"));
		uploadPanel.setSizeFull();
		uploadPanel.setContent(uploadLayout);
		
		final Label infoLabel = new Label(Messages.getString("load.nomenclature.file.dragndrop.dropbox"));
		infoLabel.setStyleName(Reindeer.LABEL_SMALL);
		infoLabel.setWidth(300, Unit.PIXELS);
		
		final VerticalLayout dropBoxLayout = new VerticalLayout();
		dropBoxLayout.setMargin(true);
		dropBoxLayout.setSizeFull();
		dropBoxLayout.addComponent(infoLabel);
		dropBoxLayout.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);

		final DropBox dropBox = new DropBox(dropBoxLayout);
		dropBox.setSizeFull();

		progress = new ProgressBar();
		progress.setIndeterminate(true);
		progress.setVisible(false);
		dropBoxLayout.addComponent(progress);
		
		final Panel dropPanel = new Panel(Messages.getString("load.nomenclature.file.dragndrop"));
		dropPanel.setSizeFull();
		dropPanel.setContent(dropBox);
		
		this.setWidth(400, Unit.PIXELS);
		this.setHeight(300, Unit.PIXELS);
		this.center();
		this.setModal(true);
		
		Panel emptyPanel = new Panel();
		emptyPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		final VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.addComponent(uploadPanel);
		rootLayout.addComponent(emptyPanel);
		rootLayout.addComponent(dropPanel);
		
		rootLayout.setExpandRatio(uploadPanel, 0.45f);
		rootLayout.setExpandRatio(emptyPanel, 0.1f);
		rootLayout.setExpandRatio(dropPanel, 0.45f);
		
		this.setContent(rootLayout);
		
	}

	private class DropBox extends DragAndDropWrapper implements DropHandler {

		private static final long serialVersionUID = 1L;

		private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB

		public DropBox(final Component root) {
			super(root);
			setDropHandler(this);
		}

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
								progress.setVisible(false);
								System.out.println(html5File.getFileName());
//								showFile(fileName, html5File.getType(), bas);
							}

							@Override
							public void streamingFailed(
									final StreamingErrorEvent event) {
								progress.setVisible(false);
							}

							@Override
							public boolean isInterrupted() {
								return false;
							}
						};
						html5File.setStreamVariable(streamVariable);
						progress.setVisible(true);
					}
				}

			} else {
				final String text = tr.getText();
				if (text != null) {
//					showText(text);
				}
			}
		}

//		private void showText(final String text) {
//			showComponent(new Label(text), "Wrapped text content");
//		}

//		private void showFile(final String name, final String type,
//				final ByteArrayOutputStream bas) {
//			// resource for serving the file contents
//			final StreamSource streamSource = new StreamSource() {
//				
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public InputStream getStream() {
//					if (bas != null) {
//						final byte[] byteArray = bas.toByteArray();
//						return new ByteArrayInputStream(byteArray);
//					}
//					return null;
//				}
//			};
//			final StreamResource resource = new StreamResource(streamSource,
//					name);
//
//			// show the file contents - images only for now
//			final Embedded embedded = new Embedded(name, resource);
//			showComponent(embedded, name);
//		}

//		private void showComponent(final Component c, final String name) {
//			final VerticalLayout layout = new VerticalLayout();
//			layout.setSizeUndefined();
//			layout.setMargin(true);
//			final Window w = new Window(name, layout);
//			w.addStyleName("dropdisplaywindow");
//			w.setSizeUndefined();
//			w.setResizable(false);
//			c.setSizeUndefined();
//			layout.addComponent(c);
//			UI.getCurrent().addWindow(w);
//
//		}

		@Override
		public AcceptCriterion getAcceptCriterion() {
			return AcceptAll.get();
		}
	}
	
	/**
	 * 
	 * @return the upload component
	 */
	public Upload getUploadComponent(){
		return uploadComponent;
	}
}
