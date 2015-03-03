package lig.steamer.cwb.model;
import lig.steamer.cwb.Prop;

public class CWBBuffer {

	private double size;
	private boolean isVisible;
	private int strokeSize;
	private double strokeOpacity;
	private double fillOpacity;
	
	public CWBBuffer(){
		setSize(Prop.DEFAULT_BUFFER_SIZE);
		setVisible(Prop.DEFAULT_BUFFER_VISIBILITY);
		setStrokeSize(Prop.DEFAULT_BUFFER_STROKE_SIZE);
		setStrokeOpacity(Prop.DEFAULT_BUFFER_STROKE_OPACITY);
		setFillOpacity(Prop.DEFAULT_BUFFER_FILL_OPACITY);
	}

	/**
	 * @return the size
	 */
	public double getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(double size) {
		this.size = size;
	}

	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * @return the strokeSize
	 */
	public int getStrokeSize() {
		return strokeSize;
	}

	/**
	 * @param strokeSize the strokeSize to set
	 */
	public void setStrokeSize(int strokeSize) {
		this.strokeSize = strokeSize;
	}

	/**
	 * @return the strokeOpacity
	 */
	public double getStrokeOpacity() {
		return strokeOpacity;
	}

	/**
	 * @param strokeOpacity the strokeOpacity to set
	 */
	public void setStrokeOpacity(double strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}

	/**
	 * @return the fillOpacity
	 */
	public double getFillOpacity() {
		return fillOpacity;
	}

	/**
	 * @param fillOpacity the fillOpacity to set
	 */
	public void setFillOpacity(double fillOpacity) {
		this.fillOpacity = fillOpacity;
	}
	
}
