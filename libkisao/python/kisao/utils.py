""" Utilities for working with the KiSAO ontology

:Author: Jonathan Karr <karr@mssm.edu>
:Date: 2021-04-28
:Copyright: 2021, SED-ML Editors
:License: Apache 2.0
"""

from .core import Kisao
from .data_model import (AlgorithmSubstitutionPolicy, ALGORITHM_SUBSTITUTION_POLICY_LEVELS, IdDialect,
                         ID_HAS_CHARACTERISTIC_RELATIONSHIP,
                         ID_ODE_PROBLEM_CHARACTERISTIC,
                         ID_SDE_PROBLEM_CHARACTERISTIC,
                         ID_PDE_PROBLEM_CHARACTERISTIC,
                         ID_EXACT_SOLUTION_CHARACTERISTIC,
                         ID_APPROXIMATE_SOLUTION_CHARACTERISTIC,
                         ID_ALGORITHM,
                         ID_GILLESPIE_LIKE_ALGORITHM,
                         ID_TAU_LEAPING_ALGORITHM,
                         ID_RULE_BASED_ALGORITHM,
                         ID_FLUX_BALANCE_ALGORITHM,
                         ID_LOGICAL_SIMULATION_ALGORITHM,
                         ID_LOGICAL_STABLE_STATE_SEARCH_ALGORITHM,
                         ID_LOGICAL_TRAP_SPACE_SEARCH_ALGORITHM,
                         ID_HYBRID_ALGORITHM,
                         TermType,
                         )
from .exceptions import AlgorithmCannotBeSubstitutedException
from .warnings import AlgorithmSubstitutedWarning
import collections
import functools
import pronto  # noqa: F401
import termcolor
import urllib.parse
import warnings

__all__ = [
    'get_term_type',
    'get_ols_url_for_term',
    'get_terms_with_characteristics',
    'get_ode_algorithms',
    'get_gillespie_like_algorithms',
    'get_tau_leaping_algorithms',
    'get_rule_based_algorithms',
    'get_sde_algorithms',
    'get_pde_algorithms',
    'get_flux_balance_algorithms',
    'get_logical_simulation_algorithms',
    'get_logical_stable_state_search_algorithms',
    'get_logical_trap_space_search_algorithms',
    'get_hybrid_algorithms',
    'get_substitutable_algorithms_for_policy',
    'get_substitutable_algorithms',
    'group_substitutable_algorithms_by_policy',
    'get_preferred_substitute_algorithm',
    'get_preferred_substitute_algorithm_by_ids',
    'get_algorithm_substitution_matrix',
]


def get_term_type(term):
    """ Get the type of a KiSAO term (e.g., algorithm, algorithm characteristic, algorithm parameter)

    Args:
        term (:obj:`pronto.Term`): term

    Returns:
        :obj:`TermType`: type of the term
    """
    kisao = Kisao()
    superclass_ids = [kisao.get_term_id(superclass) for superclass in term.superclasses()]

    for term_type in TermType.__members__.values():
        if term_type.value in superclass_ids:
            if term_type.value == kisao.get_term_id(term):
                return TermType.root
            else:
                return term_type


def get_ols_url_for_term(term):
    """ Get the URL for the OLS web page for a KiSAO term

    Args:
        term (:obj:`pronto.Term`): term

    Returns:
        :obj:`str`: URL for the OLS web page for a KiSAO term
    """
    return 'https://www.ebi.ac.uk/ols/ontologies/kisao/terms?' + urllib.parse.urlencode({'iri': term.id})


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


@ functools.lru_cache(maxsize=None)
def get_ode_algorithms():
    """ Get the terms for ODE integration algorithms::

        'modelling simulation algorithm' and 'has characteristic' some 'ordinary differential equation problem'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_ODE_PROBLEM_CHARACTERISTIC])


@ functools.lru_cache(maxsize=None)
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


@ functools.lru_cache(maxsize=None)
def get_tau_leaping_algorithms():
    """ Get the terms for tau-leaping algorithms (KISAO_0000039).::

        'tau-leaping method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_TAU_LEAPING_ALGORITHM])


