package lig.steamer.cwb.ui;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBHelpWindow extends Window {

	private static final long serialVersionUID = 1L;

	public CWBHelpWindow(){
		
		super(Messages
				.getString("about.title"));

		Label aboutText = new Label(Messages
				.getString("about.text"));
		
		VerticalLayout aboutLayout = new VerticalLayout();
		aboutLayout.setMargin(true);
		aboutLayout.addComponent(aboutText);

		this.setWidth(400, Unit.PIXELS);
		this.center();
		this.setModal(true);
		this.setContent(aboutLayout);
		
	}
	
}
