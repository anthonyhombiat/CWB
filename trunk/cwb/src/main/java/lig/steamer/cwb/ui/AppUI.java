package lig.steamer.cwb.ui;

import java.util.Observable;
import java.util.Observer;

import javax.servlet.annotation.WebServlet;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.controller.CWBController;
import lig.steamer.cwb.model.CWBAlignment;
import lig.steamer.cwb.model.CWBBuffer;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBDataSetFolkso;
import lig.steamer.cwb.model.CWBDataSetNomen;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.model.CWBStudyArea;
import lig.steamer.cwb.ui.menu.CWBMenuBar;
import lig.steamer.cwb.ui.panel.CWBAlignPanel;
import lig.steamer.cwb.ui.panel.CWBFolksoPanel;
import lig.steamer.cwb.ui.panel.CWBMapPanel;
import lig.steamer.cwb.ui.panel.CWBNomenPanel;
import lig.steamer.cwb.ui.window.CWBBufferOptionsWindow;
import lig.steamer.cwb.ui.window.CWBLoadAlignFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadAlignFromWSWindow;
import lig.steamer.cwb.ui.window.CWBLoadFolksoFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadFolksoFromWSWindow;
import lig.steamer.cwb.ui.window.CWBLoadNomenFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadNomenFromWSWindow;
import lig.steamer.cwb.ui.window.CWBOpenProjectWindow;
import lig.steamer.cwb.ui.window.CWBSelectDataProviderWindow;
import lig.steamer.cwb.ui.window.CWBSelectStudyAreaWindow;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.alump.fancylayouts.FancyNotifications;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyNotificationsState.Position;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

@Theme("cwbtheme")
@Title("Citizen Welfare Builder")
@SuppressWarnings("serial")
public class AppUI extends UI {

	private final CWBMenuBar menuBar = new CWBMenuBar();

	private final CWBOpenProjectWindow openProjectWindow = new CWBOpenProjectWindow();
	private final CWBLoadNomenFromFileWindow loadNomenFromFileWindow = new CWBLoadNomenFromFileWindow();
	private final CWBLoadNomenFromWSWindow loadNomenFromWSWindow = new CWBLoadNomenFromWSWindow();
	private final CWBLoadFolksoFromFileWindow loadFolksoFromFileWindow = new CWBLoadFolksoFromFileWindow();
	private final CWBLoadFolksoFromWSWindow loadFolksoFromWSWindow = new CWBLoadFolksoFromWSWindow();
	private final CWBLoadAlignFromFileWindow loadAlignFromFileWindow = new CWBLoadAlignFromFileWindow();
	private final CWBLoadAlignFromWSWindow loadAlignFromWSWindow = new CWBLoadAlignFromWSWindow();
	private final CWBSelectStudyAreaWindow selectStudyAreaWindow = new CWBSelectStudyAreaWindow();
	private final CWBSelectDataProviderWindow selectDataProviderWindow = new CWBSelectDataProviderWindow();
	private final CWBBufferOptionsWindow bufferOptionsWindow = new CWBBufferOptionsWindow();

	private final VerticalLayout modelLayout = new VerticalLayout();
	
	private final CWBFolksoPanel folksoPanel = new CWBFolksoPanel();
	private final CWBNomenPanel nomenPanel = new CWBNomenPanel();
	private final CWBAlignPanel alignPanel = new CWBAlignPanel();

	private final FancyNotifications notifs = new FancyNotifications();

	private CWBMapPanel mapPanel;

	private final Button matchButton = new Button();

	private CWBModel model;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AppUI.class, widgetset = "lig.steamer.cwb.ui.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		// init model
		model = new CWBModel();
		model.addObserver(new CWBFolksoObserver());
		model.addObserver(new CWBNomenObserver());
		model.addObserver(new CWBAlignmentObserver());
		model.addObserver(new CWBDatasetFolksoObserver());
		model.addObserver(new CWBDatasetNomenObserver());
		model.addObserver(new CWBIsReadyForMatchingObserver());
		model.addObserver(new CWBBufferOptionsObserver());
		model.addObserver(new CWBStudyAreaObserver());

