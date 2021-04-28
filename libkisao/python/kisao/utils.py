""" Utilities for working with the KiSAO ontology

:Author: Jonathan Karr <karr@mssm.edu>
:Date: 2021-04-28
:Copyright: 2021, SED-ML Editors
:License: Apache 2.0
"""

from .core import Kisao
from .data_model import AlgorithmSubstitutionPolicy, ALGORITHM_SUBSTITUTION_POLICY_LEVELS
import functools
import pronto  # noqa: F401

__all__ = [
    'ID_HAS_CHARACTERISTIC_RELATIONSHIP',
    'ID_ODE_PROBLEM_CHARACTERISTIC',
    'ID_SDE_PROBLEM_CHARACTERISTIC',
    'ID_PDE_PROBLEM_CHARACTERISTIC',
    'ID_EXACT_SOLUTION_CHARACTERISTIC',
    'ID_APPROXIMATE_SOLUTION_CHARACTERISTIC',
    'ID_ALGORITHM',
    'ID_GILLESPIE_LIKE_ALGORITHM',
    'ID_TAU_LEAPING_ALGORITHM',
    'ID_RULE_BASED_ALGORITHM',
    'ID_FLUX_BALANCE_ALGORITHM',
    'ID_LOGICAL_ALGORITHM',
    'ID_HYBRID_ALGORITHM',
    'get_terms_with_characteristics',
    'get_ode_algorithms',
    'get_gillespie_like_algorithms',
    'get_tau_leaping_algorithms',
    'get_rule_based_algorithms',
    'get_sde_algorithms',
    'get_pde_algorithms',
    'get_flux_balance_algorithms',
    'get_logical_algorithms',
    'get_hybrid_algorithms',
    'get_substitutable_algorithms',
]

ID_HAS_CHARACTERISTIC_RELATIONSHIP = 'KISAO_0000245'  # has characteristic

ID_ODE_PROBLEM_CHARACTERISTIC = 'KISAO_0000374'  # ordinary differential equation problem
ID_SDE_PROBLEM_CHARACTERISTIC = 'KISAO_0000371'  # stochastic differential equation problem
ID_PDE_PROBLEM_CHARACTERISTIC = 'KISAO_0000372'  # partial differential equation problem
ID_EXACT_SOLUTION_CHARACTERISTIC = 'KISAO_0000236'  # exact solution
ID_APPROXIMATE_SOLUTION_CHARACTERISTIC = 'KISAO_0000237'  # approximate solution

ID_ALGORITHM = 'KISAO_0000000'  # modelling and simulation algorithm
ID_GILLESPIE_LIKE_ALGORITHM = 'KISAO_0000241'  # Gillespie-like method
ID_TAU_LEAPING_ALGORITHM = 'KISAO_0000039'  # tau-leaping method
ID_RULE_BASED_ALGORITHM = 'KISAO_0000363'  # rule-based simulation method
ID_FLUX_BALANCE_ALGORITHM = 'KISAO_0000622'  # flux balance method
ID_LOGICAL_ALGORITHM = 'KISAO_0000448'  # logical model simulation method
ID_HYBRID_ALGORITHM = 'KISAO_0000352'  # hybrid method


def get_terms_with_characteristics(parent_ids, characteristic_ids=None):
    """ Get the subclasses of one or more terms which, optionally, have one or more characteristics
        (e.g., ``'has characteristic' some 'ordinary differential equation problem'``)

    Args:
        parent_ids (:obj:`list` of :obj:`str`): ids of the parent terms (e.g., ``KISAO_0000000``)
        characteristic_ids (:obj:`list` of :obj:`str`): ids of the characteristics (e.g., ``KISAO_0000374``)

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    characteristic_ids = characteristic_ids or []

    kisao = Kisao()
    characteristic_rel = kisao.get_relationship(ID_HAS_CHARACTERISTIC_RELATIONSHIP)

    possible_terms = set()
    for parent_id in parent_ids:
        possible_terms.update(kisao.get_term(parent_id).subclasses())
    terms = set()

    for term in possible_terms:
        if term in terms:
            continue
        has_characteristics = True
        for characteristic_id in characteristic_ids:
            characteristic = kisao.get_term(characteristic_id)
            term_characteristics = term.relationships.get(characteristic_rel)
            if term_characteristics is None or characteristic not in term_characteristics:
                has_characteristics = False
                break
        if has_characteristics:
            terms.update(term.subclasses())

    return terms


def get_ode_algorithms():
    """ Get the terms for ODE integration algorithms::

        'modelling simulation algorithm' and 'has characteristic' some 'ordinary differential equation problem'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_ODE_PROBLEM_CHARACTERISTIC])


