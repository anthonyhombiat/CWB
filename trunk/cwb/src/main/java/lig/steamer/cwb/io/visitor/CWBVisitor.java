package lig.steamer.cwb.io.visitor;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBDataSet;
import lig.steamer.cwb.model.CWBEquivalence;
import lig.steamer.cwb.model.CWBIndicatorMeasure;
import lig.steamer.cwb.model.CWBIndicatorMeasureSet;
import lig.steamer.cwb.model.CWBIndicatorModel;
import lig.steamer.cwb.model.CWBModel;

public interface CWBVisitor {

	public void visitModel(CWBModel model);
	
	public void visitDataModel(CWBDataModel dataModel);
	
	public void visitConcept(CWBConcept concept);
	
	public void visitEquivalence(CWBEquivalence equivalence);
	
	public void visitIndicatorMeasureSet(CWBIndicatorMeasureSet measureSet);
	
	public void visitIndicatorMeasure(CWBIndicatorMeasure indicatorMeasure);
	
	public void visitIndicatorModel(CWBIndicatorModel indicatorModel);
	
	public void visitDataSet(CWBDataSet dataSet);
}
