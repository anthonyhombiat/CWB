package lig.steamer.cwb.model;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CWBAlignment {

	private URI nomenURI;
	private URI folksoURI;

	private Map<CWBEquivalence, Boolean> equivalences;
	
	public CWBAlignment(URI nomenURI, URI folksoURI){
		this.nomenURI = nomenURI;
		this.folksoURI = folksoURI;
		equivalences = new HashMap<CWBEquivalence, Boolean>();
	}
	
	/**
	 * @return the equivalences
	 */
	public Collection<CWBEquivalence> getEquivalences() {
		return equivalences.keySet();
	}
	
	private boolean addEquivalence(CWBEquivalence equivalence) {
			if(!equivalences.containsKey(equivalence)){
				equivalences.put(equivalence, false);
				return true;
			} 
			return false;
	}

	private boolean removeEquivalence(CWBEquivalence equivalence) {
			if(equivalences.containsKey(equivalence)){
				equivalences.remove(equivalence);
				return true;
			}
			return false;
	}

	public boolean addEquivalences(Collection<CWBEquivalence> equivalences) {
		boolean hasChanged = false;
		for (CWBEquivalence equivalence : equivalences) {
			if (addEquivalence(equivalence) == true) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	public boolean removeEquivalences(Collection<CWBEquivalence> equivalences) {
		boolean hasChanged = false;
		for (CWBEquivalence equivalence : equivalences) {
			if (removeEquivalence(equivalence)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
	/**
	 * @return the equivalences
	 */
	public Collection<CWBEquivalence> getSelectedEquivalences() {
		Set<CWBEquivalence> selectedEquivalences = new HashSet<CWBEquivalence>();
		
		for(Entry<CWBEquivalence, Boolean> e : equivalences.entrySet()){
			if(e.getValue()){
				selectedEquivalences.add(e.getKey());
			}
		}
		
		return selectedEquivalences;
	}
	
	public boolean selectedEquivalence(CWBEquivalence selectedEquivalence) {
		if(equivalences.containsKey(selectedEquivalence)){
			if(equivalences.get(selectedEquivalence) == false){
				equivalences.put(selectedEquivalence, true);
				return true;
			}
		}
		return false;
	}

	public boolean unselectEquivalence(CWBEquivalence selectedEquivalence) {
		if(equivalences.containsKey(selectedEquivalence)){
			if(equivalences.get(selectedEquivalence) == true){
				equivalences.put(selectedEquivalence, false);
				return true;
			}
		}
		return false;
	}
	
	public boolean removeAllSelectedEquivalences(){
		boolean hasChanged = false;
		for(Entry<CWBEquivalence, Boolean> e : equivalences.entrySet()){
			if(e.getValue()){
				e.setValue(false);
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	/**
	 * @return the nomenURI
	 */
	public URI getNomenURI() {
		return nomenURI;
	}

	/**
	 * @return the folksoURI
	 */
	public URI getFolksoURI() {
		return folksoURI;
	}
	
}
