package lig.steamer.cwb.io.read.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModel;
import lig.steamer.cwb.model.CWBEquivalence;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;

public class OWL2CWB {
	
	private static Logger LOGGER = Logger.getLogger(OWL2CWB.class
			.getName());
	
	private OWLDataFactory factory;

	public OWL2CWB(OWLDataFactory factory) {
		this.factory = factory;
	}
	
	public Collection<CWBEquivalence> getCWBEquivalencesFromOwlEquivalentClassesAxiom(
			OWLEquivalentClassesAxiom axiom, CWBConcept concept,
			CWBDataModel dataModel) {

		Collection<CWBEquivalence> equivalences = new ArrayList<>();

		for (OWLClass equivalentClass : axiom.getClassesInSignature()) {

			equivalences.add(new CWBEquivalence(concept, dataModel
					.getConceptFromIRI(equivalentClass.getIRI()), 1));

			LOGGER.log(Level.FINE, "Adding CWBEquivalence class "
					+ equivalentClass.getIRI());

		}

		return equivalences;
	}

	public CWBConcept getCWBConceptFromOwlClass(OWLClass clazz,
			OWLOntology ontology) {

		CWBConcept concept = new CWBConcept(clazz.getIRI());

		Set<OWLAnnotation> labels = clazz.getAnnotations(ontology,
				factory.getRDFSLabel());

		for (OWLAnnotation label : labels) {
			OWLLiteral literal = (OWLLiteral) label.getValue();
			concept.addLabel(literal.getLiteral().toString(),
					new Locale(literal.getLang()));
		}

		Set<OWLAnnotation> comments = clazz.getAnnotations(ontology,
				factory.getRDFSComment());

		for (OWLAnnotation comment : comments) {
			OWLLiteral literal = (OWLLiteral) comment.getValue();
			concept.addDescription(literal.getLiteral().toString(), new Locale(
					literal.getLang()));
		}

		return concept;
	}

	public Set<OWLClass> findRootClasses(OWLOntology ontology) {

		LOGGER.log(Level.FINE, "Searching for root classes...");

		Set<OWLClass> rootClasses = new HashSet<OWLClass>();

		for (OWLClass clazz : ontology.getClassesInSignature()) {
			if (clazz.getSuperClasses(ontology).size() == 0) {
				rootClasses.add(clazz);
				LOGGER.log(Level.FINE, "> " + clazz.getIRI().getFragment());
			}
		}

		LOGGER.log(Level.FINE, "Searching done.");

		return rootClasses;

	}
	
}
