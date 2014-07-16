package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.util.wsclient.TaggingWebService;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBLoadTagsetWindow extends Window {

	private static final long serialVersionUID = 1L;

	private Button loadButton;
	private ComboBox tagWebServiceComboBox;

	public CWBLoadTagsetWindow() {

		super(Msg.get("load.tagset.ws.caption"));

		Label loadFromText = new Label(Msg.get("load.tagset.ws.from"));
		loadFromText.setSizeUndefined();
		
		tagWebServiceComboBox = new ComboBox();
		tagWebServiceComboBox.setTextInputAllowed(true);
		tagWebServiceComboBox.setFilteringMode(FilteringMode.CONTAINS);
		tagWebServiceComboBox.setInputPrompt(Msg
				.get("load.tagset.ws.combo.placeholder"));

		for (TaggingWebService ws : TaggingWebService.values()) {
			tagWebServiceComboBox.addItem(ws);
		}

		loadButton = new Button(Msg.get("load.tagset.ws.button"));
		loadButton.setEnabled(false);
		
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();
		rootLayout.addComponent(loadFromText);
		rootLayout.addComponent(tagWebServiceComboBox);
		rootLayout.addComponent(loadButton);
		rootLayout.setComponentAlignment(loadFromText, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(tagWebServiceComboBox, Alignment.MIDDLE_CENTER);
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
	public ComboBox getTagWebServiceComboBox() {
		return tagWebServiceComboBox;
	}

}
