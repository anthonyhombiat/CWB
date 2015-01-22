package lig.steamer.cwb.model;

import java.util.ArrayList;
import java.util.Collection;

public class CWBDataSetFolkso extends CWBDataSet<CWBInstanceFolkso> {

	private Collection<CWBInstanceFolkso> instances;
	
	public CWBDataSetFolkso(){
		super();
		instances = new ArrayList<CWBInstanceFolkso>();
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
	public boolean removeAllInstances() {
		if(instances.isEmpty()){
			return false;
		}
		instances = new ArrayList<CWBInstanceFolkso>();
		return true;
	}
	
}
