package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBLoadAlignFromWSWindow extends Window {

	private static final long serialVersionUID = 1L;

	private Button loadButton;
	private ComboBox tagWSComboBox;

	public CWBLoadAlignFromWSWindow() {

		super(Msg.get("load.align.ws.capt"));

		Label loadFromText = new Label(Msg.get("load.align.ws.from"));
		loadFromText.setSizeUndefined();
		
		tagWSComboBox = new ComboBox();
		tagWSComboBox.setTextInputAllowed(true);
		tagWSComboBox.setFilteringMode(FilteringMode.CONTAINS);
		tagWSComboBox.setInputPrompt(Msg
				.get("load.align.ws.combo.placeholder"));

		loadButton = new Button(Msg.get("load.align.ws.button"));
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
	public ComboBox getComboBox() {
		return tagWSComboBox;
	}

}
