package lig.steamer.cwb.util.matching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBEquivalence;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owl.align.Relation;
import org.semanticweb.owlapi.model.IRI;

public class CWBOntologyAlignmentVisitor implements AlignmentVisitor {

	private Collection<CWBEquivalence> equivalences;
	
	public CWBOntologyAlignmentVisitor(){
		this.equivalences = new ArrayList<CWBEquivalence>();
	}
	
	@Override
	public void init(Properties arg0) {
	}

	@Override
	public void visit(Alignment alignment) throws AlignmentException {
	
		for(Cell cell : alignment){
			
			cell.accept(this);
			
		}
	
	}

	@Override
	public void visit(Cell cell) throws AlignmentException {
		
		CWBConcept concept1 = new CWBConcept(IRI.create(cell.getObject1AsURI()));
//		for(cell.getObject1())
		CWBConcept concept2 = new CWBConcept(IRI.create(cell.getObject2AsURI()));
		
		equivalences.add(new CWBEquivalence(concept1, concept2, cell.getStrength()));
		
	}

	@Override
	public void visit(Relation arg0) throws AlignmentException {
	}

	public Collection<CWBEquivalence> getEquivalences(){
		return equivalences;
	}
	
}
