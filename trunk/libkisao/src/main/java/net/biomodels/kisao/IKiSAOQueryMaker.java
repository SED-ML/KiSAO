package net.biomodels.kisao;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides methods to query KiSAO.
 * Example:
 * <pre>
 * {@code
 * // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
 * // (URL: http://biomodels.net/kisao/KISAO).
 * IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
 * // To use kisao.owl, stored locally, instead, one should specify IRI constructor argument:
 * // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///..."));
 *
 * // Get KiSAO element IRI by name...
 * IRI iri = kisaoQuery.searchByName("tau-leaping method").iterator().next();
 * // ... or by MIRIAM URN ...
 * assert iri.equals(kisaoQuery.searchById("urn:miriam:biomodels.kisao:KISAO_0000039"));
 * // ... or by ID ...
 * assert iri.equals(kisaoQuery.searchById("kisao:0000039"));
 *
 * // Check if it's an algorithm
 * assert kisaoQuery.isAlgorithm(iri);
 * // Get algorithm name
 * String name = kisaoQuery.getName(iri);
 * System.out.printf("%s (%s)\n", name, iri);
 * // Get other names
 * for (String synonym : kisaoQuery.getAllSynonyms(iri)) {
 *      System.out.printf(" is also known as %s\n", synonym);
 * }
 * // Get definition
 * System.out.printf(" which is %s\n", kisaoQuery.getDefinition(iri));
 * // Get references
 * for (Map.Entry<String, String> entry : kisaoQuery.getLinks(iri).entrySet()) {
 *      System.out.printf(" (see also: %s -- %s)\n", entry.getKey(), entry.getValue());
 * }
 *
 * System.out.println("");
 * // Check if algorithm has characteristics discrete variable and stochastic behaviour
 * assert kisaoQuery.hasCharacteristic(iri, true, KiSAOIRI.STOCHASTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI,
 *      KiSAOIRI.DISCRETE_VARIABLE_CHARACTERISTIC_IRI);
 * // Get positive algorithm characteristics,
 * // such as: 'has characteristic' some 'continuous variable'
 * for (IRI characteristic : kisaoQuery.getCharacteristics(iri, true)) {
 *      System.out.printf(" has characteristic %s\n", kisaoQuery.getName(characteristic));
 * }
 * // Get negative algorithm characteristics,
 * // such as: not 'has characteristic' some 'spatial description'
 * for (IRI characteristic : kisaoQuery.getCharacteristics(iri, false)) {
 *      System.out.printf(" hasn't characteristic %s\n", kisaoQuery.getName(characteristic));
 * }
 *
 * System.out.println("");
 * // Get parameters
 * for (IRI parameter : kisaoQuery.getParameters(iri)) {
 *      System.out.printf(" has parameter %s of type %s\n", kisaoQuery.getName(parameter),
 *          kisaoQuery.getParameterType(parameter).getName());
 * }
 * // Get ancestors (true means that only direct ancestors are included)
 * for (IRI ancestor : kisaoQuery.getAncestors(iri, true)) {
 *      System.out.printf(" is a %s\n", kisaoQuery.getName(ancestor));
 * }
 *
 * System.out.println("");
 * // Get descendants (false means that indirect descendants are also included)
 * for (IRI descendant : kisaoQuery.getDescendants(iri, false)) {
 *      System.out.printf(" is specified in %s\n", kisaoQuery.getName(descendant));
 * }
 *
 * System.out.println("\n");
 * // Get all the algorithms using deterministic rules and continuous variables
 * for (IRI algorithm : kisaoQuery.getAlgorithmsByCharacteristic(true,
 *          KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI, KiSAOIRI.CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI,
 *          KiSAOIRI.PROGRESSION_WITH_FIXED_TIME_STEP_CHARACTERISTIC_IRI)) {
 *      System.out.printf("%s is continuous deterministic and has fixed time steps\n", kisaoQuery.getName(algorithm));
 * }
 *
 * System.out.println("\n");
 * // Make our own query
 * OWLReasoner reasoner = kisaoQuery.getReasoner();
 * OWLDataFactory dataFactory = kisaoQuery.getDataFactory();
 * System.out.printf("%s and %s are%s disjoint\n",
 * kisaoQuery.getName(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_IRI),
 * kisaoQuery.getName(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI),
 * reasoner.isEntailed(dataFactory.getOWLDisjointClassesAxiom(
 *      dataFactory.getOWLClass(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_IRI),
 *      dataFactory.getOWLClass(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI))) ?
 *          "" : " not");
 *
 * System.out.println("\n");
 * //Check which parameters has every deterministic algorithm
 * for (IRI parameterIRI : kisaoQuery.getParametersByCharacteristic(true, KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI)) {
 *      System.out.printf("every deterministic algorithm uses parameter %s\n",
 *          kisaoQuery.getName(parameterIRI));
 * }
 * }
 * </pre>
 *
 * @author Anna Zhukova
 *         Date: 13-May-2011
 *         Time: 10:20:20
 */

