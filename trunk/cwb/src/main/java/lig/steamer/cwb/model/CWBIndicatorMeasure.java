package lig.steamer.cwb.model;

import lig.steamer.cwb.io.visitor.CWBVisitable;
import lig.steamer.cwb.io.visitor.CWBVisitor;

public class CWBIndicatorMeasure implements CWBVisitable {

	@Override
	public void acceptCWBVisitor(CWBVisitor visitor) {
		visitor.visitIndicatorMeasure(this);
	}

}
