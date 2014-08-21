package lig.steamer.cwb.ui.panel;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBIndicatorModel;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class CWBIndicatorsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private final Table table;
	private final CWBIndicatorModelsContainer<CWBIndicatorModel> indicatorModelsContainer;

	public CWBIndicatorsPanel() {
		
		super(Msg.get("indicators.table.caption"));

		indicatorModelsContainer = new CWBIndicatorModelsContainer<CWBIndicatorModel>(
				CWBIndicatorModel.class);
		
		table = new Table();
		table.setSizeFull();
		table.setContainerDataSource(indicatorModelsContainer);

		table.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		table.setItemCaptionPropertyId(Msg.get("indicators.table.column.title"));

		table.setVisibleColumns(Msg.get("indicators.table.column.title"),
				Msg.get("indicators.table.column.description"));

		table.setSortContainerPropertyId(Msg
				.get("indicators.table.column.title"));
		table.sort();

		table.setSelectable(true);
		table.setColumnCollapsingAllowed(true);
		table.setColumnReorderingAllowed(true);

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(table);

		this.setSizeFull();
		this.setContent(layout);
		
	}
	
	public void clear(){
		
		indicatorModelsContainer.removeAllItems();
		
	}

	public Table getTable() {
		return table;
	}

}
