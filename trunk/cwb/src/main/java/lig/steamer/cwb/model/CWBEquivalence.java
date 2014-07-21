package lig.steamer.cwb.model;

import java.io.Serializable;

import lig.steamer.cwb.io.write.CWBDataModelVisitable;
import lig.steamer.cwb.io.write.CWBDataModelVisitor;

public class CWBEquivalence implements Serializable, CWBDataModelVisitable {

	private static final long serialVersionUID = 1L;

	private CWBConcept concept1;
	private CWBConcept concept2;
	private double confidence;

	public CWBEquivalence(CWBConcept concept1, CWBConcept concept2,
			double confidence) {
		this.concept1 = concept1;
		this.concept2 = concept2;
		this.confidence = confidence;
	}

	/**
	 * @return the concept1
	 */
	public CWBConcept getConcept1() {
		return concept1;
	}

	/**
	 * @return the concept2
	 */
	public CWBConcept getConcept2() {
		return concept2;
	}

	/**
	 * @return the strength
	 */
	public double getConfidence() {
		return confidence;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CWBEquivalence) {
			CWBEquivalence equivalence = (CWBEquivalence) o;
			return ((this.concept1.equals(equivalence.getConcept1()) && this.concept2
					.equals(equivalence.getConcept2()))
					|| (this.concept1.equals(equivalence.getConcept2()) && this.concept2
							.equals(equivalence.getConcept1())))
					&& this.confidence == equivalence.getConfidence();
		}
		return false;
	}

	@Override
	public void acceptCWBDataModelVisitor(CWBDataModelVisitor visitor) {
		visitor.visitEquivalence(this);
	}

}
