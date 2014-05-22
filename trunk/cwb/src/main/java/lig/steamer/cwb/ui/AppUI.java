package lig.steamer.cwb.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("reindeer")
@Title("Citizen Welfare Builder")
@SuppressWarnings("serial")
public class AppUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AppUI.class, widgetset = "lig.steamer.cwb.ui.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		/*
		 * Menubar
		 */
		
		MenuBar mainMenubar = new CWBMenuBar();
		mainMenubar.setWidth(100, Unit.PERCENTAGE);
		
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
		
		VerticalLayout accordionElementLayout = new VerticalLayout();
		accordionElementLayout.setSizeFull();
		
		final Panel accordionPanel1 = new Panel();
		accordionPanel1.setContent(accordionElementLayout);
		accordionPanel1.setSizeFull();
		
		final Panel accordionPanel2 = new Panel();
		accordionPanel1.setContent(accordionElementLayout);
		accordionPanel2.setSizeFull();
		
		final Panel accordionPanel3 = new Panel();
		accordionPanel1.setContent(accordionElementLayout);
		accordionPanel3.setSizeFull();
		
		final Accordion accordion = new Accordion();
		accordion.addTab(accordionPanel1, Messages.getString("accordion.title"), null);
		accordion.addTab(accordionPanel2, Messages.getString("accordion.title"), null);
		accordion.addTab(accordionPanel3, Messages.getString("accordion.title"), null);
		accordion.setSizeFull();
		
		Panel accordionPanel = new Panel("Information");
		accordionPanel.setSizeFull();
		VerticalLayout accordionPanelLayout = new VerticalLayout();
		accordionPanelLayout.addComponent(accordion);
		accordionPanelLayout.setSizeFull();
		accordionPanel.setContent(accordionPanelLayout);
		
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
		
		tabSheet.addTab(dataTab,Messages.getString("tabsheet.data.title"));
		tabSheet.addTab(indicatorsTab,Messages.getString("tabsheet.indicators.title"));
		tabSheet.addTab(map, Messages.getString("tabsheet.map.title"));
		
		final HorizontalLayout centralLayout = new HorizontalLayout();
		centralLayout.addComponent(accordionPanel);
		centralLayout.addComponent(tabSheet);
		centralLayout.setExpandRatio(accordionPanel, 0.2f);
		centralLayout.setExpandRatio(tabSheet, 0.8f);
		centralLayout.setSizeFull();
		centralLayout.setSpacing(true);
		centralLayout.setMargin(true);

		final VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.addComponent(mainMenubar);
		rootLayout.addComponent(titleLayout);
		rootLayout.addComponent(centralLayout);
		rootLayout.setExpandRatio(centralLayout, 0.9f);
		rootLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		
		setContent(rootLayout);
		
		/*
		 * Notification
		 */
		
		Notification welcomeNotification = new Notification("Welcome !",
			    "Welcome to Citizen Welfare Builder !");
		
		welcomeNotification.show(Page.getCurrent());
		welcomeNotification.setDelayMsec(Notification.DELAY_NONE);
	}

}
