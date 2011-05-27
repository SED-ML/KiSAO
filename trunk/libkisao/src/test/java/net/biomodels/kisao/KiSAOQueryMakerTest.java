package net.biomodels.kisao;

import junit.framework.TestCase;
import net.biomodels.kisao.impl.KiSAOQueryMaker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.net.URISyntaxException;

/**
 * @author zhutchok
 *         Date: 12-May-2011
 *         Time: 17:28:51
 */
public class KiSAOQueryMakerTest extends TestCase {
    private static IRI poissonMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000040");
    private static IRI gfrdMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000058");
    //private static IRI pdeMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000024");
    private static IRI tauMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000039");
    private static IRI lsoda = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000088");
    private static IRI eulerMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000261");
    private static IRI forwardEulerMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000030");
    private static IRI linearMultiStepMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000281");
    private static IRI pahleMethod = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000231");

    IKiSAOQueryMaker me;

    public void setUp() throws OWLOntologyCreationException, URISyntaxException {
        IRI kisao = IRI.create(KiSAOQueryMakerTest.class.getClassLoader().getResource("./kisao.owl"));
        me = new KiSAOQueryMaker(kisao);
    }

    public void testAllAlgorithmsIsNotNull() {
        assertNotNull(me.getAllAlgorithms());
    }

    public void testAllAlgorithmsSize() {
        assertEquals(84, me.getAllAlgorithms().size());
    }

    public void testAllAlgorithmsContent() {
        assertTrue(me.getAllAlgorithms().contains(poissonMethod));
    }

    public void testName() {
        assertEquals("GFRD", me.getName(gfrdMethod));
    }

    public void testDefinition() {
        assertEquals("Method that simulates biochemical networks on particle level. " +
                "It considers both, changes in time and space by " +
                "''exploiting both the exact solution of the Smoluchowski Equation to set up an event-driven algorithm''" +
                " which allows for large jumps in time when the considered particles are far away from each other" +
                " [in space] and thus cannot react. GFRD combines the propagation of particles in space with the reactions" +
                " taking place between them in one simulation step.", me.getDefinition(gfrdMethod));
    }

    public void testLinksNotNull() {
        assertNotNull(me.getLinks(gfrdMethod));
    }

    public void testLinkSize() {
        assertEquals(1, me.getLinks(gfrdMethod).size());
    }

    public void testLinkContentUrn() {
        assertEquals("urn:miriam:doi:10.1063/1.2137716",
                me.getLinks(gfrdMethod).keySet().iterator().next());
    }

    public void testLinkContentDescription() {
        assertEquals("Van Zon JS, Ten Wolde PR. Green's-function reaction dynamics: " +
                "A particle-based approach for simulating biochemical networks in time and space." +
                " Journal of Chemical Physics, Volume 123 (23) (2005).",
                me.getLinks(gfrdMethod).values().iterator().next());
    }

    public void testSynonymNotNull() {
        assertNotNull(me.getAllSynonyms(gfrdMethod));
    }

    public void testSynonymSize() {
        assertEquals(1, me.getAllSynonyms(gfrdMethod).size());
    }

    public void testSynonymContent() {
        assertEquals("Green's function reaction dynamics",
                me.getAllSynonyms(gfrdMethod).iterator().next());
    }

    public void testExactSynonymNotNull() {
        assertNotNull(me.getSynonyms(gfrdMethod, SynonymType.EXACT));
    }

    public void testExactSynonymSize() {
        assertEquals(1, me.getSynonyms(gfrdMethod, SynonymType.EXACT).size());
    }

    public void testExactSynonymContent() {
        assertEquals("Green's function reaction dynamics",
                me.getSynonyms(gfrdMethod, SynonymType.EXACT).iterator().next());
    }

    public void testDeprecatedFalse() {
        assertFalse(me.isDeprecated(gfrdMethod));
    }

    public void testIsATrue() {
        assertTrue(me.isA(poissonMethod, tauMethod));
    }

