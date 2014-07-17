package lig.steamer.cwb.ui.window;

import java.util.Set;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBEquivalence;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Table.Align;

public class CWBMatchingResultsWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final Table table;
	private final Button confirmButton;
	private final BeanItemContainer<CWBEquivalence> container;

	public CWBMatchingResultsWindow() {

		super(Msg.get("matching.results.caption"));

		final Label aboutText = new Label(Msg.get("matching.results.text"));
		aboutText.setSizeUndefined();

		container = new BeanItemContainer<CWBEquivalence>(CWBEquivalence.class);
		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.column.concept1")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.fragment"));
		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.column.concept2")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.fragment"));

		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.column.concept1")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.names"));
		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.column.concept2")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.names"));

		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.column.concept1")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.descriptions"));
		container
				.addNestedContainerProperty(Msg
						.get("matching.results.table.column.concept2")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.descriptions"));

		table = new Table(Msg.get("matching.results.table.caption"), container);
		table.setSizeFull();
		table.setHeight(200, Unit.PIXELS);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);
		table.setSelectable(true);
		table.setMultiSelect(true);
		table.setMultiSelectMode(MultiSelectMode.SIMPLE);
		table.setImmediate(true);
		table.addGeneratedColumn(
				Msg.get("matching.results.table.column.select"),
				new CWBCheckBoxColumnGenerator());
		table.setVisibleColumns(
				Msg.get("matching.results.table.column.select"),
				Msg.get("matching.results.table.column.concept1")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.fragment"),
				Msg.get("matching.results.table.column.concept2")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.fragment"),
				Msg.get("matching.results.table.column.concept1")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.names"),
				Msg.get("matching.results.table.column.concept2")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.names"),
				Msg.get("matching.results.table.column.concept1")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.descriptions"),
				Msg.get("matching.results.table.column.concept2")
						+ "."
						+ Msg.get("matching.results.table.column.concept.property.descriptions"),
				Msg.get("matching.results.table.column.confidence"));
		table.setColumnAlignments(Align.CENTER, Align.LEFT, Align.LEFT,
				Align.LEFT, Align.LEFT, Align.LEFT, Align.LEFT, Align.CENTER);

		confirmButton = new Button(Msg.get("matching.results.button.caption"));

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();
		rootLayout.addComponent(aboutText);
		rootLayout.addComponent(table);
		rootLayout.addComponent(confirmButton);
		rootLayout.setComponentAlignment(aboutText, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		rootLayout
				.setComponentAlignment(confirmButton, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(aboutText, 0.1f);
		rootLayout.setExpandRatio(table, 0.75f);
		rootLayout.setExpandRatio(confirmButton, 0.15f);

		this.setWidth(600, Unit.PIXELS);
		this.setHeight(400, Unit.PIXELS);
		this.center();
		this.setModal(true);
		this.setContent(rootLayout);

	}
	
	public void reset(){
		table.removeAllItems();
	}

	class CWBCheckBoxColumnGenerator implements ColumnGenerator {
		private static final long serialVersionUID = 1L;

		public CWBCheckBoxColumnGenerator() {
		}

		@Override
		public Component generateCell(Table source, Object itemId,
				Object columnId) {

			@SuppressWarnings("unchecked")
			Set<Object> values = (Set<Object>) source.getValue();
			if (values.contains(itemId)) {
				return new Label(FontAwesome.CHECK_SQUARE.getHtml(),
						ContentMode.HTML);
			}
			return new Label(FontAwesome.SQUARE_O.getHtml(), ContentMode.HTML);
		}
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @return the button
	 */
	public Button getButton() {
		return confirmButton;
	}

	/**
	 * @return the BeanContainer
	 */
	public BeanItemContainer<CWBEquivalence> getContainer() {
		return container;
	}

}
