package lig.steamer.cwb.model;

import java.util.Collection;

public class CWBDataSetFolkso extends CWBDataSet<CWBDataModelFolkso, CWBInstanceFolkso> {

	private Collection<CWBInstanceFolkso> instances;
	private CWBDataModelFolkso dataModel;
	
	public CWBDataSetFolkso(){
		super();
	}

	@Override
	public Collection<CWBInstanceFolkso> getInstances() {
		return instances;
	}

	@Override
	public boolean addInstance(CWBInstanceFolkso instance) {
		if(!instances.contains(instance)){
			instances.add(instance);
			return true;
		}
		return false;
	}
	
	public boolean removeInstance(CWBInstanceFolkso instance){
		if(instances.contains(instance)){
			instances.remove(instance);
			return true;
		}
		return false;
	}
	
	public boolean addInstances(Collection<CWBInstanceFolkso> instances){
		boolean hasChanged = false;
		for(CWBInstanceFolkso instance : instances){
			if(addInstance(instance)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
	public boolean removeInstances(Collection<CWBInstanceFolkso> instances){
		boolean hasChanged = false;
		for(CWBInstanceFolkso instance : instances){
			if(removeInstance(instance)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	@Override
	public CWBDataModelFolkso getDataModel() {
		return dataModel;
	}

	@Override
	public void setDataModel(CWBDataModelFolkso dataModel) {
		this.dataModel = dataModel;
	}
	
}
