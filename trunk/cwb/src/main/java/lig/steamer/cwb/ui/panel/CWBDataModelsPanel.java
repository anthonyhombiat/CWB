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
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class CWBDataModelsPanel extends TabSheet {

	private static final long serialVersionUID = 1L;

	private final Accordion accordionNomen = new Accordion();
	private final Accordion accordionFolkso = new Accordion();
	private final Accordion accordionMatched = new Accordion();
	
	private final Tab tabNomen;
	private final Tab tabFolkso;
	private final Tab tabMatched;

	public CWBDataModelsPanel() {

		super();

		this.setCaption(Msg.get("accordion.datamodels.caption"));

		accordionNomen.setSizeFull();
		accordionFolkso.setSizeFull();
		accordionMatched.setSizeFull();

		VerticalLayout accordionNomenPanelLayout = new VerticalLayout();
		accordionNomenPanelLayout.setSizeFull();
		accordionNomenPanelLayout.addComponent(accordionNomen);

		VerticalLayout accordionTagPanelLayout = new VerticalLayout();
		accordionTagPanelLayout.setSizeFull();
		accordionTagPanelLayout.addComponent(accordionFolkso);

		VerticalLayout accordionMatchedPanelLayout = new VerticalLayout();
		accordionMatchedPanelLayout.setSizeFull();
		accordionMatchedPanelLayout.addComponent(accordionMatched);

		tabNomen = this.addTab(accordionNomenPanelLayout, MessageFormat.format(
				Msg.get("accordion.datamodels.nomen.caption"), 0));
		tabFolkso = this.addTab(accordionTagPanelLayout, MessageFormat.format(
				Msg.get("accordion.datamodels.folkso.caption"), 0));
		tabMatched = this.addTab(accordionMatchedPanelLayout, MessageFormat.format(
				Msg.get("accordion.datamodels.matched.caption"), 0));

		this.setSizeFull();
	}

	public void clear() {
		accordionNomen.removeAllComponents();
		accordionFolkso.removeAllComponents();
		accordionMatched.removeAllComponents();
		
		tabNomen.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.nomen.caption"), 0));
		tabFolkso.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.folkso.caption"), 0));
		tabMatched.setCaption(MessageFormat.format(
				Msg.get("accordion.datamodels.matched.caption"), 0));
		
		this.setSelectedTab(this.getTab(0));
	}

	public void addDataModel(CWBDataModel dataModel) {
		if (dataModel instanceof CWBDataModelNomen) {
			addDataModelNomen((CWBDataModelNomen)dataModel);
		} else if (dataModel instanceof CWBDataModelFolkso) {
			addDataModelFolkso((CWBDataModelFolkso)dataModel);
		} else if (dataModel instanceof CWBDataModelMatched) {
			addDataModelMatched((CWBDataModelMatched)dataModel);
		}
		// TODO throw exception
	}

	public void addDataModelNomen(CWBDataModelNomen dataModelNomen) {

		CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
				CWBConcept.class, "parent");

		for (CWBConcept concept : dataModelNomen.getConcepts()) {
			dataModelContainer.addBean(concept);
		}

		Tab tab = accordionNomen.addTab(buildTreeTable(dataModelContainer),
				dataModelNomen.getNamespace().toString());
		
		accordionNomen.setSelectedTab(tab);
		tabNomen.setCaption(MessageFormat.format(Msg.get("accordion.datamodels.nomen.caption"), accordionNomen.getComponentCount()));
		
		this.setSelectedTab(tabNomen);
	}

	public void addDataModelFolkso(CWBDataModelFolkso dataModelFolkso) {

		CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
				CWBConcept.class, "parent");

		for (CWBConcept concept : dataModelFolkso.getConcepts()) {
			dataModelContainer.addBean(concept);
		}

		Tab tab = accordionFolkso.addTab(buildTreeTable(dataModelContainer),
				dataModelFolkso.getNamespace().toString());
		
		accordionFolkso.setSelectedTab(tab);
		tabFolkso.setCaption(MessageFormat.format(Msg.get("accordion.datamodels.folkso.caption"), accordionFolkso.getComponentCount()));
		
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
		tabMatched.setCaption(MessageFormat.format(Msg.get("accordion.datamodels.matched.caption"), accordionMatched.getComponentCount()));
		
		this.setSelectedTab(tabMatched);
	}

	private Component buildTreeTable(Container container) {

		TreeTable treeTable = new TreeTable();
		treeTable.setContainerDataSource(container);
		treeTable.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		treeTable.setItemCaptionPropertyId(Msg
				.get("accordion.datamodels.table.column.fragment"));

		treeTable.setColumnCollapsingAllowed(true);
		treeTable.setColumnReorderingAllowed(true);
		treeTable.setSizeFull();
		treeTable.setSelectable(true);

		treeTable.setVisibleColumns(
				Msg.get("accordion.datamodels.table.column.iri"),
				Msg.get("accordion.datamodels.table.column.fragment"),
				Msg.get("accordion.datamodels.table.column.names"),
				Msg.get("accordion.datamodels.table.column.descriptions"));

		treeTable.setColumnCollapsed(
				Msg.get("accordion.datamodels.table.column.iri"), true);
		treeTable
				.setColumnCollapsed(Msg
						.get("accordion.datamodels.table.column.descriptions"),
						true);

		treeTable.setSortContainerPropertyId(Msg
				.get("accordion.datamodels.table.column.fragment"));
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

	public Tab getTabTags() {
		return tabFolkso;
	}

	public Tab getTabMatched() {
		return tabMatched;
	}

}