@ functools.lru_cache(maxsize=None)
def get_rule_based_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_RULE_BASED_ALGORITHM])


@ functools.lru_cache(maxsize=None)
def get_sde_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_SDE_PROBLEM_CHARACTERISTIC])


@ functools.lru_cache(maxsize=None)
def get_pde_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_PDE_PROBLEM_CHARACTERISTIC])


@ functools.lru_cache(maxsize=None)
def get_flux_balance_algorithms():
    """ Get the terms for flux balance algorithms (KISAO_0000622).::

        'flux balance method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_FLUX_BALANCE_ALGORITHM])


@ functools.lru_cache(maxsize=None)
def get_logical_simulation_algorithms():
    """ Get the terms for logical simulation algorithms (KISAO_0000448).::

        'logical model simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_LOGICAL_SIMULATION_ALGORITHM])


@ functools.lru_cache(maxsize=None)
def get_logical_stable_state_search_algorithms():
    """ Get the terms for algorithms for finding the stable states of logical models (KISAO_0000660).::

        'logical model stable state search method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_LOGICAL_STABLE_STATE_SEARCH_ALGORITHM])


@ functools.lru_cache(maxsize=None)
def get_logical_trap_space_search_algorithms():
    """ Get the terms for algorithms for finding the trap spaces of logical models (KISAO_0000661).::

        'logical model trap space identification method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_LOGICAL_TRAP_SPACE_SEARCH_ALGORITHM])


@ functools.lru_cache(maxsize=None)
def get_hybrid_algorithms():
    """ Get the terms for hybrid algorithms (KISAO_0000352).::

        'hybrid method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_HYBRID_ALGORITHM])


def get_substitutable_algorithms_for_policy(algorithm, substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES):
    """ Get a set of algorithms that an algorithm can be substituted for a given substitution policy.

    Args:
        algorithm (:obj:`pronto.Term`): target algorithm (e.g., requested to be executed in a SED-ML document)
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
            (False, get_logical_simulation_algorithms),
            (True, get_logical_stable_state_search_algorithms),
            (True, get_logical_trap_space_search_algorithms),
            (False, get_flux_balance_algorithms),
        ]
        alt_algs = _find_substitutable_algorithms(algorithm, alg_set_funcs)

    elif substitution_policy == AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS:
        alg_set_funcs = [
            (True, get_ode_algorithms),
            (True, functools.partial(get_gillespie_like_algorithms, exact=True, approximate=False)),
            (True, get_tau_leaping_algorithms),
            (True, get_sde_algorithms),
            (True, get_pde_algorithms),
            (False, get_logical_simulation_algorithms),
            (True, get_logical_stable_state_search_algorithms),
            (True, get_logical_trap_space_search_algorithms),
            (False, get_flux_balance_algorithms),
        ]
        alt_algs = _find_substitutable_algorithms(algorithm, alg_set_funcs)

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
            (True, get_logical_simulation_algorithms),
            (True, get_logical_stable_state_search_algorithms),
            (True, get_logical_trap_space_search_algorithms),
            # (False, get_flux_balance_algorithms),
        ]
        alt_algs = _find_substitutable_algorithms(algorithm, alg_set_funcs)

    elif substitution_policy == AlgorithmSubstitutionPolicy.SAME_FRAMEWORK:
        alg_set_funcs = [
            (True, get_ode_algorithms),
            (True, lambda: get_gillespie_like_algorithms(
                exact=True, approximate=False) | get_tau_leaping_algorithms()),
            (True, get_sde_algorithms),
            (True, get_pde_algorithms),
            (True, get_flux_balance_algorithms),
            (True, get_logical_simulation_algorithms),
            (True, get_logical_stable_state_search_algorithms),
            (True, get_logical_trap_space_search_algorithms),
        ]
        alt_algs = _find_substitutable_algorithms(algorithm, alg_set_funcs)

    elif substitution_policy == AlgorithmSubstitutionPolicy.ANY:
        alt_algs = get_terms_with_characteristics([ID_ALGORITHM])

    else:  # pragma: no cover # above will raise errors if a substitution_policy isn't a member of :obj:`AlgorithmSubstitutionPolicy`
        raise NotImplementedError('Algorithm substitution for policy {} is not implemented.'.format(substitution_policy.value))

    if alt_algs is None:
        raise NotImplementedError('Algorithm substitution for "{}" ({}) for policy {} is not supported.'.format(
            algorithm.name, algorithm.id, substitution_policy.value))

    return alt_algs


