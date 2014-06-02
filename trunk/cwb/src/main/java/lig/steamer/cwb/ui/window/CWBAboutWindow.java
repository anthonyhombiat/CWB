package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.ui.Messages;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBAboutWindow extends Window {

	private static final long serialVersionUID = 1L;

	public CWBAboutWindow(){
		
		super(Messages
				.getString("about.caption"));

		Label aboutText = new Label(Messages
				.getString("about.text"));
		
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.addComponent(aboutText);

		this.setWidth(400, Unit.PIXELS);
		this.center();
		this.setModal(true);
		this.setContent(rootLayout);
		
	}
	
}
