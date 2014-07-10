package lig.steamer.cwb.ui;

import javax.servlet.annotation.WebServlet;

import lig.steamer.cwb.controller.CWBController;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.menu.CWBMenuBar;
import lig.steamer.cwb.ui.panel.CWBDataModelsPanel;
import lig.steamer.cwb.ui.panel.CWBMapPanel;
import lig.steamer.cwb.ui.window.CWBLoadNomenclatureFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadTagsetWindow;
import lig.steamer.cwb.ui.window.CWBMatchingResultsWindow;
import lig.steamer.cwb.ui.window.CWBMatchingWindow;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
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

@Theme("cwbtheme")
@Title("Citizen Welfare Builder")
@SuppressWarnings("serial")
public class AppUI extends UI {

	private CWBController controller;

	private final CWBMenuBar menuBar = new CWBMenuBar();
	
	private final CWBDataModelsPanel dataModelsPanel = new CWBDataModelsPanel();
	
	private final CWBLoadTagsetWindow loadTagsetWindow = new CWBLoadTagsetWindow();
	private final CWBLoadNomenclatureFromFileWindow loadNomenclatureFromFileWindow = new CWBLoadNomenclatureFromFileWindow();
	private final CWBMatchingWindow matchingWindow = new CWBMatchingWindow();
	private final CWBMatchingResultsWindow matchingResultsWindow = new CWBMatchingResultsWindow();

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AppUI.class, widgetset = "lig.steamer.cwb.ui.AppWidgetSet")
	public static class Servlet extends VaadinServlet {}

	@Override
	protected void init(VaadinRequest request) {

		Label mainTitle = new Label(Msg.get("main.title"));
		mainTitle.setStyleName(Reindeer.LABEL_H1);

		Label mainSubtitle = new Label(Msg.get("main.subtitle"));
		mainSubtitle.setStyleName(Reindeer.LABEL_SMALL);

		final VerticalLayout titleLayout = new VerticalLayout();
		titleLayout.addComponent(mainTitle);
		titleLayout.addComponent(mainSubtitle);
		titleLayout.setMargin(new MarginInfo(true, false, false, true));

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

		tabSheet.addTab(dataTab, Msg.get("tabsheet.data.caption"));
		tabSheet.addTab(indicatorsTab,
				Msg.get("tabsheet.indicators.caption"));
		tabSheet.addTab(map, Msg.get("tabsheet.map.caption"));
		tabSheet.setSelectedTab(map);

		final HorizontalLayout centralLayout = new HorizontalLayout();
		centralLayout.addComponent(dataModelsPanel);
		centralLayout.addComponent(tabSheet);
		centralLayout.setExpandRatio(dataModelsPanel, 0.25f);
		centralLayout.setExpandRatio(tabSheet, 0.75f);
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
		 * Notification
		 */

		Notification welcomeNotification = new Notification(Msg.get("notif.welcome.title"),
				Msg.get("notif.welcome.text"));

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
	 * @return the dataModelsPanel accordion
	 */
	public Accordion getDataModelsPanelAccordion() {
		return dataModelsPanel.getAccordion();
	}
	
	/**
	 * @return the loadNomenclatureFromFileWindow
	 */
	public CWBLoadNomenclatureFromFileWindow getLoadNomenclatureFromFileWindow() {
		return loadNomenclatureFromFileWindow;
	}

	/**
	 * @return the matchingWindow
	 */
	public CWBMatchingWindow getMatchWindow() {
		return matchingWindow;
	}

	/**
	 * @return the matchingWindow table
	 */
	public Table getMatchingWindowTable(){
		return matchingWindow.getTable();
	}
	
	/**
	 * @return the matchingWindow table container
	 */
	public BeanItemContainer<CWBDataModel> getMatchingWindowTableContainer(){
		return matchingWindow.getContainer();
	}
	
	/**
	 * @return the matchingResultsWindow
	 */
	public CWBMatchingResultsWindow getMatchingResultsWindow() {
		return matchingResultsWindow;
	}
	
	/**
	 * @return the matchingResultsWindow table container
	 */
	public BeanItemContainer<CWBEquivalence> getMatchingResultsWindowTableContainer(){
		return matchingResultsWindow.getContainer();
	}
	
	/**
	 * @return the matchingResultsWindow table
	 */
	public Table getMatchingResultsWindowTable(){
		return matchingResultsWindow.getTable();
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
	
	public void addMatchMenuItemCommand(Command command){
		menuBar.getMatchMenuItem().setCommand(command);
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
	
	public void addLoadNomenclatureFileDropBoxDropHandler(DropHandler dropHandler){
		loadNomenclatureFromFileWindow.getDropBox().setDropHandler(dropHandler);
	}

	public void addMatchingWindowTableValueChangeListener(ValueChangeListener listener){
		matchingWindow.getTable().addValueChangeListener(listener);
	}
	
	public void addMatchingWindowButtonClickListener(ClickListener listener){
		matchingWindow.getButton().addClickListener(listener);
	}
	
	public void addMatchingResultsWindowButtonClickListener(ClickListener listener){
		matchingResultsWindow.getButton().addClickListener(listener);
	}
	
	public void addMatchingResultsWindowTableValueChangeListener(ValueChangeListener listener){
		matchingResultsWindow.getTable().addValueChangeListener(listener);
	}
	
	/**
	 * @return the controller
	 */
	public CWBController getController() {
		return controller;
	}

}