public interface IKiSAOQueryMaker {

    /**
     * Provides a set of IRIs of simulation algorithms, stored in KiSAO.
     *
     * @return set of algorithm IRIs.
     */
    Set<IRI> getAllAlgorithms();

    /**
     * Provides a set of IRIs of simulation algorithm characteristics, stored in KiSAO.
     * Subsumption characteristics (e.g. 'type of time steps') are not included.
     *
     * @return set of algorithm characteristic IRIs.
     */
    Set<IRI> getAllCharacteristics();

    /**
     * Provides a set of IRIs of simulation algorithm parameters, stored in KiSAO.
     * Subsumption parameters (e.g. 'granularity control parameter') are not included.
     *
     * @return set of algorithm parameter IRIs.
     */
    Set<IRI> getAllParameters();

    /**
     * Returns name (rdfs:label) by given KiSAO IRI.
     *
     * @param iri KiSAO IRI.
     * @return name of the KiSAO entity.
     */
    String getName(IRI iri);

    /**
     * Returns definition (rdfs:comment) by given KiSAO IRI.
     *
     * @param iri KiSAO IRI.
     * @return definition of the KiSAO entity.
     */
    String getDefinition(IRI iri);

    /**
     * Returns whether the KiSAO entry with the given IRI is deprecated or not.
     *
     * @param iri KiSAO IRI.
     * @return true, if the KiSAO entry with the given IRI is deprecated.
     */
    boolean isDeprecated(IRI iri);

    /**
     * Returns synonyms for the KiSAO entry with the given IRI.
     *
     * @param iri KiSAO IRI.
     * @return set of synonyms.
     */
    Set<String> getAllSynonyms(IRI iri);

    /**
     * Returns synonyms of the specified type for the KiSAO entry with the given IRI.
     *
     * @param iri  KiSAO IRI.
     * @param type type of the synonyms to look for.
     * @return set of synonyms.
     * @see net.biomodels.kisao.SynonymType
     */
    Set<String> getSynonyms(IRI iri, SynonymType type);

    /**
     * Returns links to the articles/books, describing the KiSAO entry with the given IRI.
     *
     * @param iri KiSAO IRI.
     * @return map article-location (usually, a MIRIAM URN) -> article-name.
     */
    Map<String, String> getLinks(IRI iri);

    /**
     * Given a KiSAO IRI of a simulation algorithm,
     * returns a collection of IRIs of characteristics, it possess.
     *
     * @param algorithmIri KiSAO IRI of a simulation algorithm.
     * @param positive     false, if negated characteristics should be looked at,
     *                     for example 'KMC' not ('has characteristic' 'spatial description').
     * @param type         type (optional) IRIs of the characteristic categories (e.g. 'system behaviour') to be considered.
     *                     All the characteristics are considered if no type is specified.
     * @return set of IRIs of the algorithm characteristics.
     */
    Set<IRI> getCharacteristics(IRI algorithmIri, boolean positive, IRI... type);

