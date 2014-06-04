package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.ui.Messages;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class CWBAboutWindow extends Window {

	private static final long serialVersionUID = 1L;

	public CWBAboutWindow() {

		super(Messages.getString("about.caption"));

		Label aboutText = new Label(Messages.getString("about.text"));

		Button closeButton = new Button(
				Messages.getString("about.button.caption"),
				new ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						 ((Window)event.getComponent().getParent().getParent()).close();
					}
				});
		
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.addComponent(aboutText);
		rootLayout.addComponent(closeButton);
		rootLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);

		this.setWidth(500, Unit.PIXELS);
		this.center();
		this.setModal(true);
		this.setContent(rootLayout);

	}

}
