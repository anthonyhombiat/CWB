package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.util.wsclient.WSDataModelNomenProvider;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

public class CWBLoadNomenFromWSWindow extends Window {

	private static final long serialVersionUID = 1L;

	private Button loadButton;
	private ComboBox nomenWSComboBox;

	public CWBLoadNomenFromWSWindow() {

		super(Msg.get("window.load.nomen.ws.capt"));
		
		nomenWSComboBox = new ComboBox(Msg.get("window.load.nomen.ws.from"));
		nomenWSComboBox.setTextInputAllowed(true);
		nomenWSComboBox.setFilteringMode(FilteringMode.CONTAINS);
		nomenWSComboBox.setInputPrompt(Msg
				.get("window.load.nomen.ws.combo.ph"));

		for (WSDataModelNomenProvider ws : WSDataModelNomenProvider.values()) {
			nomenWSComboBox.addItem(ws);
		}

		loadButton = new Button(Msg.get("window.load.nomen.ws.button"));
		loadButton.setEnabled(false);
		
		final FormLayout layout = new FormLayout();
		layout.setMargin(true);
		layout.setSizeUndefined();
		layout.addComponent(nomenWSComboBox);
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
		return nomenWSComboBox;
	}

}
