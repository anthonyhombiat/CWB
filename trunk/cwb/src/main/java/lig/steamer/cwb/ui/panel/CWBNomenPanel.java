package lig.steamer.cwb.ui.panel;

import java.text.MessageFormat;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModelNomen;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class CWBNomenPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final TreeTable table = new TreeTable();
	private final CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer;

	public CWBNomenPanel() {

		super(MessageFormat.format(Msg.get("nomen.capt"), 0,
				Msg.get("nomen.empty")));

		dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
				CWBConcept.class, "parent");

		table.setContainerDataSource(dataModelContainer);
		table.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		table.setItemCaptionPropertyId(Msg.get("nomen.table.col.fragment"));
		table.addStyleName(Reindeer.TABLE_BORDERLESS);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);
		table.setSelectable(true);

		table.setSizeFull();

		table.setVisibleColumns(Msg.get("nomen.table.col.iri"),
				Msg.get("nomen.table.col.fragment"),
				Msg.get("nomen.table.col.labels"),
				Msg.get("nomen.table.col.descriptions"));

		table.setColumnCollapsed(Msg.get("nomen.table.col.iri"), true);
		table.setColumnCollapsed(Msg.get("nomen.table.col.labels"), true);
		table.setColumnCollapsed(Msg.get("nomen.table.col.descriptions"), true);

		table.setSortContainerPropertyId(Msg.get("nomen.table.col.fragment"));

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(table);
		layout.setSizeFull();

		this.setContent(layout);
		this.setSizeFull();

	}

	public void loadNomenclature(CWBDataModelNomen nomen) {
		dataModelContainer.removeAllItems();
		if (nomen.getConcepts().size() > 0) {
			dataModelContainer.addAll(nomen.getConcepts());
			table.sort();
			table.refreshRowCache();
			setCaption(MessageFormat.format(Msg.get("nomen.capt"), nomen
					.getConcepts().size(), nomen.getNamespace()));
		} else {
			setCaption(MessageFormat.format(Msg.get("nomen.capt"), 0,
					Msg.get("nomen.empty")));
		}
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
