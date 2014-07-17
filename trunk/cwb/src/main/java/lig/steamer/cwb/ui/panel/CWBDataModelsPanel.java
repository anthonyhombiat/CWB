package lig.steamer.cwb.ui.panel;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class CWBDataModelsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final Accordion accordion;

	public CWBDataModelsPanel() {
		
		super(Msg.get("accordion.datamodels.caption"));
		
		accordion = new Accordion();
		accordion.setSizeFull();

		VerticalLayout accordionPanelLayout = new VerticalLayout();
		accordionPanelLayout.setSizeFull();
		accordionPanelLayout.addComponent(accordion);

		this.setSizeFull();
		this.setContent(accordionPanelLayout);
	}

	public void clear() {
		
		accordion.removeAllComponents();
		
	}

	public void addDataModelTreeTable(CWBDataModel dataModel) {

		CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
				CWBConcept.class, "parent");

		for (CWBConcept concept : dataModel.getConcepts()) {
			dataModelContainer.addBean(concept);
		}

		TreeTable treeTable = new TreeTable();
		treeTable.setContainerDataSource(dataModelContainer);
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

		final Panel accordionPanel = new Panel();
		accordionPanel.setContent(accordionElementLayout);
		accordionPanel.setSizeFull();

		accordion.addTab(
				accordionPanel,
				Msg.get("accordion.datamodels.tab.caption")
						+ (accordion.getComponentCount() + 1) + " - "
						+ dataModel.getNamespace(), null);
	}

	public Accordion getAccordion() {
		return accordion;
	}

}
