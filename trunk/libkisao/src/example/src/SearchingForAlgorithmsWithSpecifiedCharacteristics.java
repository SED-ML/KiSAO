import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.KiSAOIRI;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 28-Jun-2011
 *         Time: 14:29:01
 */
public class SearchingForAlgorithmsWithSpecifiedCharacteristics {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://kisao.svn.sourceforge.net/viewvc/kisao/trunk/kisao-owl/kisao.owl).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // ... or use kisao.owl stored locally instead, by specifying IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///path-to-kisao.owl"));

        // Get all the algorithms using deterministic rules and continuous variables
        boolean hasCharacteristic = true;
        Set<IRI> continuousDeterministic = kisaoQuery.getAlgorithmsByCharacteristic(hasCharacteristic,
                KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI,
                KiSAOIRI.CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI);
        System.out.printf("There are %d continuous deterministic algorithms in KiSAO:\n", continuousDeterministic.size());
        for (IRI algorithm : continuousDeterministic) {
            System.out.printf("  %s\n", kisaoQuery.getName(algorithm));
        }

        // Get all the algorithms using non-spatial description
        hasCharacteristic = false;
        Set<IRI> nonSpatial = kisaoQuery.getAlgorithmsByCharacteristic(hasCharacteristic,
                KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI);
        System.out.printf("\nThere are %d non-spatial algorithms in KiSAO:\n", nonSpatial.size());
        for (IRI algorithm : nonSpatial) {
            System.out.printf("  %s\n", kisaoQuery.getName(algorithm));
        }

        // Get all non-spatial continuous deterministic algorithms
        Set<IRI> nonSpatialContinuousDeterministic = new HashSet<IRI>(continuousDeterministic);
        nonSpatialContinuousDeterministic.retainAll(nonSpatial);
        System.out.printf("\nThere are %d non-spatial continuous deterministic algorithms in KiSAO:\n",
                nonSpatialContinuousDeterministic.size());
        for (IRI algorithm : nonSpatialContinuousDeterministic) {
            System.out.printf("  %s\n", kisaoQuery.getName(algorithm));
        }
    }
}
