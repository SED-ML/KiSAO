package net.biomodels.kisao.impl;

import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.KiSAOIRI;
import net.biomodels.kisao.KiSAOType;
import net.biomodels.kisao.SynonymType;
import net.biomodels.ontology.IQueryMaker;
import net.biomodels.ontology.impl.QueryMaker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

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

    private final Pattern pattern = Pattern.compile("^\\s*(" + KISAO_URN + "|kisao:|http://www.biomodels.net/kisao/KISAO#)?(KISAO_|KISAO:)?\\d{7}\\s*$", Pattern.CASE_INSENSITIVE);
    private final Pattern idPattern = Pattern.compile("\\d{7}");

    protected final IQueryMaker queryMaker;

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
        if (kisaoIri == null) {
            kisaoIri = KISAO_IRI;
        }
        queryMaker = new QueryMaker(kisaoIri, factory, monitor);
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
        return queryMaker.getName(iri);
    }

    public String getDefinition(IRI iri) {
        return queryMaker.getDefinition(iri);
    }

    public boolean isDeprecated(IRI iri) {
        return queryMaker.isDeprecated(iri);
    }

    public Set<String> getAllSynonyms(IRI iri) {
        return queryMaker.getSynonyms(iri);
    }

    public Set<String> getSynonyms(IRI iri, SynonymType type) {
        return type == null ? queryMaker.getSynonyms(iri) : queryMaker.getSynonyms(iri, type.getName());
    }

    public Map<String, String> getLinks(IRI iri) {
        return queryMaker.getLinks(iri);
    }

    public Set<IRI> getAllAlgorithms() {
        return queryMaker.getSubBranch(KINETIC_SIMULATION_ALGORITHM_IRI);
    }

    public Set<IRI> getAllCharacteristics() {
        return queryMaker.getSubBranchLeaves(KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI);
    }

    public Set<IRI> getAllParameters() {
        return queryMaker.getSubBranchLeaves(KINETIC_SIMULATION_ALGORITHM_PARAMETER_IRI);
    }

    public Set<IRI> getCharacteristics(IRI algorithmIri, boolean positive, IRI... type) {
        return getCharacteristics(getDataFactory().getOWLClass(algorithmIri), positive, type);
    }

    public Set<IRI> getCharacteristics(OWLClassExpression algorithm, final boolean positive, IRI... types) {
        return queryMaker.getPropertyValues(algorithm,
                getDataFactory().getOWLObjectProperty(HAS_CHARACTERISTIC_IRI), positive, types);
    }

    public Set<IRI> getAlgorithmsByCharacteristic(final boolean positive, IRI... characteristicIri) {
        OWLDataFactory dataFactory = getDataFactory();
        return queryMaker.getPropertySubjects(dataFactory.getOWLClass(KINETIC_SIMULATION_ALGORITHM_IRI),
                dataFactory.getOWLObjectProperty(HAS_CHARACTERISTIC_IRI), positive, characteristicIri);
    }

    public Set<IRI> getAlgorithmsByQuery(OWLClassExpression query) {
        return queryMaker.getByQuery(query);
    }

    public boolean isA(IRI descendantCandidate, IRI ancestorCandidate) {
        return queryMaker.isA(descendantCandidate, ancestorCandidate);
    }

    public boolean isA(OWLClassExpression descendant, OWLClassExpression ancestor) {
        return queryMaker.isA(descendant, ancestor);
    }

    public boolean hasCharacteristic(IRI algorithmIRI, boolean positive, final IRI... characteristicIRI) {
        return queryMaker.hasPropertyValue(getDataFactory().getOWLClass(algorithmIRI),
                getDataFactory().getOWLObjectProperty(HAS_CHARACTERISTIC_IRI),
                positive, characteristicIRI);
    }

    public Set<IRI> getDescendants(IRI entryIRI, boolean direct) {
        return queryMaker.getDescendants(entryIRI, direct);
    }

    public Set<IRI> getDescendants(OWLClassExpression entry, boolean direct) {
        return queryMaker.getDescendants(entry, direct);
    }

    public Set<IRI> getAncestors(IRI entryIRI, boolean direct) {
        return queryMaker.getAncestors(entryIRI, direct);
    }

    public Set<IRI> getAncestors(OWLClassExpression entry, boolean direct) {
        return queryMaker.getAncestors(entry, direct);
    }

    public boolean isHybrid(IRI iri) {
        OWLDataFactory dataFactory = getDataFactory();
        return queryMaker.hasPropertyValue(dataFactory.getOWLClass(iri), dataFactory.getOWLObjectProperty(IS_HYBRID_OF_IRI),
                true, KINETIC_SIMULATION_ALGORITHM_IRI);
    }

    public Set<IRI> getHybridOf(IRI iri) {
        OWLDataFactory dataFactory = getDataFactory();
        return queryMaker.getPropertyValues(dataFactory.getOWLClass(iri),
                dataFactory.getOWLObjectProperty(IS_HYBRID_OF_IRI), true);
    }

    public boolean isComplex(IRI iri) {
        OWLDataFactory dataFactory = getDataFactory();
        return isHybrid(iri)
                || queryMaker.hasPropertyValue(dataFactory.getOWLClass(iri),
                dataFactory.getOWLObjectProperty(USES_IRI), true);
    }

    public Set<IRI> getUsedAlgorithms(IRI iri) {
        OWLDataFactory dataFactory = getDataFactory();
        Set<IRI> result = new HashSet<IRI>();
        result.addAll(queryMaker.getPropertyValues(dataFactory.getOWLClass(iri),
                dataFactory.getOWLObjectProperty(USES_IRI),
                true, KINETIC_SIMULATION_ALGORITHM_IRI));
        Set<IRI> hybridOf = getHybridOf(iri);
        if (hybridOf != null) {
            result.addAll(hybridOf);
        }
        return Collections.unmodifiableSet(result);
    }

    public Set<IRI> getParameters(IRI algorithmIri) {
        return getParameters(getDataFactory().getOWLClass(algorithmIri));
    }

    public Set<IRI> getParameters(OWLClassExpression algorithm) {
        return queryMaker.getPropertyValues(algorithm, getDataFactory().getOWLObjectProperty(HAS_PARAMETER_IRI), true);
    }

    public Set<IRI> getParametersByCharacteristic(boolean positive, IRI... characteristicIRI) {
        return getParametersByAncestorAndCharacteristic(KINETIC_SIMULATION_ALGORITHM_IRI, positive, characteristicIRI);
    }

    public Set<IRI> getParametersByAncestorAndCharacteristic(IRI ancestor, boolean positive, IRI... characteristicIRI) {
        return getParametersByAncestorAndCharacteristic(getDataFactory().getOWLClass(ancestor), positive, characteristicIRI);
    }

    public Set<IRI> getParametersByAncestorAndCharacteristic(OWLClassExpression ancestor, boolean positive, IRI... characteristicIRI) {
        OWLClassExpression algorithm = queryMaker.getPropertySubjectExpression(ancestor,
                getDataFactory().getOWLObjectProperty(HAS_CHARACTERISTIC_IRI),
                positive, characteristicIRI);
        return getParameters(algorithm);
    }

    public boolean hasParameter(OWLClassExpression algorithmExpression, final IRI... parameterIRI) {
        return queryMaker.hasPropertyValue(algorithmExpression,
                getDataFactory().getOWLObjectProperty(HAS_PARAMETER_IRI), true, parameterIRI);
    }

    public boolean hasParameter(IRI algorithmIRI, final IRI... parameterIRI) {
        OWLClass algorithm = getDataFactory().getOWLClass(algorithmIRI);
        return hasParameter(algorithm, parameterIRI);
    }

    public boolean hasParameter(boolean positive, IRI[] characteristicIRIs, IRI... parameterIRI) {
        assert characteristicIRIs != null;
        OWLClassExpression algorithm = queryMaker.getPropertySubjectExpression(
                getDataFactory().getOWLClass(KINETIC_SIMULATION_ALGORITHM_IRI),
                getDataFactory().getOWLObjectProperty(HAS_CHARACTERISTIC_IRI),
                positive, characteristicIRIs);
        return hasParameter(algorithm, parameterIRI);
    }

    public Class getParameterType(IRI parameterIri) {
        OWLDataFactory dataFactory = getDataFactory();
        Set<IRI> types = queryMaker.getPropertyValues(dataFactory.getOWLClass(parameterIri), dataFactory.getOWLDataProperty(HAS_TYPE), true);
        if (types == null || types.isEmpty()) return null;
        OWLDatatype type = dataFactory.getOWLDatatype(types.iterator().next());
        if (type == null) return null;
        if (type.isBoolean()) return Boolean.class;
        if (type.isDouble()) return Double.class;
        if (type.isFloat()) return Float.class;
        if (type.isInteger()) return Integer.class;
        return null;
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

    public Set<IRI> searchByName(String name) {
        return queryMaker.searchByName(name);
    }

    public IRI searchById(String name) {
        if (name == null || name.length() <= 0) {
            return null;
        }
        if (pattern.matcher(name).find()) {
            Matcher matcher = idPattern.matcher(name);
            matcher.find();
            return IRI.create(String.format("%sKISAO_%s", KISAO_PREFIX, matcher.group()));
        }
        return null;
    }

    public String getMiriamURI(IRI iri) {
        String id = iri.getFragment();
        return String.format("%s%s", KISAO_URN, id);
    }

    public String getId(IRI iri) {
        if (iri == null) return null;
        String fragment = iri.getFragment();
        if (fragment == null || !pattern.matcher(fragment).find()) {
            return null;
        }
        return fragment.replace("_", ":");
    }

    public OWLReasoner getReasoner() {
        return queryMaker.getReasoner();
    }

    public OWLDataFactory getDataFactory() {
        return queryMaker.getDataFactory();
    }

    public Set<IRI> getAlgorithmsWithSameCharacteristics(IRI algorithm, IRI... type) {
        OWLDataFactory dataFactory = getDataFactory();
        OWLClass ksa = dataFactory.getOWLClass(KINETIC_SIMULATION_ALGORITHM_IRI);
        Set<IRI> positive = getCharacteristics(algorithm, true, type);
        OWLObjectProperty hasCharacteristic = dataFactory.getOWLObjectProperty(HAS_CHARACTERISTIC_IRI);
        OWLClassExpression query = queryMaker.getPropertySubjectExpression(ksa, hasCharacteristic, true,
                positive.toArray(new IRI[positive.size()]));
        Set<IRI> negative = getCharacteristics(algorithm, false, type);
        query = queryMaker.getPropertySubjectExpression(query, hasCharacteristic, false,
                negative.toArray(new IRI[negative.size()]));
        Set<IRI> similar = getAlgorithmsByQuery(query);
        // remove self
        similar.remove(algorithm);
        return similar;
    }

    public List<IRI> getNMostSimilarAlgorithms(final IRI algorithm, int n, IRI... type) {
        Set<IRI> result = new HashSet<IRI>();
        result.addAll(getAlgorithmsWithSameCharacteristics(algorithm, type));
        List<IRI> resultList = new ArrayList<IRI>(result);
        final Set<IRI> characteristics = getCharacteristics(algorithm, true);
        final Set<IRI> negativeCharacteristics = getCharacteristics(algorithm, false);
        Collections.sort(resultList, new Comparator<IRI>() {

            public int compare(IRI o1, IRI o2) {
                double diff = distance(algorithm, o1) - distance(algorithm, o2);
                if (diff == 0) {
                    Set<IRI> firstPositive = new HashSet<IRI>(getCharacteristics(o1, true));
                    firstPositive.retainAll(characteristics);
                    Set<IRI> firstNegative = new HashSet<IRI>(getCharacteristics(o1, false));
                    firstNegative.retainAll(negativeCharacteristics);

                    Set<IRI> secondPositive = new HashSet<IRI>(getCharacteristics(o2, true));
                    secondPositive.retainAll(characteristics);
                    Set<IRI> secondNegative = new HashSet<IRI>(getCharacteristics(o2, false));
                    secondNegative.retainAll(negativeCharacteristics);
                    return firstPositive.size() + firstNegative.size() - (secondPositive.size() + secondNegative.size());
                }
                return diff > 0 ? 1 : -1;
            }
        });
        if (resultList.size() > n) resultList = resultList.subList(0, n);
        return resultList;
    }

    public double distance(IRI algorithm1, IRI algorithm2) {
        if (algorithm1.equals(algorithm2)) return 0;
        Set<IRI> firstAncestors = getAncestors(algorithm1, false);
        firstAncestors.add(algorithm1);
        int first = firstAncestors.size();
        Set<IRI> secondAncestors = getAncestors(algorithm2, false);
        secondAncestors.add(algorithm2);
        int second = secondAncestors.size();
        firstAncestors.retainAll(secondAncestors);
        int common = firstAncestors.size();
        return 1 - 2.0 * common / (first + second);
    }

    public Set<IRI> getComplexAlgorithms() {
        OWLDataFactory dataFactory = getDataFactory();
        return queryMaker.getPropertySubjects(dataFactory.getOWLObjectProperty(USES_IRI), true, KINETIC_SIMULATION_ALGORITHM_IRI);
    }
}