def _find_substitutable_algorithms(algorithm, alg_set_funcs):
    for substitutable, alg_set_func in alg_set_funcs:
        alg_set = alg_set_func()
        if algorithm in alg_set:
            if substitutable:
                return alg_set
            else:
                return set([algorithm])
    return None


def get_substitutable_algorithms(algorithm):
    """ Get a list of alternative algorithms that an algorithm can be substituted for and the most restrictive policy at which the
    alternative algorithm can be substituted.

    Args:
        algorithm (:obj:`pronto.Term`): target algorithm (e.g., requested to be executed in a SED-ML document)

    Returns:
        :obj:`collections.OrderedDict` of :obj:`pronto.Term` => :obj:`AlgorithmSubstitutionPolicy`: dictionary that
            maps alternative algorithms to the the most restrictive policy at which the alternative algorithm can be
            substituted for the target algorithm.
    """
    levels = list(ALGORITHM_SUBSTITUTION_POLICY_LEVELS.keys())
    alt_algs = collections.OrderedDict()
    for policy in levels[1:-1]:
        try:
            alt_algs_at_policy = get_substitutable_algorithms_for_policy(algorithm, substitution_policy=policy)
        except NotImplementedError:
            continue

        for alt_alg_at_policy in alt_algs_at_policy:
            if alt_alg_at_policy not in alt_algs:
                alt_algs[alt_alg_at_policy] = policy

    return alt_algs


def group_substitutable_algorithms_by_policy(alt_algorithms):
    """ Group a map of substitutable algorithms by the most restrictive policies at which they can be substituted

    Args:
        :obj:`dict` of :obj:`pronto.Term` => :obj:`AlgorithmSubstitutionPolicy`: dictionary that maps alternative algorithms to the
            the most restrictive policy at which the alternative algorithm can be substituted for the target algorithm.

    Returns:
        :obj:`dict` of :obj:`AlgorithmSubstitutionPolicy` => :obj:`set` of :obj:`pronto.Term`: diction that maps algorithm
            substitution policies to the algorithms which can be substituted at that policy and less restrictive policies
    """
    algs_at_policy = {}
    for alg, policy in alt_algorithms.items():
        if policy not in algs_at_policy:
            algs_at_policy[policy] = set()
        algs_at_policy[policy].add(alg)
    return algs_at_policy


def get_preferred_substitute_algorithm(algorithm, alt_algorithms, substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES):
    """ Get the preferred substitute for an algorithm for a given substitution policy.

    Args:
        algorithm (:obj:`pronto.Term`): target algorithm (e.g., requested to be executed in a SED-ML document)
        alt_algorithms (:obj:`list` of :obj:`pronto.Term`): possible alternative algorithms in order of their substitution preference
        substitution_policy (:obj:`AlgorithmSubstitutionPolicy`, optional): algorithm substitution policy; defines the degree to which
            alternative algorithms can be substituted

    Returns:
        :obj:`term`: set of algorithms that an algorithm can be substituted for
    """
    if algorithm in alt_algorithms:
        return algorithm

    sub_algorithms = get_substitutable_algorithms_for_policy(algorithm, substitution_policy=substitution_policy)
    alt_algorithm = None
    for possible_alt_algorithm in alt_algorithms:
        if possible_alt_algorithm in sub_algorithms:
            alt_algorithm = possible_alt_algorithm
            break

    if alt_algorithm is None:
        raise AlgorithmCannotBeSubstitutedException(
            (
                "No algorithm can be substituted for '{}' ({}) at substitution policy '{}'. "
                "Algorithms can only be substituted for the following algorithms:\n  {}"
            ).format(
                algorithm.name, algorithm.id.partition('#')[2], substitution_policy.name,
                '\n  '.join(sorted('{}: {}'.format(alt_algorithm.id.partition('#')[2], alt_algorithm.name)
                                   for alt_algorithm in alt_algorithms))
            ))

    if alt_algorithm != algorithm:
        msg = "'{}' ({}) will be substituted for '{}'' ({}) at substitution policy '{}'.".format(
            alt_algorithm.name, alt_algorithm.id.partition('#')[2],
            algorithm.name, algorithm.id.partition('#')[2],
            substitution_policy.name)
        warnings.warn(termcolor.colored(msg, 'yellow'), AlgorithmSubstitutedWarning)

    return alt_algorithm


