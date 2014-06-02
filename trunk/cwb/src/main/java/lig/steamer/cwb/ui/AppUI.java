package lig.steamer.cwb.ui;

import javax.servlet.annotation.WebServlet;

import lig.steamer.cwb.controller.CWBController;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.menu.CWBMenuBar;
import lig.steamer.cwb.ui.panel.CWBDataModelsPanel;
import lig.steamer.cwb.ui.panel.CWBMapPanel;
import lig.steamer.cwb.ui.window.CWBLoadNomenclatureFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadTagsetWindow;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

@Theme("reindeer")
@Title("Citizen Welfare Builder")
@SuppressWarnings("serial")
public class AppUI extends UI {

	private CWBController controller;

	private CWBMenuBar menuBar;
	private CWBLoadTagsetWindow loadTagsetWindow;
	private CWBDataModelsPanel dataModelsPanel;
	private CWBLoadNomenclatureFromFileWindow loadNomenclatureFromFileWindow;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AppUI.class, widgetset = "lig.steamer.cwb.ui.AppWidgetSet")
	public static class Servlet extends VaadinServlet {}

	@Override
	protected void init(VaadinRequest request) {
		
		/*
		 * Menubar
		 */

		menuBar = new CWBMenuBar();

		Label mainTitle = new Label(Messages.getString("main.title"));
		mainTitle.setStyleName(Reindeer.LABEL_H1);

		Label mainSubtitle = new Label(Messages.getString("main.subtitle"));
		mainSubtitle.setStyleName(Reindeer.LABEL_SMALL);

		final VerticalLayout titleLayout = new VerticalLayout();
		titleLayout.addComponent(mainTitle);
		titleLayout.addComponent(mainSubtitle);
		titleLayout.setMargin(new MarginInfo(true, false, false, true));

		/*
		 * Accordion
		 */

		dataModelsPanel = new CWBDataModelsPanel();

		/*
		 * Tabsheet
		 */

		final TabSheet tabSheet = new TabSheet();
		tabSheet.setSizeFull();

		VerticalLayout dataTab = new VerticalLayout();
		dataTab.setSizeFull();

		VerticalLayout indicatorsTab = new VerticalLayout();
		indicatorsTab.setSizeFull();

		CWBMapPanel map = new CWBMapPanel();
		map.setSizeFull();

		tabSheet.addTab(dataTab, Messages.getString("tabsheet.data.caption"));
		tabSheet.addTab(indicatorsTab,
				Messages.getString("tabsheet.indicators.caption"));
		tabSheet.addTab(map, Messages.getString("tabsheet.map.caption"));
		tabSheet.setSelectedTab(map);

		final HorizontalLayout centralLayout = new HorizontalLayout();
		centralLayout.addComponent(dataModelsPanel);
		centralLayout.addComponent(tabSheet);
		centralLayout.setExpandRatio(dataModelsPanel, 0.2f);
		centralLayout.setExpandRatio(tabSheet, 0.8f);
		centralLayout.setSizeFull();
		centralLayout.setSpacing(true);
		centralLayout.setMargin(true);

		final VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.addComponent(menuBar);
		rootLayout.addComponent(titleLayout);
		rootLayout.addComponent(centralLayout);
		rootLayout.setExpandRatio(centralLayout, 0.9f);
		rootLayout.setStyleName(Reindeer.LAYOUT_BLUE);

		setContent(rootLayout);

		/*
		 * Pop-up windows
		 */

		loadTagsetWindow = new CWBLoadTagsetWindow();
		loadNomenclatureFromFileWindow = new CWBLoadNomenclatureFromFileWindow();

		/*
		 * Notification
		 */

		Notification welcomeNotification = new Notification(Messages.getString("notif.welcome.title"),
				Messages.getString("notif.welcome.text"));

		welcomeNotification.show(Page.getCurrent());
		welcomeNotification.setDelayMsec(Notification.DELAY_NONE);

		controller = new CWBController(new CWBModel(), this);
	}

	/*
	 * Embedded component getter
	 */
	
	/**
	 * @return the loadTagsetWindow
	 */
	public Window getLoadTagsetWindow() {
		return loadTagsetWindow;
	}
	
	/**
	 * @return the loadTagsetWindow tagWebServiceCombobox
	 */
	public ComboBox getTagWebServiceCombobox(){
		return loadTagsetWindow.getTagWebServiceComboBox();
	}
	
	/**
	 * @return the loadTagsetWindow loadButton
	 */
	public Button getLoadTagsetButton(){
		return loadTagsetWindow.getLoadButton();
	}
	
	/**
	 * @return the dataModelsPanel
	 */
	public CWBDataModelsPanel getDataModelsPanel() {
		return dataModelsPanel;
	}
	
	/**
	 * @return the loadNomenclatureFromFileWindow
	 */
	public CWBLoadNomenclatureFromFileWindow getLoadNomenclatureFromFileWindow() {
		return loadNomenclatureFromFileWindow;
	}

	/*
	 * Add listener methods
	 */
	
	public void addLoadTagsetButtonListener(ClickListener listener) {
		loadTagsetWindow.getLoadButton().addClickListener(listener);
	}
	
	public void addAboutMenuItemCommand(Command command){
		menuBar.getAboutMenuItem().setCommand(command);
	}
	
	public void addDocMenuItemCommand(Command command){
		menuBar.getDocMenuItem().setCommand(command);
	}
	
	public void addLoadTagsetFromWSMenuItemCommand(Command command){
		menuBar.getLoadTagsetFromWSMenuItem().setCommand(command);
	}
	
	public void addLoadNomenclatureFromFileMenuItemCommand(Command command){
		menuBar.getLoadNomenclatureFromFileMenuItem().setCommand(command);
	}
	
	public void addDataModelsMenuItemCommand(Command command){
		menuBar.getDataModelsMenuItem().setCommand(command);
	}
	
	public void addTagWebServiceComboBoxListener(ValueChangeListener listener){
		loadTagsetWindow.getTagWebServiceComboBox().addValueChangeListener(listener);
	}
	
	public void addLoadNomenclatureFileUploadComponentReceiver(Receiver receiver){
		loadNomenclatureFromFileWindow.getUploadComponent().setReceiver(receiver);
	}
	
	public void addLoadNomenclatureFileUploadComponentSucceededListener(SucceededListener listener){
		loadNomenclatureFromFileWindow.getUploadComponent().addSucceededListener(listener);
	}
	
	public void addLoadNomenclatureFileUploadComponentFailedListener(FailedListener listener){
		loadNomenclatureFromFileWindow.getUploadComponent().addFailedListener(listener);
	}
	
	public void addLoadNomenclatureFileUploadComponentProgressListener(ProgressListener listener){
		loadNomenclatureFromFileWindow.getUploadComponent().addProgressListener(listener);
	}
	
	public void addLoadNomenclatureFileUploadComponentFinishedListener(FinishedListener listener){
		loadNomenclatureFromFileWindow.getUploadComponent().addFinishedListener(listener);
	}
	
	public void addLoadNomenclatureFileUploadComponentStartedListener(StartedListener listener){
		loadNomenclatureFromFileWindow.getUploadComponent().addStartedListener(listener);
	}

	/**
	 * @return the controller
	 */
	public CWBController getController() {
		return controller;
	}

}
