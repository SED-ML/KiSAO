package net.biomodels.kisao;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/**
 * Declares the most common KiSAO IRIs.
 * @author Anna Zhukova
 *         Date: 13-May-2011
 *         Time: 17:36:36
 */
public interface KiSAOIRI {
    IRI KISAO_IRI = IRI.create("http://biomodels.net/kisao/KISAO");
    IRI KISAO_FULL_IRI = IRI.create("http://biomodels.net/kisao/KISAO_FULL");

    String KISAO_PREFIX = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000259").getStart();
    String KISAO_URN = "urn:miriam:biomodels.kisao:";
    String KISAO_URL = "http://identifiers.org/biomodels.kisao/";

    IRI SYNONYM_IRI = IRI.create("http://www.w3.org/2004/02/skos/core#altLabel");
    IRI SYNONYM_TYPE_IRI = OWLRDFVocabulary.RDFS_COMMENT.getIRI();
    IRI NAME_IRI = OWLRDFVocabulary.RDFS_LABEL.getIRI();
    IRI DEFINITION_IRI = IRI.create("http://www.w3.org/2004/02/skos/core#definition");

    IRI KINETIC_SIMULATION_ALGORITHM_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000000");
    IRI KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000097");
    IRI KINETIC_SIMULATION_ALGORITHM_PARAMETER_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000201");

    IRI HAS_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000245");
    IRI HAS_PARAMETER_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000259");
    IRI IS_HYBRID_OF_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000246");
    IRI USES_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000360");


    IRI HAS_TYPE = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000251");

    IRI STOCHASTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000104");
    IRI DETERMINISTIC_SYSTEM_BEHAVIOUR_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000103");
    IRI CONTINUOUS_VARIABLE_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000106");
    IRI DISCRETE_VARIABLE_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000105");
    IRI PROGRESSION_WITH_ADAPTIVE_TIME_STEP_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000107");
    IRI PROGRESSION_WITH_FIXED_TIME_STEP_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000108");
    IRI SPATIAL_DESCRIPTION_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000102");
    IRI IMPLICIT_METHOD_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000240");
    IRI EXPLICIT_METHOD_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000239");
    IRI HYBRIDITY_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000322");
    IRI TYPE_OF_VARIABLE_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000098");
    IRI TYPE_OF_SYSTEM_BEHAVIOUR_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000099");
    IRI TYPE_OF_DIFFERENTIAL_EQUATION_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000405");
    IRI TYPE_OF_PROGRESSION_TIME_STEP_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000100");

    IRI IS_ORGANIZATIONAL_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#isOrganizational");
   
}
