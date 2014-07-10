package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.ui.Msg;

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

		super(Msg.get("about.caption"));

		Label aboutText = new Label(Msg.get("about.text"));

		Button closeButton = new Button(
				Msg.get("about.button.caption"),
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
