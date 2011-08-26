package net.biomodels.ontology;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides general ontology query functionality.
 * @author Anna Zhukova
 *         Date: 26-Aug-2011
 *         Time: 11:11:44
 */
public interface IQueryMaker {
    /**
     * Returns the value of the specified annotation.
     * @param entity Annotated entity.
     * @param property Annotation property.
     * @return string value.
     */
    String getAnnotation(OWLEntity entity, OWLAnnotationProperty property);

    /**
     * Returns the value of the specified annotation.
     * @param entity Annotated entity.
     * @param property Annotation property IRI.
     * @return string value.
     */
    String getAnnotation(OWLEntity entity, IRI property);

    /**
     * Returns the value of the specified annotation which annotates specified annotation.
     * @param entityIRI IRI of the annotated entity.
     * @param annotatedAnnotation Annotation which is annotated with the specified annotation.
     * @param property Annotation property.
     * @return string value.
     */
    OWLAnnotation getAnnotationAnnotation(IRI entityIRI, OWLAnnotation annotatedAnnotation,
                                                  OWLAnnotationProperty property);

    /**
     * Normalizes a string.
     * @param s string to be normalized..
     * @return normalized string.
     */
    String normalize(String s);

    /**
     * Returns name annotation value for the specified entity.
     * @param iri Entity IRI.
     * @return name string.
     */
    String getName(IRI iri);

    /**
     * Returns definition annotation value for the specified entity.
     * @param iri Entity IRI.
     * @return definition string.
     */
    String getDefinition(IRI iri);

    /**
     * Returns if the specified entity is deprecated.
     * @param iri Entity IRI.
     * @return true, if the entity is deprecated.
     */
    boolean isDeprecated(IRI iri);

    /**
    * Returns synonym annotations for the specified entity.
    * @param iri Entity IRI.
    * @return set of synonyms.
    */
    Set<String> getSynonyms(IRI iri);

    /**
     * Returns synonym annotations of the specified synonym type for the specified entity.
     * @param iri Entity IRI.
     * @param type Synonym type.
     * @return set of synonyms.
     */
    Set<String> getSynonyms(IRI iri, String type);

    /**
     * Returns link to description map for the specified entity.
     * @param iri Entity IRI.
     * @return map link -> human-readable description.
     */
    Map<String, String> getLinks(IRI iri);

    /**
     * Looks for the "subject (not) 'has property' object"-like axioms and returns possible objects
     * for the specified subject and property.
     * @param subject entry.
     * @param property property.
     * @param positive true/false if positive/negative axiom should be considered,
     *        e.g. "subject _/not 'has property' object"
     * @param types optional array of object ancestors.
     *        If the types are specified,
     *        only the descendants (or equal) of any of the specified type will be returned.
     * @return set of objects.
     */
    Set<IRI> getPropertyValues(OWLClassExpression subject, OWLPropertyExpression property, boolean positive, IRI... types);

    /**
     * Looks for the "subject (not) 'has property' object"-like axioms and returns possible subjects
     * for the specified objects and property.
     * @param property property.
     * @param positive true/false if positive/negative axiom should be considered,
     *        e.g. "subject _/not 'has property' object"
     * @param objects array of objects.
     * @return set of subjects.
     */
    Set<IRI> getPropertySubjects(OWLObjectPropertyExpression property, boolean positive, IRI... objects);

    /**
     * Looks for the "subject (not) 'has property' object"-like axioms and returns possible subjects
     * for the specified objects, property and ancestor.
     * @param ancestor ancestor entity.
     * @param property property.
     * @param positive true/false if positive/negative axiom should be considered,
     *        e.g. "subject _/not 'has property' object"
     * @param objects array of objects.
     * @return set of subjects.
     */
    Set<IRI> getPropertySubjects(OWLClassExpression ancestor, OWLObjectPropertyExpression property, boolean positive, IRI... objects);

