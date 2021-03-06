package lig.steamer.cwb.ui.window;

import java.util.Set;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBDataModel;

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

public class CWBMatchingWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final Table table;
	private final Button matchButton;
	private final BeanItemContainer<CWBDataModel> container;

	public CWBMatchingWindow() {
		
		super(Msg.get("match.caption"));

		final Label aboutText = new Label(
				Msg.get("match.sources.text"));
		aboutText.setSizeUndefined();

		container = new BeanItemContainer<CWBDataModel>(CWBDataModel.class);

		table = new Table(Msg.get("match.sources.table.caption"),
				container);
		table.setSizeFull();
		table.setHeight(200, Unit.PIXELS);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);
		table.setSelectable(true);
		table.setMultiSelect(true);
		table.setMultiSelectMode(MultiSelectMode.SIMPLE);
		table.setImmediate(true);
		table.addGeneratedColumn(
				Msg.get("match.sources.table.column.select"),
				new CWBCheckBoxColumnGenerator());
		table.setVisibleColumns(
				Msg.get("match.sources.table.column.select"),
				Msg.get("match.sources.table.column.namespace"),
				Msg.get("match.sources.table.column.creationdate"),
				Msg.get("match.sources.table.column.lastupdate"));
		table.setColumnAlignments(Align.CENTER, Align.LEFT, Align.CENTER,
				Align.CENTER);
		
		matchButton = new Button(
				Msg.get("match.button.caption"));

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();
		rootLayout.addComponent(aboutText);
		rootLayout.addComponent(table);
		rootLayout.addComponent(matchButton);
		rootLayout.setComponentAlignment(aboutText, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(matchButton, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(aboutText, 0.1f);
		rootLayout.setExpandRatio(table, 0.75f);
		rootLayout.setExpandRatio(matchButton, 0.15f);

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
		return matchButton;
	}

	/**
	 * @return the BeanContainer
	 */
	public BeanItemContainer<CWBDataModel> getContainer() {
		return container;
	}

}
