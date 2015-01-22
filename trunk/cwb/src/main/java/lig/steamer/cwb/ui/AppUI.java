package lig.steamer.cwb.ui;

import java.text.MessageFormat;
import java.util.Observable;
import java.util.Observer;

import javax.servlet.annotation.WebServlet;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.controller.CWBController;
import lig.steamer.cwb.model.CWBAlignment;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBDataSetFolkso;
import lig.steamer.cwb.model.CWBDataSetNomen;
import lig.steamer.cwb.model.CWBInstanceFolkso;
import lig.steamer.cwb.model.CWBInstanceNomen;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.map.CWBMap;
import lig.steamer.cwb.ui.menu.CWBMenuBar;
import lig.steamer.cwb.ui.panel.CWBAlignPanel;
import lig.steamer.cwb.ui.panel.CWBFolksoPanel;
import lig.steamer.cwb.ui.panel.CWBNomenPanel;
import lig.steamer.cwb.ui.window.CWBLoadAlignFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadAlignFromWSWindow;
import lig.steamer.cwb.ui.window.CWBLoadFolksoFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadFolksoFromWSWindow;
import lig.steamer.cwb.ui.window.CWBLoadNomenFromFileWindow;
import lig.steamer.cwb.ui.window.CWBLoadNomenFromWSWindow;
import lig.steamer.cwb.ui.window.CWBOpenProjectWindow;

