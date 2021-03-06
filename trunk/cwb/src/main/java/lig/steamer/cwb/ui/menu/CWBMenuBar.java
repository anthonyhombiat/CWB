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
	private final MenuItem loadAlignFromWSMenuItem;
	private final MenuItem loadAlignFromFileMenuItem;

	private final MenuItem dataProviderMenuItem;
	private final MenuItem bufferOptionsMenuItem;
	private final MenuItem studyAreaMenuItem;

	private final MenuItem mapMenuItem;
	private final MenuItem modelMenuItem;
	
	private final MenuItem aboutMenuItem;
	private final MenuItem docMenuItem;

	public CWBMenuBar() {

		super();

		this.setWidth(100, Unit.PERCENTAGE);

		/*
		 * File
		 */

		MenuItem fileItem = this.addItem(
				Msg.get("main.menu.file"), null, null);

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

		MenuItem editItem = this.addItem(
				Msg.get("main.menu.edit"), null, null);

		undoMenuItem = editItem.addItem(
				Msg.get("main.menu.edit.undo"), null);
		undoMenuItem.setEnabled(false);

		redoMenuItem = editItem.addItem(
				Msg.get("main.menu.edit.redo"), null);
		redoMenuItem.setEnabled(false);

		/*
		 * Data model
		 */

		MenuItem datamodelItem = this.addItem(
				Msg.get("main.menu.data"), null);

		loadNomenFromWSMenuItem = datamodelItem.addItem(
				Msg.get("main.menu.data.load.nomen.ws"), null);

		loadNomenFromFileMenuItem = datamodelItem.addItem(
				Msg.get("main.menu.data.load.nomen.file"), null);

		datamodelItem.addSeparator();

		loadFolksoFromWSMenuItem = datamodelItem.addItem(
				Msg.get("main.menu.data.load.folkso.ws"), null);

		loadFolksoFromFileMenuItem = datamodelItem.addItem(
				Msg.get("main.menu.data.load.folkso.file"), null);

		datamodelItem.addSeparator();

		loadAlignFromWSMenuItem = datamodelItem.addItem(
				Msg.get("main.menu.data.load.align.ws"), null);

		loadAlignFromFileMenuItem = datamodelItem.addItem(
				Msg.get("main.menu.data.load.align.file"), null);

		/*
		 * Map
		 */

		MenuItem mapItem = this.addItem(
				Msg.get("main.menu.map"), null);
		
		studyAreaMenuItem = mapItem.addItem(
				Msg.get("main.menu.map.area"), null);
		
		dataProviderMenuItem = mapItem.addItem(
				Msg.get("main.menu.map.data"), null);

		bufferOptionsMenuItem = mapItem.addItem(
				Msg.get("main.menu.map.buffer"), null);
				
		/*
		 * Window
		 */

		MenuItem windowItem = this.addItem(
				Msg.get("main.menu.window"), null);

		mapMenuItem = windowItem.addItem(
				Msg.get("main.menu.window.map.capt"),
				null);
		mapMenuItem.setCheckable(true);
		mapMenuItem.setChecked(true);
		
		modelMenuItem = windowItem.addItem(
				Msg.get("main.menu.window.datamodels.capt"),
				null);
		modelMenuItem.setCheckable(true);
		modelMenuItem.setChecked(true);

		/*
		 * Help
		 */

		MenuItem helpItem = this.addItem(
				Msg.get("main.menu.help"), null, null);

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
	 * @return the loadAlignFromWSMenuItem
	 */
	public MenuItem getLoadAlignFromWSMenuItem() {
		return loadAlignFromWSMenuItem;
	}

	/**
	 * @return the loadAlignFromFileMenuItem
	 */
	public MenuItem getLoadAlignFromFileMenuItem() {
		return loadAlignFromFileMenuItem;
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
	 * @return the mapMenuItem
	 */
	public MenuItem getMapMenuItem() {
		return mapMenuItem;
	}

	/**
	 * @return the dataProviderMenuItem
	 */
	public MenuItem getdataProviderMenuItem() {
		return dataProviderMenuItem;
	}

	/**
	 * @return the bufferOptionsMenuItem
	 */
	public MenuItem getBufferOptionsMenuItem() {
		return bufferOptionsMenuItem;
	}

	/**
	 * @return the studyAreaMenuItem
	 */
	public MenuItem getStudyAreaMenuItem() {
		return studyAreaMenuItem;
	}

	/**
	 * @return the modelMenuItem
	 */
	public MenuItem getModelMenuItem() {
		return modelMenuItem;
	}

}