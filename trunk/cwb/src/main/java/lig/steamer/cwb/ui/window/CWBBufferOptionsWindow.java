package lig.steamer.cwb.ui.window;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.Prop;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Window;

public class CWBBufferOptionsWindow extends Window {

	private static final long serialVersionUID = 1L;

	private final Slider bufferSizeSlider;
	private final CheckBox bufferDisplayCheckBox;
	private static final double BUFFER_MIN_SIZE = 0;
	private static final double BUFFER_MAX_SIZE = 100;
	private static final double DEFAULT_BUFFER_SIZE = 10;
	private Button okButton;

	public CWBBufferOptionsWindow() {

		super(Msg.get("window.map.buffer.capt"));

		bufferDisplayCheckBox = new CheckBox(
				Msg.get("window.map.buffer.display"));

		bufferDisplayCheckBox.setValue(Prop.DEFAULT_BUFFER_VISIBILITY);
		
		bufferSizeSlider = new Slider(Msg.get("window.map.buffer.size"));
		bufferSizeSlider.setWidth(100, Unit.PIXELS);
		bufferSizeSlider.setMin(BUFFER_MIN_SIZE);
		bufferSizeSlider.setMax(BUFFER_MAX_SIZE);
		bufferSizeSlider.setValue(DEFAULT_BUFFER_SIZE);
		
		okButton = new Button(Msg.get("window.map.buffer.ok"));

		final FormLayout layout = new FormLayout();
		layout.addComponent(bufferDisplayCheckBox);
		layout.addComponent(bufferSizeSlider);
		layout.addComponent(okButton);
		layout.setSizeUndefined();
		layout.setSpacing(true);
		layout.setMargin(true);

		this.center();
		this.setModal(true);
		this.setContent(layout);
	}

	/**
	 * @return the okButton
	 */
	public Button getOkButton() {
		return okButton;
	}

	/**
	 * @return the bufferSizeSlider
	 */
	public Slider getBufferSizeSlider() {
		return bufferSizeSlider;
	}

	/**
	 * @return the bufferDisplayCheckBox
	 */
	public CheckBox getBufferDisplayCheckBox() {
		return bufferDisplayCheckBox;
	}

}
