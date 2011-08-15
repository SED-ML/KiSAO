import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.Collection;

/**
 * @author Anna Zhukova
 *         Date: 28-Jun-2011
 *         Time: 13:38:45
 */
public class BrowsingAlgorithmHierarchy {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://biomodels.net/kisao/KISAO).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // ... or use kisao.owl stored locally instead, by specifying IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///path-to-kisao.owl"));

        // List simulation algorithms, stored in KiSAO
        for (IRI iri : kisaoQuery.getAllAlgorithms()) {
            String id = kisaoQuery.getId(iri);
            String name = kisaoQuery.getName(iri);
            boolean directOnly = true;
            Collection<IRI> ancestorIRIs = kisaoQuery.getAncestors(iri, directOnly);
            Collection<IRI> descendantIRIs = kisaoQuery.getDescendants(iri, directOnly);

            System.out.printf("%s (%s)\n", name, id);
            for (IRI ancestor : ancestorIRIs) {
                System.out.printf("  specifies %s\n", kisaoQuery.getName(ancestor));
            }
            for (IRI descendant : descendantIRIs) {
                System.out.printf("  is specified in %s\n", kisaoQuery.getName(descendant));
            }
            System.out.println("");
        }

        // Check if there is a descendant-ancestor relationship between algorithms
        IRI tauLeap = kisaoQuery.searchByName("tau-leaping method").iterator().next();
        IRI monteCarlo = kisaoQuery.searchByName("Monte Carlo method").iterator().next();
        System.out.printf("\nIt is %s, that tau-leaping method is Monte Carlo based.\n",
            kisaoQuery.isA(tauLeap, monteCarlo));
    }
}
