import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.KiSAOIRI;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.Map;

/**
 * Shows how KiSAO Query library might be used.
 *
 * @author Anna Zhukova
 *         Date: 18-May-2011
 *         Time: 13:39:08
 */
public class Main {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://kisao.svn.sourceforge.net/viewvc/kisao/trunk/kisao-owl/kisao.owl).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // To use kisao.owl, stored locally, instead, one should specify IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///..."));

        // Get KiSAO element IRI by name...
        IRI iri = kisaoQuery.getIRIByName("tau-leaping method");
        // ... or by MIRIAM URN ...
        assert iri.equals(kisaoQuery.getIRIbyURN("urn:miriam:biomodels.kisao:KISAO_0000039"));
        // ... or by ID ...
        assert iri.equals(kisaoQuery.getIRIbyURN("kisao:0000039"));

        // Check if it's an algorithm
        assert kisaoQuery.isAlgorithm(iri);
        // Get algorithm name
        String name = kisaoQuery.getName(iri);
        System.out.printf("%s (%s)\n", name, iri);
        // Get other names
        for (String synonym : kisaoQuery.getAllSynonyms(iri)) {
            System.out.printf(" is also known as %s\n", synonym);
        }
        // Get definition
        System.out.printf(" which is %s\n", kisaoQuery.getDefinition(iri));
        // Get references
        for (Map.Entry<String, String> entry : kisaoQuery.getLinks(iri).entrySet()) {
            System.out.printf(" (see also: %s -- %s)\n", entry.getKey(), entry.getValue());
        }

        System.out.println("");
        // Check if algorithm has characteristics discrete variable and stochastic behaviour
        assert kisaoQuery.hasCharacteristic(iri, true, KiSAOIRI.STOCHASTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI,
                KiSAOIRI.DISCRETE_VARIABLE_CHARACTERISTIC_IRI);
        // Get positive algorithm characteristics,
        // such as: 'has characteristic' some 'continuous variable'
        for (IRI characteristic : kisaoQuery.getCharacteristics(iri, true)) {
            System.out.printf(" has characteristic %s\n", kisaoQuery.getName(characteristic));
        }
        // Get negative algorithm characteristics,
        // such as: not 'has characteristic' some 'spatial description'
        for (IRI characteristic : kisaoQuery.getCharacteristics(iri, false)) {
            System.out.printf(" hasn't characteristic %s\n", kisaoQuery.getName(characteristic));
        }

        System.out.println("");
        // Get parameters
        for (IRI parameter : kisaoQuery.getParameters(iri)) {
            System.out.printf(" has parameter %s of type %s\n", kisaoQuery.getName(parameter),
                    kisaoQuery.getParameterType(parameter).getName());
        }
        // Get ancestors (true means that only direct ancestors are included)
        for (IRI ancestor : kisaoQuery.getAncestors(iri, true)) {
            System.out.printf(" is a %s\n", kisaoQuery.getName(ancestor));
        }

        System.out.println("");
        // Get descendants (false means that indirect descendants are also included)
        for (IRI descendant : kisaoQuery.getDescendants(iri, false)) {
            System.out.printf(" is specified in %s\n", kisaoQuery.getName(descendant));
        }

        System.out.println("\n");
        // Get all the algorithms using deterministic rules and continuous variables
        for (IRI algorithm : kisaoQuery.getAlgorithmsByCharacteristic(true,
                KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI, KiSAOIRI.CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI,
                KiSAOIRI.PROGRESSION_WITH_FIXED_TIME_STEP_CHARACTERISTIC_IRI)) {
            System.out.printf("%s is continuous deterministic and has fixed time steps\n", kisaoQuery.getName(algorithm));
        }

        System.out.println("\n");
        // Make our own query
        OWLReasoner reasoner = kisaoQuery.getReasoner();
        OWLDataFactory dataFactory = kisaoQuery.getDataFactory();
        System.out.printf("%s and %s are%s disjoint\n",
                kisaoQuery.getName(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_IRI),
                kisaoQuery.getName(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI),
                reasoner.isEntailed(dataFactory.getOWLDisjointClassesAxiom(
                        dataFactory.getOWLClass(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_IRI),
                        dataFactory.getOWLClass(KiSAOIRI.KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI))) ?
                        "" : " not");

        System.out.println("\n");
        //Check which parameters has every deterministic algorithm
        for (IRI parameterIRI : kisaoQuery.getParametersByCharacteristic(true, KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI)) {
            System.out.printf("every deterministic algorithm uses parameter %s\n",
                    kisaoQuery.getName(parameterIRI));
        }
    }
}
