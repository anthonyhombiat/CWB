package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class CWBLoadTagsetFromFileWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final ProgressBar progress;
	private final Upload uploadComponent;
	private final DragAndDropWrapper dropBox;

	public CWBLoadTagsetFromFileWindow() {

		super(Msg.get("load.folkso.file.caption"));

		uploadComponent = new Upload("", null);

		final VerticalLayout uploadLayout = new VerticalLayout();
		uploadLayout.setSizeFull();
		uploadLayout.setMargin(true);
		uploadLayout.addComponent(uploadComponent);
		uploadLayout.setComponentAlignment(uploadComponent,
				Alignment.MIDDLE_CENTER);

		Panel uploadPanel = new Panel(
				Msg.get("load.folkso.file.explorer"));
		uploadPanel.setSizeFull();
		uploadPanel.setContent(uploadLayout);

		final Label infoLabel = new Label(
				Msg.get("load.folkso.file.dnd.dropbox"));
		infoLabel.setStyleName(Reindeer.LABEL_SMALL);
		infoLabel.setWidth(300, Unit.PIXELS);

		final VerticalLayout dropBoxLayout = new VerticalLayout();
		dropBoxLayout.setMargin(true);
		dropBoxLayout.setSizeFull();
		dropBoxLayout.addComponent(infoLabel);
		dropBoxLayout.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);

		dropBox = new DragAndDropWrapper(dropBoxLayout);
		dropBox.setSizeFull();

		progress = new ProgressBar();
		progress.setIndeterminate(true);
		progress.setVisible(false);
		dropBoxLayout.addComponent(progress);

		final Panel dropPanel = new Panel(
				Msg.get("load.folkso.file.dnd"));
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

	/**
	 * @return the upload component
	 */
	public Upload getUploadComponent() {
		return uploadComponent;
	}
	
	/**
	 * @return the dropbox
	 */
	public DragAndDropWrapper getDropBox() {
		return dropBox;
	}
}
