package lig.steamer.cwb.ui;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import javax.servlet.annotation.WebServlet;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.controller.CWBController;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBInstanceFolkso;
import lig.steamer.cwb.model.CWBInstanceNomen;
import lig.steamer.cwb.model.CWBModel;
import lig.steamer.cwb.ui.map.CWBMap;
import lig.steamer.cwb.ui.menu.CWBMenuBar;
import lig.steamer.cwb.ui.panel.CWBAlignPanel;
import lig.steamer.cwb.ui.panel.CWBFolksoPanel;
import lig.steamer.cwb.ui.panel.CWBNomenPanel;
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

	private final CWBMenuBar menuBar = new CWBMenuBar();

	private final CWBOpenProjectWindow openProjectWindow = new CWBOpenProjectWindow();
	private final CWBLoadFolksoFromWSWindow loadFolksoFromWSWindow = new CWBLoadFolksoFromWSWindow();
	private final CWBLoadNomenFromFileWindow loadNomenFromFileWindow = new CWBLoadNomenFromFileWindow();
	private final CWBLoadNomenFromWSWindow loadNomenFromWSWindow = new CWBLoadNomenFromWSWindow();
	private final CWBLoadFolksoFromFileWindow loadFolksoFromFileWindow = new CWBLoadFolksoFromFileWindow();

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
		horizontalLayout.addComponent(folksoPanel);
		horizontalLayout.addComponent(nomenPanel);
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
		model.addObserver(new CWBEquivalencesObserver());
		model.addObserver(new CWBInstancesFolksoObserver());
		model.addObserver(new CWBInstancesNomenObserver());
		model.addObserver(new CWBIsReadyForMatchingObserver());

		new CWBController(model, this);

	}

	public void clear() {
		nomenPanel.clear();
		folksoPanel.clear();
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

	public Window getLoadNomenFromWSWindow() {
		return loadNomenFromWSWindow;
	}
	
	public ComboBox getNomenWSCombobox() {
		return loadNomenFromWSWindow.getComboBox();
	}

	public Button getLoadNomenFromWSButton() {
		return loadNomenFromWSWindow.getLoadButton();
	}
	
	public Window getLoadFolksoFromWSWindow() {
		return loadFolksoFromWSWindow;
	}

	public ComboBox getFolksoWSCombobox() {
		return loadFolksoFromWSWindow.getComboBox();
	}

	public Button getLoadFolksoFromWSButton() {
		return loadFolksoFromWSWindow.getLoadButton();
	}

	public CWBLoadFolksoFromFileWindow getLoadFolksoFromFileWindow() {
		return loadFolksoFromFileWindow;
	}

	public CWBLoadNomenFromFileWindow getLoadNomenFromFileWindow() {
		return loadNomenFromFileWindow;
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

	public void addLoadFolksoFromWSWindowButtonListener(ClickListener listener) {
		loadFolksoFromWSWindow.getLoadButton().addClickListener(listener);
	}
	
	public void addLoadNomenFromWSWindowButtonListener(ClickListener listener) {
		loadNomenFromWSWindow.getLoadButton().addClickListener(listener);
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
	
	public void addLoadNomenFromWSMenuItemCommand(Command command) {
		menuBar.getLoadNomenFromWSMenuItem().setCommand(command);
	}

	public void addLoadNomenFromFileMenuItemCommand(Command command) {
		menuBar.getLoadNomenFromFileMenuItem().setCommand(command);
	}

	public void addLoadFolksoFromWSButtonListener(ClickListener listener) {
		folksoPanel.getLoadFromWSButton().addClickListener(listener);
	}

	public void addLoadFolksoFromFileButtonListener(ClickListener listener) {
		folksoPanel.getLoadFromFileButton().addClickListener(listener);
	}

	public void addLoadNomenFromFileButtonListener(ClickListener listener) {
		nomenPanel.getLoadFromFileButton().addClickListener(listener);
	}
	
	public void addLoadNomenFromWSButtonListener(ClickListener listener) {
		nomenPanel.getLoadFromWSButton().addClickListener(listener);
	}
	
	public void addNomenWSComboBoxListener(ValueChangeListener listener) {
		loadNomenFromWSWindow.getComboBox().addValueChangeListener(
				listener);
	}

	public void addMapMenuItemCommand(Command command) {
		menuBar.getMapMenuItem().setCommand(command);
	}

	public void addFolksoWSComboBoxListener(ValueChangeListener listener) {
		loadFolksoFromWSWindow.getComboBox().addValueChangeListener(
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

	public void addLoadFolksoFromFileUploadReceiver(Receiver receiver) {
		loadFolksoFromFileWindow.getUploadComponent().setReceiver(receiver);
	}

	public void addLoadFolksoFromFileUploadSucceededListener(
			SucceededListener listener) {
		loadFolksoFromFileWindow.getUploadComponent().addSucceededListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadFailedListener(
			FailedListener listener) {
		loadFolksoFromFileWindow.getUploadComponent().addFailedListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadProgressListener(
			ProgressListener listener) {
		loadFolksoFromFileWindow.getUploadComponent().addProgressListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadFinishedListener(
			FinishedListener listener) {
		loadFolksoFromFileWindow.getUploadComponent().addFinishedListener(
				listener);
	}

	public void addLoadFolksoFromFileUploadStartedListener(
			StartedListener listener) {
		loadFolksoFromFileWindow.getUploadComponent().addStartedListener(
				listener);
	}

	public void addMatchButtonListener(ClickListener listener) {
		matchButton.addClickListener(listener);
	}

	public void addMatchingResultsTableValueChangedListener(
			ValueChangeListener listener) {
		alignPanel.getTable().addValueChangeListener(listener);
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
				CWBDataModelFolkso folkso = (CWBDataModelFolkso) arg;
				folksoPanel.setFolkso(folkso);
			}
		}

	}

	class CWBNomenObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof CWBDataModelNomen) {
				CWBDataModelNomen nomen = (CWBDataModelNomen) arg;
				nomenPanel.setNomen(nomen);
			}
		}

	}

	class CWBEquivalencesObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof Collection<?>) {
				Collection<?> collec = (Collection<?>) arg;
				if (collec.size() > 0) {
					boolean isEquivalences = true;
					for (Object obj : collec) {
						if (!(obj instanceof CWBEquivalence)) {
							isEquivalences = false;
							break;
						}
					}
					if (isEquivalences) {
						alignPanel.getTable().setEnabled(true);
						alignPanel.getContainer().removeAllItems();
						alignPanel.getContainer().addAll(
								(Collection<CWBEquivalence>) collec);
						alignPanel.getTable().refreshRowCache();
					}
				}
			}
		}

	}

	class CWBInstancesFolksoObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof Collection<?>) {
				Collection<?> collec = (Collection<?>) arg;
				
				boolean isInstancesFolkso = true;

				for (Object obj : collec) {
					if (!(obj instanceof CWBInstanceFolkso)) {
						isInstancesFolkso = false;
						break;
					}
				}

				if (isInstancesFolkso) {
					map.getClusterFolkso().removeAllComponents();
					for (Object obj : collec) {
						if (obj instanceof CWBInstanceFolkso) {
							CWBInstanceFolkso instance = (CWBInstanceFolkso) obj;
							map.addMarkerFolkso(instance);
						}
					}
				}

			}
		}
	}

	class CWBInstancesNomenObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			if (arg instanceof Collection<?>) {
				Collection<?> collec = (Collection<?>) arg;
				boolean isInstancesNomen = true;
				for (Object obj : collec) {
					if (!(obj instanceof CWBInstanceNomen)) {
						isInstancesNomen = false;
						break;
					}
				}
				if (isInstancesNomen) {
					map.getClusterNomen().removeAllComponents();
					for (Object obj : collec) {
						if (obj instanceof CWBInstanceNomen) {
							CWBInstanceNomen instance = (CWBInstanceNomen) obj;
							map.addMarkerNomen(instance);
						}
					}
				}
			}
		}
	}

}
