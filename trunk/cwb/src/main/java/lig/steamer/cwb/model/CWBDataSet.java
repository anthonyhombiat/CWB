package lig.steamer.cwb.model;

import java.util.Collection;
import java.util.Date;

import lig.steamer.cwb.io.visitor.CWBVisitable;
import lig.steamer.cwb.io.visitor.CWBVisitor;

public class CWBDataSet implements CWBVisitable {

	private Date creationDate;
	private Date lastUpdate;
	private CWBDataProvider dataProvider;
	private CWBStudyArea studyArea;
	private CWBZoning zoning;
	private Collection<CWBInstance> instances;
	
	private CWBDataModel dataModel;
	
	public CWBDataSet(){
		
	}

	/**
	 * @return the dataProvider
	 */
	public CWBDataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(CWBDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return the studyArea
	 */
	public CWBStudyArea getStudyArea() {
		return studyArea;
	}

	/**
	 * @param studyArea the studyArea to set
	 */
	public void setStudyArea(CWBStudyArea studyArea) {
		this.studyArea = studyArea;
	}

	/**
	 * @return the zoning
	 */
	public CWBZoning getZoning() {
		return zoning;
	}

	/**
	 * @param zoning the zoning to set
	 */
	public void setZoning(CWBZoning zoning) {
		this.zoning = zoning;
	}

	/**
	 * @return the instances
	 */
	public Collection<CWBInstance> getInstances() {
		return instances;
	}

	/**
	 * @param instances the instances to set
	 */
	public void setInstances(Collection<CWBInstance> instances) {
		this.instances = instances;
	}
	
	public boolean addInstance(CWBInstance instance){
		if(!instances.contains(instance)){
			instances.add(instance);
			return true;
		}
		return false;
	}

	public boolean removeInstance(CWBInstance instance){
		if(instances.contains(instance)){
			instances.remove(instance);
			return true;
		}
		return false;
	}
	
	public boolean addInstances(Collection<CWBInstance> instances){
		boolean hasChanged = false;
		for(CWBInstance instance : instances){
			if(addInstance(instance)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
	public boolean removeInstances(Collection<CWBInstance> instances){
		boolean hasChanged = false;
		for(CWBInstance instance : instances){
			if(removeInstance(instance)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return the dataModel
	 */
	public CWBDataModel getDataModel() {
		return dataModel;
	}

	/**
	 * @param dataModel the dataModel to set
	 */
	public void setDataModel(CWBDataModel dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	public void acceptCWBVisitor(CWBVisitor visitor) {
		visitor.visitDataSet(this);
	}
	
}