		Label mainTitle = new Label(Msg.get("main.title"));
		mainTitle.setStyleName(Reindeer.LABEL_H1);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(nomenPanel);
		horizontalLayout.addComponent(folksoPanel);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();

		matchButton.setEnabled(false);
		matchButton.setCaption(Msg.get("matching.button"));

		modelLayout.addComponent(horizontalLayout);
		modelLayout.setExpandRatio(horizontalLayout, 0.58f);
		modelLayout.addComponent(matchButton);
		modelLayout.setComponentAlignment(matchButton, Alignment.MIDDLE_CENTER);
		modelLayout.setExpandRatio(matchButton, 0.02f);
		modelLayout.addComponent(alignPanel);
		modelLayout.setExpandRatio(alignPanel, 0.4f);
		modelLayout.setSizeFull();
		modelLayout.setSpacing(true);

		mapPanel = new CWBMapPanel(model.getStudyArea(), model.getBuffer());

		final HorizontalLayout centralLayout = new HorizontalLayout();
		centralLayout.addComponent(modelLayout);
		centralLayout.addComponent(mapPanel);
		centralLayout.setExpandRatio(modelLayout, 0.5f);
		centralLayout.setExpandRatio(mapPanel, 0.5f);
		centralLayout.setSizeFull();
		centralLayout.setSpacing(true);
		centralLayout.setMargin(new MarginInfo(true, true, false, true));

		final CssLayout notifLayout = new CssLayout();
		notifs.setPosition(Position.BOTTOM_RIGHT);
		notifs.setClickClose(true);
		notifLayout.addComponent(notifs);

		final VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.addComponent(mainTitle);
		rootLayout.addComponent(menuBar);
		rootLayout.addComponent(centralLayout);
		rootLayout.setExpandRatio(centralLayout, 0.9f);
		rootLayout.addComponent(notifLayout);
		rootLayout.setStyleName(Reindeer.LAYOUT_BLUE);

		setContent(rootLayout);

		new CWBController(model, this);

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

	public Window getLoadFolksoFromFileWindow() {
		return loadFolksoFromFileWindow;
	}

	public Window getLoadFolksoFromWSWindow() {
		return loadFolksoFromWSWindow;
	}

	public Window getLoadNomenFromFileWindow() {
		return loadNomenFromFileWindow;
	}

	public Window getLoadNomenFromWSWindow() {
		return loadNomenFromWSWindow;
	}

	public Window getLoadAlignFromFileWindow() {
		return loadAlignFromFileWindow;
	}

	public Window getLoadAlignFromWSWindow() {
		return loadAlignFromWSWindow;
	}

	public Window getSelectStudyAreaWindow() {
		return selectStudyAreaWindow;
	}

	public Window getSelectDataProviderWindow() {
		return selectDataProviderWindow;
	}

	public Window getBufferOptionsWindow() {
		return bufferOptionsWindow;
	}

	public ComboBox getNomenWSCombobox() {
		return loadNomenFromWSWindow.getComboBox();
	}

	public Button getLoadNomenFromWSButton() {
		return loadNomenFromWSWindow.getLoadButton();
	}

	public ComboBox getFolksoWSCombobox() {
		return loadFolksoFromWSWindow.getComboBox();
	}

	public Button getLoadFolksoFromWSButton() {
		return loadFolksoFromWSWindow.getLoadButton();
	}

	public Button getDataProviderOkButton() {
		return selectDataProviderWindow.getOkButton();
	}

	public Button getStudyAreaOkButton() {
		return selectStudyAreaWindow.getOkButton();
	}

	public Button getBufferOptionsOkButton() {
		return bufferOptionsWindow.getOkButton();
	}

	public ComboBox getStudyAreaComboBox() {
		return selectStudyAreaWindow.getComboBox();
	}

	public ComboBox getDataProviderNomenComboBox() {
		return selectDataProviderWindow.getNomenDataProviderComboBox();
	}

