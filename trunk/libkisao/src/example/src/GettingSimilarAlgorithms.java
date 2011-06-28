import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 28-Jun-2011
 *         Time: 15:24:06
 */
public class GettingSimilarAlgorithms {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://kisao.svn.sourceforge.net/viewvc/kisao/trunk/kisao-owl/kisao.owl).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // ... or use kisao.owl stored locally instead, by specifying IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///path-to-kisao.owl"));

        String name = "binomial tau-leaping method";
        // Get algorithm IRI by name
        IRI iri = kisaoQuery.getIRIByName(name);

        // Get algorithms with all the same characteristics
        // (system behaviour, type of variables, spatial description, method type, etc.)
        Set<IRI> similarIRIs = kisaoQuery.getAlgorithmsWithSameCharacteristics(iri);
        System.out.printf("%s is similar (has all the same characteristics) to %d methods in KiSAO:\n",
                name, similarIRIs.size());
        for (IRI similar : similarIRIs) {
            System.out.printf("  %s\n", kisaoQuery.getName(similar));
        }

        System.out.println("");

        // Get algorithms with the same system behaviour and type of variable characteristics
        IRI systemBehaviour = kisaoQuery.getIRIByName("system behaviour");
        IRI typeOfVariable = kisaoQuery.getIRIByName("type of variable");
        similarIRIs = kisaoQuery.getAlgorithmsWithSameCharacteristics(iri, systemBehaviour, typeOfVariable);
        System.out.printf("%s is similar to %d methods in KiSAO, concerning system behaviour and type of variables:\n",
                name, similarIRIs.size());
        for (IRI similar : similarIRIs) {
            System.out.printf("  %s\n", kisaoQuery.getName(similar));
        }

    }
}
