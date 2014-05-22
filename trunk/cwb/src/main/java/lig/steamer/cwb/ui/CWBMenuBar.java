package lig.steamer.cwb.ui;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CWBMenuBar extends MenuBar {

	private static final long serialVersionUID = 1L;

	public CWBMenuBar() {
		super();

		MenuItem fileItem = this.addItem(Messages.getString("main.menu.file"),
				null, null);

		fileItem.addItem(Messages.getString("main.menu.file.open"),
				new Command() {

					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {

					}
				});

		fileItem.addItem(Messages.getString("main.menu.file.saveas"),
				new Command() {

					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {

					}
				});

		fileItem.addItem(Messages.getString("main.menu.file.save"),
				new Command() {

					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {

					}
				});
		
		fileItem.addSeparator();

		fileItem.addItem(Messages.getString("main.menu.file.quit"),
				new Command() {

					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {

					}
				});

		MenuItem editItem = this.addItem(Messages.getString("main.menu.edit"),
				null, null);

		MenuItem undoItem = editItem.addItem(
				Messages.getString("main.menu.edit.undo"), null);
		undoItem.setEnabled(false);

		MenuItem redoItem = editItem.addItem(
				Messages.getString("main.menu.edit.redo"), null);
		redoItem.setEnabled(false);

		MenuItem dataItem = this.addItem(Messages.getString("main.menu.data"),
				null);

		dataItem.addItem(Messages.getString("main.menu.data.load.tagset"),
				new Command() {

					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						Window aboutWindow = new Window(Messages
								.getString("load.tagset.title"));
						aboutWindow.setModal(true);

						VerticalLayout aboutLayout = new VerticalLayout();
						aboutLayout.setMargin(true);
						aboutWindow.setContent(aboutLayout);

						Label aboutText = new Label(Messages
								.getString("about.text"));

						aboutLayout.addComponent(aboutText);

						aboutWindow.setWidth(400, Unit.PIXELS);
						UI.getCurrent().addWindow(aboutWindow);
						aboutWindow.center();
					}
				});

		dataItem.addItem(
				Messages.getString("main.menu.data.load.nomenclature"),
				new Command() {

					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						Window aboutWindow = new Window(Messages
								.getString("load.nomenclature.title"));
						aboutWindow.setModal(true);

						VerticalLayout aboutLayout = new VerticalLayout();
						aboutLayout.setMargin(true);
						aboutWindow.setContent(aboutLayout);

						Label aboutText = new Label(Messages
								.getString("about.text"));

						aboutLayout.addComponent(aboutText);

						aboutWindow.setWidth(400, Unit.PIXELS);
						UI.getCurrent().addWindow(aboutWindow);
						aboutWindow.center();
					}
				});

		dataItem.addItem(Messages.getString("main.menu.data.match"),
				new Command() {

					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						Window aboutWindow = new Window(Messages
								.getString("match.title"));
						aboutWindow.setModal(true);

						VerticalLayout aboutLayout = new VerticalLayout();
						aboutLayout.setMargin(true);
						aboutWindow.setContent(aboutLayout);

						Label aboutText = new Label(Messages
								.getString("about.text"));

						aboutLayout.addComponent(aboutText);

						aboutWindow.setWidth(400, Unit.PIXELS);
						UI.getCurrent().addWindow(aboutWindow);
						aboutWindow.center();
					}
				});

		MenuItem indicatorsItem = this.addItem(Messages.getString("main.menu.indicators"), null);
		
		this.addItem(
				Messages.getString("main.menu.window"), null);

		MenuItem helpItem = this.addItem(Messages.getString("main.menu.help"),
				null, null);

		helpItem.addItem(
				Messages.getString("main.menu.help.doc"), new Command() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						BrowserWindowOpener opener = new BrowserWindowOpener(
								Messages.getString("doc.url"));
						
						Button b = new Button();
						opener.extend(b);
						b.click();
					}
				});
		
		helpItem.addItem(
				Messages.getString("main.menu.help.about"), new Command() {

					private static final long serialVersionUID = 1L;

					@Override
					public void menuSelected(MenuItem selectedItem) {
						
						UI.getCurrent().addWindow(new CWBHelpWindow());

					}
				});
	}

}