    /**
     * Looks for the "subject (not) 'has property' object"-like axioms and returns a class expression describing the subject
     * for the specified objects, property and subject ancestor.
     * @param ancestor ancestor entity.
     * @param property property.
     * @param positive true/false if positive/negative axiom should be considered,
     *        e.g. "subject _/not 'has property' object"
     * @param objects array of objects.
     * @return OWLClassExpression describing the subject.
     */
    OWLClassExpression getPropertySubjectExpression(OWLClassExpression ancestor,
                                                           OWLObjectPropertyExpression property, boolean positive,
                                                           IRI... objects);

    /**
     * Checks if the "subject (not) 'has property' object"-like axiom is true.
     * @param property property.
     * @param positive true/false if positive/negative axiom should be considered,
     *        e.g. "subject _/not 'has property' object"
     * @param objects array of objects.
     * @param subject subject.
     * @return true, if the "subject (not) 'has property' object"-like axiom holds.
     */
    boolean hasPropertyValue(OWLClassExpression subject, OWLObjectProperty property, boolean positive,
                                    IRI... objects);

    /**
     * Returns entities described by the specified query.
     * @param query entity description.
     * @return set of entity IRIs.
     */
    Set<IRI> getByQuery(OWLClassExpression query);

    /**
     * Checks the descendant-ancestor relationship.
     * @param descendantCandidate potential descendant.
     * @param ancestorCandidate potential ancestor.
     * @return true, if the descendantCandidate is a descendant or equal to ancestorCandidate.
     */
    boolean isA(IRI descendantCandidate, IRI ancestorCandidate);

    /**
     * Checks the descendant-ancestor relationship.
     * @param descendantCandidate potential descendant.
     * @param ancestorCandidate potential ancestor.
     * @return true, if the descendantCandidate is a descendant or equal to ancestorCandidate.
     */
    boolean isA(OWLClassExpression descendantCandidate, OWLClassExpression ancestorCandidate);

    /**
     * Returns descendants of the specified entry.
     * @param entry ancestor.
     * @param direct true if only direct descendants should be considered.
     * @return set of descendants.
     */
    Set<IRI> getDescendants(IRI entry, boolean direct);

    /**
     * Returns descendants of the specified entry.
     * @param entry ancestor.
     * @param direct true if only direct descendants should be considered.
     * @return set of descendants.
     */
    Set<IRI> getDescendants(OWLClassExpression entry, boolean direct);

    /**
     * Returns ancestors of the specified entry.
     * @param entry descendant.
     * @param direct true if only direct ancestors should be considered.
     * @return set of ancestors.
     */
    Set<IRI> getAncestors(IRI entry, boolean direct);

    /**
     * Returns ancestors of the specified entry.
     * @param entry descendant.
     * @param direct true if only direct ancestors should be considered.
     * @return set of ancestors.
     */
    Set<IRI> getAncestors(OWLClassExpression entry, boolean direct);

    /**
     * Returns ancestors of the specified entry.
     * @param entry OWLClassExpression describing the descendant.
     * @return set of ancestors.
     */
    Set<OWLClass> getAncestors(OWLClassExpression entry);

    /**
     * Looks for the entry with the specified name or synonym.
     * @param name or synonym string.
     * @return entry IRI.
     */
    Set<IRI> searchByName(String name);

    /**
     * Returns reasoner.
     * @return OWLReasoner.
     */
    OWLReasoner getReasoner();

    /**
     * Returns data factory.
     * @return OWLDataFactory.
     */
    OWLDataFactory getDataFactory();

    /**
     * Returns elements of the specified branch.
     * @param branchParent branch root IRI.
     * @return set of branch element IRIs.
     */
    Set<IRI> getSubBranch(IRI branchParent);

    /**
     * Returns leaf elements of the specified branch.
     * @param branchParent branch root IRI.
     * @return set of branch leaf element IRIs.
     */
    Set<IRI> getSubBranchLeaves(IRI branchParent);
}
