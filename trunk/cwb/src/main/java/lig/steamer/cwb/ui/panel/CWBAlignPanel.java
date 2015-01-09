package lig.steamer.cwb.ui.panel;

import java.text.MessageFormat;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBEquivalence;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class CWBAlignPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private final Table table = new Table();
	private final BeanItemContainer<CWBEquivalence> container = new BeanItemContainer<CWBEquivalence>(
			CWBEquivalence.class);
	private ValueChangeListener checkboxListener;

	public CWBAlignPanel() {

		super(MessageFormat.format(Msg
				.get("align.capt"), 0));

		container
				.addNestedContainerProperty(Msg
						.get("align.table.col.concept1")
						+ "."
						+ Msg.get("align.table.col.concept.property.fragment"));
		container
				.addNestedContainerProperty(Msg
						.get("align.table.col.concept2")
						+ "."
						+ Msg.get("align.table.col.concept.property.fragment"));

		container
				.addNestedContainerProperty(Msg
						.get("align.table.col.concept1")
						+ "."
						+ Msg.get("align.table.col.concept.property.labels"));
		container
				.addNestedContainerProperty(Msg
						.get("align.table.col.concept2")
						+ "."
						+ Msg.get("align.table.col.concept.property.labels"));

		container
				.addNestedContainerProperty(Msg
						.get("align.table.col.concept1")
						+ "."
						+ Msg.get("align.table.col.concept.property.descriptions"));
		container
				.addNestedContainerProperty(Msg
						.get("align.table.col.concept2")
						+ "."
						+ Msg.get("align.table.col.concept.property.descriptions"));

		table.setContainerDataSource(container);
		table.setSizeFull();
		table.addStyleName(Reindeer.TABLE_BORDERLESS);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);
		table.setSelectable(true);
		table.setImmediate(true);
		
		table.addGeneratedColumn(
				Msg.get("align.table.col.select"),
				new ColumnGenerator(){

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						CheckBox checkbox = new CheckBox();
						checkbox.addValueChangeListener(checkboxListener);
						checkbox.addStyleName(itemId.toString());
						return checkbox;
					}
					
				});
		
		table.setVisibleColumns(
				Msg.get("align.table.col.select"),
				Msg.get("align.table.col.concept1")
						+ "."
						+ Msg.get("align.table.col.concept.property.fragment"),
				Msg.get("align.table.col.concept2")
						+ "."
						+ Msg.get("align.table.col.concept.property.fragment"),
				Msg.get("align.table.col.confidence"));
		
		table.setSortContainerPropertyId(Msg
				.get("align.table.col.confidence"));
		table.setSortAscending(false);
		
		table.setColumnAlignments(Align.CENTER, Align.LEFT, Align.LEFT, Align.CENTER);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(table);

		this.setContent(layout);
		this.setSizeFull();

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

	/**
	 * @return the checkboxListener
	 */
	public ValueChangeListener getCheckboxListener() {
		return checkboxListener;
	}

	/**
	 * @param checkboxListener the checkboxListener to set
	 */
	public void setCheckboxListener(ValueChangeListener checkboxListener) {
		this.checkboxListener = checkboxListener;
	}

}
