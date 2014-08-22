package lig.steamer.cwb.ui;

import javax.servlet.annotation.WebServlet;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.controller.CWBController;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.menu.CWBMenuBar;
import lig.steamer.cwb.ui.panel.CWBDataModelsPanel;
import lig.steamer.cwb.ui.panel.CWBIndicatorsPanel;
import lig.steamer.cwb.ui.panel.CWBMapPanel;
import lig.steamer.cwb.ui.window.CWBLoadNomenFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadTagsetFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadTagsetFromWSWindow;
import lig.steamer.cwb.ui.window.CWBMatchingResultsWindow;
import lig.steamer.cwb.ui.window.CWBMatchingWindow;
import lig.steamer.cwb.ui.window.CWBOpenProjectWindow;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.dd.DropHandler;
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
import com.vaadin.ui.MenuBar.MenuItem;
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
	private final VerticalLayout leftLayout = new VerticalLayout();
	private final TabSheet tabSheet = new TabSheet();

	private final CWBDataModelsPanel dataModelsPanel = new CWBDataModelsPanel();
	private final CWBIndicatorsPanel indicatorsPanel = new CWBIndicatorsPanel();
	CWBMapPanel map = new CWBMapPanel();

	private final CWBOpenProjectWindow openProjectWindow = new CWBOpenProjectWindow();
	private final CWBLoadTagsetFromWSWindow loadTagsetFromWSWindow = new CWBLoadTagsetFromWSWindow();
	private final CWBLoadNomenFromFileWindow loadNomenFromFileWindow = new CWBLoadNomenFromFileWindow();
	private final CWBLoadTagsetFromFileWindow loadTagsetFromFileWindow = new CWBLoadTagsetFromFileWindow();
	private final CWBMatchingWindow matchingWindow = new CWBMatchingWindow();
	private final CWBMatchingResultsWindow matchingResultsWindow = new CWBMatchingResultsWindow();

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AppUI.class, widgetset = "lig.steamer.cwb.ui.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

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

		tabSheet.setSizeFull();

		map.setSizeFull();

		tabSheet.addTab(map, Msg.get("tabsheet.map.caption"));
		tabSheet.setSelectedTab(map);

		leftLayout.addComponent(dataModelsPanel);
		leftLayout.addComponent(indicatorsPanel);
		leftLayout.setExpandRatio(dataModelsPanel, 0.5f);
		leftLayout.setExpandRatio(indicatorsPanel, 0.5f);
		leftLayout.setSizeFull();
		leftLayout.setSpacing(true);

		final HorizontalLayout centralLayout = new HorizontalLayout();
		centralLayout.addComponent(leftLayout);
		centralLayout.addComponent(tabSheet);
		centralLayout.setExpandRatio(leftLayout, 0.35f);
		centralLayout.setExpandRatio(tabSheet, 0.65f);
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

		Notification.show(Msg.get("notif.info.welcome.title"),
				Msg.get("notif.info.welcome.text"),
				Notification.Type.HUMANIZED_MESSAGE);

		controller = new CWBController(new CWBModel(), this);

	}

	public void clear() {
		dataModelsPanel.clear();
		indicatorsPanel.clear();
		map.clear();
	}

	/**********************************/
	/*** EMBEDDED COMPONENT GETTERS ***/
	/**********************************/

	public MenuItem getSaveMenuItem() {
		return menuBar.getSaveMenuItem();
	}

	public Window getOpenProjectWindow() {
		return openProjectWindow;
	}

	public Window getLoadFolksoFromWSWindow() {
		return loadTagsetFromWSWindow;
	}

	public ComboBox getTagWSCombobox() {
		return loadTagsetFromWSWindow.getTagWSComboBox();
	}

	public Button getLoadTagsetFromWSButton() {
		return loadTagsetFromWSWindow.getLoadButton();
	}

	public VerticalLayout getLeftLayout() {
		return leftLayout;
	}

	public CWBDataModelsPanel getDataModelsPanel() {
		return dataModelsPanel;
	}

	public CWBIndicatorsPanel getIndicatorsPanel() {
		return indicatorsPanel;
	}

	public Accordion getDataModelsPanelAccordion() {
		return dataModelsPanel.getAccordionNomen();
	}

	public CWBLoadTagsetFromFileWindow getLoadFolksoFromFileWindow() {
		return loadTagsetFromFileWindow;
	}

	public CWBLoadNomenFromFileWindow getLoadNomenFromFileWindow() {
		return loadNomenFromFileWindow;
	}

	public CWBMatchingWindow getMatchWindow() {
		return matchingWindow;
	}

	public Table getMatchingWindowTable() {
		return matchingWindow.getTable();
	}

	public BeanItemContainer<CWBDataModel> getMatchingWindowTableContainer() {
		return matchingWindow.getContainer();
	}

	public CWBMatchingResultsWindow getMatchingResultsWindow() {
		return matchingResultsWindow;
	}

	public BeanItemContainer<CWBEquivalence> getMatchingResultsWindowTableContainer() {
		return matchingResultsWindow.getContainer();
	}

	public Table getMatchingResultsWindowTable() {
		return matchingResultsWindow.getTable();
	}

	public TabSheet getTabSheet() {
		return tabSheet;
	}

	/****************************/
	/*** ADD LISTENER METHODS ***/
	/****************************/

	public void addLoadFolksoButtonListener(ClickListener listener) {
		loadTagsetFromWSWindow.getLoadButton().addClickListener(listener);
	}

	public void addOpenMenuItemCommand(Command command) {
		menuBar.getOpenMenuItem().setCommand(command);
	}

	public void addSaveMenuItemCommand(Command command) {
		menuBar.getSaveMenuItem().setCommand(command);
	}

	public void addCloseMenuItemCommand(Command command) {
		menuBar.getCloseMenuItem().setCommand(command);
	}

	public void addLogoutMenuItemCommand(Command command) {
		menuBar.getLogoutMenuItem().setCommand(command);
	}

	public void addAboutMenuItemCommand(Command command) {
		menuBar.getAboutMenuItem().setCommand(command);
	}

	public void addDocMenuItemCommand(Command command) {
		menuBar.getDocMenuItem().setCommand(command);
	}

	public void addLoadFolksoFromWSMenuItemCommand(Command command) {
		menuBar.getLoadFolksoFromWSMenuItem().setCommand(command);
	}

	public void addLoadFolksoFromFileMenuItemCommand(Command command) {
		menuBar.getLoadFolksoFromFileMenuItem().setCommand(command);
	}

	public void addLoadNomenFromFileMenuItemCommand(Command command) {
		menuBar.getLoadNomenFromFileMenuItem().setCommand(command);
	}
	
	public void addLoadFolksoFromWSButtonListener(ClickListener listener) {
		dataModelsPanel.getAddFolksoFromWS().addClickListener(listener);
	}

	public void addLoadFolksoFromFileButtonListener(ClickListener listener) {
		dataModelsPanel.getAddFolksoFromFile().addClickListener(listener);
	}

	public void addLoadNomenFromFileButtonListener(ClickListener listener) {
		dataModelsPanel.getAddNomenFromFile().addClickListener(listener);
	}

	public void addDataModelsMenuItemCommand(Command command) {
		menuBar.getDataModelsMenuItem().setCommand(command);
	}

	public void addIndicatorsMenuItemCommand(Command command) {
		menuBar.getIndicatorsMenuItem().setCommand(command);
	}

	public void addMapMenuItemCommand(Command command) {
		menuBar.getMapMenuItem().setCommand(command);
	}

	public void addMatchMenuItemCommand(Command command) {
		menuBar.getMatchMenuItem().setCommand(command);
	}

	public void addTagWSComboBoxListener(ValueChangeListener listener) {
		loadTagsetFromWSWindow.getTagWSComboBox().addValueChangeListener(
				listener);
	}

	public void addOpenProjectUploadReceiver(Receiver receiver) {
		openProjectWindow.getUploadComponent().setReceiver(receiver);
	}

	public void addOpenProjectUploadSucceededListener(SucceededListener listener) {
		openProjectWindow.getUploadComponent().addSucceededListener(listener);
	}

	public void addOpenProjectUploadFailedListener(FailedListener listener) {
		openProjectWindow.getUploadComponent().addFailedListener(listener);
	}

	public void addOpenProjectUploadProgressListener(ProgressListener listener) {
		openProjectWindow.getUploadComponent().addProgressListener(listener);
	}

	public void addOpenProjectUploadFinishedListener(FinishedListener listener) {
		openProjectWindow.getUploadComponent().addFinishedListener(listener);
	}

	public void addOpenProjectUploadStartedListener(StartedListener listener) {
		openProjectWindow.getUploadComponent().addStartedListener(listener);
	}

	public void addOpenProjectDropBoxDropHandler(DropHandler dropHandler) {
		openProjectWindow.getDropBox().setDropHandler(dropHandler);
	}

	public void addLoadNomenFromFileUploadReceiver(Receiver receiver) {
		loadNomenFromFileWindow.getUploadComponent().setReceiver(receiver);
	}

	public void addLoadNomenFromFileUploadSucceededListener(
			SucceededListener listener) {
		loadNomenFromFileWindow.getUploadComponent().addSucceededListener(
				listener);
	}

	public void addLoadNomenFromFileUploadFailedListener(FailedListener listener) {
		loadNomenFromFileWindow.getUploadComponent()
				.addFailedListener(listener);
	}

	public void addLoadNomenFromFileUploadProgressListener(
			ProgressListener listener) {
		loadNomenFromFileWindow.getUploadComponent().addProgressListener(
				listener);
	}

	public void addLoadNomenFromFileUploadFinishedListener(
			FinishedListener listener) {
		loadNomenFromFileWindow.getUploadComponent().addFinishedListener(
				listener);
	}

	public void addLoadNomenFromFileUploadStartedListener(
			StartedListener listener) {
		loadNomenFromFileWindow.getUploadComponent().addStartedListener(
				listener);
	}

	public void addLoadNomenFromFileDropBoxDropHandler(DropHandler dropHandler) {
		loadNomenFromFileWindow.getDropBox().setDropHandler(dropHandler);
	}

	public void addLoadFolksoFromFileUploadReceiver(Receiver receiver) {
		loadTagsetFromFileWindow.getUploadComponent().setReceiver(receiver);
	}

	public void addLoadFolksoFromFileUploadSucceededListener(
			SucceededListener listener) {
		loadTagsetFromFileWindow.getUploadComponent().addSucceededListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadFailedListener(
			FailedListener listener) {
		loadTagsetFromFileWindow.getUploadComponent().addFailedListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadProgressListener(
			ProgressListener listener) {
		loadTagsetFromFileWindow.getUploadComponent().addProgressListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadFinishedListener(
			FinishedListener listener) {
		loadTagsetFromFileWindow.getUploadComponent().addFinishedListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadStartedListener(
			StartedListener listener) {
		loadTagsetFromFileWindow.getUploadComponent().addStartedListener(
				listener);
	}

	public void addLoadFolksoFromFileDropBoxDropHandler(DropHandler dropHandler) {
		loadTagsetFromFileWindow.getDropBox().setDropHandler(dropHandler);
	}

	public void addMatchingWindowTableValueChangeListener(
			ValueChangeListener listener) {
		matchingWindow.getTable().addValueChangeListener(listener);
	}

	public void addMatchingWindowButtonClickListener(ClickListener listener) {
		matchingWindow.getButton().addClickListener(listener);
	}

	public void addMatchingResultsWindowButtonClickListener(
			ClickListener listener) {
		matchingResultsWindow.getButton().addClickListener(listener);
	}

	public void addMatchingResultsWindowTableValueChangeListener(
			ValueChangeListener listener) {
		matchingResultsWindow.getTable().addValueChangeListener(listener);
	}

	/**
	 * @return the controller
	 */
	public CWBController getController() {
		return controller;
	}

}
