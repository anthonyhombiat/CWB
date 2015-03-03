package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBStudyArea;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

public class CWBSelectStudyAreaWindow extends Window {

	private static final long serialVersionUID = 1L;

	private Button okButton;
	private ComboBox comboBox;

	public CWBSelectStudyAreaWindow() {

		super(Msg.get("window.map.area.capt"));
		
		comboBox = new ComboBox(Msg.get("window.map.area.txt"));
		comboBox.setTextInputAllowed(true);
		comboBox.setFilteringMode(FilteringMode.CONTAINS);
		comboBox.setInputPrompt(Msg
				.get("window.map.area.ph"));

		for (CWBStudyArea sa : CWBStudyArea.values()) {
			comboBox.addItem(sa);
		}

		okButton = new Button(Msg.get("window.map.area.ok"));
		okButton.setEnabled(false);
		
		final FormLayout layout = new FormLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeUndefined();
		layout.addComponent(comboBox);
		layout.addComponent(okButton);

		this.center();
		this.setModal(true);
		this.setContent(layout);
	}

	/**
	 * @return the okButton
	 */
	public Button getOkButton() {
		return okButton;
	}
	
	/**
	 * @return the comboBox
	 */
	public ComboBox getComboBox() {
		return comboBox;
	}

}
