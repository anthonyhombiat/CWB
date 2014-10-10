package lig.steamer.cwb.io.write;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBDataModelMatched;
import lig.steamer.cwb.model.CWBDataModelNomen;

public interface CWBDataModelVisitor {
	
	public void visitNomenclature(CWBDataModelNomen nomenclature);
	
	public void visitFolksonomy(CWBDataModelFolkso folksonomy);
	
	public void visitMatchedDataModel(CWBDataModelMatched matchedDataModel);
	
	public void visitDataModel(CWBDataModel dataModel);
	
	public void visitConcept(CWBConcept concept);
	
	public void visitEquivalence(CWBEquivalence equivalence);
	
}
