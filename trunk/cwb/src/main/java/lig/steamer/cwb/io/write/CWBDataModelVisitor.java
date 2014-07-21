package lig.steamer.cwb.io.write;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;

public interface CWBDataModelVisitor {
	
	public void visitDataModel(CWBDataModel dataModel);
	
	public void visitConcept(CWBConcept concept);
	
	public void visitEquivalence(CWBEquivalence equivalence);
	
}
