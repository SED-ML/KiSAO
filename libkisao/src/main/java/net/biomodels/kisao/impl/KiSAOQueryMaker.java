package net.biomodels.kisao.impl;

import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.KiSAOIRI;
import net.biomodels.kisao.KiSAOType;
import net.biomodels.kisao.SynonymType;
import net.biomodels.kisao.visitors.IParameterTypeVisitor;
import net.biomodels.kisao.visitors.IPropertyValueByAlgorithmVisitor;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.util.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.biomodels.kisao.KiSAOIRI.*;

/**
 * Provides methods to query KiSAO by implementing IKiSAOQueryMaker interface.
 *
 * @author Anna Zhukova
 *         Date: 12-May-2011
 *         Time: 11:29:03
 */
public class KiSAOQueryMaker implements IKiSAOQueryMaker {

    private final Pattern pattern = Pattern.compile("^\\s*(" + KISAO_URN + "|kisao:)?(KISAO_|KISAO:)?\\d{7}\\s*$");
    private final Pattern idPattern = Pattern.compile("\\d{7}");
    private final OWLOntology kisao;
    private final OWLReasoner reasoner;
    private final OWLDataFactory dataFactory;

    private final Map<String, IRI> label2iri = new HashMap<String, IRI>();

    /**
     * Creates a new KiSAOQueryMaker.
     *
     * @param kisaoIri IRI for the KiSAO ontology.
     *                 If kisaoIri == null, then default KiSAO_IRI will be used.
     * @param factory  OWLReasonerFactory to specify which reasoner should be used.
     *                 If factory == null, Hermit reasoner will be used.
     * @param monitor  ReasonerProgressMonitor to be used to listen to the reasoner progress.
     *                 If monitor == null, NullReasonerProgressMonitor will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     * @see net.biomodels.kisao.impl.KiSAOQueryMaker KISAO_IRI
     * @see org.semanticweb.HermiT.Reasoner
     */
    public KiSAOQueryMaker(IRI kisaoIri, OWLReasonerFactory factory, ReasonerProgressMonitor monitor) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        if (kisaoIri == null) {
            kisaoIri = KISAO_IRI;
        }
        kisao = manager.loadOntology(kisaoIri);

        addImportedAxioms(manager);

        collectLabels();

