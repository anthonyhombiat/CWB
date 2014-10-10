package lig.steamer.cwb.ui.panel;

import java.text.MessageFormat;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelMatched;
import lig.steamer.cwb.model.CWBDataModelNomen;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class CWBDataModelsPanel extends TabSheet {

	private static final long serialVersionUID = 1L;

	private final VerticalLayout accordionNomenPanelLayout = new VerticalLayout();
	private final VerticalLayout accordionFolksoPanelLayout = new VerticalLayout();
	private final VerticalLayout accordionMatchedPanelLayout = new VerticalLayout();

	private final Accordion accordionNomen = new Accordion();
	private final Accordion accordionFolkso = new Accordion();
	private final Accordion accordionMatched = new Accordion();

	private final Panel defaultFolksoPanel = new Panel();
	private final Panel defaultNomenPanel = new Panel();

	private final Button addFolksoFromWS = new Button();
	private final Button addFolksoFromFile = new Button();

	private final Button addNomenFromWS = new Button();
	private final Button addNomenFromFile = new Button();

	private final Tab tabNomen;
	private final Tab tabFolkso;
	private final Tab tabMatched;

	public CWBDataModelsPanel() {

		super();

		this.setCaption(Msg.get("accordion.datamodels.capt"));

		accordionNomen.setSizeFull();
		accordionFolkso.setSizeFull();
		accordionMatched.setSizeFull();

		accordionNomenPanelLayout.setSizeFull();
		accordionFolksoPanelLayout.setSizeFull();
		accordionMatchedPanelLayout.setSizeFull();

		tabNomen = this.addTab(accordionNomenPanelLayout);
		tabFolkso = this.addTab(accordionFolksoPanelLayout);
		tabMatched = this.addTab(accordionMatchedPanelLayout);

		init();

		this.setSizeFull();
	}

	public void init() {

		tabNomen.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.nomen.capt"), 0));
		tabFolkso.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.folkso.capt"), 0));
		tabMatched.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.matched.capt"), 0));

		/* Default folkso panel */

		addFolksoFromWS.setCaption(Msg.get("main.menu.data.load.folkso.ws"));
		addFolksoFromFile
				.setCaption(Msg.get("main.menu.data.load.folkso.file"));
		addFolksoFromWS.setStyleName(Reindeer.BUTTON_DEFAULT);
		addFolksoFromFile.setStyleName(Reindeer.BUTTON_DEFAULT);

		Label defaultFolksoLabel = new Label("No folksonomy loaded yet.");
		defaultFolksoLabel.setStyleName(Reindeer.LABEL_SMALL);
		defaultFolksoLabel.setSizeUndefined();

		VerticalLayout defaultFolksoLayout = new VerticalLayout();
		defaultFolksoLayout.setSpacing(true);
		defaultFolksoLayout.addComponent(defaultFolksoLabel);
		defaultFolksoLayout.addComponent(addFolksoFromWS);
		defaultFolksoLayout.addComponent(addFolksoFromFile);
		defaultFolksoLayout.setComponentAlignment(defaultFolksoLabel,
				Alignment.MIDDLE_CENTER);
		defaultFolksoLayout.setComponentAlignment(addFolksoFromWS,
				Alignment.MIDDLE_CENTER);
		defaultFolksoLayout.setComponentAlignment(addFolksoFromFile,
				Alignment.MIDDLE_CENTER);

		defaultFolksoPanel.setContent(defaultFolksoLayout);
		defaultFolksoPanel.setStyleName(Reindeer.PANEL_LIGHT);

		accordionFolksoPanelLayout.addComponent(defaultFolksoPanel);
		accordionFolksoPanelLayout.setComponentAlignment(defaultFolksoPanel,
				Alignment.MIDDLE_CENTER);

		/* Default nomen panel */

		addNomenFromWS.setCaption(Msg.get("main.menu.data.load.nomen.ws"));
		addNomenFromFile.setCaption(Msg.get("main.menu.data.load.nomen.file"));
		addNomenFromWS.setStyleName(Reindeer.BUTTON_DEFAULT);
		addNomenFromFile.setStyleName(Reindeer.BUTTON_DEFAULT);

		Label defaultNomenLabel = new Label("No nomenclature loaded yet.");
		defaultNomenLabel.setStyleName(Reindeer.LABEL_SMALL);
		defaultNomenLabel.setSizeUndefined();

		VerticalLayout defaultNomenLayout = new VerticalLayout();
		defaultNomenLayout.setSpacing(true);
		defaultNomenLayout.addComponent(defaultNomenLabel);
		defaultNomenLayout.addComponent(addNomenFromWS);
		defaultNomenLayout.addComponent(addNomenFromFile);
		defaultNomenLayout.setComponentAlignment(defaultNomenLabel,
				Alignment.MIDDLE_CENTER);
		defaultNomenLayout.setComponentAlignment(addNomenFromWS,
				Alignment.MIDDLE_CENTER);
		defaultNomenLayout.setComponentAlignment(addNomenFromFile,
				Alignment.MIDDLE_CENTER);

		defaultNomenPanel.setContent(defaultNomenLayout);
		defaultNomenPanel.setStyleName(Reindeer.PANEL_LIGHT);

		accordionNomenPanelLayout.addComponent(defaultNomenPanel);
		accordionNomenPanelLayout.setComponentAlignment(defaultNomenPanel,
				Alignment.MIDDLE_CENTER);

		this.setSelectedTab(this.getTab(0));
	}

	public void clear() {

		accordionNomen.removeAllComponents();
		accordionFolkso.removeAllComponents();
		accordionMatched.removeAllComponents();

		accordionFolksoPanelLayout.removeComponent(accordionFolkso);
		accordionNomenPanelLayout.removeComponent(accordionNomen);

		init();

	}

	public void addDataModel(CWBDataModel dataModel) {

		if (dataModel instanceof CWBDataModelNomen) {
			addDataModelNomen((CWBDataModelNomen) dataModel);
		} else if (dataModel instanceof CWBDataModelFolkso) {
			addDataModelFolkso((CWBDataModelFolkso) dataModel);
		} else if (dataModel instanceof CWBDataModelMatched) {
			addDataModelMatched((CWBDataModelMatched) dataModel);
		}
		// TODO throw exception
	}

	public void addDataModelNomen(CWBDataModelNomen dataModelNomen) {

		accordionNomenPanelLayout.removeComponent(defaultNomenPanel);
		accordionNomenPanelLayout.addComponent(accordionNomen);

		CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
				CWBConcept.class, "parent");

		for (CWBConcept concept : dataModelNomen.getConcepts()) {
			dataModelContainer.addBean(concept);
		}

		Tab tab = accordionNomen.addTab(buildTreeTable(dataModelContainer),
				dataModelNomen.getNamespace().toString());

		accordionNomen.setSelectedTab(tab);
		tabNomen.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.nomen.capt"),
				accordionNomen.getComponentCount()));

		this.setSelectedTab(tabNomen);
	}

	public void addDataModelFolkso(CWBDataModelFolkso dataModelFolkso) {

		accordionFolksoPanelLayout.removeComponent(defaultFolksoPanel);
		accordionFolksoPanelLayout.addComponent(accordionFolkso);

		CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
				CWBConcept.class, "parent");

		for (CWBConcept concept : dataModelFolkso.getConcepts()) {
			dataModelContainer.addBean(concept);
		}

		Tab tab = accordionFolkso.addTab(buildTreeTable(dataModelContainer),
				dataModelFolkso.getNamespace().toString());

		accordionFolkso.setSelectedTab(tab);
		tabFolkso.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.folkso.capt"),
				accordionFolkso.getComponentCount()));

		this.setSelectedTab(tabFolkso);
	}

	public void addDataModelMatched(CWBDataModelMatched dataModelMatched) {

		CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
				CWBConcept.class, "parent");

		for (CWBConcept concept : dataModelMatched.getConcepts()) {
			dataModelContainer.addBean(concept);
		}

		Tab tab = accordionMatched.addTab(buildTreeTable(dataModelContainer),
				dataModelMatched.getNamespace().toString());

		accordionMatched.setSelectedTab(tab);
		tabMatched.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.matched.capt"),
				accordionMatched.getComponentCount()));

		this.setSelectedTab(tabMatched);
	}

	private Component buildTreeTable(Container container) {

		TreeTable treeTable = new TreeTable();
		treeTable.setContainerDataSource(container);
		treeTable.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		treeTable.setItemCaptionPropertyId(Msg
				.get("accordion.datamodels.table.col.fragment"));

		treeTable.setColumnCollapsingAllowed(true);
		treeTable.setColumnReorderingAllowed(true);
		treeTable.setSizeFull();
		treeTable.setSelectable(true);

		treeTable.setVisibleColumns(
				Msg.get("accordion.datamodels.table.col.iri"),
				Msg.get("accordion.datamodels.table.col.fragment"),
				Msg.get("accordion.datamodels.table.col.labels"),
				Msg.get("accordion.datamodels.table.col.descriptions"));

		treeTable.setColumnCollapsed(
				Msg.get("accordion.datamodels.table.col.iri"), true);
		treeTable
				.setColumnCollapsed(Msg
						.get("accordion.datamodels.table.col.descriptions"),
						true);

		treeTable.setSortContainerPropertyId(Msg
				.get("accordion.datamodels.table.col.fragment"));
		treeTable.sort();

		VerticalLayout accordionElementLayout = new VerticalLayout();
		accordionElementLayout.setSizeFull();
		accordionElementLayout.addComponent(treeTable);

		return accordionElementLayout;
	}

	public Accordion getAccordionNomen() {
		return accordionNomen;
	}

	public Accordion getAccordionTags() {
		return accordionFolkso;
	}

	public Accordion getAccordionMatched() {
		return accordionMatched;
	}

	public Tab getTabNomen() {
		return tabNomen;
	}

	public Tab getTabFolkso() {
		return tabFolkso;
	}

	public Tab getTabMatched() {
		return tabMatched;
	}
	
	public Button getAddFolksoFromWS() {
		return addFolksoFromWS;
	}

	public Button getAddFolksoFromFile() {
		return addFolksoFromFile;
	}

	public Button getAddNomenFromWS() {
		return addNomenFromWS;
	}

	public Button getAddNomenFromFile() {
		return addNomenFromFile;
	}

}
