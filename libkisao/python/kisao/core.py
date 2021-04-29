""" Utilities for working with the KiSAO ontology

:Author: Jonathan Karr <karr@mssm.edu>
:Date: 2021-04-28
:Copyright: 2021, SED-ML Editors
:License: Apache 2.0
"""

from .data_model import IdDialect
import pronto
import pkg_resources
import re
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

    @staticmethod
    def get_normalized_id(id, dialect=IdDialect.kisao):
        """ Normalize an id for a KiSAO term to the official pattern ``KISAO_\\d{7}``.

        The official id pattern for KiSAO terms is ``KISAO_\\d{7}``. This is often confused with ``KISAO:\\d{7}`` and ``\\d{7}``.
        This function automatically converts these other patterns to the offfical pattern.

        Args:
            id (:obj:`str` or :obj:`int`): offical KiSAO id with pattern ``"KISAO_\\d{7}"`` or
                a variant such as ``"KISAO:\\d{7}"``, ``"\\d{7}"`` (string), or ``\\d{7}`` (integer)
            dialect (:obj:`IdDialect`, optional): dialect for id

        Returns:
            :obj:`str`: normalized KiSAO id that follows the official pattern ``KISAO_\\d{7}``
        """
        unnormalized_id = id

        id = str(id)

        if id.startswith('KISAO:'):
            id = 'KISAO_' + id[6:]

        if re.match(r'\d+', id):
            id = 'KISAO_' + '0' * (7 - len(id)) + id

        if not re.match(r'KISAO_\d{7}', id):
            raise ValueError("'{}' is not an id for a KiSAO term.".format(unnormalized_id))

        return Kisao.get_id_in_dialect(id, dialect)

    @staticmethod
    def get_id_in_dialect(id, dialect):
        """ Get an id in a specific dialect

        Args:
            id (:obj:`str`): id e.g., (KISAO_0000019)
            dialect (:obj:`IdDialect`, optional): dialect for id

        Returns:
            :obj:`str` or :obj:`int`: id in the chosen dialect (e.g., KISAO_0000352)
        """
        if dialect == IdDialect.kisao:
            return id
        elif dialect == IdDialect.sedml:
            return id.replace('_', ':')
        elif dialect == IdDialect.integer:
            return int(id.partition('_')[2])
        else:
            raise NotImplementedError('Dialect {} is not supported.'.format(dialect))

    def get_term(self, id):
        """ Get a term by its id (e.g., ``KISAO_0000019``)

        Args:
            id (:obj:`str`): id (e.g., ``KISAO_0000019``)

        Returns:
            :obj:`pronto.Term`: term
        """
        base_iri = self.get_base_iri()
        try:
            return self._ontology.get_term(base_iri + self.get_normalized_id(id))
        except KeyError:
            raise ValueError('No KiSAO term has the id `{}`.'.format(id))

    def get_relationship(self, id):
        """ Get a relationship by its id (e.g., ``KISAO_0000245``)

        Args:
            id (:obj:`str`): id (e.g., ``KISAO_0000245``)

        Returns:
            :obj:`pronto.Relationship`: relationship
        """
        base_iri = self.get_base_iri()
        try:
            return self._ontology.get_relationship(base_iri + self.get_normalized_id(id))
        except KeyError:
            raise ValueError('No KiSAO relationship has the id `{}`.'.format(id))

    def get_term_id(self, term, dialect=IdDialect.kisao):
        """ Get the ids of a list of KiSAO terms

        Args:
            term (:obj:`list` of :obj:`pronto.Term`): terms
            dialect (:obj:`IdDialect`, optional): dialect for ids

        Returns:
            :obj:`str` or :obj:`int`: id of the KiSAO term in the chosen dialect (e.g., KISAO_0000352)
        """
        base_iri = self.get_base_iri()
        id = term.id.partition(base_iri)[2]
        return self.get_id_in_dialect(id, dialect=dialect)

    def get_term_ids(self, terms, dialect=IdDialect.kisao):
        """ Get the ids of a list of KiSAO terms

        Args:
            terms (:obj:`list` of :obj:`pronto.Term`): terms
            dialect (:obj:`IdDialect`, optional): dialect for ids

        Returns:
            :obj:`list` of :obj:`str` or :obj:`int`: ids of a list of KiSAO terms
                in the chosen dialect (e.g., KISAO_0000352)
        """
        return [self.get_term_id(term, dialect=dialect) for term in terms]