    /**
     * Given the class expression, describing potential algorithm,
     * returns a collection of IRIs of characteristics, it possess.
     *
     * @param algorithm    OWLClassExpression describing algorithm.
     * @param positive     false, if negated characteristics should be looked at,
     *                     for example 'KMC' not ('has characteristic' 'spatial description').
     * @param type         type (optional) IRIs of the characteristic categories (e.g. 'system behaviour') to be considered.
     *             All the characteristics are considered if no type is specified.
     * @return set of IRIs of the algorithm characteristics.
     */
    Set<IRI> getCharacteristics(OWLClassExpression algorithm, boolean positive, IRI... type);

    /**
     * Given KiSAO IRIs of simulation algorithm characteristics,
     * returns a collection of IRIs of simulation algorithms, which have them.
     *
     * @param characteristicIri KiSAO IRIs of simulation algorithm characteristics.
     * @param positive          false, if negated characteristics should be looked at,
     *                          for example 'KMC' not ('has characteristic' 'spatial description').
     * @return set of IRIs of algorithms, which have the characteristic.
     */
    Set<IRI> getAlgorithmsByCharacteristic(boolean positive, IRI... characteristicIri);

    /**
     * Checks if the algorithm with the specified KiSAO IRI 'has characteristic' (or not 'has characteristic', if positive is false)
     * with the specified IRIs.
     * Example:
     * <pre>
     * {@code
     * IKiSAOQueryMaker query = ...
     * IRI lsoda = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000094");
     * // false specifies that we check a negated axiom: "LSODA not ('has characteristic' some 'spatial description')"
     * assert true ==  query.hasCharacteristic(lsoda, KiSAO.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI, false);
     * // true specifies that we check a normal axiom: "LSODA 'has characteristic' some 'continuous variable'"
     * assert true ==  query.hasCharacteristic(lsoda, KiSAO.CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI, true);
     * }
     * </pre>
     *
     * @param algorithmIRI      KiSAO IRI of the simulation algorithm.
     * @param characteristicIRI KiSAO IRIs of the simulation algorithm characteristics.
     * @param positive          false, if negated characteristics should be looked at.
     * @return true, if the algorithms with the specified KiSAO IRI 'has characteristic' (or not 'has characteristic', if positive is false)
     *         with the specified IRI.
     */
    boolean hasCharacteristic(IRI algorithmIRI, boolean positive, IRI... characteristicIRI);

    /**
     * Returns a set of parameters that are used by the specified algorithm.
     *
     * @param algorithm KiSAO IRI of the algorithm.
     * @return set of parameter KiSAO IRIs.
     */
    Set<IRI> getParameters(IRI algorithm);

    /**
     * Returns a set of parameters that are used by the class expression, describing potential algorithm.
     * Example:
     * <pre>
     * {@code
     * OWLDataFactory dataFactory = kisaoQueryMaker.getDataFactory();
     * OWLClassExpression algorithm = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(HAS_CHARACTERISTIC_IRI),
     * dataFactory.getOWLClass(DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI));
     * // parameters of methods with deterministic behaviour
     * for (IRI parameter : kisaoQueryMaker.getParameters(algorithm)) {
     * System.out.println(kisaoQueryMaker.getName(parameter));
     * }
     * }
     * </pre>
     *
     * @param algorithm OWLClassExpression describing potential algorithm.
     * @return set of parameter KiSAO IRIs.
     */
    Set<IRI> getParameters(OWLClassExpression algorithm);

    /**
     * Returns a set of parameters that are used by the algorithm, which have specified characteristic(s).
     *
     * @param characteristicIRI characteristic KiSAO IRIs.
     * @param positive          false, if negated characteristics should be looked at.
     * @return set of parameter KiSAO IRIs.
     */
    Set<IRI> getParametersByCharacteristic(boolean positive, IRI... characteristicIRI);