	public ComboBox getDataProviderFolksoComboBox() {
		return selectDataProviderWindow.getFolksoDataProviderComboBox();
	}

	public CheckBox getBufferDisplayCheckBox() {
		return bufferOptionsWindow.getBufferDisplayCheckBox();
	}

	public Slider getBufferSizeSlider() {
		return bufferOptionsWindow.getBufferSizeSlider();
	}

	public CWBNomenPanel getNomenPanel() {
		return nomenPanel;
	}

	public CWBFolksoPanel getFolksoPanel() {
		return folksoPanel;
	}

	public CWBAlignPanel getAlignPanel() {
		return alignPanel;
	}

	public LMap getMap() {
		return mapPanel.getMap();
	}
	
	public VerticalLayout getModelLayout(){
		return modelLayout;
	}

	public Panel getMapPanel(){
		return mapPanel;
	}
	
	/****************************/
	/*** ADD LISTENER METHODS ***/
	/****************************/

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

	public void addLoadAlignFromWSMenuItemCommand(Command command) {
		menuBar.getLoadAlignFromWSMenuItem().setCommand(command);
	}

	public void addLoadNomenFromWSMenuItemCommand(Command command) {
		menuBar.getLoadNomenFromWSMenuItem().setCommand(command);
	}

	public void addLoadNomenFromFileMenuItemCommand(Command command) {
		menuBar.getLoadNomenFromFileMenuItem().setCommand(command);
	}

	public void addLoadAlignFromFileMenuItemCommand(Command command) {
		menuBar.getLoadAlignFromFileMenuItem().setCommand(command);
	}

	public void addStudyAreaMenuItemCommand(Command command) {
		menuBar.getStudyAreaMenuItem().setCommand(command);
	}

	public void addDataProviderMenuItemCommand(Command command) {
		menuBar.getdataProviderMenuItem().setCommand(command);
	}

	public void addBufferOptionsMenuItemCommand(Command command) {
		menuBar.getBufferOptionsMenuItem().setCommand(command);
	}

	public void addMapMenuItemCommand(Command command) {
		menuBar.getMapMenuItem().setCommand(command);
	}
	
	public void addModelMenuItemCommand(Command command) {
		menuBar.getModelMenuItem().setCommand(command);
	}

	public void addLoadFolksoFromWSWindowButtonListener(ClickListener listener) {
		loadFolksoFromWSWindow.getLoadButton().addClickListener(listener);
	}

	public void addLoadNomenFromWSWindowButtonListener(ClickListener listener) {
		loadNomenFromWSWindow.getLoadButton().addClickListener(listener);
	}

	public void addNomenWSComboBoxListener(ValueChangeListener listener) {
		loadNomenFromWSWindow.getComboBox().addValueChangeListener(listener);
	}

	public void addFolksoWSComboBoxListener(ValueChangeListener listener) {
		loadFolksoFromWSWindow.getComboBox().addValueChangeListener(listener);
	}

	public void addOpenProjectUploadReceiver(Receiver receiver) {
		openProjectWindow.getUploadComponent().setReceiver(receiver);
	}

	public void addOpenProjectUploadSucceededListener(SucceededListener listener) {
		openProjectWindow.getUploadComponent().addSucceededListener(listener);
	}

	public void addLoadNomenFromFileUploadReceiver(Receiver receiver) {
		loadNomenFromFileWindow.getUploadComponent().setReceiver(receiver);
	}

