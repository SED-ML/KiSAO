import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.Collection;
import java.util.Map;

/**
 * @author Anna Zhukova
 *         Date: 28-Jun-2011
 *         Time: 14:18:00
 */
public class RetrievingAlgorithmInformation {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://biomodels.net/kisao/KISAO).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // ... or use kisao.owl stored locally instead, by specifying IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///path-to-kisao.owl"));

        IRI iri = kisaoQuery.searchById("KISAO_0000039");
        String id = kisaoQuery.getId(iri);
        IRI identifiersOrgURL = kisaoQuery.getIdentifiersOrgURL(iri);
        String name = kisaoQuery.getName(iri);
        Collection<String> synonymNames = kisaoQuery.getAllSynonyms(iri);
        String definition = kisaoQuery.getDefinition(iri);
        Map<String, String> linkMap = kisaoQuery.getLinks(iri);

        System.out.printf("%s (%s): %s\n", name, id, definition);
        System.out.printf("  has Identifiers.org URI %s\n", identifiersOrgURL);
        for (String synonym : synonymNames) {
            System.out.printf("  is also known as %s\n", synonym);
        }
        for (Map.Entry<String, String> link : linkMap.entrySet()) {
            System.out.printf("  is described in %s (%s)\n", link.getValue(), link.getKey());
        }

    }
}
