package lig.steamer.cwb.model;

import java.util.Collection;

public class CWBDataSetNomen extends CWBDataSet<CWBDataModelNomen, CWBInstanceNomen> {

	private Collection<CWBInstanceNomen> instances;
	private CWBDataModelNomen dataModel;
	
	public CWBDataSetNomen(){
		super();
	}

	@Override
	public Collection<CWBInstanceNomen> getInstances() {
		return instances;
	}

	@Override
	public boolean addInstance(CWBInstanceNomen instance) {
		if(!instances.contains(instance)){
			instances.add(instance);
			return true;
		}
		return false;
	}
	
	public boolean removeInstance(CWBInstanceNomen instance){
		if(instances.contains(instance)){
			instances.remove(instance);
			return true;
		}
		return false;
	}
	
	public boolean addInstances(Collection<CWBInstanceNomen> instances){
		boolean hasChanged = false;
		for(CWBInstanceNomen instance : instances){
			if(addInstance(instance)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
	public boolean removeInstances(Collection<CWBInstanceNomen> instances){
		boolean hasChanged = false;
		for(CWBInstanceNomen instance : instances){
			if(removeInstance(instance)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	@Override
	public CWBDataModelNomen getDataModel() {
		return dataModel;
	}

	@Override
	public void setDataModel(CWBDataModelNomen dataModel) {
		this.dataModel = dataModel;
	}
	
}
