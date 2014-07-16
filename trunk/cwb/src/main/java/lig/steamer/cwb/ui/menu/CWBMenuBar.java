package lig.steamer.cwb.ui.menu;

import lig.steamer.cwb.Msg;

import com.vaadin.ui.MenuBar;

public class CWBMenuBar extends MenuBar {

	private static final long serialVersionUID = 1L;

	private final MenuItem openMenuItem;
	private final MenuItem saveMenuItem;
	private final MenuItem saveAsMenuItem;
	private final MenuItem quitMenuItem;

	private final MenuItem undoMenuItem;
	private final MenuItem redoMenuItem;

	private final MenuItem loadTagsetFromWSMenuItem;
	private final MenuItem loadTagsetFromFileMenuItem;
	private final MenuItem loadNomenclatureFromWSMenuItem;
	private final MenuItem loadNomenclatureFromFileMenuItem;
	private final MenuItem matchMenuItem;
	
	private final MenuItem dataModelsMenuItem;

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
		saveAsMenuItem = fileItem.addItem(
				Msg.get("main.menu.file.saveas"), null);
		saveMenuItem = fileItem.addItem(
				Msg.get("main.menu.file.save"), null);
		
		fileItem.addSeparator();

		quitMenuItem = fileItem.addItem(
				Msg.get("main.menu.file.quit"), null);

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

		loadTagsetFromWSMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.load.tagset.ws"), null);
		
		loadTagsetFromFileMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.load.tagset.file"), null);
		
		dataItem.addSeparator();
		
		loadNomenclatureFromWSMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.load.nomenclature.ws"), null);

		loadNomenclatureFromFileMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.load.nomenclature.file"), null);

		dataItem.addSeparator();
		
		matchMenuItem = dataItem.addItem(
				Msg.get("main.menu.data.match"), null);

		/*
		 * Indicators
		 */

		this.addItem(Msg.get("main.menu.indicators"), null);

		/*
		 * Window
		 */

		MenuItem windowItem = this.addItem(Msg.get("main.menu.window"), null);

		dataModelsMenuItem = windowItem.addItem(Msg.get("main.menu.window.datamodels.caption"), null);
		dataModelsMenuItem.setCheckable(true);
		dataModelsMenuItem.setChecked(true);
		
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
	 * @return the aboutItem
	 */
	public MenuItem getAboutMenuItem() {
		return aboutMenuItem;
	}

	/**
	 * @return the docItem
	 */
	public MenuItem getDocMenuItem() {
		return docMenuItem;
	}

	/**
	 * @return the loadTagsetMenuItem
	 */
	public MenuItem getLoadTagsetFromWSMenuItem() {
		return loadTagsetFromWSMenuItem;
	}
	
	/**
	 * @return the loadTagsetFromFileMenuItem
	 */
	public MenuItem getLoadTagsetFromFileMenuItem() {
		return loadTagsetFromFileMenuItem;
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
	 * @return the saveAsMenuItem
	 */
	public MenuItem getSaveAsMenuItem() {
		return saveAsMenuItem;
	}

	/**
	 * @return the quitMenuItem
	 */
	public MenuItem getQuitMenuItem() {
		return quitMenuItem;
	}

	/**
	 * @return the loadNomenclatureFromWSMenuItem
	 */
	public MenuItem getLoadNomenclatureFromWSMenuItem() {
		return loadNomenclatureFromWSMenuItem;
	}
	
	/**
	 * @return the loadNomenclatureMenuItem
	 */
	public MenuItem getLoadNomenclatureFromFileMenuItem() {
		return loadNomenclatureFromFileMenuItem;
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
	
}