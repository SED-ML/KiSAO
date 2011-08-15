import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.KiSAOIRI;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * @author Anna Zhukova
 *         Date: 18-May-2011
 *         Time: 13:39:08
 */
public class ComplicatedQueries {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://biomodels.net/kisao/KISAO).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // ... or use kisao.owl stored locally instead, by specifying IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///path-to-kisao.owl"));
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

        System.out.println();
        
        // Check which parameters has algorithm, which is implicit, uses adaptive time-steps and is tau-leaping based?
        IRI tauIRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000039");
        for (IRI parameterIRI : kisaoQuery.getParametersByAncestorAndCharacteristic(tauIRI, true,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI)) {
            System.out.printf("Every implicit tau-leaping based algorithm uses parameter %s\n",
                    kisaoQuery.getName(parameterIRI));
        }
    }
}