    /**
     * Returns a set of parameters that are used by the algorithm, which have specified characteristic(s)
     * and is descendant of the specified algorithm.
     *
     * @param ancestor          ancestor algorithm KiSAO IRI.
     * @param characteristicIRI characteristic KiSAO IRIs.
     * @param positive          false, if negated characteristics should be looked at.
     * @return set of parameter KiSAO IRIs.
     */
    Set<IRI> getParametersByAncestorAndCharacteristic(IRI ancestor, boolean positive, IRI... characteristicIRI);

    /**
     * Returns a set of parameters that are used by the algorithm, which have specified characteristic(s)
     * and is descendant of the specified algorithm.
     *
     * @param ancestor          OWLClassExpression describing ancestor algorithm.
     * @param characteristicIRI characteristic KiSAO IRIs.
     * @param positive          false, if negated characteristics should be looked at.
     * @return set of parameter KiSAO IRIs.
     */
    Set<IRI> getParametersByAncestorAndCharacteristic(OWLClassExpression ancestor, boolean positive, IRI... characteristicIRI);

    /**
     * Checks if the algorithm with the specified KiSAO IRI 'has parameter' with the specified IRIs.
     * Example:
     * <pre>
     * {@code
     * IKiSAOQueryMaker kisaoQueryMaker = ...
     * IRI lsoda = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000094");
     * IRI stepSize = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000114");
     * assert true ==  kisaoQueryMaker.hasParameter(lsoda, stepSize);
     * }
     * </pre>
     *
     * @param algorithmIRI KiSAO IRI of the simulation algorithm.
     * @param parameterIRI KiSAO IRIs of the simulation algorithm parameters.
     * @return true, if the algorithms with the specified KiSAO IRI 'has parameter' with the specified IRI.
     */
    boolean hasParameter(IRI algorithmIRI, IRI... parameterIRI);

    /**
     * Checks if the algorithm, described by given OWLClassExpression, 'has parameter' with the specified IRIs.
     * Example:
     * <pre>
     * {@code
     * IKiSAOQueryMaker kisaoQueryMaker = ...
     * OWLDataFactory dataFactory = kisaoQueryMaker.getDataFactory();
     * OWLClassExpression algorithm = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(HAS_CHARACTERISTIC_IRI),
     * dataFactory.getOWLClass(DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI));
     * IRI stepSize = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000114");
     * assert true ==  kisaoQueryMaker.hasParameter(algorithm, stepSize);
     * }
     * </pre>
     *
     * @param algorithm    OWLClassExpression describing algorithm.
     * @param parameterIRI KiSAO IRIs of the simulation algorithm parameters.
     * @return true, if the algorithms, described by given OWLClassExpression, 'has parameter' with the specified IRI.
     */
    boolean hasParameter(OWLClassExpression algorithm, IRI... parameterIRI);

    /**
     * Checks if the algorithm, described by its characteristics, 'has parameter' with the specified IRIs.
     *
     * @param characteristicIRIs KiSAO IRIs of the characteristics.
     * @param parameterIRI       KiSAO IRIs of the simulation algorithm parameters.
     * @param positive           false, if negated characteristics should be looked at.
     * @return true, if the algorithms, described by given characteristics, 'has parameter' with the specified IRI.
     */
    boolean hasParameter(boolean positive, IRI[] characteristicIRIs, IRI... parameterIRI);

    /**
     * Given the KiSAO IRI of a parameter, returns parameter type, e.g. Double for 'absolute tolerance'.
     *
     * @param parameter KiSAO IRI of the parameter.
     * @return Class specifying the parameter type.
     */
    Class getParameterType(IRI parameter);

    /**
     * Returns type (algorithm, characteristic or parameter) of the KiSAO element with the given IRI.
     *
     * @param iri KiSAO IRI.
     * @return KiSAOType
     */
    KiSAOType getType(IRI iri);

    /**
     * Checks if the element with the specified IRI represents simulation algorithm in KiSAO.
     *
     * @param iri KiSAO IRI.
     * @return true, if the element with the specified IRI represents simulation algorithm in KiSAO.
     */
    boolean isAlgorithm(IRI iri);

