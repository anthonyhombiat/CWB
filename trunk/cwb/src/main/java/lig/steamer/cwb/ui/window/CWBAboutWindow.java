package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBAboutWindow extends Window {

	private static final long serialVersionUID = 1L;

	public CWBAboutWindow() {

		super(Msg.get("window.about.capt"));

		Label aboutText = new Label(Msg.get("window.about.txt"));

		Button closeButton = new Button(
				Msg.get("window.about.button"),
				new ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						 ((Window)event.getComponent().getParent().getParent()).close();
					}
				});
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(aboutText);
		layout.addComponent(closeButton);
		layout.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);

		this.setWidth(500, Unit.PIXELS);
		this.center();
		this.setModal(true);
		this.setContent(layout);
	}

}