def get_preferred_substitute_algorithm_by_ids(algorithm, alt_algorithms,
                                              substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES,
                                              id_dialect=IdDialect.kisao):
    """ Get the preferred substitute for an algorithm working with the ids of algorithms

    Args:
        algorithm (:obj:`str` or :obj:`int`): KiSAO id of the target algorithm (e.g., ``KISAO_0000019``)
        alt_algorithms (:obj:`list` of :obj:`str` or :obj:`int`): KiSAO ids of potential alternative algorithms
            (e.g., that a simulation tool implements)
        substitution_policy (:obj:`AlgorithmSubstitutionPolicy`, optional): algorithm substitution policy
        id_dialect (:obj:`IdDialect`, optional): dialect for id

    Returns:
        :obj:`str`: KiSAO id of the preferred algorithm to execute (e.g., ``KISAO_0000088``)
    """
    kisao = Kisao()
    algorithm_term = kisao.get_term(algorithm)
    alt_algorithm_terms = [kisao.get_term(alt_algorithm) for alt_algorithm in alt_algorithms]
    alt_algorithm = get_preferred_substitute_algorithm(algorithm_term, alt_algorithm_terms, substitution_policy=substitution_policy)
    return kisao.get_term_id(alt_algorithm, dialect=id_dialect)


def get_algorithm_substitution_matrix():
    """ Get a matrix of the substitutability of algorithms

    Returns:
        :obj:`pandas.DataFrame`: matrix of the substitutability of algorithms
    """
    import natsort
    import numpy
    import pandas

    algs = (
        natsort.natsorted(get_ode_algorithms(), key=lambda alg: alg.name)
        + natsort.natsorted(get_gillespie_like_algorithms(exact=True, approximate=False), key=lambda alg: alg.name)
        + natsort.natsorted(get_tau_leaping_algorithms(), key=lambda alg: alg.name)
        + natsort.natsorted(get_sde_algorithms(), key=lambda alg: alg.name)
        + natsort.natsorted(get_pde_algorithms(), key=lambda alg: alg.name)
        + natsort.natsorted(get_logical_simulation_algorithms(), key=lambda alg: alg.name)
        + natsort.natsorted(get_logical_stable_state_search_algorithms(), key=lambda alg: alg.name)
        + natsort.natsorted(get_logical_trap_space_search_algorithms(), key=lambda alg: alg.name)
        + natsort.natsorted(get_flux_balance_algorithms(), key=lambda alg: alg.name)
    )

    matrix = []
    for alg in algs:
        matrix.append([None] * len(algs))

    for i_alg, alg in enumerate(algs):
        alt_algs = get_substitutable_algorithms(alg)
        for alt_alg, policy in alt_algs.items():
            try:
                i_alt_alg = algs.index(alt_alg)
            except ValueError:
                continue
            matrix[i_alg][i_alt_alg] = policy.name
            matrix[i_alt_alg][i_alg] = policy.name

    alg_id_names = pandas.MultiIndex.from_tuples([(alg.id.partition('#')[2], alg.name) for alg in algs])
    report_df = pandas.DataFrame(numpy.array(matrix), index=alg_id_names, columns=alg_id_names)
    return report_df
