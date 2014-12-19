package lig.steamer.cwb.ui.panel;

import java.text.MessageFormat;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBEquivalence;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

public class CWBAlignPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private final Table table = new Table();
	private final BeanItemContainer<CWBEquivalence> container = new BeanItemContainer<CWBEquivalence>(
			CWBEquivalence.class);

	public CWBAlignPanel() {

		super(MessageFormat.format(Msg
				.get("align.capt"), 0));

		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.col.concept1")
						+ "."
						+ Msg.get("matching.results.table.col.concept.property.fragment"));
		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.col.concept2")
						+ "."
						+ Msg.get("matching.results.table.col.concept.property.fragment"));

		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.col.concept1")
						+ "."
						+ Msg.get("matching.results.table.col.concept.property.labels"));
		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.col.concept2")
						+ "."
						+ Msg.get("matching.results.table.col.concept.property.labels"));

		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.col.concept1")
						+ "."
						+ Msg.get("matching.results.table.col.concept.property.descriptions"));
		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.col.concept2")
						+ "."
						+ Msg.get("matching.results.table.col.concept.property.descriptions"));

		table.setContainerDataSource(container);
		table.setSizeFull();
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);
		table.setSelectable(true);
		table.setMultiSelect(true);
		table.setMultiSelectMode(MultiSelectMode.SIMPLE);
		table.setImmediate(true);
		
		table.addGeneratedColumn(
				Msg.get("matching.results.table.col.select"),
				new CWBCheckBoxColumnGenerator());
		
		table.setVisibleColumns(
				Msg.get("matching.results.table.col.select"),
				Msg.get("matching.results.table.col.concept1")
						+ "."
						+ Msg.get("matching.results.table.col.concept.property.fragment"),
				Msg.get("matching.results.table.col.concept2")
						+ "."
						+ Msg.get("matching.results.table.col.concept.property.fragment"),
				Msg.get("matching.results.table.col.confidence"));
		
//		table.setSortContainerPropertyId(Msg
//				.get("matching.results.table.col.confidence"));
		
		table.setColumnAlignments(Align.CENTER, Align.LEFT, Align.LEFT, Align.CENTER);
		table.refreshRowCache();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(table);

		this.setContent(layout);
		this.setSizeFull();

	}

	class CWBCheckBoxColumnGenerator implements ColumnGenerator {
		private static final long serialVersionUID = 1L;

		public CWBCheckBoxColumnGenerator() {
		}

		@Override
		public Component generateCell(Table source, Object itemId,
				Object columnId) {
			return new CheckBox();
		}
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @return the BeanContainer
	 */
	public BeanItemContainer<CWBEquivalence> getDataModelContainer() {
		return container;
	}

}
