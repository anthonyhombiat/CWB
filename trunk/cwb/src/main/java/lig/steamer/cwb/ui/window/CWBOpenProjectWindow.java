package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBOpenProjectWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final Upload uploadComponent;

	public CWBOpenProjectWindow() {

		super(Msg.get("open.capt"));

		uploadComponent = new Upload("", null);

		final VerticalLayout uploadLayout = new VerticalLayout();
		uploadLayout.setSizeFull();
		uploadLayout.setMargin(true);
		uploadLayout.addComponent(uploadComponent);
		uploadLayout.setComponentAlignment(uploadComponent,
				Alignment.MIDDLE_CENTER);

		Panel uploadPanel = new Panel(
				Msg.get("open.explorer"));
		uploadPanel.setSizeFull();
		uploadPanel.setContent(uploadLayout);

		final VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.addComponent(uploadPanel);

		this.setWidth(400, Unit.PIXELS);
		this.setHeight(300, Unit.PIXELS);
		this.center();
		this.setModal(true);
		this.setContent(rootLayout);

	}

	/**
	 * @return the upload component
	 */
	public Upload getUploadComponent() {
		return uploadComponent;
	}
}
