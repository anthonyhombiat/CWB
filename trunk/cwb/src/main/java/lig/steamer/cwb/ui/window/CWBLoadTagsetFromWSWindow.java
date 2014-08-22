package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.util.wsclient.TaggingWS;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBLoadTagsetFromWSWindow extends Window {

	private static final long serialVersionUID = 1L;

	private Button loadButton;
	private ComboBox tagWSComboBox;

	public CWBLoadTagsetFromWSWindow() {

		super(Msg.get("load.folkso.ws.caption"));

		Label loadFromText = new Label(Msg.get("load.folkso.ws.from"));
		loadFromText.setSizeUndefined();
		
		tagWSComboBox = new ComboBox();
		tagWSComboBox.setTextInputAllowed(true);
		tagWSComboBox.setFilteringMode(FilteringMode.CONTAINS);
		tagWSComboBox.setInputPrompt(Msg
				.get("load.folkso.ws.combo.placeholder"));

		for (TaggingWS ws : TaggingWS.values()) {
			tagWSComboBox.addItem(ws);
		}

		loadButton = new Button(Msg.get("load.folkso.ws.button"));
		loadButton.setEnabled(false);
		
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();
		rootLayout.addComponent(loadFromText);
		rootLayout.addComponent(tagWSComboBox);
		rootLayout.addComponent(loadButton);
		rootLayout.setComponentAlignment(loadFromText, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(tagWSComboBox, Alignment.MIDDLE_CENTER);
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
	public ComboBox getTagWSComboBox() {
		return tagWSComboBox;
	}

}