	public void addLoadNomenFromFileUploadSucceededListener(
			SucceededListener listener) {
		loadNomenFromFileWindow.getUploadComponent().addSucceededListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadReceiver(Receiver receiver) {
		loadFolksoFromFileWindow.getUploadComponent().setReceiver(receiver);
	}

	public void addLoadFolksoFromFileUploadSucceededListener(
			SucceededListener listener) {
		loadFolksoFromFileWindow.getUploadComponent().addSucceededListener(
				listener);
	}

	public void addLoadAlignFromFileUploadReceiver(Receiver receiver) {
		loadAlignFromFileWindow.getUploadComponent().setReceiver(receiver);
	}

	public void addLoadAlignFromFileUploadSucceededListener(
			SucceededListener listener) {
		loadAlignFromFileWindow.getUploadComponent().addSucceededListener(
				listener);
	}

	public void addDataProviderWindowButtonListener(ClickListener listener) {
		selectDataProviderWindow.getOkButton().addClickListener(listener);
	}

	public void addStudyAreaWindowButtonListener(ClickListener listener) {
		selectStudyAreaWindow.getOkButton().addClickListener(listener);
	}

	public void addBufferOptionsWindowButtonListener(ClickListener listener) {
		bufferOptionsWindow.getOkButton().addClickListener(listener);
	}

	public void addStudyAreaComboboxValueChangedListener(
			ValueChangeListener listener) {
		selectStudyAreaWindow.getComboBox().addValueChangeListener(listener);
	}

	public void addDataProviderNomenComboboxValueChangedListener(
			ValueChangeListener listener) {
		selectDataProviderWindow.getNomenDataProviderComboBox()
				.addValueChangeListener(listener);
	}

	public void addDataProviderFolksoComboboxValueChangedListener(
			ValueChangeListener listener) {
		selectDataProviderWindow.getFolksoDataProviderComboBox()
				.addValueChangeListener(listener);
	}

	public void addMatchButtonListener(ClickListener listener) {
		matchButton.addClickListener(listener);
	}

	public void addMatchingResultsTableValueChangedListener(
			ValueChangeListener listener) {
		alignPanel.getTable().addValueChangeListener(listener);
	}

	public void addMatchingResultsTableCheckboxesListener(
			ValueChangeListener listener) {
		alignPanel.setCheckboxListener(listener);
	}

	public void addMapMoveEndListener(LeafletMoveEndListener listener) {
		mapPanel.getMap().addMoveEndListener(listener);
	}

	/******************/
	/** NOTIFICATIONS */
	/******************/

	public void info(String title, String description) {
		notifs.showNotification(null, title, description);
	}

	public void err(String title, String description) {
		notifs.showNotification(null, title, description, null,
				"fancy-notif-error");
	}

	class CWBIsReadyForMatchingObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof Boolean) {
				matchButton.setEnabled((Boolean) arg);
			}
		}

	}

	class CWBFolksoObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBDataModelFolkso) {
				if (arg != null) {
					folksoPanel.loadFolksonomy((CWBDataModelFolkso) arg);
				}
			}
		}

	}

	class CWBNomenObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBDataModelNomen) {
				if (arg != null) {
					nomenPanel.loadNomenclature((CWBDataModelNomen) arg);
				}
			}
		}

	}

	class CWBAlignmentObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBAlignment) {
				if (arg != null) {
					alignPanel.loadAlignment((CWBAlignment) arg);
				}
			}
		}

	}

	class CWBDatasetFolksoObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBDataSetFolkso) {
				CWBDataSetFolkso dataset = (CWBDataSetFolkso) arg;
				mapPanel.removeAllFolksoMarkers();
				mapPanel.addMarkersFolkso(dataset);
				mapPanel.drawFolksoBufferPolygons();
				mapPanel.updateInstanceInfoPanel();
			}
		}
	}

	class CWBDatasetNomenObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBDataSetNomen) {
				CWBDataSetNomen dataset = (CWBDataSetNomen) arg;
				mapPanel.removeAllNomenMarkers();
				mapPanel.addMarkersNomen(dataset);
				mapPanel.drawNomenBufferPolygons();
				mapPanel.updateInstanceInfoPanel();
			}
		}
	}

	class CWBBufferOptionsObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBBuffer) {
				mapPanel.setBuffer((CWBBuffer) arg);
				mapPanel.drawNomenBufferPolygons();
				mapPanel.drawFolksoBufferPolygons();
				mapPanel.updateInstanceInfoPanel();
			}
		}

	}

	class CWBStudyAreaObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBStudyArea) {
				CWBStudyArea studyArea = (CWBStudyArea) arg;
				mapPanel.setStudyArea(studyArea);
			}
		}

	}

}