    public void testIsAFalse() {
        assertFalse(me.isA(tauMethod, poissonMethod));
    }

    public void testDirectDescendantsNotNull() {
        assertNotNull(me.getDescendants(linearMultiStepMethod, true));
    }

    public void testDirectDescendantsSize() {
        assertEquals(3, me.getDescendants(linearMultiStepMethod, true).size());
    }

    public void testDirectDescendantsContent() {
        assertTrue(me.getDescendants(linearMultiStepMethod, true).contains(eulerMethod));
    }

    public void testDescendantsNotNull() {
        assertNotNull(me.getDescendants(linearMultiStepMethod, false));
    }

    public void testDescendantsSize() {
        assertEquals(7, me.getDescendants(linearMultiStepMethod, false).size());
    }

    public void testDescendantsContent() {
        assertTrue(me.getDescendants(linearMultiStepMethod, false).contains(forwardEulerMethod));
    }

    public void testDirectAncestorsNotNull() {
        assertNotNull(me.getAncestors(forwardEulerMethod, true));
    }

    public void testDirectAncestorsSize() {
        assertEquals(1, me.getAncestors(forwardEulerMethod, true).size());
    }

    public void testDirectAncestorsContent() {
        assertTrue(me.getAncestors(forwardEulerMethod, true).contains(eulerMethod));
    }

    public void testAncestorsNotNull() {
        assertNotNull(me.getAncestors(forwardEulerMethod, false));
    }

    public void testAncestorsSize() {
        assertEquals(3, me.getAncestors(forwardEulerMethod, false).size());
    }

    public void testAncestorsContent() {
        assertTrue(me.getAncestors(forwardEulerMethod, false).contains(linearMultiStepMethod));
    }

    public void testGetTypeOfAlgorithm() {
        assertEquals(KiSAOType.ALGORITHM, me.getType(forwardEulerMethod));
    }

    public void testGetTypeOfOther() {
        assertEquals(KiSAOType.OTHER, me.getType(KiSAOIRI.HAS_CHARACTERISTIC_IRI));
    }

    public void testIsAlgorithm() {
        assertTrue(me.isAlgorithm(forwardEulerMethod));
    }

    public void testGetByName() {
        assertEquals(forwardEulerMethod, me.getIRIByName("Euler forward method"));
    }

    public void testGetByNameOrSynonym() {
        assertEquals(gfrdMethod, me.getIRIByNameOrSynonym("greens function reaction dynamics"));
    }

    public void testUrnByIRI() {
        assertEquals("urn:miriam:biomodels.kisao:KISAO_0000245", me.getURNByIRI(KiSAOIRI.HAS_CHARACTERISTIC_IRI));
    }

    public void testIRIByURN() {
        assertEquals(KiSAOIRI.HAS_CHARACTERISTIC_IRI, me.getIRIbyURN("urn:miriam:biomodels.kisao:KISAO_0000245"));
    }

    public void testIRIById() {
        assertEquals(KiSAOIRI.HAS_CHARACTERISTIC_IRI, me.getIRIbyURN("kisao:KISAO_0000245"));
    }

    public void testIRIById2() {
        assertEquals(KiSAOIRI.HAS_CHARACTERISTIC_IRI, me.getIRIbyURN("kisao:0000245"));
    }

    public void testIsNotHybrid() {
        assertFalse(me.isHybrid(gfrdMethod));
    }

    public void testIsHybrid() {
        assertTrue(me.isHybrid(pahleMethod));
    }

    public void testHybridOfNotNull() {
        assertNotNull(me.getHybridOf(pahleMethod));
    }

    public void testHybridOfSize() {
        assertEquals(2, me.getHybridOf(pahleMethod).size());
    }

    public void testHybridOfContent() {
        assertTrue(me.getHybridOf(pahleMethod).contains(lsoda));
    }

    public void testReasoner() {
        assertNotNull(me.getReasoner());
    }

    public void testDataFactory() {
        assertNotNull(me.getDataFactory());
    }
}
