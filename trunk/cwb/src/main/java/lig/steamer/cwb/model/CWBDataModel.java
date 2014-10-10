package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lig.steamer.cwb.io.write.CWBDataModelVisitable;
import lig.steamer.cwb.io.write.CWBDataModelVisitor;

import org.semanticweb.owlapi.model.IRI;

public abstract class CWBDataModel implements Serializable, CWBDataModelVisitable {

	private static final long serialVersionUID = 1L;
	
	private IRI namespace;
	private String title;
	private String description;
	private CWBDataProvider dataProvider;
	private Collection<CWBConcept> concepts;
	private String creator;
	private Date creationDate;
	private Date lastUpdate;
	
	public CWBDataModel(IRI namespace){
		this.namespace = namespace;
		this.concepts = new ArrayList<CWBConcept>();
		this.creationDate = new Date();
		this.lastUpdate = new Date();
	}
	
	/**
	 * @return the namespace
	 */
	public IRI getNamespace() {
		return namespace;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
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
	
	public CWBConcept getConceptFromIRI(IRI iri){
		for(CWBConcept concept : concepts){
			if(concept.getIri().equals(iri)){
				return concept;
			}
		}
		return null;
	}
	
	public Collection<CWBConcept> getRootConcepts(){
		Collection<CWBConcept> rootConcepts = new ArrayList<>();
		
		for(CWBConcept concept : this.concepts){
			if(concept.getParent() == null){
				rootConcepts.add(concept);
			}
		}
		
		return rootConcepts;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof CWBDataModel){
			return this.getNamespace().equals(((CWBDataModel)o).getNamespace());
		}
		return false;
	}
	
	@Override
	public abstract void acceptCWBDataModelVisitor(CWBDataModelVisitor visitor);

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
