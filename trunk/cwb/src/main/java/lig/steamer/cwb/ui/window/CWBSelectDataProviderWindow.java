package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.util.wsclient.WSDatasetFolksoProvider;
import lig.steamer.cwb.util.wsclient.WSDatasetNomenProvider;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

public class CWBSelectDataProviderWindow extends Window {

	private static final long serialVersionUID = 1L;

	private Button okButton;
	private ComboBox nomenDataProviderComboBox;
	private ComboBox folksoDataProviderComboBox;

	public CWBSelectDataProviderWindow() {

		super(Msg.get("window.map.data.capt"));
		
		nomenDataProviderComboBox = new ComboBox(Msg.get("window.map.data.nomen.capt"));
		nomenDataProviderComboBox.setTextInputAllowed(true);
		nomenDataProviderComboBox.setFilteringMode(FilteringMode.CONTAINS);
		nomenDataProviderComboBox.setInputPrompt(Msg
				.get("window.map.data.nomen.ph"));

		for (WSDatasetNomenProvider ws : WSDatasetNomenProvider.values()) {
			nomenDataProviderComboBox.addItem(ws);
		}
		
		folksoDataProviderComboBox = new ComboBox(Msg.get("window.map.data.folkso.capt"));
		folksoDataProviderComboBox.setTextInputAllowed(true);
		folksoDataProviderComboBox.setFilteringMode(FilteringMode.CONTAINS);
		folksoDataProviderComboBox.setInputPrompt(Msg
				.get("window.map.data.folkso.ph"));

		for (WSDatasetFolksoProvider ws : WSDatasetFolksoProvider.values()) {
			folksoDataProviderComboBox.addItem(ws);
		}

		okButton = new Button(Msg.get("window.map.data.ok"));
		okButton.setEnabled(false);
		
		final FormLayout layout = new FormLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeUndefined();
		layout.addComponent(nomenDataProviderComboBox);
		layout.addComponent(folksoDataProviderComboBox);
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
	 * @return the nomenDataProviderComboBox
	 */
	public ComboBox getNomenDataProviderComboBox() {
		return nomenDataProviderComboBox;
	}
	
	/**
	 * @return the folksoDataProviderComboBox
	 */
	public ComboBox getFolksoDataProviderComboBox() {
		return folksoDataProviderComboBox;
	}

}
