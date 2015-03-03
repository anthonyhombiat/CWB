package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;

public class CWBLoadNomenFromFileWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final Upload uploadComponent;

	public CWBLoadNomenFromFileWindow() {

		super(Msg.get("window.load.nomen.file.capt"));

		uploadComponent = new Upload("", null);

		final FormLayout layout = new FormLayout();
		layout.setSizeUndefined();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(uploadComponent);

		this.center();
		this.setModal(true);
		this.setContent(layout);
	}

	/**
	 * @return the upload component
	 */
	public Upload getUploadComponent() {
		return uploadComponent;
	}
}
