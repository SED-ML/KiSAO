package net.biomodels.ontology.impl;

import net.biomodels.ontology.IQueryMaker;
import net.biomodels.ontology.impl.IPropertyValueBySubjectVisitor;
import net.biomodels.ontology.impl.NegativePropertyValueBySubjectVisitor;
import net.biomodels.ontology.impl.PropertyValueBySubjectVisitor;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.*;

/**
 * @author Anna Zhukova
 *         Date: 25-Aug-2011
 *         Time: 16:27:24
 */
public class QueryMaker implements IQueryMaker {
    private IRI synonymIRI = IRI.create("http://www.w3.org/2004/02/skos/core#altLabel");
    private IRI synonymTypeIRI = OWLRDFVocabulary.RDFS_COMMENT.getIRI();
    private IRI nameIRI = OWLRDFVocabulary.RDFS_LABEL.getIRI();
    private IRI definitionIRI = IRI.create("http://www.w3.org/2004/02/skos/core#definition");
    private IRI linkIRI = OWLRDFVocabulary.RDFS_SEE_ALSO.getIRI();
    private IRI linkCommentIRI = OWLRDFVocabulary.RDFS_COMMENT.getIRI();

    private final OWLOntology ontology;
    private final OWLReasoner reasoner;
    private final OWLDataFactory dataFactory;

    private Map<String, Set<IRI>> name2iri = null;

