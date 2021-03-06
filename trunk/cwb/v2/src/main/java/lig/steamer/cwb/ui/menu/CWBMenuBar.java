package lig.steamer.cwb.ui.menu;

import lig.steamer.cwb.Msg;

import com.vaadin.ui.MenuBar;

public class CWBMenuBar extends MenuBar {

	private static final long serialVersionUID = 1L;

	private final MenuItem openMenuItem;
	private final MenuItem saveMenuItem;
	private final MenuItem closeMenuItem;
	private final MenuItem logoutMenuItem;

	private final MenuItem undoMenuItem;
	private final MenuItem redoMenuItem;

	private final MenuItem loadFolksoFromWSMenuItem;
	private final MenuItem loadFolksoFromFileMenuItem;
	private final MenuItem loadNomenFromWSMenuItem;
	private final MenuItem loadNomenFromFileMenuItem;
	private final MenuItem matchMenuItem;
	
	private final MenuItem dataModelsMenuItem;
	private final MenuItem indicatorsMenuItem;
	private final MenuItem mapMenuItem;

	private final MenuItem aboutMenuItem;
	private final MenuItem docMenuItem;

	public CWBMenuBar() {

		super();

		this.setWidth(100, Unit.PERCENTAGE);
		
		/*
		 * File
		 */

		MenuItem fileItem = this.addItem(Msg.get("main.menu.file"),
				null, null);

		openMenuItem = fileItem.addItem(
				Msg.get("main.menu.file.open"), null);
		
		saveMenuItem = fileItem.addItem(
				Msg.get("main.menu.file.save"), null);
		
		closeMenuItem = fileItem.addItem(
				Msg.get("main.menu.file.close"), null);
		
		fileItem.addSeparator();

		logoutMenuItem = fileItem.addItem(
				Msg.get("main.menu.file.logout"), null);

		/*
		 * Edit
		 */

		MenuItem editItem = this.addItem(Msg.get("main.menu.edit"),
				null, null);

		undoMenuItem = editItem.addItem(
				Msg.get("main.menu.edit.undo"), null);
		undoMenuItem.setEnabled(false);

		redoMenuItem = editItem.addItem(
				Msg.get("main.menu.edit.redo"), null);
		redoMenuItem.setEnabled(false);

		/*
		 * Data
		 */

		MenuItem dataItem = this.addItem(Msg.get("main.menu.data"),
				null);

		loadFolksoFromWSMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.load.folkso.ws"), null);
		
		loadFolksoFromFileMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.load.folkso.file"), null);
		
		dataItem.addSeparator();
		
		loadNomenFromWSMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.load.nomen.ws"), null);

		loadNomenFromFileMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.load.nomen.file"), null);

		dataItem.addSeparator();
		
		matchMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.match"), null);

		/*
		 * Indicators
		 */

		this.addItem(Msg.get("main.menu.indicators"), null);
		
		/*
		 * Map
		 */

		this.addItem(Msg.get("main.menu.map"), null);

		/*
		 * Window
		 */

		MenuItem windowItem = this.addItem(Msg.get("main.menu.window"), null);

		dataModelsMenuItem = windowItem.addItem(Msg.get("main.menu.window.datamodels.caption"), null);
		dataModelsMenuItem.setCheckable(true);
		dataModelsMenuItem.setChecked(true);
		
		indicatorsMenuItem = windowItem.addItem(Msg.get("main.menu.window.indicators.caption"), null);
		indicatorsMenuItem.setCheckable(true);
		indicatorsMenuItem.setChecked(true);
		
		mapMenuItem = windowItem.addItem(Msg.get("main.menu.window.map.caption"), null);
		mapMenuItem.setCheckable(true);
		mapMenuItem.setChecked(true);
		
		/*
		 * Help
		 */

		MenuItem helpItem = this.addItem(Msg.get("main.menu.help"),
				null, null);

		docMenuItem = helpItem.addItem(
				Msg.get("main.menu.help.doc"), null);
		
		helpItem.addSeparator();
		
		aboutMenuItem = helpItem.addItem(
				Msg.get("main.menu.help.about"), null);
	}

	/**
	 * @return the openMenuItem
	 */
	public MenuItem getOpenMenuItem() {
		return openMenuItem;
	}

	/**
	 * @return the saveMenuItem
	 */
	public MenuItem getSaveMenuItem() {
		return saveMenuItem;
	}
	
	/**
	 * @return the closeMenuItem
	 */
	public MenuItem getCloseMenuItem() {
		return closeMenuItem;
	}
	
	/**
	 * @return the logoutMenuItem
	 */
	public MenuItem getLogoutMenuItem() {
		return logoutMenuItem;
	}
	
	/**
	 * @return the docItem
	 */
	public MenuItem getDocMenuItem() {
		return docMenuItem;
	}
	
	/**
	 * @return the aboutItem
	 */
	public MenuItem getAboutMenuItem() {
		return aboutMenuItem;
	}

	/**
	 * @return the loadTagsetMenuItem
	 */
	public MenuItem getLoadFolksoFromWSMenuItem() {
		return loadFolksoFromWSMenuItem;
	}
	
	/**
	 * @return the loadFolksoFromFileMenuItem
	 */
	public MenuItem getLoadFolksoFromFileMenuItem() {
		return loadFolksoFromFileMenuItem;
	}

	/**
	 * @return the loadNomenFromWSMenuItem
	 */
	public MenuItem getLoadNomenFromWSMenuItem() {
		return loadNomenFromWSMenuItem;
	}
	
	/**
	 * @return the loadNomenclatureMenuItem
	 */
	public MenuItem getLoadNomenFromFileMenuItem() {
		return loadNomenFromFileMenuItem;
	}
	
	/**
	 * @return the redoMenuItem
	 */
	public MenuItem getRedoMenuItem() {
		return redoMenuItem;
	}

	/**
	 * @return the undoMenuItem
	 */
	public MenuItem getUndoMenuItem() {
		return undoMenuItem;
	}

	/**
	 * @return the matchMenuItem
	 */
	public MenuItem getMatchMenuItem() {
		return matchMenuItem;
	}

	/**
	 * @return the dataModelsMenuItem
	 */
	public MenuItem getDataModelsMenuItem() {
		return dataModelsMenuItem;
	}
	
	/**
	 * @return the indicatorsMenuItem
	 */
	public MenuItem getIndicatorsMenuItem() {
		return indicatorsMenuItem;
	}
	
	/**
	 * @return the mapMenuItem
	 */
	public MenuItem getMapMenuItem() {
		return mapMenuItem;
	}
	
}