import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 28-Jun-2011
 *         Time: 14:11:32
 */
public class SearchingForAlgorithms {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://biomodels.net/kisao/KISAO).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // ... or use kisao.owl stored locally instead, by specifying IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///path-to-kisao.owl"));

        // Get KiSAO element IRI by name...
        Set<IRI> iriSet = kisaoQuery.searchByName("tau-leaping method");
        System.out.printf("tau-leaping method has IRI: %s\n", iriSet);
        // or by possible synonym...
        iriSet = kisaoQuery.searchByName("tauL");
        System.out.printf("tauL has IRI: %s\n", iriSet);
        // ... or by MIRIAM URI ...
        IRI iri = kisaoQuery.searchById("urn:miriam:biomodels.kisao:KISAO_0000039");
        System.out.printf("urn:miriam:biomodels.kisao:KISAO_0000039 has IRI: %s\n", iri);
        // ... or by ID ...
        iri = kisaoQuery.searchById("kisao:0000039");
        System.out.printf("kisao:0000039 has IRI: %s\n", iri);
    }
}
