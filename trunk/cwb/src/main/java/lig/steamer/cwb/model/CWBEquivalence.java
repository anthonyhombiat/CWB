package lig.steamer.cwb.model;

public class CWBEquivalence {

	private CWBConcept concept1;
	private CWBConcept concept2;
	private double strength;
	
	public CWBEquivalence(CWBConcept concept1, CWBConcept concept2, double strength){
		this.concept1 = concept1;
		this.concept2 = concept2;
		this.strength = strength;
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
	public double getStrength() {
		return strength;
	}
	
}
