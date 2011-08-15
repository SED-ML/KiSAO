import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.KiSAOIRI;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Anna Zhukova
 *         Date: 28-Jun-2011
 *         Time: 15:08:44
 */
public class GettingAlgorithmCharacteristics {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://biomodels.net/kisao/KISAO).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // ... or use kisao.owl stored locally instead, by specifying IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///path-to-kisao.owl"));

        String name = "trapezoidal tau-leaping method";
        // Get algorithm IRI by name
        IRI iri = kisaoQuery.searchByName(name).iterator().next();
        System.out.printf("%s\n", name);
        
        // Get positive algorithm characteristics,
        // such as: 'has characteristic' some 'continuous variable'
        boolean hasCharacteristic = true;
        for (IRI characteristic : kisaoQuery.getCharacteristics(iri, hasCharacteristic)) {
            System.out.printf("  has characteristic %s\n", kisaoQuery.getName(characteristic));
        }
        // Get negative algorithm characteristics,
        // such as: not 'has characteristic' some 'spatial description'
        hasCharacteristic = false;
        for (IRI characteristic : kisaoQuery.getCharacteristics(iri, hasCharacteristic)) {
            System.out.printf("  hasn't characteristic %s\n", kisaoQuery.getName(characteristic));
        }
        
        // Check if algorithm is discrete stochastic
        hasCharacteristic = true;
        System.out.printf("\nThat's %s, that %s is discrete stochastic.\n", kisaoQuery.hasCharacteristic(iri, hasCharacteristic,
                KiSAOIRI.STOCHASTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI,
                KiSAOIRI.DISCRETE_VARIABLE_CHARACTERISTIC_IRI), name);

        // Check if algorithm is non-spatial
        hasCharacteristic = false;
        System.out.printf("\nThat's %s, that %s is non-spatial.\n", kisaoQuery.hasCharacteristic(iri, hasCharacteristic,
                KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI), name);
    }
}
