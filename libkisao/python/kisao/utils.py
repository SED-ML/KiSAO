""" Utilities for working with the KiSAO ontology

:Author: Jonathan Karr <karr@mssm.edu>
:Date: 2021-04-28
:Copyright: 2021, SED-ML Editors
:License: Apache 2.0
"""

from .core import Kisao
from .data_model import AlgorithmSubstitutionPolicy, ALGORITHM_SUBSTITUTION_POLICY_LEVELS
from .exceptions import AlgorithmCannotBeSubstitutedException
from .warnings import AlgorithmSubstitutedWarning
import functools
import pronto  # noqa: F401
import termcolor
import warnings

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
    'get_perferred_substitute_algorithm',
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


@functools.lru_cache(maxsize=None)
def get_ode_algorithms():
    """ Get the terms for ODE integration algorithms::

        'modelling simulation algorithm' and 'has characteristic' some 'ordinary differential equation problem'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_ODE_PROBLEM_CHARACTERISTIC])


@functools.lru_cache(maxsize=None)
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


@functools.lru_cache(maxsize=None)
def get_tau_leaping_algorithms():
    """ Get the terms for tau-leaping algorithms (KISAO_0000039).::

        'tau-leaping method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_TAU_LEAPING_ALGORITHM])


@functools.lru_cache(maxsize=None)
def get_rule_based_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_RULE_BASED_ALGORITHM])


@functools.lru_cache(maxsize=None)
def get_sde_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_SDE_PROBLEM_CHARACTERISTIC])


@functools.lru_cache(maxsize=None)
def get_pde_algorithms():
    """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

        'rule-based simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_ALGORITHM], [ID_PDE_PROBLEM_CHARACTERISTIC])


@functools.lru_cache(maxsize=None)
def get_flux_balance_algorithms():
    """ Get the terms for flux balance algorithms (KISAO_0000622).::

        'flux balance method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_FLUX_BALANCE_ALGORITHM])


@functools.lru_cache(maxsize=None)
def get_logical_algorithms():
    """ Get the terms for logical simulation algorithms (KISAO_0000448).::

        'logical simulation method'

    Returns:
        :obj:`set` of :obj:`pronto.Term`: terms
    """
    return get_terms_with_characteristics([ID_LOGICAL_ALGORITHM])


@functools.lru_cache(maxsize=None)
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
        alt_algs = _find_substitutable_algorithms(algorithm, alg_set_funcs)

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
            (True, get_logical_algorithms),
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
            (True, get_logical_algorithms),
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


def get_perferred_substitute_algorithm(algorithm, alt_algorithms, substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES):
    """ Get the preferred substitute for an algorithm for a given substitution policy.

    Args:
        algorithm (:obj:`pronto.Term`): algorithm requested to be executed
        alt_algorithms (:obj:`list` of :obj:`pronto.Term`): possible alternative algorithms in order of their substitution preference
        substitution_policy (:obj:`AlgorithmSubstitutionPolicy`, optional): algorithm substitution policy; defines the degree to which
            alternative algorithms can be substituted

    Returns:
        :obj:`term`: set of algorithms that an algorithm can be substituted for
    """
    if algorithm in alt_algorithms:
        return algorithm

    sub_algorithms = get_substitutable_algorithms(algorithm, substitution_policy=substitution_policy)
    alt_algorithm = None
    for possible_alt_algorithm in alt_algorithms:
        if possible_alt_algorithm in sub_algorithms:
            alt_algorithm = possible_alt_algorithm
            break

    if alt_algorithm is None:
        raise AlgorithmCannotBeSubstitutedException(
            "No algorithm can be substituted for '{}' ({}) at substitution policy '{}'.".format(
                algorithm.name, algorithm.id.partition('#')[2], substitution_policy.name))

    if alt_algorithm != algorithm:
        msg = "'{}' ({}) will be substituted for '{}'' ({}) at substitution policy '{}'.".format(
            alt_algorithm.name, alt_algorithm.id.partition('#')[2],
            algorithm.name, algorithm.id.partition('#')[2],
            substitution_policy.name)
        warnings.warn(termcolor.colored(msg, 'yellow'), AlgorithmSubstitutedWarning)

    return alt_algorithm


def get_algorithm_substitution_report():
    """ Get a report of the substitutability of algorithms

    Returns:
        :obj:`pandas.DataFrame`: report of the substitutability of algorithms
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
        + natsort.natsorted(get_logical_algorithms(), key=lambda alg: alg.name)
        + natsort.natsorted(get_flux_balance_algorithms(), key=lambda alg: alg.name)
    )

    levels = list(ALGORITHM_SUBSTITUTION_POLICY_LEVELS.keys())

    report = []
    for alg in algs:
        report.append([None] * len(algs))

    for i_alg, alg in enumerate(algs):

        for policy in levels[1:-1]:
            try:
                alt_algs = get_substitutable_algorithms(alg, substitution_policy=policy)
            except NotImplementedError:
                continue

            for alt_alg in alt_algs:
                try:
                    i_alt_alg = algs.index(alt_alg)
                except ValueError:
                    continue

                if report[i_alg][i_alt_alg] is None:
                    report[i_alg][i_alt_alg] = policy.name
                    report[i_alt_alg][i_alg] = policy.name

    alg_id_names = pandas.MultiIndex.from_tuples([(alg.id.partition('#')[2], alg.name) for alg in algs])
    report_df = pandas.DataFrame(numpy.array(report), index=alg_id_names, columns=alg_id_names)
    return report_df