        if (factory == null) {
            factory = new Reasoner.ReasonerFactory();//new FaCTPlusPlusReasonerFactory();
        }
        if (monitor == null) {
            monitor = new NullReasonerProgressMonitor();
        }
        reasoner = factory.createNonBufferingReasoner(kisao, new SimpleConfiguration(monitor));
        addInferredAxioms(manager);
    }

    private void addImportedAxioms(OWLOntologyManager manager) {
        for (OWLOntology importedOntology : kisao.getImports()) {
            manager.addAxioms(kisao, importedOntology.getAxioms());
        }
    }

    /**
     * Creates a new KiSAOQueryMaker.
     *
     * @param kisaoIRI IRI for the KiSAO ontology.
     *                 If kisaoIri == null, then default KiSAO_IRI will be used.
     * @param factory  OWLReasonerFactory to specify which reasoner should be used.
     *                 If factory == null, Hermit reasoner will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     * @see net.biomodels.kisao.impl.KiSAOQueryMaker KISAO_IRI
     * @see org.semanticweb.HermiT.Reasoner
     */
    public KiSAOQueryMaker(IRI kisaoIRI, OWLReasonerFactory factory) throws OWLOntologyCreationException {
        this(kisaoIRI, factory, null);
    }

    /**
     * Creates a new KiSAOQueryMaker.
     * Default KiSAO IRI is used to find KiSAO.
     *
     * @param factory OWLReasonerFactory to specify which reasoner should be used.
     *                If factory == null, Hermit reasoner will be used.
     * @param monitor ReasonerProgressMonitor to be used to listen to the reasoner progress.
     *                If monitor == null, NullReasonerProgressMonitor will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     * @see net.biomodels.kisao.impl.KiSAOQueryMaker KISAO_IRI
     * @see org.semanticweb.HermiT.Reasoner
     */
    public KiSAOQueryMaker(OWLReasonerFactory factory, ReasonerProgressMonitor monitor) throws OWLOntologyCreationException {
        this(null, factory, monitor);
    }

    /**
     * Creates a new KiSAOQueryMaker.
     *
     * @param kisaoIRI IRI for the KiSAO ontology.
     *                 If kisaoIri == null, then default KiSAO_IRI will be used.
     * @param monitor  ReasonerProgressMonitor to be used to listen to the reasoner progress.
     *                 If monitor == null, NullReasonerProgressMonitor will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     * @see net.biomodels.kisao.impl.KiSAOQueryMaker KISAO_IRI
     *      Hermit reasoner is used.
     * @see org.semanticweb.HermiT.Reasoner
     */
    public KiSAOQueryMaker(IRI kisaoIRI, ReasonerProgressMonitor monitor) throws OWLOntologyCreationException {
        this(kisaoIRI, null, monitor);
    }

    /**
     * Creates a new KiSAOQueryMaker.
     *
     * @param kisaoIRI IRI for the KiSAO ontology.
     *                 If kisaoIri == null, then default KiSAO_IRI will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     * @see net.biomodels.kisao.impl.KiSAOQueryMaker KISAO_IRI
     *      Hermit reasoner is used.
     */
    public KiSAOQueryMaker(IRI kisaoIRI) throws OWLOntologyCreationException {
        this(kisaoIRI, null, null);
    }

    /**
     * Creates a new KiSAOQueryMaker.
     * Default KiSAO IRI is used to find KiSAO.
     *
     * @param monitor ReasonerProgressMonitor to be used to listen to the reasoner progress.
     *                If monitor == null, NullReasonerProgressMonitor will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     * @see net.biomodels.kisao.impl.KiSAOQueryMaker KISAO_IRI
     *      Hermit reasoner is used.
     * @see org.semanticweb.HermiT.Reasoner
     */
    public KiSAOQueryMaker(ReasonerProgressMonitor monitor) throws OWLOntologyCreationException {
        this(null, null, monitor);
    }

    /**
     * Creates a new KiSAOQueryMaker.
     * Default KiSAO IRI is used to find KiSAO.
     *
     * @param factory OWLReasonerFactory to specify which reasoner should be used.
     *                If factory == null, Hermit reasoner will be used.
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     * @see net.biomodels.kisao.impl.KiSAOQueryMaker KISAO_IRI
     * @see org.semanticweb.HermiT.Reasoner
     */
    public KiSAOQueryMaker(OWLReasonerFactory factory) throws OWLOntologyCreationException {
        this(null, factory, null);
    }

    /**
     * Creates a new KiSAOQueryMaker.
     * Default KiSAO IRI is used to find KiSAO.
     *
     * @throws OWLOntologyCreationException If something is wrong with the ontology file/url.
     * @see net.biomodels.kisao.impl.KiSAOQueryMaker KISAO_IRI
     *      Hermit reasoner is used.
     * @see org.semanticweb.HermiT.Reasoner
     */
    public KiSAOQueryMaker() throws OWLOntologyCreationException {
        this(null, null, null);
    }

    public String getName(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        return clazz == null ? null : getAnnotation(clazz, dataFactory.getOWLAnnotationProperty(NAME_IRI));
    }

    public String getDefinition(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        return clazz == null ? null : getAnnotation(clazz, dataFactory.getOWLAnnotationProperty(DEFINITION_IRI));
    }

    public boolean isDeprecated(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        return clazz != null &&
                "true".equalsIgnoreCase(getAnnotation(clazz, dataFactory.getOWLDeprecated()));
    }

    public Set<String> getAllSynonyms(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        Set<String> result = new HashSet<String>();
        Set<OWLAnnotation> synonym = clazz.getAnnotations(kisao,
                dataFactory.getOWLAnnotationProperty(SYNONYM_IRI));
        for (OWLAnnotation name : synonym) {
            String syn = getLiteral(name);
            if (syn != null) {
                result.add(syn);
            }
        }
        return result;
    }

    public Set<String> getSynonyms(IRI iri, SynonymType type) {
        assert type != null;
        OWLClass clazz = dataFactory.getOWLClass(iri);
        Set<String> result = new HashSet<String>();
        Set<OWLAnnotation> synonym = clazz.getAnnotations(kisao,
                dataFactory.getOWLAnnotationProperty(SYNONYM_IRI));
        OWLAnnotationProperty synonymType = dataFactory.getOWLAnnotationProperty(SYNONYM_TYPE_IRI);
        for (OWLAnnotation name : synonym) {
            if (type.equals(SynonymType.byName(getLiteral(getAnnotationAnnotation(iri, name, synonymType))))) {
                result.add(getLiteral(name));
            }
        }
        return result;
    }

    public Map<String, String> getLinks(IRI iri) {
        OWLClass clazz = dataFactory.getOWLClass(iri);
        Map<String, String> result = new HashMap<String, String>();
        Set<OWLAnnotation> seeAlso = clazz.getAnnotations(kisao, dataFactory.getRDFSSeeAlso());
        for (OWLAnnotation link : seeAlso) {
            String description = getLiteral(getAnnotationAnnotation(iri, link, dataFactory.getRDFSComment()));
            result.put(getLiteral(link), description);
        }
        return result;
    }

    public List<IRI> getAllAlgorithms() {
        return getSubBranch(KINETIC_SIMULATION_ALGORITHM_IRI);
    }

    public List<IRI> getAllCharacteristics() {
        return getSubBranchLeaves(KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI);
    }

    public List<IRI> getAllParameters() {
        return getSubBranchLeaves(KINETIC_SIMULATION_ALGORITHM_PARAMETER_IRI);
    }

    public Set<IRI> getCharacteristics(IRI algorithmIri, boolean positive, IRI... type) {
        return getCharacteristics(dataFactory.getOWLClass(algorithmIri), positive, type);
    }

    public Set<IRI> getCharacteristics(OWLClassExpression algorithm, final boolean positive, IRI... types) {
        Set<IRI> characteristics = getPropertyValues(algorithm,
                positive ? new PropertyValueByAlgorithmVisitor(HAS_CHARACTERISTIC_IRI) :
                        new NegativePropertyValueByAlgorithmVisitor(HAS_CHARACTERISTIC_IRI));
        Set<IRI> result = characteristics;
        if (types != null && types.length > 0) {
            result = new HashSet<IRI>();
            for (IRI characteristic : characteristics) {
                for (IRI type : types) {
                    if (isA(characteristic, type)) {
                        result.add(characteristic);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Set<IRI> getAlgorithmsByCharacteristic(final boolean positive, IRI... characteristicIri) {
        OWLClassExpression query = getClassExpressionByCharacteristics(
                dataFactory.getOWLClass(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_IRI),
                positive, characteristicIri);
        return getAlgorithmsByQuery(query);
    }

    public Set<IRI> getAlgorithmsByQuery(OWLClassExpression query) {
        final Set<IRI> algorithms = new HashSet<IRI>();
        for (OWLClass expression : reasoner.getSubClasses(query, false).getFlattened()) {
            if (!expression.isOWLNothing()) {
                algorithms.add(expression.getIRI());
            }
        }
        return algorithms;
    }

    public boolean isA(IRI descendantCandidate, IRI ancestorCandidate) {
        if (descendantCandidate == null || ancestorCandidate == null) return false;
        OWLClass descendant = dataFactory.getOWLClass(descendantCandidate);
        OWLClass ancestor = dataFactory.getOWLClass(ancestorCandidate);
        return isA(descendant, ancestor);
    }

    public boolean isA(OWLClassExpression descendant, OWLClassExpression ancestor) {
        if (descendant == null || ancestor == null) return false;
        return descendant.equals(ancestor) ||
                reasoner.isEntailed(dataFactory.getOWLSubClassOfAxiom(descendant, ancestor));
    }

    public boolean hasCharacteristic(IRI algorithmIRI, boolean positive, final IRI... characteristicIRI) {
        OWLClass algorithm = dataFactory.getOWLClass(algorithmIRI);
        OWLClassExpression query = getClassExpressionByCharacteristics(
                dataFactory.getOWLClass(algorithmIRI), positive, characteristicIRI);
        return reasoner.isEntailed(dataFactory.getOWLEquivalentClassesAxiom(query, algorithm));
    }

    public Set<IRI> getDescendants(IRI entryIRI, boolean direct) {
        return getDescendants(dataFactory.getOWLClass(entryIRI), direct);
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

    public Set<IRI> getAncestors(IRI entryIRI, boolean direct) {
        return getAncestors(dataFactory.getOWLClass(entryIRI), direct);
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

    public boolean isHybrid(IRI iri) {
        OWLObjectProperty isHybrid = dataFactory.getOWLObjectProperty(IS_HYBRID_OF_IRI);
        OWLClass ksa = dataFactory.getOWLClass(KINETIC_SIMULATION_ALGORITHM_IRI);
        OWLObjectSomeValuesFrom someValuesFrom = dataFactory.getOWLObjectSomeValuesFrom(isHybrid, ksa);
        OWLClass algorithm = dataFactory.getOWLClass(iri);
        OWLClassExpression query = dataFactory.getOWLObjectIntersectionOf(algorithm, someValuesFrom);
        return reasoner.getEquivalentClasses(query).contains(algorithm);
    }

    public Set<IRI> getHybridOf(IRI iri) {
        return isHybrid(iri) ?
                getPropertyValues(dataFactory.getOWLClass(iri), new PropertyValueByAlgorithmVisitor(IS_HYBRID_OF_IRI))
                : null;
    }

    public Set<IRI> getParameters(IRI algorithmIri) {
        return getParameters(dataFactory.getOWLClass(algorithmIri));
    }

    // TODO: make it faster
    public Set<IRI> getParameters(OWLClassExpression algorithm) {
        OWLObjectProperty hasParameter = dataFactory.getOWLObjectProperty(HAS_PARAMETER_IRI);
        Set<IRI> result = new HashSet<IRI>();
        for (Node<OWLClass> node : reasoner.getSubClasses(
                dataFactory.getOWLClass(KINETIC_SIMULATION_ALGORITHM_PARAMETER_IRI), false)) {
            OWLClass element = node.getRepresentativeElement();
            if (element.getSubClasses(kisao).isEmpty() && !element.isOWLNothing()) {
                OWLClassExpression query = dataFactory.getOWLObjectSomeValuesFrom(hasParameter, element);
                query = dataFactory.getOWLObjectIntersectionOf(algorithm, query);
                if (reasoner.isEntailed(dataFactory.getOWLEquivalentClassesAxiom(algorithm, query))) {
                    result.add(element.getIRI());
                }
            }
        }
        return result;
    }

    public Set<IRI> getParametersByCharacteristic(boolean positive, IRI... characteristicIRI) {
        return getParametersByAncestorAndCharacteristic(KINETIC_SIMULATION_ALGORITHM_IRI, positive, characteristicIRI);
    }

    public Set<IRI> getParametersByAncestorAndCharacteristic(IRI ancestor, boolean positive, IRI... characteristicIRI) {
        return getParametersByAncestorAndCharacteristic(dataFactory.getOWLClass(ancestor), positive, characteristicIRI);
    }

    public Set<IRI> getParametersByAncestorAndCharacteristic(OWLClassExpression ancestor, boolean positive, IRI... characteristicIRI) {
        OWLClassExpression algorithm = getClassExpressionByCharacteristics(ancestor,
                positive, characteristicIRI);
        return getParameters(algorithm);
    }

    public boolean hasParameter(OWLClassExpression algorithmExpression, final IRI... parameterIRI) {
        OWLObjectProperty hasParameter = dataFactory.getOWLObjectProperty(HAS_PARAMETER_IRI);
        for (IRI iri : parameterIRI) {
            OWLClassExpression query = algorithmExpression;
            OWLClass parameter = dataFactory.getOWLClass(iri);
            OWLObjectSomeValuesFrom someValuesFrom = dataFactory.getOWLObjectSomeValuesFrom(hasParameter, parameter);
            query = dataFactory.getOWLObjectIntersectionOf(query, someValuesFrom);
            if (!reasoner.isEntailed(dataFactory.getOWLEquivalentClassesAxiom(algorithmExpression, query))) {
                return false;
            }
        }
        return true;
    }

    public boolean hasParameter(IRI algorithmIRI, final IRI... parameterIRI) {
        OWLClass algorithm = dataFactory.getOWLClass(algorithmIRI);
        return hasParameter(algorithm, parameterIRI);
    }

    public boolean hasParameter(boolean positive, IRI[] characteristicIRIs, IRI... parameterIRI) {
        assert characteristicIRIs != null;
        OWLClassExpression algorithm = getClassExpressionByCharacteristics(
                dataFactory.getOWLClass(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_IRI),
                positive, characteristicIRIs);
        return hasParameter(algorithm, parameterIRI);
    }

    public Class getParameterType(IRI parameterIri) {
        final OWLClass parameter = dataFactory.getOWLClass(parameterIri);
        final IParameterTypeVisitor visitor = new ParameterTypeVisitor();
        for (final OWLAxiom ax : kisao.getAxioms(AxiomType.SUBCLASS_OF)) {
            ax.accept(new OWLAxiomVisitorAdapter() {
                public void visit(OWLSubClassOfAxiom axiom) {
                    OWLClassExpression subClz = axiom.getSubClass();
                    if (subClz != null && (parameter.equals(subClz))) {
                        OWLClassExpression expression = axiom.getSuperClass();
                        if (expression.isAnonymous()) {
                            expression.accept(visitor);
                        }
                    }
                }
            });
        }
        return visitor.getResult();
    }

    public KiSAOType getType(IRI iri) {
        assert iri != null;
        if (isAlgorithm(iri)) {
            return KiSAOType.ALGORITHM;
        } else if (isCharacteristic(iri)) {
            return KiSAOType.CHARACTERISTIC;
        } else if (isParameter(iri)) {
            return KiSAOType.PARAMETER;
        } else {
            return KiSAOType.OTHER;
        }
    }

    public boolean isAlgorithm(IRI iri) {
        return isA(iri, KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_IRI);
    }

    public boolean isCharacteristic(IRI iri) {
        return isA(iri, KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI);
    }

    public boolean isParameter(IRI iri) {
        return isA(iri, KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_PARAMETER_IRI);
    }

    public IRI getIRIByName(String name) {
        assert name != null;
        return label2iri.get(normalize(name));
    }

    public IRI getIRIByNameOrSynonym(String name) {
        assert name != null;
        name = normalize(name);
        IRI iri = label2iri.get(name);
        if (iri != null) return iri;
        OWLAnnotationProperty synonym = dataFactory.getOWLAnnotationProperty(SYNONYM_IRI);
        for (OWLAnnotationAssertionAxiom ax : kisao.getAxioms(AxiomType.ANNOTATION_ASSERTION)) {
            OWLAnnotation annotation = ax.getAnnotation();
            if (synonym.equals(annotation.getProperty())
                    && name.equals(normalize(getLiteral(annotation)))) {
                OWLAnnotationSubject subject = ax.getSubject();
                if (subject instanceof IRI) {
                    return (IRI) subject;
                }
            }
        }
        return null;
    }

    public IRI getIRIbyMiriamURIorId(String id) {
        if (pattern.matcher(id).find()) {
            Matcher matcher = idPattern.matcher(id);
            matcher.find();
            id = matcher.group();
        }
        return IRI.create(String.format("%sKISAO_%s", KISAO_PREFIX, id));
    }

    public String getMiriamURIByIRI(IRI iri) {
        String id = iri.getFragment();
        return String.format("%s%s", KISAO_URN, id);
    }

    public String getIdByIRI(IRI iri) {
        if (iri == null) return null;
        String fragment = iri.getFragment();
        if (fragment == null || !pattern.matcher(fragment).find()) {
            return null;
        }
        return fragment.replace("_", ":");
    }

    public OWLReasoner getReasoner() {
        return reasoner;
    }

    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    public Set<IRI> getAlgorithmsWithSameCharacteristics(IRI algorithm, IRI... type) {
        Set<IRI> positive = getCharacteristics(algorithm, true, type);
        OWLClass ksa = dataFactory.getOWLClass(KINETIC_SIMULATION_ALGORITHM_IRI);
        OWLClassExpression query = getClassExpressionByCharacteristics(ksa, true,
                positive.toArray(new IRI[positive.size()]));
        Set<IRI> negative = getCharacteristics(algorithm, false, type);
        query = getClassExpressionByCharacteristics(query, false, negative.toArray(new IRI[negative.size()]));
        Set<IRI> similar = getAlgorithmsByQuery(query);
        // remove self
        similar.remove(algorithm);
        return similar;
    }

    /* ------------------- private methods ------------------------ */

    private OWLClassExpression getClassExpressionByCharacteristics(OWLClassExpression algorithm, boolean positive, IRI... characteristicIRI) {
        OWLObjectProperty hasCharacteristic = dataFactory.getOWLObjectProperty(HAS_CHARACTERISTIC_IRI);
        for (IRI characteristic : characteristicIRI) {
            OWLObjectSomeValuesFrom someValues = dataFactory.getOWLObjectSomeValuesFrom(hasCharacteristic,
                    dataFactory.getOWLClass(characteristic));
            algorithm = dataFactory.getOWLObjectIntersectionOf(algorithm,
                    positive ? someValues : someValues.getObjectComplementOf());
        }
        return algorithm;
    }

    private Set<IRI> getPropertyValues(OWLClassExpression algorithm, final IPropertyValueByAlgorithmVisitor visitor) {
        visitor.clear();
        final Set<OWLClass> superAlgorithms = getAncestors(algorithm);
        Set<OWLSubClassOfAxiom> axioms = algorithm instanceof OWLClass ?
                kisao.getSubClassAxiomsForSubClass((OWLClass) algorithm) : new HashSet<OWLSubClassOfAxiom>();
        for (OWLClass clzz : superAlgorithms) {
            axioms.addAll(kisao.getSubClassAxiomsForSubClass(clzz));
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

    private void collectLabels() {
        for (OWLClass clazz : kisao.getClassesInSignature()) {
            String label = getAnnotation(clazz, dataFactory.getRDFSLabel());
            if (label != null) {
                label2iri.put(normalize(label), clazz.getIRI());
            }
        }
        for (OWLObjectProperty property : kisao.getObjectPropertiesInSignature()) {
            IRI iri = property.getIRI();
            Set<OWLAnnotation> labels = property.getAnnotations(kisao, dataFactory.getRDFSLabel());
            if (!labels.isEmpty()) {
                String label = getLiteral(labels);
                if (label != null) {
                    label2iri.put(normalize(label), iri);
                }
            }
        }
    }

    private String normalize(String label) {
        return label == null ? label : label.trim().toLowerCase().replace("'", "");
    }

    private String getLiteral(Set<OWLAnnotation> labels) {
        return ((OWLLiteral) labels.iterator().next().getValue()).getLiteral();
    }

    private String getLiteral(OWLAnnotation annotation) {
        return annotation == null ? "" : ((OWLLiteral) annotation.getValue()).getLiteral();
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
        new InferredOntologyGenerator(reasoner, generators).fillOntology(manager, kisao);
    }

    private Set<OWLClass> getAncestors(OWLClassExpression algorithm) {
        final Set<OWLClass> ancestors = new HashSet<OWLClass>();
        // not anonymous ancestors
        for (Node<OWLClass> clazz : reasoner.getSuperClasses(algorithm, false)) {
            ancestors.add(clazz.getRepresentativeElement());
        }
        return ancestors;
    }

    private List<IRI> getSubBranch(IRI branchParent) {
        List<IRI> result = new ArrayList<IRI>();
        for (Node<OWLClass> node : reasoner.getSubClasses(dataFactory.getOWLClass(branchParent), false)) {
            OWLClass element = node.getRepresentativeElement();
            if (!element.isOWLThing() && !element.isOWLNothing()) {
                result.add(element.getIRI());
            }
        }
        return result;
    }

    private List<IRI> getSubBranchLeaves(IRI branchParent) {
        List<IRI> result = new ArrayList<IRI>();
        for (Node<OWLClass> node : reasoner.getSubClasses(dataFactory.getOWLClass(branchParent), false)) {
            OWLClass element = node.getRepresentativeElement();
            if (element.getSubClasses(kisao).isEmpty() && !element.isOWLNothing() && !element.isOWLThing()) {
                result.add(element.getIRI());
            }
        }
        return result;
    }

    private String getAnnotation(OWLClass clazz, OWLAnnotationProperty property) {
        assert clazz != null;
        Set<OWLAnnotation> definition = clazz.getAnnotations(kisao, property);
        if (!definition.isEmpty()) {
            return getLiteral(definition);
        }
        return null;
    }

    private OWLAnnotation getAnnotationAnnotation(IRI entityIRI, OWLAnnotation annotatedAnnotation,
                                                  OWLAnnotationProperty property) {
        for (OWLAnnotationAssertionAxiom ax : kisao.getAnnotationAssertionAxioms(entityIRI)) {
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
}