    /**
     * Checks if the element with the specified IRI represents
     * simulation algorithm characteristic in KiSAO.
     *
     * @param iri KiSAO IRI.
     * @return true, if the element with the specified IRI represents
     *         simulation algorithm characteristic in KiSAO.
     */
    boolean isCharacteristic(IRI iri);

    /**
     * Checks if the element with the specified IRI represents
     * simulation algorithm parameter in KiSAO.
     *
     * @param iri KiSAO IRI.
     * @return true, if the element with the specified IRI represents
     *         simulation algorithm parameter in KiSAO.
     */
    boolean isParameter(IRI iri);

    /**
     * Looks for a KiSAO entry with the specified name or synonym.
     *
     * @param name String representing name or synonym.
     * @return set of KiSAO IRI.
     */
    Set<IRI> searchByName(String name);

    /**
     * Looks for a KiSAO entries matching specified query.
     *
     * @param query String containing entry identifier (e.g. kisao:0000001)
     *        or MIRIAM URI (e.g. urn:miriam:biomodels.kisao:KISAO_0000001)
     *        or URI (e.g. "http://www.biomodels.net/kisao/KISAO#KISAO_0000259").
     * @return KiSAO IRI.
     */
    IRI searchById(String query);

    /**
     * Returns a set of descendant IRIs for the specified entry.
     *
     * @param entryIRI KiSAO IRI.
     * @param direct   Whether should look only for direct descendants.
     * @return set of descendant IRIs.
     */
    Set<IRI> getDescendants(IRI entryIRI, boolean direct);

    /**
     * Returns a set of descendant IRIs for the specified entry.
     *
     * @param entry  OWLClassExpression describing ancestor entry.
     * @param direct Whether should look only for direct descendants.
     * @return set of descendant IRIs.
     */
    Set<IRI> getDescendants(OWLClassExpression entry, boolean direct);

    /**
     * Returns a set of ancestor IRIs for the specified entry.
     *
     * @param entryIRI KiSAO IRI.
     * @param direct   Whether should look only for direct ancestors.
     * @return set of ancestor IRIs.
     */
    Set<IRI> getAncestors(IRI entryIRI, boolean direct);

    /**
     * Returns a set of ancestor IRIs for the specified entry.
     *
     * @param entry  OWLClassExpression describing descendant entry.
     * @param direct Whether should look only for direct ancestors.
     * @return set of ancestor IRIs.
     */
    Set<IRI> getAncestors(OWLClassExpression entry, boolean direct);

    /**
     * Checks if a descendantCandidate entry is a descendant (concerning rdf:subClassOf relationship)
     * of the ancestorCandidate entry in KiSAO.
     *
     * @param descendantCandidate KiSAO IRI of the possible descendant.
     * @param ancestorCandidate   KiSAO IRI of the possible ancestor.
     * @return true, if the descendantCandidate entry is a descendant (concerning rdf:subClassOf relationship)
     *         of the ancestorCandidate entry.
     */
    boolean isA(IRI descendantCandidate, IRI ancestorCandidate);

    /**
     * Checks if a descendantCandidate entry is a descendant (concerning rdf:subClassOf relationship)
     * of the ancestorCandidate entry in KiSAO.
     *
     * @param descendantCandidate OWLClassExpression for descendant.
     * @param ancestorCandidate   OWLClassExpression for ancestor.
     * @return true, if the descendantCandidate entry is a descendant (concerning rdf:subClassOf relationship)
     *         of the ancestorCandidate entry.
     */
    boolean isA(OWLClassExpression descendantCandidate, OWLClassExpression ancestorCandidate);

    /**
     * Returns miriam urn (urn:miriam:biomodels.kisao:KISAO_XXXXXXX) by KiSAO IRI.
     *
     * @param iri KiSAO IRI.
     * @return string, representing miriam urn (urn:miriam:biomodels.kisao:KISAO_XXXXXXX)
     */
    String getMiriamURN(IRI iri);

