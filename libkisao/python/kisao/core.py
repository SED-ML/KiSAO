import pronto
import pkg_resources
import warnings


class Kisao(object):
    """ KiSAO ontology """
    _ontology = None

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

    def __init__(self):  # __new__ always a classmethod
        if self.__class__._ontology is None:
            filename = pkg_resources.resource_filename('kisao', 'kisao.owl')
            with warnings.catch_warnings():
                warnings.simplefilter("ignore", pronto.utils.warnings.SyntaxWarning)
                warnings.simplefilter("ignore", pronto.utils.warnings.NotImplementedWarning)
                self.__class__._ontology = pronto.Ontology(filename)

    def get_proto_ontology(self):
        """ Get a proto object for the ontology

        Returns:
            :obj:`pronto.Ontology`: pronto object for the ontology
        """
        return self._ontology

    def get_base_iri(self):
        """ Get the base IRI for the ontology

        Returns:
            :obj:`str`: base IRI for the ontology
        """
        return self._ontology.metadata.ontology

    def get_term(self, id):
        """ Get a term by its id (e.g., ``KISAO_0000019``)

        Args:
            id (:obj:`str`): id (e.g., ``KISAO_0000019``)

        Returns:
            :obj:`pronto.Term`: term
        """
        base_iri = self.get_base_iri()
        return self._ontology.get_term(base_iri + id)

    def get_relationship(self, id):
        """ Get a relationship by its id (e.g., ``KISAO_0000245``)

        Args:
            id (:obj:`str`): id (e.g., ``KISAO_0000245``)

        Returns:
            :obj:`pronto.Relationship`: relationship
        """
        base_iri = self.get_base_iri()
        return self._ontology.get_relationship(base_iri + id)

    def get_terms_with_characteristics(self, parent_ids, characteristic_ids=None):
        """ Get the subclasses of one or more terms which, optionally, have one or more characteristics
            (e.g., ``'has characteristic' some 'ordinary differential equation problem'``)

        Args:
            parent_ids (:obj:`list` of :obj:`str`): ids of the parent terms (e.g., ``KISAO_0000000``)
            characteristic_ids (:obj:`list` of :obj:`str`): ids of the characteristics (e.g., ``KISAO_0000374``)

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        characteristic_rel = self.get_relationship(self.ID_HAS_CHARACTERISTIC_RELATIONSHIP)

        possible_terms = set()
        for parent_id in parent_ids:
            possible_terms.update(self.get_term(parent_id).subclasses())
        terms = set()

        for term in possible_terms:
            if term in terms:
                continue
            has_characteristics = True
            for characteristic_id in characteristic_ids:
                characteristic = self.get_term(characteristic_id)
                term_characteristics = term.relationships.get(characteristic_rel)
                if term_characteristics is None or characteristic not in term_characteristics:
                    has_characteristics = False
                    break
            if has_characteristics:
                terms.update(term.subclasses())

        return terms

    def get_ode_algorithms(self):
        """ Get the terms for ODE integration algorithms::

            'modelling simulation algorithm' and 'has characteristic' some 'ordinary differential equation problem'

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        return self.get_terms_with_characteristics([self.ID_ALGORITHM], [self.ID_ODE_PROBLEM_CHARACTERISTIC])

    def get_gillespie_like_algorithms(self, exact=True, approximate=False):
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
            characteristics = [self.ID_EXACT_SOLUTION_CHARACTERISTIC]
        elif approximate:
            characteristics = [self.ID_APPROXIMATE_SOLUTION_CHARACTERISTIC]

        return self.get_terms_with_characteristics([self.ID_GILLESPIE_LIKE_ALGORITHM], characteristics)

    def get_tau_leaping_algorithms(self):
        """ Get the terms for tau-leaping algorithms (KISAO_0000039).::

            'tau-leaping method'

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        return self.get_terms_with_characteristics([self.ID_TAU_LEAPING_ALGORITHM], [])

    def get_rule_based_algorithms(self):
        """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

            'rule-based simulation method'

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        return self.get_terms_with_characteristics([self.ID_RULE_BASED_ALGORITHM], [])

    def get_sde_algorithms(self):
        """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

            'rule-based simulation method'

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        return self.get_terms_with_characteristics([self.ID_ALGORITHM], [self.ID_SDE_PROBLEM_CHARACTERISTIC])

    def get_pde_algorithms(self):
        """ Get the terms for rule-based simulation algorithms (KISAO_0000363).::

            'rule-based simulation method'

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        return self.get_terms_with_characteristics([self.ID_ALGORITHM], [self.ID_PDE_PROBLEM_CHARACTERISTIC])

    def get_flux_balance_algorithms(self):
        """ Get the terms for flux balance algorithms (KISAO_0000622).::

            'flux balance method'

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        return self.get_terms_with_characteristics([self.ID_FLUX_BALANCE_ALGORITHM], [])

    def get_logical_algorithms(self):
        """ Get the terms for logical simulation algorithms (KISAO_0000448).::

            'logical simulation method'

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        return self.get_terms_with_characteristics([self.ID_LOGICAL_ALGORITHM], [])

    def get_hybrid_algorithms(self):
        """ Get the terms for hybrid algorithms (KISAO_0000352).::

            'hybrid method'

        Returns:
            :obj:`set` of :obj:`pronto.Term`: terms
        """
        return self.get_terms_with_characteristics([self.ID_HYBRID_ALGORITHM], [])
