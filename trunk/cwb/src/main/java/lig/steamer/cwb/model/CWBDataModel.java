package lig.steamer.cwb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class CWBDataModel {

	private CWBDataProvider dataProvider;
	private Collection<CWBConcept> concepts;
	private Date creationDate;
	private Date lastUpdate;
	
	public CWBDataModel(CWBDataProvider dataProvider){
		this.dataProvider = dataProvider;
		concepts = new ArrayList<CWBConcept>();
		creationDate = new Date();
		lastUpdate = new Date();
	}

	/**
	 * @return the dataProvider
	 */
	public CWBDataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(CWBDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * @return the concepts
	 */
	public Collection<CWBConcept> getConcepts() {
		return concepts;
	}

	/**
	 * @param concepts the concepts to set
	 */
	public void setConcepts(Collection<CWBConcept> concepts) {
		this.concepts = concepts;
	}
	
	public boolean addConcept(CWBConcept concept){
		if(!concepts.contains(concept)){
			concepts.add(concept);
			return true;
		}
		return false;
	}

	public boolean removeConcept(CWBConcept concept){
		if(concepts.contains(concept)){
			concepts.remove(concept);
			return true;
		}
		return false;
	}
	
	public boolean addConcepts(Collection<CWBConcept> concepts){
		boolean hasChanged = false;
		for(CWBConcept concept : concepts){
			if(addConcept(concept)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
	public boolean removeConcepts(Collection<CWBConcept> concepts){
		boolean hasChanged = false;
		for(CWBConcept concept : concepts){
			if(removeConcept(concept)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
}