    /**
     * Returns identifiers.org URL (http://identifiers.org/biomodels.kisao/KISAO_XXXXXXX) by KiSAO IRI.
     *
     * @param iri KiSAO IRI.
     * @return IRI, representing identifiers.org URL (http://identifiers.org/biomodels.kisao/KISAO_XXXXXXX)
     */
    IRI getIdentifiersOrgURL(IRI iri);

    /**
     * Returns id (kisao:XXXXXXX) by KiSAO IRI.
     *
     * @param iri KiSAO IRI.
     * @return string, representing id (kisao:XXXXXXX)
     */
    String getId(IRI iri);

    /**
     * Checks whether the algorithm with the specified IRI is a hybrid one.
     *
     * @param iri KiSAO IRI.
     * @return if algorithm with the specified IRI is a hybrid one.
     */
    boolean isHybrid(IRI iri);

    /**
     * Returns IRIs of the algorithms, the specified one is a hybrid of.
     *
     * @param iri KiSAO IRI of the hybrid algorithm.
     * @return set of IRIs of the algorithms, the specified one is a hybrid of or null if it's not hybrid.
     */
    Set<IRI> getHybridOf(IRI iri);

    /**
     * Checks whether the algorithm with the specified IRI is a complex one:
     * is a hybrid one of uses other algorithms.
     *
     * @param iri KiSAO IRI.
     * @return if algorithm with the specified IRI is a complex one.
     */
    boolean isComplex(IRI iri);

    /**
     * Returns IRIs of the algorithms, the specified one uses.
     *
     * @param iri KiSAO IRI of the complex algorithm.
     * @return set of IRIs of the algorithms, the specified one uses.
     */
    Set<IRI> getUsedAlgorithms(IRI iri);

    /**
     * Returns IRIs of the algorithms, which use other algorithms.
     *
     * @return set of IRIs of complex algorithms.
     */
    Set<IRI> getComplexAlgorithms();

    /**
     * Returns reasoner.
     *
     * @return OWLReasoner.
     */
    OWLReasoner getReasoner();

    /**
     * Returns data factory.
     *
     * @return OWLDataFactory.
     */
    OWLDataFactory getDataFactory();

    /**
     * Returns algorithms having the same characteristics as the specified one.
     * @param algorithm The sample algorithm IRI.
     * @param type (optional) IRIs of the characteristic categories (e.g. 'system behaviour') to be considered.
     *             All the characteristics are considered if no type is specified.
     * @return set of algorithm IRIs.
     */
    Set<IRI> getAlgorithmsWithSameCharacteristics(IRI algorithm, IRI... type);

    /**
     * Returns algorithms having the same characteristics as the specified one.
     * @param algorithm The sample algorithm IRI.
     * @param n The number of algorithms to return. n &lt; 0 is considered as infinity.
     * @param type (optional) IRIs of the characteristic categories (e.g. 'system behaviour') to be considered.
     *             All the characteristics are considered if no type is specified.
     * @return sorted (by similarity) list of n (or less if n were not found) IRIs of the algorithms most similar to the given one.
     */
    List<IRI> getNMostSimilarAlgorithms(IRI algorithm, int n, IRI... type);

    /**
     * Distance between two algorithms in the hierarchy tree.
     * @param algorithm1 IRI of the first algorithm.
     * @param algorithm2 IRI of the second algorithm.
     * @return distance value which is not less that 0 and not greater than 1.
     */
    double distance(IRI algorithm1, IRI algorithm2);

    /**
     * Returns algorithms described by the specified query.
     * @param query OWLClassExpression describing algorithm.
     * @return set of algorithm IRIS.
     */
    Set<IRI> getAlgorithmsByQuery(OWLClassExpression query);

    /**
     * Checks whether the algorithm with the specified IRI is an organisational one:
     * not a concrete algorithm, but a subsumption.
     *
     * @param iri KiSAO IRI.
     * @return if algorithm with the specified IRI is an organisational one.
     */
    boolean isOrganisational(IRI iri);
}
