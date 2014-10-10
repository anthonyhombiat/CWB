package lig.steamer.cwb.ui.panel;

import java.text.MessageFormat;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModelNomen;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class CWBNomenPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final VerticalLayout layout = new VerticalLayout();
	private final VerticalLayout defaultLayout = new VerticalLayout();

	private final Button loadFromWSButton = new Button();
	private final Button loadFromFileButton = new Button();

	private final TreeTable table = new TreeTable();

	public CWBNomenPanel() {

		super(MessageFormat.format(Msg.get("nomen.capt"),
				Msg.get("nomen.empty")));

		/**
		 * MAIN LAYOUT
		 */

		layout.addComponent(table);
		layout.setSizeFull();

		/**
		 * DEFAULT LAYOUT
		 */

		loadFromWSButton.setCaption(Msg.get("nomen.load.ws"));
		loadFromFileButton.setCaption(Msg.get("nomen.load.file"));
		loadFromWSButton.setStyleName(Reindeer.BUTTON_DEFAULT);
		loadFromFileButton.setStyleName(Reindeer.BUTTON_DEFAULT);

		Label defaultLabel = new Label(Msg.get("nomen.empty"));
		defaultLabel.setStyleName(Reindeer.LABEL_SMALL);
		defaultLabel.setSizeUndefined();

		defaultLayout.setSpacing(true);
		defaultLayout.addComponent(defaultLabel);
		defaultLayout.addComponent(loadFromWSButton);
		defaultLayout.addComponent(loadFromFileButton);
		defaultLayout.setComponentAlignment(defaultLabel,
				Alignment.MIDDLE_CENTER);
		defaultLayout.setComponentAlignment(loadFromWSButton,
				Alignment.MIDDLE_CENTER);
		defaultLayout.setComponentAlignment(loadFromFileButton,
				Alignment.MIDDLE_CENTER);

		defaultLayout.setMargin(true);

		this.setContent(defaultLayout);
		this.setSizeFull();

	}

	public void clear() {

		this.setContent(defaultLayout);

	}

	public void setNomen(CWBDataModelNomen nomen) {

		if (nomen != null) {
			CWBHierarchicalDataModelContainer<CWBConcept> dataModelContainer = new CWBHierarchicalDataModelContainer<CWBConcept>(
					CWBConcept.class, "parent");

			for (CWBConcept concept : nomen.getConcepts()) {
				dataModelContainer.addBean(concept);
			}

			table.setContainerDataSource(dataModelContainer);
			table.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			table.setItemCaptionPropertyId(Msg.get("nomen.table.col.fragment"));

			table.setColumnCollapsingAllowed(true);
			table.setColumnReorderingAllowed(true);
			table.setSizeFull();
			table.setSelectable(true);
			table.setMultiSelect(true);

			table.setVisibleColumns(Msg.get("nomen.table.col.iri"),
					Msg.get("nomen.table.col.fragment"),
					Msg.get("nomen.table.col.labels"),
					Msg.get("nomen.table.col.descriptions"));

			table.setColumnCollapsed(Msg.get("nomen.table.col.iri"), true);
			table.setColumnCollapsed(Msg.get("nomen.table.col.labels"), true);
			table.setColumnCollapsed(Msg.get("nomen.table.col.descriptions"),
					true);

			table.setSortContainerPropertyId(Msg
					.get("nomen.table.col.fragment"));
			table.sort();

			this.setContent(layout);
			this.setCaption(MessageFormat.format(Msg.get("nomen.capt"), nomen
					.getNamespace().toString()));
		}
	}

	public Button getLoadFromWSButton() {
		return loadFromWSButton;
	}

	public Button getLoadFromFileButton() {
		return loadFromFileButton;
	}

	public TreeTable getTable() {
		return table;
	}

}
