import net.biomodels.kisao.IKiSAOQueryMaker;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 28-Jun-2011
 *         Time: 15:19:16
 */
public class GettingAlgorithmParameters {

    public static void main(String[] args) throws OWLOntologyCreationException {
        // Create KiSAOQueryMaker instance, which uses last version of kisao.owl ontology
        // (URL: http://kisao.svn.sourceforge.net/viewvc/kisao/trunk/kisao-owl/kisao.owl).
        IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker();
        // ... or use kisao.owl stored locally instead, by specifying IRI constructor argument:
        // IKiSAOQueryMaker kisaoQuery = new KiSAOQueryMaker(IRI.create("file:///path-to-kisao.owl"));

        String name = "binomial tau-leaping method";
        // Get algorithm IRI by name
        IRI iri = kisaoQuery.getIRIByName(name);
        System.out.printf("%s\n", name);

        // Get algorithm parameters
        Set<IRI> parameterIRIs = kisaoQuery.getParameters(iri);
        for (IRI parameter : parameterIRIs) {
            // Get parameter name
            String parameterName = kisaoQuery.getName(parameter);
            // Get parameter type
            Class parameterType = kisaoQuery.getParameterType(parameter);
            System.out.printf(" has parameter %s of type %s\n", parameterName,
                    parameterType.getName());
        }
    }
}
