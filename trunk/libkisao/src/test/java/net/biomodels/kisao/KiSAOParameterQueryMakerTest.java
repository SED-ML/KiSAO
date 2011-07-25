package net.biomodels.kisao;

import junit.framework.TestCase;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.net.URISyntaxException;

/**
 * @author Anna Zhukova
 *         Date: 16-May-2011
 *         Time: 17:02:56
 */
public class KiSAOParameterQueryMakerTest extends TestCase {
    private static final IRI nonNegativePoissonMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000084");
    private static final IRI lsodaMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000094");
    private static final IRI rungeKuttaIri = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000064");

    private static final IRI absoluteTolerance = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000211");
    private static final IRI tauLeapEpsilon = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000228");
    private static final IRI criticalFiringThreshold = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000249");

    IKiSAOQueryMaker me;

    public void setUp() throws OWLOntologyCreationException, URISyntaxException {
        IRI kisao = IRI.create(KiSAOQueryMakerTest.class.getClassLoader().getResource("./kisao.owl"));
        me = new KiSAOQueryMaker(kisao);
    }

    public void testAllParametersIsNotNull() {
        assertNotNull(me.getAllParameters());
    }

    public void testAllParametersSize() {
        assertEquals(24, me.getAllParameters().size());
    }

    public void testAllParametersContent() {
        assertTrue(me.getAllParameters().contains(absoluteTolerance));
    }

    public void testParameterTypeNotNull() {
        assertNotNull(me.getParameterType(absoluteTolerance));
    }

    public void testParameterTypeContent() {
        assertEquals(Double.class, me.getParameterType(absoluteTolerance));
    }

    public void testParameterByAlgorithmNotNull() {
        assertNotNull(me.getParameters(nonNegativePoissonMethod));
    }

    public void testParameterByAlgorithmSize() {
        assertEquals(2, me.getParameters(nonNegativePoissonMethod).size());
    }

    public void testHiddenParameterByAlgorithmSize() {
        assertEquals(0, me.getParameters(lsodaMethod).size());
    }

    public void testHasParameter() {
        assertTrue(me.hasParameter(nonNegativePoissonMethod, tauLeapEpsilon));
    }

    public void testHasParameterFalse() {
        assertFalse(me.hasParameter(lsodaMethod, tauLeapEpsilon));
    }

    public void testHasMultiParameter() {
        assertTrue(me.hasParameter(nonNegativePoissonMethod, tauLeapEpsilon, criticalFiringThreshold));
    }

    public void testHasMultiParameterFalse() {
        assertFalse(me.hasParameter(lsodaMethod, tauLeapEpsilon, absoluteTolerance));
    }

    public void testHasMultiParameterFalse2() {
        assertFalse(me.hasParameter(lsodaMethod, tauLeapEpsilon, criticalFiringThreshold));
    }

    public void testParameterByAlgorithmContent() {
        assertTrue(me.getParameters(nonNegativePoissonMethod).contains(tauLeapEpsilon));
    }

    public void testGetTypeOfParameter() {
        assertEquals(KiSAOType.PARAMETER, me.getType(tauLeapEpsilon));
    }

    public void testIsParameter() {
        assertTrue(me.isParameter(tauLeapEpsilon));
    }

    public void testHasParameterByCharacteristicFalse() {
        assertFalse(me.hasParameter(true, new IRI[]{KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI},
                tauLeapEpsilon));
    }

    public void testHasMultiParameterByCharacteristicFalse() {
        assertFalse(me.hasParameter(true, new IRI[]{KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI},
                tauLeapEpsilon, absoluteTolerance));
    }

    public void testHasMultiParameterByCharacteristicFalse2() {
        assertFalse(me.hasParameter(true, new IRI[]{KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI},
                tauLeapEpsilon, criticalFiringThreshold));
    }

    public void testHasParameterByCharacteristicAndAncestorNotNull() {
        assertNotNull(me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI));
    }

    public void testHasParameterByCharacteristicAndAncestorSize() {
        assertEquals(0, me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI).size());
    }

    public void testHasMultiParameterByCharacteristicAndAncestorNotNull() {
        assertNotNull(me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.PROGRESSION_WITH_ADAPTIVE_TIME_STEP_CHARACTERISTIC_IRI,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI));
    }

    public void testHasMultiParameterByCharacteristicAndAncestorSize() {
        assertEquals(0, me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.PROGRESSION_WITH_ADAPTIVE_TIME_STEP_CHARACTERISTIC_IRI,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI).size());
    }
}
