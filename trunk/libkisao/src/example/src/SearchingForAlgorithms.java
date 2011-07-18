import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

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
        IRI iri = kisaoQuery.getIRIByName("tau-leaping method");
        System.out.printf("tau-leaping method has IRI: %s\n", iri);
        // or by possible synonym...
        iri = kisaoQuery.getIRIByNameOrSynonym("tauL");
        System.out.printf("tauL has IRI: %s\n", iri);
        // ... or by MIRIAM URI ...
        iri = kisaoQuery.getIRIbyMiriamURIorId("urn:miriam:biomodels.kisao:KISAO_0000039");
        System.out.printf("urn:miriam:biomodels.kisao:KISAO_0000039 has IRI: %s\n", iri);
        // ... or by ID ...
        iri = kisaoQuery.getIRIbyMiriamURIorId("kisao:0000039");
        System.out.printf("kisao:0000039 has IRI: %s\n", iri);
    }
}