def get_gillespie_like_algorithms(exact=True, approximate=False):
    """ Get the terms for algorithms that execute similar simulations to Gillespie's
    algorithm (KISAO_0000029).

    Three subsets of terms can be returned:

    * Terms for algorithms that produce simulation results that are mathematically equivalent to Gillespie's
      algorithm such as the Gibson-Bruck Next Reaction Method (KISAO_0000027).::

        'Gillespie-like method' and 'has characteristic' some 'exact solution'

    * Terms for algorithms that approximate Gillespie's algorithm such as tau-leaping (KISAO_0000039).::

        'Gillespie-like method' and 'has characteristic' some 'approximate solution'

    * Any Gillespie-like method.::

        'Gillespie-like method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    if not exact and not approximate:
        return set()
    elif exact and approximate:
        characteristics = []
    elif exact:
        characteristics = [ID_EXACT_SOLUTION_CHARACTERISTIC]
    elif approximate:
        characteristics = [ID_APPROXIMATE_SOLUTION_CHARACTERISTIC]

    return get_terms_with_characteristics([ID_GILLESPIE_LIKE_ALGORITHM], characteristics)


def get_tau_leaping_algorithms():
    """ Get the terms for tau-leaping algorithms (KISAO_0000039).::

        'tau-leaping method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_TAU_LEAPING_ALGORITHM])


def get_rule_based_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_RULE_BASED_ALGORITHM])


def get_sde_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_SDE_PROBLEM_CHARACTERISTIC])


def get_pde_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_PDE_PROBLEM_CHARACTERISTIC])


def get_flux_balance_algorithms():
    """ Get the terms for flux balance algorithms (KISAO_0000622).::

        'flux balance method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_FLUX_BALANCE_ALGORITHM])


def get_logical_algorithms():
    """ Get the terms for logical simulation algorithms (KISAO_0000448).::

        'logical simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_LOGICAL_ALGORITHM])


def get_hybrid_algorithms():
    """ Get the terms for hybrid algorithms (KISAO_0000352).::

        'hybrid method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_HYBRID_ALGORITHM])


def get_substitutable_algorithms(algorithm, substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES):
    """ Get a set of algorithms that an algorithm can be substituted for a given substitution policy.

    Args:
        algorithm (:obj:`pronto.Term`): algorithm requested to be executed
        substitution_policy (:obj:`AlgorithmSubstitutionPolicy`, optional): algorithm substitution policy; defines the degree to which
            alternative algorithms can be substituted

    Returns:
        :obj:`set` of :obj:`term`: set of algorithms that an algorithm can be substituted for
    """
    alt_algs = None

    if (
        ALGORITHM_SUBSTITUTION_POLICY_LEVELS[substitution_policy]
        <= ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.SAME_METHOD]
    ):
        alt_algs = set([algorithm])

    elif substitution_policy == AlgorithmSubstitutionPolicy.SAME_MATH:
        alg_set_funcs = [
            (False, get_ode_algorithms),
            (True, functools.partial(get_gillespie_like_algorithms, exact=True, approximate=False)),
            (False, get_tau_leaping_algorithms),
            (False, get_sde_algorithms),
            (False, get_pde_algorithms),
            (False, get_logical_algorithms),
            (False, get_flux_balance_algorithms),
        ]
        for substitutable, alg_set_func in alg_set_funcs:
            alg_set = alg_set_func()
            if algorithm in alg_set:
                if substitutable:
                    alt_algs = alg_set
                else:
                    alt_algs = set([algorithm])
                break

    elif substitution_policy == AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS:
        alg_set_funcs = [
            (True, get_ode_algorithms),
            (True, functools.partial(get_gillespie_like_algorithms, exact=True, approximate=False)),
            (True, get_tau_leaping_algorithms),
            (True, get_sde_algorithms),
            (True, get_pde_algorithms),
            (False, get_logical_algorithms),
            (False, get_flux_balance_algorithms),
        ]
        for substitutable, alg_set_func in alg_set_funcs:
            alg_set = alg_set_func()
            if algorithm in alg_set:
                if substitutable:
                    alt_algs = alg_set
                else:
                    alt_algs = set([algorithm])
                break

    elif (
        ALGORITHM_SUBSTITUTION_POLICY_LEVELS[substitution_policy]
        <= ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES]
    ):
        alg_set_funcs = [
            (True, get_ode_algorithms),
            (True, lambda: get_gillespie_like_algorithms(
                exact=True, approximate=False) | get_tau_leaping_algorithms()),
            (True, get_sde_algorithms),
            (True, get_pde_algorithms),
            (True, get_logical_algorithms),
            # (False, get_flux_balance_algorithms),
        ]
        for substitutable, alg_set_func in alg_set_funcs:
            alg_set = alg_set_func()
            if algorithm in alg_set:
                if substitutable:
                    alt_algs = alg_set
                else:
                    alt_algs = set([algorithm])
                break

    elif substitution_policy == AlgorithmSubstitutionPolicy.SAME_FRAMEWORK:
        alg_set_funcs = [
            get_ode_algorithms,
            lambda: get_gillespie_like_algorithms(
                exact=True, approximate=False) | get_tau_leaping_algorithms(),
            get_sde_algorithms,
            get_pde_algorithms,
            get_flux_balance_algorithms,
            get_logical_algorithms,
        ]
        for alg_set_func in alg_set_funcs:
            alg_set = alg_set_func()
            if algorithm in alg_set:
                alt_algs = alg_set
                break

    elif substitution_policy == AlgorithmSubstitutionPolicy.ANY:
        alt_algs = get_terms_with_characteristics([ID_ALGORITHM])

    else:
        raise NotImplementedError('Algorithm substitution for policy {} is not implemented.'.format(substitution_policy.value))

    if alt_algs is None:
        raise NotImplementedError('Algorithm substitution for "{}" ({}) for policy {} is not supported.'.format(
            algorithm.name, algorithm.id, substitution_policy.value))

    return alt_algs
