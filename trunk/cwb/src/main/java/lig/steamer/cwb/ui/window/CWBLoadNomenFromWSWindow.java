package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.util.wsclient.WSDataModelNomenProvider;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBLoadNomenFromWSWindow extends Window {

	private static final long serialVersionUID = 1L;

	private Button loadButton;
	private ComboBox nomenWSComboBox;

	public CWBLoadNomenFromWSWindow() {

		super(Msg.get("load.nomen.ws.capt"));

		Label loadFromText = new Label(Msg.get("load.nomen.ws.from"));
		loadFromText.setSizeUndefined();
		
		nomenWSComboBox = new ComboBox();
		nomenWSComboBox.setTextInputAllowed(true);
		nomenWSComboBox.setFilteringMode(FilteringMode.CONTAINS);
		nomenWSComboBox.setInputPrompt(Msg
				.get("load.nomen.ws.combo.placeholder"));

		for (WSDataModelNomenProvider ws : WSDataModelNomenProvider.values()) {
			nomenWSComboBox.addItem(ws);
		}

		loadButton = new Button(Msg.get("load.nomen.ws.button"));
		loadButton.setEnabled(false);
		
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();
		rootLayout.addComponent(loadFromText);
		rootLayout.addComponent(nomenWSComboBox);
		rootLayout.addComponent(loadButton);
		rootLayout.setComponentAlignment(loadFromText, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(nomenWSComboBox, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(loadButton, Alignment.MIDDLE_CENTER);

		this.setWidth(350, Unit.PIXELS);
		this.setHeight(200, Unit.PIXELS);
		this.center();
		this.setModal(true);
		this.setContent(rootLayout);

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
