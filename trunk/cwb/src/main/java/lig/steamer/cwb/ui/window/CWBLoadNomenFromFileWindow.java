package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBLoadNomenFromFileWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final Upload uploadComponent;

	public CWBLoadNomenFromFileWindow() {

		super(Msg.get("load.nomen.file.capt"));

		uploadComponent = new Upload("", null);

		final Label label = new Label(Msg.get("load.nomen.file.explorer"));

		final VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.addComponent(label);
		rootLayout.addComponent(uploadComponent);
		rootLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(uploadComponent, Alignment.MIDDLE_CENTER);

		this.setWidth(400, Unit.PIXELS);
		this.setHeight(200, Unit.PIXELS);
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
