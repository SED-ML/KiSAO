package net.biomodels.kisao;

import org.semanticweb.owlapi.model.IRI;

/**
 * Declares the most common KiSAO IRIs.
 * @author Anna Zhukova
 *         Date: 13-May-2011
 *         Time: 17:36:36
 */
public interface KiSAOIRI {
    String KISAO_PREFIX = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000259").getStart();
    String KISAO_URN = "urn:miriam:biomodels.kisao:";

    IRI SYNONYM_IRI = IRI.create("http://www.geneontology.org/formats/oboInOwl#Synonym");
    IRI SYNONYM_TYPE_IRI = IRI.create("http://www.geneontology.org/formats/oboInOwl#SynonymType");

    IRI KINETIC_SIMULATION_ALGORITHM_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000000");
    IRI KINETIC_SIMULATION_ALGORITHM_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000097");
    IRI KINETIC_SIMULATION_ALGORITHM_PARAMETER_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000201");

    IRI HAS_CHARACTERISTIC_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000245");
    IRI HAS_PARAMETER_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000259");
    IRI IS_HYBRID_OF_IRI = IRI.create("http://www.biomodels.net/kisao/KISAO#KISAO_0000246");


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

}
