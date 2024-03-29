from kisao import utils
from kisao.core import Kisao
from kisao.data_model import AlgorithmSubstitutionPolicy, ALGORITHM_SUBSTITUTION_POLICY_LEVELS, IdDialect, TermType
from kisao.exceptions import AlgorithmCannotBeSubstitutedException
from kisao.warnings import AlgorithmSubstitutedWarning
import os
import tempfile
import unittest


class UtilsTestCase(unittest.TestCase):
    def test_get_term_type(self):
        kisao = Kisao()

        term_type = utils.get_term_type(kisao.get_term('KISAO_0000000'))
        self.assertEqual(term_type, TermType.root)

        term_type = utils.get_term_type(kisao.get_term('KISAO_0000019'))
        self.assertEqual(term_type, TermType.algorithm)

        term_type = utils.get_term_type(kisao.get_term('KISAO_0000575'))
        self.assertEqual(term_type, TermType.algorithm)

        term_type = utils.get_term_type(kisao.get_term('KISAO_0000211'))
        self.assertEqual(term_type, TermType.algorithm_parameter)

        term_type = utils.get_term_type(kisao.get_term('KISAO_0000322'))
        self.assertEqual(term_type, TermType.algorithm_characteristic)

        with self.assertRaises(AttributeError):
            utils.get_term_type(None)

        with self.assertRaises(AttributeError):
            utils.get_term_type('KISAO_0000322')

    def test_get_url_for_term(self):
        term = Kisao().get_term('KISAO_0000029')
        self.assertEqual(
            utils.get_ols_url_for_term(term),
            'https://www.ebi.ac.uk/ols/ontologies/kisao/terms?iri=http%3A%2F%2Fwww.biomodels.net%2Fkisao%2FKISAO%23KISAO_0000029')

    def test_sets_of_algorithms_disjoint(self):
        kisao = Kisao()
        alg_sets = [
            utils.get_ode_algorithms(),
            utils.get_sde_algorithms(),
            utils.get_steadystate_algorithms(),
            utils.get_pde_algorithms(),
            utils.get_gillespie_like_algorithms(exact=True, approximate=False),
            utils.get_gillespie_like_algorithms(exact=False, approximate=True),
            utils.get_rule_based_algorithms(),
            utils.get_flux_balance_algorithms(),
            utils.get_logical_simulation_algorithms(),
            utils.get_logical_stable_state_search_algorithms(),
            utils.get_logical_trap_space_search_algorithms(),
            # utils.get_hybrid_algorithms(),
        ]

        all_algs_set = set()
        all_algs_list = []
        for alg_set in alg_sets:
            all_algs_set.update(alg_set)
            all_algs_list.extend(alg_set)

        self.assertEqual(len(all_algs_set), len(all_algs_list))

    def test_ode_algorithms(self):
        kisao = Kisao()
        terms = utils.get_ode_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000019'), terms)  # CVODE
        self.assertIn(kisao.get_term('KISAO_0000030'), terms)  # Euler forward
        self.assertIn(kisao.get_term('KISAO_0000086'), terms)  # Fehlberg method
        self.assertIn(kisao.get_term('KISAO_0000088'), terms)  # LSODA
        self.assertIn(kisao.get_term('KISAO_0000560'), terms)  # LSODA/LSODAR hybrid method
        self.assertIn(kisao.get_term('KISAO_0000355'), terms)  # DASPK
        self.assertIn(kisao.get_term('KISAO_0000283'), terms)  # IDA

        self.assertNotIn(kisao.get_term('KISAO_0000499'), terms)  # DFBA

        self.assertEqual(terms.intersection(utils.get_hybrid_algorithms()), set())  # disjoint from hybrid terms
        self.assertEqual(terms.intersection(utils.get_gillespie_like_algorithms(
            exact=True, approximate=False)), set())  # disjoint from Gillespie-like terms
        self.assertEqual(terms.intersection(utils.get_gillespie_like_algorithms(
            exact=False, approximate=True)), set())  # disjoint from Gillespie-like terms

    def test_gillespie_like_algorithms(self):
        kisao = Kisao()

        # exact algorithms
        exact_terms = utils.get_gillespie_like_algorithms(exact=True, approximate=False)

        self.assertIn(kisao.get_term('KISAO_0000610'), exact_terms)  # Composite-rejection stochastic simulation algorithm
        self.assertIn(kisao.get_term('KISAO_0000027'), exact_terms)  # Gibson-Bruck next reaction algorithm
        self.assertIn(kisao.get_term('KISAO_0000586'), exact_terms)  # Gibson-Bruck next reaction algorithm with indexed priority queue
        self.assertIn(kisao.get_term('KISAO_0000029'), exact_terms)  # Gillespie direct algorithm
        self.assertIn(kisao.get_term('KISAO_0000015'), exact_terms)  # Gillespie first reaction algorithm
        self.assertIn(kisao.get_term('KISAO_0000606'), exact_terms)  # Hierarchical Stochastic Simulation Algorithm
        self.assertIn(kisao.get_term('KISAO_0000329'), exact_terms)  # constant-time kinetic Monte Carlo algorithm
        self.assertIn(kisao.get_term('KISAO_0000331'), exact_terms)  # exact R-leaping algorithm
        self.assertIn(kisao.get_term('KISAO_0000038'), exact_terms)  # sorting stochastic simulation algorithm
        self.assertIn(kisao.get_term('KISAO_0000003'), exact_terms)  # weighted stochastic simulation algorithm

        self.assertNotIn(kisao.get_term('KISAO_0000581'), exact_terms)  # BKMC

        # approximate
        approx_terms = utils.get_gillespie_like_algorithms(exact=False, approximate=True)

        self.assertIn(kisao.get_term('KISAO_0000323'), approx_terms)  # equation-free probabilistic steady-state approximation
        self.assertIn(kisao.get_term('KISAO_0000039'), approx_terms)  # tau-leaping method
        self.assertIn(kisao.get_term('KISAO_0000324'), approx_terms)  # nested stochastic simulation algorithm
        self.assertIn(kisao.get_term('KISAO_0000323'), approx_terms)  # equation-free probabilistic steady-state approximation
        self.assertIn(kisao.get_term('KISAO_0000350'), approx_terms)  # probability-weighted dynamic Monte Carlo method
        self.assertIn(kisao.get_term('KISAO_0000028'), approx_terms)  # slow-scale stochastic simulation algorithm
        self.assertIn(kisao.get_term('KISAO_0000082'), approx_terms)  # k-alpha leaping method
        self.assertIn(kisao.get_term('KISAO_0000330'), approx_terms)  # R-leaping algorithm

        self.assertNotIn(kisao.get_term('KISAO_0000581'), approx_terms)  # BKMC
        self.assertNotIn(kisao.get_term('KISAO_0000619'), approx_terms)  # emc-sim
        self.assertNotIn(kisao.get_term('KISAO_0000618'), approx_terms)  # bunker

        # exact and approximate disjoint
        tau_leaping_terms = utils.get_tau_leaping_algorithms()
        self.assertEqual(exact_terms.intersection(approx_terms), set())  # exact, approximate disjoint
        self.assertEqual(exact_terms.intersection(tau_leaping_terms), set())  # exact disjoint from tau leaping
        self.assertEqual(tau_leaping_terms.difference(approx_terms), set())  # all tau leaping algorithms are approximate

        # none, all terms
        none_terms = utils.get_gillespie_like_algorithms(exact=False, approximate=False)
        self.assertEqual(none_terms, set())

        all_terms = utils.get_gillespie_like_algorithms(exact=True, approximate=True)
        self.assertEqual(
            (exact_terms | approx_terms).difference(all_terms),
            set())

    def test_sde_algorithms(self):
        kisao = Kisao()
        odes = utils.get_ode_algorithms()
        sdes = utils.get_sde_algorithms()
        pdes = utils.get_pde_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000564'), sdes)  # stochastic Runge-Kutta method
        self.assertIn(kisao.get_term('KISAO_0000287'), sdes)  # Milstein method
        self.assertIn(kisao.get_term('KISAO_0000286'), sdes)  # Euler-Maruyama method

        self.assertEqual(sdes.intersection(odes), set())
        self.assertEqual(sdes.intersection(pdes), set())

    def test_dae_algorithms(self):
        kisao = Kisao()
        daes = utils.get_dae_algorithms()
        odes = utils.get_ode_algorithms()

        self.assertNotIn(kisao.get_term('KISAO_0000019'), daes)  # CVODE
        self.assertNotIn(kisao.get_term('KISAO_0000030'), daes)  # Euler forward
        self.assertIn(kisao.get_term('KISAO_0000355'), daes)     # DASPK
        self.assertIn(kisao.get_term('KISAO_0000283'), daes)     # IDA

        self.assertNotIn(kisao.get_term('KISAO_0000499'), daes)  # DFBA

        self.assertEqual(daes.intersection(odes), daes)  # subset of ODE algorithms
        self.assertEqual(daes.intersection(utils.get_gillespie_like_algorithms(
            exact=True, approximate=False)), set())  # disjoint from Gillespie-like terms
        self.assertEqual(daes.intersection(utils.get_gillespie_like_algorithms(
            exact=False, approximate=True)), set())  # disjoint from Gillespie-like terms

    def test_steadystate_algorithms(self):
        kisao = Kisao()
        terms = utils.get_steadystate_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000407'), terms)  # steady state root-finding algorithm
        self.assertIn(kisao.get_term('KISAO_0000568'), terms)  # NLEQ1
        self.assertIn(kisao.get_term('KISAO_0000569'), terms)  # NLEQ2
        self.assertIn(kisao.get_term('KISAO_0000413'), terms)  # Exact Newton Method

        self.assertNotIn(kisao.get_term('KISAO_0000499'), terms)  # DFBA

        self.assertEqual(terms.intersection(utils.get_hybrid_algorithms()), set())  # disjoint from hybrid terms
        self.assertEqual(terms.intersection(utils.get_gillespie_like_algorithms(
            exact=True, approximate=False)), set())  # disjoint from Gillespie-like terms
        self.assertEqual(terms.intersection(utils.get_ode_algorithms()), set())  # disjoint from ODE terms

    def test_pde_algorithms(self):
        kisao = Kisao()
        odes = utils.get_ode_algorithms()
        sdes = utils.get_sde_algorithms()
        pdes = utils.get_pde_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000285'), pdes)  # finite volume method

        self.assertEqual(pdes.intersection(odes), set())
        self.assertEqual(pdes.intersection(sdes), set())

    def test_rule_based_algorithms(self):
        kisao = Kisao()
        terms = utils.get_rule_based_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000263'), terms)  # NFSim agent-based simulation method
        self.assertIn(kisao.get_term('KISAO_0000362'), terms)  # implicit-state Doob-Gillespie algorithm

    def test_flux_balance_algorithms(self):
        kisao = Kisao()
        terms = utils.get_flux_balance_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000499'), terms)  # dynamic flux balance analysis
        self.assertIn(kisao.get_term('KISAO_0000608'), terms)  # Hierarchical flux balance analysis
        self.assertIn(kisao.get_term('KISAO_0000592'), terms)  # dynamic rFBA

    def test_logical_simulation_algorithms(self):
        kisao = Kisao()
        terms = utils.get_logical_simulation_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000450'), terms)  # asynchronous logical model simulation method
        self.assertIn(kisao.get_term('KISAO_0000581'), terms)  # BKMC
        self.assertIn(kisao.get_term('KISAO_0000449'), terms)  # synchronous logical model simulation method

    def test_get_logical_stable_state_search_algorithms(self):
        kisao = Kisao()
        terms = utils.get_logical_stable_state_search_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000659'), terms)  # Naldi MDD logical model stable state search method

    def test_get_logical_trap_space_search_algorithms(self):
        kisao = Kisao()
        terms = utils.get_logical_trap_space_search_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000663'), terms)  # BDD logical model trap space identificatio method
        self.assertIn(kisao.get_term('KISAO_0000662'), terms)  # Klarner ASP logical model trap space identification method

    def test_get_substitutable_algorithms_for_policy(self):
        kisao = Kisao()

        cvode = kisao.get_term('KISAO_0000019')
        lsoda = kisao.get_term('KISAO_0000088')
        lsoda_lsodar_hybrid = kisao.get_term('KISAO_0000560')
        gillespie_direct = kisao.get_term('KISAO_0000029')
        gillespie_first = kisao.get_term('KISAO_0000015')
        nrm = kisao.get_term('KISAO_0000027')
        tau_leaping = kisao.get_term('KISAO_0000039')
        binomial_tau_leaping = kisao.get_term('KISAO_0000074')
        poisson_tau_leaping = kisao.get_term('KISAO_0000040')
        fva = kisao.get_term('KISAO_0000526')
        fba = kisao.get_term('KISAO_0000437')
        sync_alg = kisao.get_term('KISAO_0000449')
        async_alg = kisao.get_term('KISAO_0000450')

        self.assertEqual(utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.NONE), set([cvode]))
        self.assertEqual(utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.SAME_METHOD), set([cvode]))
        self.assertEqual(utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.SAME_MATH), set([cvode]))
        self.assertIn(cvode, utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS))
        self.assertIn(lsoda, utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS))
        self.assertIn(lsoda_lsodar_hybrid, utils.get_substitutable_algorithms_for_policy(
            cvode, AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS))
        self.assertNotIn(fva, utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS))
        self.assertNotIn(fba, utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.SAME_FRAMEWORK))
        self.assertIn(fva, utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.ANY))
        self.assertIn(fba, utils.get_substitutable_algorithms_for_policy(cvode, AlgorithmSubstitutionPolicy.ANY))

        self.assertEqual(utils.get_substitutable_algorithms_for_policy(
            gillespie_direct, AlgorithmSubstitutionPolicy.NONE), set([gillespie_direct]))
        self.assertEqual(utils.get_substitutable_algorithms_for_policy(gillespie_direct,
                                                                       AlgorithmSubstitutionPolicy.SAME_METHOD), set([gillespie_direct]))
        self.assertIn(gillespie_direct, utils.get_substitutable_algorithms_for_policy(
            gillespie_direct, AlgorithmSubstitutionPolicy.SAME_MATH))
        self.assertIn(gillespie_first, utils.get_substitutable_algorithms_for_policy(
            gillespie_direct, AlgorithmSubstitutionPolicy.SAME_MATH))
        self.assertIn(nrm, utils.get_substitutable_algorithms_for_policy(gillespie_direct, AlgorithmSubstitutionPolicy.SAME_MATH))
        self.assertNotIn(tau_leaping, utils.get_substitutable_algorithms_for_policy(
            gillespie_direct, AlgorithmSubstitutionPolicy.SAME_MATH))
        self.assertNotIn(tau_leaping, utils.get_substitutable_algorithms_for_policy(
            gillespie_direct, AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS))
        self.assertIn(tau_leaping, utils.get_substitutable_algorithms_for_policy(
            gillespie_direct, AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS))
        self.assertIn(gillespie_direct, utils.get_substitutable_algorithms_for_policy(
            tau_leaping, AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS))

        self.assertEqual(utils.get_substitutable_algorithms_for_policy(
            binomial_tau_leaping,
            AlgorithmSubstitutionPolicy.NONE), set([binomial_tau_leaping]))
        self.assertEqual(utils.get_substitutable_algorithms_for_policy(
            binomial_tau_leaping,
            AlgorithmSubstitutionPolicy.SAME_METHOD), set([binomial_tau_leaping]))
        self.assertEqual(utils.get_substitutable_algorithms_for_policy(
            binomial_tau_leaping,
            AlgorithmSubstitutionPolicy.SAME_MATH), set([binomial_tau_leaping]))
        self.assertIn(poisson_tau_leaping, utils.get_substitutable_algorithms_for_policy(
            binomial_tau_leaping, AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS))

        self.assertEqual(utils.get_substitutable_algorithms_for_policy(fba, AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS), set([fba]))
        with self.assertRaises(NotImplementedError):
            utils.get_substitutable_algorithms_for_policy(fba, AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES)
        self.assertIn(fva, utils.get_substitutable_algorithms_for_policy(fba, AlgorithmSubstitutionPolicy.SAME_FRAMEWORK))

        self.assertEqual(utils.get_substitutable_algorithms_for_policy(
            sync_alg, AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS), set([sync_alg]))
        self.assertIn(sync_alg, utils.get_substitutable_algorithms_for_policy(
            async_alg, AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS))
        self.assertIn(sync_alg, utils.get_substitutable_algorithms_for_policy(async_alg, AlgorithmSubstitutionPolicy.SAME_FRAMEWORK))
        self.assertIn(async_alg, utils.get_substitutable_algorithms_for_policy(sync_alg, AlgorithmSubstitutionPolicy.SAME_FRAMEWORK))

        # check that substition sets get larger with increasing policy levels
        alg_sets = [
            ('ODE', utils.get_ode_algorithms()),
            ('Gillespie', utils.get_gillespie_like_algorithms(exact=True, approximate=False)),
            ('tau', utils.get_tau_leaping_algorithms()),
            ('SDE', utils.get_sde_algorithms()),
            ('PDE', utils.get_pde_algorithms()),
            ('flux', utils.get_flux_balance_algorithms()),
            ('logical-simulation', utils.get_logical_simulation_algorithms()),
            ('logical-steady-state-search', utils.get_logical_stable_state_search_algorithms()),
            ('logical-trap-space-identification', utils.get_logical_trap_space_search_algorithms()),
        ]
        for id, alg_set in alg_sets:
            alg = list(alg_set)[0]
            prev_alt_algs = set()
            for policy in ALGORITHM_SUBSTITUTION_POLICY_LEVELS.keys():
                if (
                    id == 'flux'
                    and (ALGORITHM_SUBSTITUTION_POLICY_LEVELS[policy] >=
                         ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS])
                    and (ALGORITHM_SUBSTITUTION_POLICY_LEVELS[policy] <=
                         ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.SIMILAR_VARIABLES])
                ):
                    continue

                alt_algs = utils.get_substitutable_algorithms_for_policy(alg, policy)
                assert prev_alt_algs.difference(alt_algs) == set(), 'Substition error'
                prev_alt_algs = alt_algs

    def test_get_substitutable_algorithms(self):
        kisao = Kisao()
        cvode = kisao.get_term('KISAO_0000019')
        lsoda = kisao.get_term('KISAO_0000088')

        alt_algs = utils.get_substitutable_algorithms(cvode)
        self.assertEqual(alt_algs[cvode], AlgorithmSubstitutionPolicy.SAME_METHOD)
        self.assertEqual(alt_algs[lsoda], AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS)

        alt_algs_by_policy = utils.group_substitutable_algorithms_by_policy(alt_algs)
        self.assertEqual(alt_algs_by_policy[AlgorithmSubstitutionPolicy.SAME_METHOD], set([cvode]))
        self.assertEqual(alt_algs_by_policy[AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS],
                         utils.get_ode_algorithms() - set([cvode]))
        self.assertEqual(alt_algs_by_policy, {
            AlgorithmSubstitutionPolicy.SAME_METHOD: set([cvode]),
            AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS: utils.get_ode_algorithms() - set([cvode]),
        })

        gillespie_direct = kisao.get_term('KISAO_0000029')
        alt_algs = utils.get_substitutable_algorithms(gillespie_direct)
        alt_algs_by_policy = utils.group_substitutable_algorithms_by_policy(alt_algs)
        self.assertEqual(alt_algs_by_policy[AlgorithmSubstitutionPolicy.SAME_METHOD], set([gillespie_direct]))
        self.assertEqual(alt_algs_by_policy[AlgorithmSubstitutionPolicy.SAME_MATH],
                         utils.get_gillespie_like_algorithms(exact=True) - set([gillespie_direct]))
        self.assertEqual(alt_algs_by_policy[AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS],
                         utils.get_tau_leaping_algorithms())
        self.assertEqual(alt_algs_by_policy, {
            AlgorithmSubstitutionPolicy.SAME_METHOD: set([gillespie_direct]),
            AlgorithmSubstitutionPolicy.SAME_MATH: utils.get_gillespie_like_algorithms(exact=True) - set([gillespie_direct]),
            AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS: utils.get_tau_leaping_algorithms(),
        })

    def test_get_preferred_substitute_algorithm(self):
        kisao = Kisao()

        cvode = kisao.get_term('KISAO_0000019')
        lsoda = kisao.get_term('KISAO_0000088')
        lsoda_lsodar_hybrid = kisao.get_term('KISAO_0000560')
        euler_method = kisao.get_term('KISAO_0000030')

        self.assertEqual(utils.get_preferred_substitute_algorithm(lsoda, [cvode, lsoda, lsoda_lsodar_hybrid, euler_method]),
                         lsoda)
        with self.assertWarns(AlgorithmSubstitutedWarning):
            self.assertEqual(utils.get_preferred_substitute_algorithm(lsoda, [cvode, lsoda_lsodar_hybrid, euler_method]),
                             cvode)
        with self.assertRaises(AlgorithmCannotBeSubstitutedException):
            self.assertEqual(utils.get_preferred_substitute_algorithm(lsoda, [cvode], substitution_policy=AlgorithmSubstitutionPolicy.SAME_METHOD),
                             cvode)
        with self.assertRaises(AlgorithmCannotBeSubstitutedException):
            utils.get_preferred_substitute_algorithm(lsoda, [])

        self.assertEqual(
            utils.get_preferred_substitute_algorithm_by_ids(
                'KISAO_0000088',
                ['KISAO_0000019', 'KISAO_0000088', 'KISAO_0000560', 'KISAO_0000030']),
            'KISAO_0000088')
        self.assertEqual(
            utils.get_preferred_substitute_algorithm_by_ids(
                'KISAO_0000088',
                ['KISAO_0000019', 'KISAO_0000560', 'KISAO_0000030']),
            'KISAO_0000019')

        self.assertEqual(
            utils.get_preferred_substitute_algorithm_by_ids(
                88,
                [19, 88, 560, 30],
                id_dialect=IdDialect.integer),
            88)
        self.assertEqual(
            utils.get_preferred_substitute_algorithm_by_ids(
                88,
                [19, 560, 30],
                id_dialect=IdDialect.integer),
            19)

        self.assertEqual(
            utils.get_preferred_substitute_algorithm_by_ids(
                19,
                [19, 560, 30],
                id_dialect=IdDialect.integer,
                substitution_policy=AlgorithmSubstitutionPolicy.NONE),
            19)
        with self.assertRaises(AlgorithmCannotBeSubstitutedException):
            utils.get_preferred_substitute_algorithm_by_ids(
                88,
                [19, 560, 30],
                id_dialect=IdDialect.integer,
                substitution_policy=AlgorithmSubstitutionPolicy.NONE)

    def test_get_algorithm_substitution_matrix(self):
        fid, filename = tempfile.mkstemp()
        os.close(fid)

        report = utils.get_algorithm_substitution_matrix()
        report.to_csv(filename)

        self.assertEqual(report['KISAO_0000019']['CVODE']['KISAO_0000019']['CVODE'],
                         'SAME_METHOD')

        os.remove(filename)

if __name__ == "__main__":
    unittest.main()
