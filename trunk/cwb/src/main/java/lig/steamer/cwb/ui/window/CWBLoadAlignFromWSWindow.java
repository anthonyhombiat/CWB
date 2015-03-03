package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

public class CWBLoadAlignFromWSWindow extends Window {

	private static final long serialVersionUID = 1L;

	private Button loadButton;
	private ComboBox tagWSComboBox;

	public CWBLoadAlignFromWSWindow() {

		super(Msg.get("window.load.align.ws.capt"));
		
		tagWSComboBox = new ComboBox(Msg.get("window.load.align.ws.from"));
		tagWSComboBox.setTextInputAllowed(true);
		tagWSComboBox.setFilteringMode(FilteringMode.CONTAINS);
		tagWSComboBox.setInputPrompt(Msg
				.get("window.load.align.ws.combo.ph"));

		loadButton = new Button(Msg.get("window.load.align.ws.button"));
		loadButton.setEnabled(false);
		
		final FormLayout layout = new FormLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeUndefined();
		layout.addComponent(tagWSComboBox);
		layout.addComponent(loadButton);

		this.center();
		this.setModal(true);
		this.setContent(layout);
	}

	/**
	 * @return the loadButton
	 */
	public Button getLoadButton() {
		return loadButton;
	}
	
	/**
	 * @return the comboBox
	 */
	public ComboBox getComboBox() {
		return tagWSComboBox;
	}

}
