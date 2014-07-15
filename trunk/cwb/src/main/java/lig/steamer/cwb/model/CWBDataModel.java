package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lig.steamer.cwb.io.visitor.CWBVisitable;
import lig.steamer.cwb.io.visitor.CWBVisitor;

import org.semanticweb.owlapi.model.IRI;

public class CWBDataModel implements Serializable, CWBVisitable {

	private static final long serialVersionUID = 1L;
	
	private IRI namespace;
	private CWBDataProvider dataProvider;
	private Collection<CWBConcept> concepts;
	private Collection<CWBEquivalence> equivalences;
	private Date creationDate;
	private Date lastUpdate;
	
	public CWBDataModel(IRI namespace){
		this.namespace = namespace;
		concepts = new ArrayList<CWBConcept>();
		equivalences = new ArrayList<CWBEquivalence>();
		creationDate = new Date();
		lastUpdate = new Date();
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

	/**
	 * @return the equivalences
	 */
	public Collection<CWBEquivalence> getEquivalences() {
		return equivalences;
	}

	/**
	 * @param equivalences the equivalences to set
	 */
	public void setEquivalences(Collection<CWBEquivalence> equivalences) {
		this.equivalences = equivalences;
	}
	
	public boolean addEquivalence(CWBEquivalence equivalence){
		if(!equivalences.contains(equivalence)){
			equivalences.add(equivalence);
			return true;
		}
		return false;
	}

	public boolean removeEquivalence(CWBEquivalence equivalence){
		if(equivalences.contains(equivalence)){
			equivalences.remove(equivalence);
			return true;
		}
		return false;
	}
	
	public boolean addEquivalences(Collection<CWBEquivalence> equivalences){
		boolean hasChanged = false;
		for(CWBEquivalence equivalence : equivalences){
			if(addEquivalence(equivalence)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
	public boolean removeEquivalences(Collection<CWBEquivalence> equivalences){
		boolean hasChanged = false;
		for(CWBEquivalence equivalence : equivalences){
			if(removeEquivalence(equivalence)){
				hasChanged = true;
			}
		}
		return hasChanged;
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
	public void acceptCWBVisitor(CWBVisitor visitor){
		visitor.visitDataModel(this);
	}
	
}
