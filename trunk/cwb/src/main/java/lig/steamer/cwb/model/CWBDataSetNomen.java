package lig.steamer.cwb.model;

import java.util.ArrayList;
import java.util.Collection;

public class CWBDataSetNomen extends CWBDataSet<CWBInstanceNomen> {

	private Collection<CWBInstanceNomen> instances;
	
	public CWBDataSetNomen(){
		super();
		instances = new ArrayList<CWBInstanceNomen>();
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
	public boolean removeAllInstances() {
		if(instances.isEmpty()){
			return false;
		}
		instances = new ArrayList<CWBInstanceNomen>();
		return true;
	}
	
}
