from kisao import Kisao
import unittest


class TestCase(unittest.TestCase):
    def test_labels_are_unique(self):
        ontology = Kisao().get_proto_ontology()

        seen_labels = set()
        duplicatate_labels = set()
        for term in ontology.terms():
            label = term.name
            if label in seen_labels:
                duplicatate_labels.add(label)
            seen_labels.add(label)

        if duplicatate_labels:
            raise ValueError('Each term should have a unique label. The following labels are repeated:\n  {}'.format(
                '\n  '.join(sorted(duplicatate_labels))
            ))

    def test_sets_of_algorithms_disjoint(self):
        kisao = Kisao()
        alg_sets = [
            kisao.get_ode_algorithms(),
            kisao.get_sde_algorithms(),
            kisao.get_pde_algorithms(),
            kisao.get_gillespie_like_algorithms(exact=True, approximate=False),
            kisao.get_gillespie_like_algorithms(exact=False, approximate=True),
            kisao.get_rule_based_algorithms(),
            kisao.get_flux_balance_algorithms(),
            kisao.get_logical_algorithms(),
            # kisao.get_hybrid_algorithms(),
        ]

        all_algs_set = set()
        all_algs_list = []
        for alg_set in alg_sets:
            all_algs_set.update(alg_set)
            all_algs_list.extend(alg_set)

        self.assertEqual(len(all_algs_set), len(all_algs_list))

    def test_ode_algorithms(self):
        kisao = Kisao()
        terms = kisao.get_ode_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000019'), terms)  # CVODE
        self.assertIn(kisao.get_term('KISAO_0000030'), terms)  # Euler forward
        self.assertIn(kisao.get_term('KISAO_0000086'), terms)  # Fehlberg method
        self.assertIn(kisao.get_term('KISAO_0000088'), terms)  # LSODA
        self.assertIn(kisao.get_term('KISAO_0000560'), terms)  # LSODA/LSODAR hybrid method

        self.assertNotIn(kisao.get_term('KISAO_0000499'), terms)  # DFBA

        self.assertEqual(terms.intersection(kisao.get_hybrid_algorithms()), set())  # disjoint from hybrid terms
        self.assertEqual(terms.intersection(kisao.get_gillespie_like_algorithms(
            exact=True, approximate=False)), set())  # disjoint from Gillespie-like terms
        self.assertEqual(terms.intersection(kisao.get_gillespie_like_algorithms(
            exact=False, approximate=True)), set())  # disjoint from Gillespie-like terms

    def test_gillespie_like_algorithms(self):
        kisao = Kisao()

        # exact algorithms
        exact_terms = kisao.get_gillespie_like_algorithms(exact=True, approximate=False)

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
        approx_terms = kisao.get_gillespie_like_algorithms(exact=False, approximate=True)

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
        tau_leaping_terms = kisao.get_tau_leaping_algorithms()
        self.assertEqual(exact_terms.intersection(approx_terms), set())  # exact, approximate disjoint
        self.assertEqual(exact_terms.intersection(tau_leaping_terms), set())  # exact disjoint from tau leaping
        self.assertEqual(tau_leaping_terms.difference(approx_terms), set())  # all tau leaping algorithms are approximate

        # none, all terms
        none_terms = kisao.get_gillespie_like_algorithms(exact=False, approximate=False)
        self.assertEqual(none_terms, set())

        all_terms = kisao.get_gillespie_like_algorithms(exact=True, approximate=True)
        self.assertEqual(
            (exact_terms | approx_terms).difference(all_terms),
            set())

    def test_rule_based_algorithms(self):
        kisao = Kisao()
        terms = kisao.get_rule_based_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000263'), terms)  # NFSim agent-based simulation method
        self.assertIn(kisao.get_term('KISAO_0000362'), terms)  # implicit-state Doob-Gillespie algorithm

    def test_sde_algorithms(self):
        kisao = Kisao()
        odes = kisao.get_ode_algorithms()
        sdes = kisao.get_sde_algorithms()
        pdes = kisao.get_pde_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000564'), sdes)  # stochastic Runge-Kutta method
        self.assertIn(kisao.get_term('KISAO_0000287'), sdes)  # Milstein method
        self.assertIn(kisao.get_term('KISAO_0000286'), sdes)  # Euler-Maruyama method

        self.assertEqual(sdes.intersection(odes), set())
        self.assertEqual(sdes.intersection(pdes), set())

    def test_pde_algorithms(self):
        kisao = Kisao()
        odes = kisao.get_ode_algorithms()
        sdes = kisao.get_sde_algorithms()
        pdes = kisao.get_pde_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000285'), pdes)  # finite volume method

        self.assertEqual(pdes.intersection(odes), set())
        self.assertEqual(pdes.intersection(sdes), set())

    def test_rule_based_algorithms(self):
        kisao = Kisao()
        terms = kisao.get_rule_based_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000263'), terms)  # NFSim agent-based simulation method
        self.assertIn(kisao.get_term('KISAO_0000362'), terms)  # implicit-state Doob-Gillespie algorithm

    def test_flux_balance_algorithms(self):
        kisao = Kisao()
        terms = kisao.get_flux_balance_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000499'), terms)  # dynamic flux balance analysis
        self.assertIn(kisao.get_term('KISAO_0000608'), terms)  # Hierarchical flux balance analysis
        self.assertIn(kisao.get_term('KISAO_0000592'), terms)  # dynamic rFBA

    def test_logical_algorithms(self):
        kisao = Kisao()
        terms = kisao.get_logical_algorithms()

        self.assertIn(kisao.get_term('KISAO_0000450'), terms)  # asynchronous logical model simulation method
        self.assertIn(kisao.get_term('KISAO_0000581'), terms)  # BKMC
        self.assertIn(kisao.get_term('KISAO_0000449'), terms)  # synchronous logical model simulation method