import org.vaadin.addon.leaflet.LeafletMoveEndListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
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

	private final CWBFolksoPanel folksoPanel = new CWBFolksoPanel();
	private final CWBNomenPanel nomenPanel = new CWBNomenPanel();
	private final CWBAlignPanel alignPanel = new CWBAlignPanel();
	private final CWBMap map = new CWBMap();

	private final Button matchButton = new Button();

	private CWBModel model;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AppUI.class, widgetset = "lig.steamer.cwb.ui.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		AbsoluteLayout absoluteLayout = new AbsoluteLayout();
		absoluteLayout.setWidth("20%");
		absoluteLayout.setHeight("0px");
		absoluteLayout.setStyleName("header-layout");

		Label mainTitle = new Label(Msg.get("main.title"));
		mainTitle.setStyleName(Reindeer.LABEL_H1);

		absoluteLayout.addComponent(mainTitle, "right: 2%; top: -3px;");

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(nomenPanel);
		horizontalLayout.addComponent(folksoPanel);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();

		matchButton.setEnabled(false);
		matchButton.setCaption(Msg.get("matching.button"));

		final VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.addComponent(horizontalLayout);
		leftLayout.setExpandRatio(horizontalLayout, 0.58f);
		leftLayout.addComponent(matchButton);
		leftLayout.setComponentAlignment(matchButton, Alignment.MIDDLE_CENTER);
		leftLayout.setExpandRatio(matchButton, 0.02f);
		leftLayout.addComponent(alignPanel);
		leftLayout.setExpandRatio(alignPanel, 0.4f);
		leftLayout.setSizeFull();
		leftLayout.setSpacing(true);

		final VerticalLayout mapLayout = new VerticalLayout();
		mapLayout.setSizeFull();
		mapLayout.addComponent(map);

		final Panel mapPanel = new Panel(Msg.get("map.capt"), mapLayout);
		mapPanel.setSizeFull();

		final HorizontalLayout centralLayout = new HorizontalLayout();
		centralLayout.addComponent(leftLayout);
		centralLayout.addComponent(mapPanel);
		centralLayout.setExpandRatio(leftLayout, 0.5f);
		centralLayout.setExpandRatio(mapPanel, 0.5f);
		centralLayout.setSizeFull();
		centralLayout.setSpacing(true);
		centralLayout.setMargin(new MarginInfo(true, true, false, true));

		final VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.addComponent(absoluteLayout);
		rootLayout.addComponent(menuBar);
		rootLayout.addComponent(centralLayout);
		rootLayout.setExpandRatio(centralLayout, 0.9f);
		rootLayout.setStyleName(Reindeer.LAYOUT_BLUE);

		setContent(rootLayout);

		model = new CWBModel();
		model.addObserver(new CWBFolksoObserver());
		model.addObserver(new CWBNomenObserver());
		model.addObserver(new CWBAlignmentObserver());
		model.addObserver(new CWBDatasetFolksoObserver());
		model.addObserver(new CWBDatasetNomenObserver());
		model.addObserver(new CWBIsReadyForMatchingObserver());

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

	public CWBNomenPanel getNomenPanel() {
		return nomenPanel;
	}

	public CWBFolksoPanel getFolksoPanel() {
		return folksoPanel;
	}

	public CWBAlignPanel getAlignPanel() {
		return alignPanel;
	}

	public CWBMap getMap() {
		return map;
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

	public void addLoadFolksoFromWSWindowButtonListener(ClickListener listener) {
		loadFolksoFromWSWindow.getLoadButton().addClickListener(listener);
	}

	public void addLoadNomenFromWSWindowButtonListener(ClickListener listener) {
		loadNomenFromWSWindow.getLoadButton().addClickListener(listener);
	}

	public void addNomenWSComboBoxListener(ValueChangeListener listener) {
		loadNomenFromWSWindow.getComboBox().addValueChangeListener(listener);
	}

	public void addMapMenuItemCommand(Command command) {
		menuBar.getMapMenuItem().setCommand(command);
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
		map.addMoveEndListener(listener);
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
					System.out.println("folkso changed !");
					CWBDataModelFolkso folkso = (CWBDataModelFolkso) arg;
					folksoPanel.getDataModelContainer().removeAllItems();
					folksoPanel.getDataModelContainer().addAll(
							folkso.getConcepts());
					folksoPanel.getTable().sort();
					folksoPanel.getTable().refreshRowCache();
					folksoPanel.setCaption(MessageFormat.format(Msg
							.get("folkso.capt"), folkso.getConcepts().size(),
							folkso.getNamespace().toString()));
				}
			}
		}

	}

	class CWBNomenObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBDataModelNomen) {
				if (arg != null) {
					System.out.println("nomen changed !");
					CWBDataModelNomen nomen = (CWBDataModelNomen) arg;
					nomenPanel.getDataModelContainer().removeAllItems();
					nomenPanel.getDataModelContainer().addAll(
							nomen.getConcepts());
					nomenPanel.getTable().sort();
					nomenPanel.getTable().refreshRowCache();
					nomenPanel.setCaption(MessageFormat.format(
							Msg.get("nomen.capt"), nomen.getConcepts().size(),
							nomen.getNamespace().toString()));
				}
			}
		}

	}

	class CWBAlignmentObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBAlignment) {
				System.out.println("alignment changed");
				if (arg != null) {
					CWBAlignment align = (CWBAlignment) arg;
					alignPanel.getTable().setEnabled(true);
					alignPanel.getDataModelContainer().removeAllItems();
					alignPanel.getDataModelContainer().addAll(
							align.getEquivalences());
					alignPanel.getTable().refreshRowCache();
					alignPanel.getTable().sort();
					alignPanel
							.setCaption(MessageFormat.format(Msg
									.get("align.capt"), align.getEquivalences()
									.size()));

				}
			}
		}

	}

	class CWBDatasetFolksoObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBDataSetFolkso) {
				map.removeFolksoMarkers();
				System.out.println("instances folkso: ");
				for (CWBInstanceFolkso instance : ((CWBDataSetFolkso) arg)
						.getInstances()) {
					System.out.println("=> " + instance.getLabel());
					map.addMarkerFolkso(instance);
				}

			}
		}
	}

	class CWBDatasetNomenObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBDataSetNomen) {
				map.removeNomenMarkers();
				System.out.println("instances nomen: ");
				for (CWBInstanceNomen instance : ((CWBDataSetNomen) arg)
						.getInstances()) {
					System.out.println("=> " + instance.getLabel());
					map.addMarkerNomen(instance);
				}
			}
		}
	}

}
