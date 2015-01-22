package lig.steamer.cwb.ui.notif;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class CWBToast extends Notification {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CWBToast(String caption) {
		super(caption);
		setPosition(Position.BOTTOM_RIGHT);
	}

}
