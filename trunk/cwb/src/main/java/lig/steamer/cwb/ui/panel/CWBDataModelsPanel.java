package lig.steamer.cwb.ui.panel;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.ui.Messages;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class CWBDataModelsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final Accordion accordion;

	public CWBDataModelsPanel() {

		super();
		this.setCaption(Messages.getString("accordion.caption"));

		accordion = new Accordion();
		accordion.setSizeFull();

		VerticalLayout accordionPanelLayout = new VerticalLayout();
		accordionPanelLayout.setSizeFull();
		accordionPanelLayout.addComponent(accordion);

		this.setSizeFull();
		this.setContent(accordionPanelLayout);

	}

	public void addDataModelTreeTable(CWBDataModel dataModel) {
		
		HierarchicalDataModelContainer<CWBConcept> dataModelContainer = new HierarchicalDataModelContainer<CWBConcept>(CWBConcept.class, "parent");

		for (CWBConcept concept : dataModel.getConcepts()) {
			dataModelContainer.addBean(concept);
		}

		TreeTable treeTable = new TreeTable();
		treeTable.setContainerDataSource(dataModelContainer);
		treeTable.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		treeTable.setItemCaptionPropertyId(Messages
				.getString("accordion.table.column.fragment"));

		treeTable.setColumnCollapsingAllowed(true);
		treeTable.setColumnReorderingAllowed(true);
		treeTable.setSizeFull();
		treeTable.setSelectable(true);
		
		treeTable.setVisibleColumns(Messages
				.getString("accordion.table.column.uri"), Messages
				.getString("accordion.table.column.fragment"), Messages
				.getString("accordion.table.column.names"), Messages
				.getString("accordion.table.column.descriptions"));
		
		treeTable.setColumnCollapsed(Messages
				.getString("accordion.table.column.uri"), true);
		treeTable.setColumnCollapsed(Messages
				.getString("accordion.table.column.names"), true);
		treeTable.setColumnCollapsed(Messages
				.getString("accordion.table.column.descriptions"), true);
		
//		treeTable.setSortContainerPropertyId(Messages
//				.getString("accordion.table.column.fragment"));
//		treeTable.sort();

		VerticalLayout accordionElementLayout = new VerticalLayout();
		accordionElementLayout.setSizeFull();
		accordionElementLayout.addComponent(treeTable);

		final Panel accordionPanel = new Panel();
		accordionPanel.setContent(accordionElementLayout);
		accordionPanel.setSizeFull();

		accordion.addTab(
				accordionPanel,
				Messages.getString("accordion.tab.caption")
						+ (accordion.getComponentCount() + 1) + " - "
						+ dataModel.getDataProvider().getName(), null);
	}

}