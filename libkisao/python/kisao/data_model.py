""" Data model for working with the KiSAO ontology

:Author: Jonathan Karr <karr@mssm.edu>
:Date: 2021-04-28
:Copyright: 2021, SED-ML Editors
:License: Apache 2.0
"""

import collections
import enum

__all__ = [
    'IdDialect',
    'AlgorithmSubstitutionPolicy',
    'ALGORITHM_SUBSTITUTION_POLICY_LEVELS',
    'ALGORITHM_SUBSTITUTION_POLICY_NAMES',
]


class IdDialect(str, enum.Enum):
    """ Dialect of ids of KiSAO terms """
    kisao = 'kisao'  # e.g., ``KISAO_0000019``
    sedml = 'sedml'  # e.g., ``KISAO:0000019``
    integer = 'integer'  # e.g., ``19`


class AlgorithmSubstitutionPolicy(str, enum.Enum):
    """ Algorithm substitution policy

    More information: `https://biosimulators.org/conventions/simulator-interfaces <https://biosimulators.org/conventions/simulator-interfaces>`_
    """

    NONE = 'NONE'
    # Algorithms should not be substituted.

    SAME_METHOD = 'SAME_METHOD'
    """ Algorithms can be substituted with different realizations of the same method.

    Examples:

    * GLPK Simplex method <=> SciPy Simplex method
    """

    SAME_MATH = 'SAME_MATH'
    """ Algorithms can be substituted with mathematically-equivalent algorithms.

    Examples:

    * SSA <=> Next Reaction Method
    * Simplex method <=> interior point method
    """

    SIMILAR_APPROXIMATIONS = 'SIMILAR_APPROXIMATIONS'
    """ Algorithms can be substituted with others that make similar approximations
    to the same math.

    Examples:

    * CVODE <=> LSODA <=> RK-45
    * tau leaping <=> partitioned tau leaping
    """

    DISTINCT_APPROXIMATIONS = 'DISTINCT_APPROXIMATIONS'
    """ Algorithms can be substituted with others that make distinct approximations
    to the same math.

    Examples:

    * SSA <=> tau leaping <=> Pahle hybrid method
    """

    DISTINCT_SCALES = 'DISTINCT_SCALES'
    """ Algorithms can be substituted with others that make distinct approximations
    to the same math that substantially differ in their scale.

    Examples:

    * SSA <=> CVODE
    """

    SAME_VARIABLES = 'SAME_VARIABLES'
    """ Algorithms that predict the same dependent variables can be substituted.

    Examples:

    * FBA <=> parsimonious FBA
    """

    SIMILAR_VARIABLES = 'SIMILAR_VARIABLES'
    """ Algorithms that predict similar dependent variables can be substituted.

    This is the recommended default value.

    Examples:

    * FBA <=> geometric FBA
    """

    SAME_FRAMEWORK = 'SAME_FRAMEWORK'
    """ Any algorithm of the same framework can be substituted (e.g., CVODE, LSODA).

    Examples:

    * FBA <=> FVA
    """

    ANY = 'ANY'
    # any algorithm can be substituted. Note, using any other algorithm can substantively
    # change the interpretation of a simulation. For example, switching SSA to CVODE loses
    # all information about the variance in the simulated system.


ALGORITHM_SUBSTITUTION_POLICY_LEVELS = collections.OrderedDict([
    (AlgorithmSubstitutionPolicy.NONE, 0),
    (AlgorithmSubstitutionPolicy.SAME_METHOD, 1),
    (AlgorithmSubstitutionPolicy.SAME_MATH, 2),
    (AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS, 3),
    (AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS, 4),
    (AlgorithmSubstitutionPolicy.DISTINCT_SCALES, 5),
    (AlgorithmSubstitutionPolicy.SAME_VARIABLES, 6),
    (AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES, 7),
    (AlgorithmSubstitutionPolicy.SAME_FRAMEWORK, 8),
    (AlgorithmSubstitutionPolicy.ANY, 9),
])

ALGORITHM_SUBSTITUTION_POLICY_NAMES = {
    AlgorithmSubstitutionPolicy.NONE: 'None',
    AlgorithmSubstitutionPolicy.SAME_METHOD: 'Same method',
    AlgorithmSubstitutionPolicy.SAME_MATH: 'Same math',
    AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS: 'Similar approximations',
    AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS: 'Distinct approximations',
    AlgorithmSubstitutionPolicy.DISTINCT_SCALES: 'Distinct scales',
    AlgorithmSubstitutionPolicy.SAME_VARIABLES: 'Same variables',
    AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES: 'Similar variables',
    AlgorithmSubstitutionPolicy.SAME_FRAMEWORK: 'Same framework',
    AlgorithmSubstitutionPolicy.ANY: 'Any',
}
