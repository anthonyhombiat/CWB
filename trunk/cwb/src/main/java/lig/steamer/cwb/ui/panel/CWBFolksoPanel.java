package lig.steamer.cwb.ui.panel;

import java.text.MessageFormat;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBConcept;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class CWBFolksoPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final TreeTable table = new TreeTable();
	private final CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer;

	public CWBFolksoPanel() {

		super(MessageFormat.format(Msg.get("folkso.capt"), 0,
				Msg.get("folkso.empty")));
		
		dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
				CWBConcept.class, "parent");
		
		table.setContainerDataSource(dataModelContainer);
		table.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		table.setItemCaptionPropertyId(Msg.get("folkso.table.col.fragment"));
		table.addStyleName(Reindeer.TABLE_BORDERLESS);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);
		table.setSelectable(true);

		table.setSizeFull();
		
		table.setVisibleColumns(Msg.get("folkso.table.col.iri"),
				Msg.get("folkso.table.col.fragment"),
				Msg.get("folkso.table.col.labels"),
				Msg.get("folkso.table.col.descriptions"));

		table.setColumnCollapsed(Msg.get("folkso.table.col.iri"), true);
		table.setColumnCollapsed(Msg.get("folkso.table.col.labels"), true);
		table.setColumnCollapsed(Msg.get("folkso.table.col.descriptions"),
				true);

		table.setSortContainerPropertyId(Msg
				.get("folkso.table.col.fragment"));

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(table);
		layout.setSizeFull();

		this.setContent(layout);
		this.setSizeFull();

	}

	/**
	 * @return the table
	 */
	public TreeTable getTable() {
		return table;
	}

	/**
	 * @return the dataModelContainer
	 */
	public CWBHierarchicalDataModelContainer<CWBConcept> getDataModelContainer() {
		return dataModelContainer;
	}

}
