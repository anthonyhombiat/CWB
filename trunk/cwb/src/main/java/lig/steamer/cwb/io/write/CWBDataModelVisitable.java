package lig.steamer.cwb.io.write;

public interface CWBDataModelVisitable {

	public void acceptCWBDataModelVisitor(CWBDataModelVisitor visitor);
	
}
