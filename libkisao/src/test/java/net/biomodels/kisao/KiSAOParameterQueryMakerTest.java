package net.biomodels.kisao;

import junit.framework.TestCase;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
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
    private static final IRI stepSize = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000114");
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
        assertEquals(21, me.getAllParameters().size());
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
        assertEquals(3, me.getParameters(nonNegativePoissonMethod).size());
    }

    public void testHiddenParameterByAlgorithmSize() {
        assertEquals(1, me.getParameters(lsodaMethod).size());
    }

    public void testHasParameter() {
        assertTrue(me.hasParameter(lsodaMethod, stepSize));
    }

    public void testHasParameterFalse() {
        assertFalse(me.hasParameter(lsodaMethod, tauLeapEpsilon));
    }

    public void testHasMultiParameter() {
        assertTrue(me.hasParameter(nonNegativePoissonMethod, tauLeapEpsilon, criticalFiringThreshold));
    }

    public void testHasMultiParameterFalse() {
        assertFalse(me.hasParameter(lsodaMethod, tauLeapEpsilon, stepSize));
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

    public void testParameterByOWLClassNotNull() {
        OWLClassExpression query = getOWLClassExpression();
        assertNotNull(me.getParameters(query));
    }

    public void testParameterByOWLClassSize() {
        OWLClassExpression query = getOWLClassExpression();
        assertEquals(1, me.getParameters(query).size());
    }

    public void testParameterByOWLClassContent() {
        OWLClassExpression query = getOWLClassExpression();
        assertTrue(me.getParameters(query).contains(stepSize));
    }

    public void testClassExpressionHasParameter() {
        OWLClassExpression query = getOWLClassExpression();
        assertTrue(me.hasParameter(query, stepSize));
    }

    public void testClassExpressionHasParameterFalse() {
        OWLClassExpression query = getOWLClassExpression();
        assertFalse(me.hasParameter(query, tauLeapEpsilon));
    }

    public void testClassExpressionHasMultiParameterFalse() {
        OWLClassExpression query = getOWLClassExpression();
        assertFalse(me.hasParameter(query, tauLeapEpsilon, stepSize));
    }

    public void testClassExpressionHasMultiParameterFalse2() {
        OWLClassExpression query = getOWLClassExpression();
        assertFalse(me.hasParameter(query, tauLeapEpsilon, criticalFiringThreshold));
    }

    public void testHasParameterByCharacteristic() {
        assertTrue(me.hasParameter(true, new IRI[]{KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI}, stepSize));
    }

    public void testHasParameterByMultiCharacteristic() {
        assertTrue(me.hasParameter(true, new IRI[]{KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI,
                KiSAOIRI.CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI}, stepSize));
    }

    public void testHasParameterByCharacteristicFalse() {
        assertFalse(me.hasParameter(true, new IRI[]{KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI},
                tauLeapEpsilon));
    }

    public void testHasMultiParameterByCharacteristicFalse() {
        assertFalse(me.hasParameter(true, new IRI[]{KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI},
                tauLeapEpsilon, stepSize));
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
        assertEquals(1, me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI).size());
    }

    public void testHasParameterByCharacteristicAndAncestorContent() {
        assertTrue(me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI).contains(stepSize));
    }

    public void testHasMultiParameterByCharacteristicAndAncestorNotNull() {
        assertNotNull(me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.PROGRESSION_WITH_ADAPTIVE_TIME_STEP_CHARACTERISTIC_IRI,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI));
    }

    public void testHasMultiParameterByCharacteristicAndAncestorSize() {
        assertEquals(1, me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.PROGRESSION_WITH_ADAPTIVE_TIME_STEP_CHARACTERISTIC_IRI,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI).size());
    }

    public void testHasMultiParameterByCharacteristicAndAncestorContent() {
        assertTrue(me.getParametersByAncestorAndCharacteristic(rungeKuttaIri, true,
                KiSAOIRI.PROGRESSION_WITH_ADAPTIVE_TIME_STEP_CHARACTERISTIC_IRI,
                KiSAOIRI.IMPLICIT_METHOD_CHARACTERISTIC_IRI).contains(stepSize));
    }


    private OWLClassExpression getOWLClassExpression() {
        OWLDataFactory dataFactory = me.getDataFactory();
        return dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(KiSAOIRI.HAS_CHARACTERISTIC_IRI),
                dataFactory.getOWLClass(KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI));
    }
}
