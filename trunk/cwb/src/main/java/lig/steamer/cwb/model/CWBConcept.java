package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lig.steamer.cwb.io.visitor.CWBVisitable;
import lig.steamer.cwb.io.visitor.CWBVisitor;

import org.semanticweb.owlapi.model.IRI;

public class CWBConcept implements Serializable, CWBVisitable {

	private static final long serialVersionUID = 1L;
	
	private IRI iri;
	private Map<Locale, String> names;
	private Map<Locale, String> descriptions;

//	private Collection<CWBConcept> superClasses;
//	private Collection<CWBConcept> subClasses;
	
	private CWBConcept parent;
	
	public CWBConcept(IRI iri, CWBConcept parent) {
		this.iri = iri;
		names = new HashMap<Locale, String>();
		descriptions = new HashMap<Locale, String>();
		parent = null;
	}
	
	public CWBConcept(IRI iri) {
		this(iri, null);
	}

	/**
	 * @return the uri
	 */
	public IRI getIri() {
		return iri;
	}

	/**
	 * @return the names
	 */
	public Map<Locale, String> getNames() {
		return names;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(Map<Locale, String> names) {
		this.names = names;
	}

	public boolean addName(String name, Locale locale){
		return names.put(locale, name) != null;
	}

	public boolean removeNameByLocale(Locale locale){
		return names.remove(locale) != null;
	}
	
	/**
	 * @return the descriptions
	 */
	public Map<Locale, String> getDescriptions() {
		return descriptions;
	}

	/**
	 * @param descriptions the descriptions to set
	 */
	public void setDescriptions(Map<Locale, String> descriptions) {
		this.descriptions = descriptions;
	}
	
	public boolean addDescription(String description, Locale locale){
		return descriptions.put(locale, description) != null;
	}

	public boolean removeDescriptionByLocale(Locale locale){
		return descriptions.remove(locale) != null;
	}

	public String getNameByLanguage(Locale locale) {
		return names.get(locale);
	}

	public String getDescriptionByLanguage(Locale locale) {
		return descriptions.get(locale);
	}
	
	public String getFragment(){
		return iri.getFragment();
	}
	
	public CWBConcept getParent(){
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(CWBConcept parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof CWBConcept){
			return this.getIri().equals(((CWBConcept)o).getIri());
		}
		return false;
	}

	@Override
	public void acceptCWBVisitor(CWBVisitor visitor) {
		visitor.visitConcept(this);
	}

//	/**
//	 * @return the superClasses
//	 */
//	public Collection<CWBConcept> getSuperClasses() {
//		return superClasses;
//	}
//
//	/**
//	 * @param superClasses the superClasses to set
//	 */
//	public void setSuperClasses(Collection<CWBConcept> superClasses) {
//		this.superClasses = superClasses;
//	}
//	
//	public boolean addSuperClass(CWBConcept concept) {
//		if (!superClasses.contains(concept)) {
//			superClasses.add(concept);
//			return true;
//		}
//		return false;
//	}
//
//	public boolean removeSuperClass(CWBConcept concept) {
//		if (superClasses.contains(concept)) {
//			superClasses.remove(concept);
//			return true;
//		}
//		return false;
//	}
//
//	public boolean addSuperClasses(Collection<CWBConcept> concepts) {
//		boolean hasChanged = false;
//		for (CWBConcept concept : concepts) {
//			if (addSuperClass(concept)) {
//				hasChanged = true;
//			}
//		}
//		return hasChanged;
//	}
//
//	public boolean removeSuperClasses(Collection<CWBConcept> concepts) {
//		boolean hasChanged = false;
//		for (CWBConcept concept : concepts) {
//			if (removeSuperClass(concept)) {
//				hasChanged = true;
//			}
//		}
//		return hasChanged;
//	}
//
//	/**
//	 * @return the subClasses
//	 */
//	public Collection<CWBConcept> getSubClasses() {
//		return subClasses;
//	}
//
//	/**
//	 * @param subClasses the subClasses to set
//	 */
//	public void setSubClasses(Collection<CWBConcept> subClasses) {
//		this.subClasses = subClasses;
//	}
//	
//	public boolean addSubClass(CWBConcept concept) {
//		if (!subClasses.contains(concept)) {
//			subClasses.add(concept);
//			return true;
//		}
//		return false;
//	}
//
//	public boolean removeSubClass(CWBConcept concept) {
//		if (subClasses.contains(concept)) {
//			subClasses.remove(concept);
//			return true;
//		}
//		return false;
//	}
//
//	public boolean addSubClasses(Collection<CWBConcept> concepts) {
//		boolean hasChanged = false;
//		for (CWBConcept concept : concepts) {
//			if (addSubClass(concept)) {
//				hasChanged = true;
//			}
//		}
//		return hasChanged;
//	}
//
//	public boolean removeSubClasses(Collection<CWBConcept> concepts) {
//		boolean hasChanged = false;
//		for (CWBConcept concept : concepts) {
//			if (removeSubClass(concept)) {
//				hasChanged = true;
//			}
//		}
//		return hasChanged;
//	}
//
//	public boolean isRoot() {
//		return superClasses == null || superClasses.size() == 0;
//	}
//
//	public boolean isLeaf() {
//		return subClasses == null || subClasses.size() == 0;
//	}
//
//	public boolean isSubClassOf(CWBConcept concept) {
//		if (!concept.isLeaf()) {
//			// test if this is a direct subclass of clazz
//			if (concept.getSubClasses().contains(this))
//				return true;
//			// recursively test if this is a subclass of clazz subclasses
//
//			for (CWBConcept currentConcept : concept.getSubClasses()) {
//				if (isSubClassOf(currentConcept))
//					return true;
//			}
//		}
//		return false;
//	}
//
//	public boolean isSuperClassOf(CWBConcept concept) {
//		return concept.isSubClassOf(this);
//	}
//
//	public CWBConcept findRoot() {
//		if (isRoot())
//			return this;
//
//		return this.getSuperClasses().iterator().next().findRoot();
//	}
//
//	public CWBConcept getParent(){
//		return superClasses.iterator().next();
//	}
	
}