    /**
     * Creates a new KiSAOQueryMaker.
     *
     * @param ontologyIri IRI for the ontology.
     * @param factory  OWLReasonerFactory to specify which reasoner should be used.
     *                 If factory == null, Hermit reasoner will be used.
     * @param monitor  ReasonerProgressMonitor to be used to listen to the reasoner progress.
     *                 If monitor == null, NullReasonerProgressMonitor will be used.
     * @throws org.semanticweb.owlapi.model.OWLOntologyCreationException If something is wrong with the ontology file/url.
     *
     * @see org.semanticweb.HermiT.Reasoner
     */
    public QueryMaker(IRI ontologyIri, OWLReasonerFactory factory, ReasonerProgressMonitor monitor) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        if (ontologyIri == null) {
            throw new IllegalArgumentException("Ontology IRI can not be null");
        }
        ontology = manager.loadOntology(ontologyIri);
        addImportedAxioms(manager);
        if (factory == null) {
            factory = new Reasoner.ReasonerFactory();//new FaCTPlusPlusReasonerFactory();
        }
        if (monitor == null) {
            monitor = new NullReasonerProgressMonitor();
        }
        reasoner = factory.createNonBufferingReasoner(ontology, new SimpleConfiguration(monitor));
        addInferredAxioms(manager);
    }

    public String getAnnotation(OWLEntity entity, OWLAnnotationProperty property) {
        assert entity != null;
        Set<OWLAnnotation> annotations = entity.getAnnotations(ontology, property);
        if (!annotations.isEmpty()) {
            return getLiteral(annotations.iterator().next());
        }
        return null;
    }

    public String getAnnotation(OWLEntity entity, IRI property) {
        assert entity != null;
        Set<OWLAnnotation> annotations = entity.getAnnotations(ontology, dataFactory.getOWLAnnotationProperty(property));
        if (!annotations.isEmpty()) {
            return getLiteral(annotations.iterator().next());
        }
        return null;
    }


    public OWLAnnotation getAnnotationAnnotation(IRI entityIRI, OWLAnnotation annotatedAnnotation,
                                                  OWLAnnotationProperty property) {
        for (OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(entityIRI)) {
            if (ax.getProperty().getIRI().equals(annotatedAnnotation.getProperty().getIRI())
                    && annotatedAnnotation.getValue().equals(ax.getValue())) {
                for (OWLAnnotation annotation : ax.getAnnotations()) {
                    if (annotation.getProperty().equals(property)) {
                        return annotation;
                    }
                }
                break;
            }
        }
        return null;
    }

    public String normalize(String s) {
        return s == null ? s : s.trim().toLowerCase().replace("'", "");
    }

    /**
     * Returns annotation value as a string.
     * @param annotation OWLAnnotation.
     * @return string.
     */
    private String getLiteral(OWLAnnotation annotation) {
        return annotation == null ? "" : ((OWLLiteral) annotation.getValue()).getLiteral();
    }

    /**
     * Creates a new QueryMaker.
     *
     * @param iri IRI for the ontology.
     * @param factory  OWLReasonerFactory to specify which reasoner should be used.
     *                 If factory == null, Hermit reasoner will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     *
     * @see org.semanticweb.HermiT.Reasoner
     */
    public QueryMaker(IRI iri, OWLReasonerFactory factory) throws OWLOntologyCreationException {
        this(iri, factory, null);
    }

    /**
     * Creates a new QueryMaker.
     *
     * @param ontologyIRI IRI for the ontology.
     * @param monitor  ReasonerProgressMonitor to be used to listen to the reasoner progress.
     *                 If monitor == null, NullReasonerProgressMonitor will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     *
     * @see org.semanticweb.HermiT.Reasoner
     */
    public QueryMaker(IRI ontologyIRI, ReasonerProgressMonitor monitor) throws OWLOntologyCreationException {
        this(ontologyIRI, null, monitor);
    }

    /**
     * Creates a new QueryMaker.
     *
     * @param ontologyIRI IRI for the ontology.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     */
    public QueryMaker(IRI ontologyIRI) throws OWLOntologyCreationException {
        this(ontologyIRI, null, null);
    }

    public String getName(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        return clazz == null ? null : getAnnotation(clazz, dataFactory.getOWLAnnotationProperty(nameIRI));
    }

    public String getDefinition(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        return clazz == null ? null : getAnnotation(clazz, dataFactory.getOWLAnnotationProperty(definitionIRI));
    }

    public boolean isDeprecated(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        return clazz != null &&
                "true".equalsIgnoreCase(getAnnotation(clazz, dataFactory.getOWLDeprecated()));
    }

     public Set<String> getSynonyms(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        Set<String> result = new HashSet<String>();
        Set<OWLAnnotation> synonym = clazz.getAnnotations(ontology,
                dataFactory.getOWLAnnotationProperty(synonymIRI));
        for (OWLAnnotation name : synonym) {
            String syn = getLiteral(name);
            if (syn != null) {
                result.add(syn);
            }
        }
        return result;
    }

    public Set<String> getSynonyms(IRI iri, String type) {
        assert type != null;
        OWLClass clazz = dataFactory.getOWLClass(iri);
        Set<String> result = new HashSet<String>();
        Set<OWLAnnotation> synonym = clazz.getAnnotations(ontology,
                dataFactory.getOWLAnnotationProperty(synonymIRI));
        OWLAnnotationProperty synonymType = dataFactory.getOWLAnnotationProperty(synonymTypeIRI);
        for (OWLAnnotation name : synonym) {
            if (type.equals(getLiteral(getAnnotationAnnotation(iri, name, synonymType)))) {
                result.add(getLiteral(name));
            }
        }
        return result;
    }

    public Map<String, String> getLinks(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        Map<String, String> result = new HashMap<String, String>();
        Set<OWLAnnotation> seeAlso = clazz.getAnnotations(ontology, dataFactory.getOWLAnnotationProperty(linkIRI));
        for (OWLAnnotation link : seeAlso) {
            String description = getLiteral(getAnnotationAnnotation(iri, link,
                    dataFactory.getOWLAnnotationProperty(linkCommentIRI)));
            result.put(getLiteral(link), description);
        }
        return result;
    }

    public Set<IRI> getPropertyValues(OWLClassExpression subject, OWLPropertyExpression property, final boolean positive, IRI... types) {
        Set<IRI> propertyValues = getPropertyValues(subject,
                positive ? new PropertyValueBySubjectVisitor(property) :
                        new NegativePropertyValueBySubjectVisitor(property));
        Set<IRI> result = propertyValues;
        if (types != null && types.length > 0) {
            result = new HashSet<IRI>();
            for (IRI iri : propertyValues) {
                for (IRI type : types) {
                    if (isA(iri, type)) {
                        result.add(iri);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Set<IRI> getPropertySubjects(OWLObjectPropertyExpression property, final boolean positive, IRI... objects) {
        return getPropertySubjects(dataFactory.getOWLThing(), property, positive, objects);
    }

    public Set<IRI> getPropertySubjects(OWLClassExpression ancestor, OWLObjectPropertyExpression property, boolean positive, IRI... objects) {
        OWLClassExpression query = getPropertySubjectExpression(ancestor, property,
                positive, objects);
        return getByQuery(query);
    }

    public OWLClassExpression getPropertySubjectExpression(OWLClassExpression ancestor,
                                                           OWLObjectPropertyExpression property, boolean positive,
                                                           IRI... objects) {
        for (IRI obj : objects) {
            OWLObjectSomeValuesFrom someValues = dataFactory.getOWLObjectSomeValuesFrom(property,
                    dataFactory.getOWLClass(obj));
            ancestor = dataFactory.getOWLObjectIntersectionOf(ancestor,
                    positive ? someValues : someValues.getObjectComplementOf());
        }
        return ancestor;
    }

    public boolean hasPropertyValue(OWLClassExpression subject, OWLObjectProperty property, boolean positive,
                                    final IRI... objects) {
        OWLClassExpression query = getPropertySubjectExpression(
                subject, property, positive, objects);
        return reasoner.isEntailed(dataFactory.getOWLEquivalentClassesAxiom(query, subject));
    }

    public Set<IRI> getByQuery(OWLClassExpression query) {
        final Set<IRI> result = new HashSet<IRI>();
        for (OWLClass expression : reasoner.getSubClasses(query, false).getFlattened()) {
            if (!expression.isOWLNothing()) {
                result.add(expression.getIRI());
            }
        }
        return result;
    }

    public boolean isA(IRI descendantCandidate, IRI ancestorCandidate) {
        if (descendantCandidate == null || ancestorCandidate == null) return false;
        OWLClass descendant = dataFactory.getOWLClass(descendantCandidate);
        OWLClass ancestor = dataFactory.getOWLClass(ancestorCandidate);
        return isA(descendant, ancestor);
    }

    public boolean isA(OWLClassExpression descendantCandidate, OWLClassExpression ancestorCandidate) {
        return !(descendantCandidate == null || ancestorCandidate == null)
                && (descendantCandidate.equals(ancestorCandidate)
                    || reasoner.isEntailed(dataFactory.getOWLSubClassOfAxiom(descendantCandidate, ancestorCandidate)));
    }

    public Set<IRI> getDescendants(IRI entry, boolean direct) {
        return getDescendants(dataFactory.getOWLClass(entry), direct);
    }

    public Set<IRI> getDescendants(OWLClassExpression entry, boolean direct) {
        if (entry == null) return null;
        final Set<IRI> result = new HashSet<IRI>();
        // not anonymous descendants
        for (Node<OWLClass> clazz : reasoner.getSubClasses(entry, direct)) {
            OWLClass descendant = clazz.getRepresentativeElement();
            if (descendant != null && !descendant.isOWLNothing()) {
                result.add(descendant.getIRI());
            }
        }
        return result;
    }

    public Set<IRI> getAncestors(IRI entry, boolean direct) {
        return getAncestors(dataFactory.getOWLClass(entry), direct);
    }

    public Set<IRI> getAncestors(OWLClassExpression entry, boolean direct) {
        if (entry == null) return null;
        final Set<IRI> result = new HashSet<IRI>();
        // not anonymous ancestors
        for (Node<OWLClass> clazz : reasoner.getSuperClasses(entry, direct)) {
            OWLClass ancestor = clazz.getRepresentativeElement();
            if (ancestor != null && !ancestor.isOWLThing()) {
                result.add(ancestor.getIRI());
            }
        }
        return result;
    }

    public Set<OWLClass> getAncestors(OWLClassExpression entry) {
        final Set<OWLClass> ancestors = new HashSet<OWLClass>();
        // not anonymous ancestors
        for (Node<OWLClass> clazz : reasoner.getSuperClasses(entry, false)) {
            ancestors.add(clazz.getRepresentativeElement());
        }
        return ancestors;
    }

    public Set<IRI> searchByName(String name) {
    	if (name == null || name.length() <= 0) return null;
    	name = normalize(name);
        if (name2iri == null) collectNames();
        Set<IRI> names = name2iri.get(name);
        return names == null ? null : Collections.unmodifiableSet(names);
    }

    public OWLReasoner getReasoner() {
        return reasoner;
    }

    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    public Set<IRI> getSubBranch(IRI branchParent) {
        Set<IRI> result = new HashSet<IRI>();
        for (Node<OWLClass> node : reasoner.getSubClasses(dataFactory.getOWLClass(branchParent), false)) {
            OWLClass element = node.getRepresentativeElement();
            if (!element.isOWLThing() && !element.isOWLNothing()) {
                result.add(element.getIRI());
            }
        }
        return result;
    }

    public Set<IRI> getSubBranchLeaves(IRI branchParent) {
        Set<IRI> result = new HashSet<IRI>();
        for (Node<OWLClass> node : reasoner.getSubClasses(dataFactory.getOWLClass(branchParent), false)) {
            OWLClass element = node.getRepresentativeElement();
            if (element.getSubClasses(ontology).isEmpty() && !element.isOWLNothing() && !element.isOWLThing()) {
                result.add(element.getIRI());
            }
        }
        return result;
    }

        public IRI getSynonymIRI() {
        return synonymIRI;
    }

    public void setSynonymIRI(IRI synonymIRI) {
        if (synonymIRI == null) throw new IllegalArgumentException("Synonym IRI must not be null");
        this.synonymIRI = synonymIRI;
    }

    public IRI getSynonymTypeIRI() {
        return synonymTypeIRI;
    }

    public void setSynonymTypeIRI(IRI synonymTypeIRI) {
        if (synonymTypeIRI == null) throw new IllegalArgumentException("Synonym type IRI must not be null");
        this.synonymTypeIRI = synonymTypeIRI;
        if (name2iri != null) collectNames();
    }

    public IRI getNameIRI() {
        return nameIRI;
    }

    public void setNameIRI(IRI nameIRI) {
        if (nameIRI == null) throw new IllegalArgumentException("Name IRI must not be null");
        this.nameIRI = nameIRI;
        if (name2iri != null) collectNames();
    }

    public IRI getDefinitionIRI() {
        return definitionIRI;
    }

    public void setDefinitionIRI(IRI definitionIRI) {
        if (definitionIRI == null) throw new IllegalArgumentException("Definition IRI must not be null");
        this.definitionIRI = definitionIRI;
    }

    public IRI getLinkIRI() {
        return linkIRI;
    }

    public void setLinkIRI(IRI linkIRI) {
        if (linkIRI == null) throw new IllegalArgumentException("Link IRI must not be null");
        this.linkIRI = linkIRI;
    }

    public IRI getLinkCommentIRI() {
        return linkCommentIRI;
    }

    public void setLinkCommentIRI(IRI linkCommentIRI) {
        if (linkCommentIRI == null) throw new IllegalArgumentException("Link comment IRI must not be null");
        this.linkCommentIRI = linkCommentIRI;
    }

    /* --------------------------- Private methods ------------------------- */

    private void addImportedAxioms(OWLOntologyManager manager) {
        for (OWLOntology importedOntology : ontology.getImports()) {
            manager.addAxioms(ontology, importedOntology.getAxioms());
        }
    }

    private void collectNames() {
        name2iri = new HashMap<String, Set<IRI>>();
        for (OWLClass clazz : ontology.getClassesInSignature()) {
            collectAnnotations(clazz, dataFactory.getOWLAnnotationProperty(nameIRI));
            collectAnnotations(clazz, dataFactory.getOWLAnnotationProperty(synonymIRI));
        }
    }

    private void collectAnnotations(OWLClass clazz, OWLAnnotationProperty property) {
        for (OWLAnnotation annotation : clazz.getAnnotations(ontology, property)) {
            String name = getLiteral(annotation);
            if (name != null) {
                name = normalize(name);
                Set<IRI> iriSet = name2iri.get(name);
                if (iriSet == null) {
                    iriSet = new HashSet<IRI>();
                    name2iri.put(name, iriSet);
                }
                iriSet.add(clazz.getIRI());
            }
        }
    }

    private void addInferredAxioms(OWLOntologyManager manager) {
        List<InferredAxiomGenerator<? extends OWLAxiom>> generators = Arrays.asList(
                (InferredAxiomGenerator<? extends OWLAxiom>)new InferredClassAssertionAxiomGenerator(),
                new InferredEquivalentClassAxiomGenerator(),
                new InferredInverseObjectPropertiesAxiomGenerator(),
                new InferredPropertyAssertionGenerator(),
                new InferredSubClassAxiomGenerator(),
                new InferredSubObjectPropertyAxiomGenerator()
        );
        new InferredOntologyGenerator(reasoner, generators).fillOntology(manager, ontology);
    }

    private Set<IRI> getPropertyValues(OWLClassExpression subject, final IPropertyValueBySubjectVisitor visitor) {
        visitor.clear();
        final Set<OWLClass> superSubjects = getAncestors(subject);
        Set<OWLSubClassOfAxiom> axioms = subject instanceof OWLClass ?
                ontology.getSubClassAxiomsForSubClass((OWLClass) subject) : new HashSet<OWLSubClassOfAxiom>();
        for (OWLClass clzz : superSubjects) {
            axioms.addAll(ontology.getSubClassAxiomsForSubClass(clzz));
        }
        for (final OWLAxiom ax : axioms) {
            ax.accept(new OWLAxiomVisitorAdapter() {
                public void visit(OWLSubClassOfAxiom axiom) {
                    OWLClassExpression expression = axiom.getSuperClass();
                    if (expression.isAnonymous()) {
                        expression.accept(visitor);
                    }
                }
            });
        }
        return visitor.getValues();
    }
}
