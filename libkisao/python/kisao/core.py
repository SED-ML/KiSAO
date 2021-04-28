""" Utilities for working with the KiSAO ontology

:Author: Jonathan Karr <karr@mssm.edu>
:Date: 2021-04-28
:Copyright: 2021, SED-ML Editors
:License: Apache 2.0
"""

import pronto
import pkg_resources
import warnings


class Kisao(object):
    """ KiSAO ontology """
    _ontology = None

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

    def get_term_ids(self, terms):
        """ Get the ids of a list of KiSAO terms

        Args:
            terms (:obj:`list` of :obj:`pronto.Term`): terms

        Returns:
            :obj:`list` of :obj:`str`: ids of a list of KiSAO terms (e.g., KISAO_0000352)
        """
        base_iri = self.get_base_iri()
        return [term.id.partition(base_iri)[2] for term in terms]
