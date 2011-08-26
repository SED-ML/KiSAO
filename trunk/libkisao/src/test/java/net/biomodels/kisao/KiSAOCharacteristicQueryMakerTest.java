package net.biomodels.kisao;

import junit.framework.TestCase;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.net.URISyntaxException;
import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 16-May-2011
 *         Time: 17:03:59
 */
public class KiSAOCharacteristicQueryMakerTest extends TestCase {
    private static IRI poissonMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000040");
    private static IRI gfrdMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000058");
    private static IRI bklMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000051");
    private static IRI tauMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000039");
    private static IRI macCormackMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000308");

    IKiSAOQueryMaker me;

    public void setUp() throws OWLOntologyCreationException, URISyntaxException {
        IRI kisao = IRI.create(KiSAOQueryMakerTest.class.getClassLoader().getResource("./kisao.owl"));
        me = new KiSAOQueryMaker(kisao);
    }

    public void testAllCharacteristicsIsNotNull() {
        assertNotNull(me.getAllCharacteristics());
    }

    public void testAllCharacteristicsSize() {
        assertEquals(22, me.getAllCharacteristics().size());
    }

    public void testAllCharacteristicsContent() {
        assertTrue(me.getAllCharacteristics().contains(KiSAOIRI.DISCRETE_VARIABLE_CHARACTERISTIC_IRI));
    }

    public void testCharacteristicByAlgorithmIsNotNull() {
        assertNotNull(me.getCharacteristics(poissonMethod, true));
    }

    public void testCharacteristicByAlgorithmSize() {
        assertEquals(6, me.getCharacteristics(poissonMethod, true).size());
    }

    public void testCharacteristicByAlgorithmContent() {
        assertTrue(me.getCharacteristics(poissonMethod, true).contains(KiSAOIRI.DISCRETE_VARIABLE_CHARACTERISTIC_IRI));
    }

    public void testAlgorithmByCharacteristicIsNotNull() {
        assertNotNull(me.getAlgorithmsByCharacteristic(true, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI));
    }

    public void testAlgorithmByCharacteristicSize() {
        assertEquals(32, me.getAlgorithmsByCharacteristic(true, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI).size());
    }

    public void testAlgorithmByMultiCharacteristicSize() {
        assertEquals(16, me.getAlgorithmsByCharacteristic(true, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI,
                KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI).size());
    }

    public void testAlgorithmByCharacteristicContent() {
        assertTrue(me.getAlgorithmsByCharacteristic(true, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI).contains(gfrdMethod));
    }

    public void testAlgorithmByMultiCharacteristicContent() {
        assertTrue(me.getAlgorithmsByCharacteristic(true, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI,
                KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI).contains(macCormackMethod));
    }

    public void testNegativeCharacteristicByAlgorithmIsNotNull() {
        assertNotNull(me.getCharacteristics(bklMethod, false));
    }

    public void testNegativeCharacteristicByAlgorithmSize() {
        assertEquals(1, me.getCharacteristics(bklMethod, false).size());
    }

    public void testNegativeCharacteristicByAlgorithmContent() {
        assertTrue(me.getCharacteristics(bklMethod, false).contains(KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI));
    }

    public void testNegativeAlgorithmByCharacteristicIsNotNull() {
        assertNotNull(me.getAlgorithmsByCharacteristic(false, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI));
    }

    public void testNegativeAlgorithmByCharacteristicSize() {
        assertEquals(70, me.getAlgorithmsByCharacteristic(false, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI).size());
    }

    public void testNegativeAlgorithmByMultiCharacteristicSize() {
        assertEquals(0, me.getAlgorithmsByCharacteristic(false, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI,
                KiSAOIRI.STOCHASTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI).size());
    }

    public void testNegativeAlgorithmByCharacteristicContent() {
        assertTrue(me.getAlgorithmsByCharacteristic(false, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI).contains(bklMethod));
    }

    public void testIfAlgorithmHasPositiveCharacteristic() {
        assertTrue(me.hasCharacteristic(tauMethod, true, KiSAOIRI.STOCHASTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI));
    }

    public void testIfAlgorithmHasPositiveCharacteristicFalse() {
        assertFalse(me.hasCharacteristic(tauMethod, true, KiSAOIRI.CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI));
    }

    public void testIfAlgorithmHasMultiPositiveCharacteristic() {
        assertTrue(me.hasCharacteristic(tauMethod, true, KiSAOIRI.STOCHASTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI,
                KiSAOIRI.DISCRETE_VARIABLE_CHARACTERISTIC_IRI));
    }

    public void testIfAlgorithmHasMultiPositiveCharacteristicFalse() {
        assertFalse(me.hasCharacteristic(tauMethod, true, KiSAOIRI.CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI,
                KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI));
    }

    public void testIfAlgorithmHasMultiPositiveCharacteristicFalse2() {
        assertFalse(me.hasCharacteristic(tauMethod, true, KiSAOIRI.CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI,
                KiSAOIRI.STOCHASTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI));
    }

    public void testIfAlgorithmHasNegativeCharacteristic() {
        assertTrue(me.hasCharacteristic(bklMethod, false, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI));
    }

    public void testIfAlgorithmHasMultiNegativeCharacteristicFalse() {
        assertFalse(me.hasCharacteristic(bklMethod, false, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI,
                KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI));
    }

    public void testIfAlgorithmHasMultiNegativeCharacteristicFalse2() {
        assertFalse(me.hasCharacteristic(bklMethod, false, KiSAOIRI.DISCRETE_VARIABLE_CHARACTERISTIC_IRI,
                KiSAOIRI.DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI));
    }

    public void testIfAlgorithmHasNegativeCharacteristicFalse() {
        assertFalse(me.hasCharacteristic(gfrdMethod, false, KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI));
    }

    public void testGetTypeOfCharacteristic() {
        assertEquals(KiSAOType.CHARACTERISTIC, me.getType(KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI));
    }

    public void testIsCharacteristic() {
        assertTrue(me.isCharacteristic(KiSAOIRI.SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI));
    }
}
