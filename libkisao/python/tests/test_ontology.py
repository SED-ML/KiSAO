from kisao import Kisao
from kisao.data_model import IdDialect
import re
import unittest
import warnings


class OntologyTestCase(unittest.TestCase):
    def test_ids_valid_and_unique(self):
        kisao = Kisao()
        onto = kisao.get_proto_ontology()
        id_pattern = r'^http://www\.biomodels\.net/kisao/KISAO#KISAO_\d{7,7}$'
        ids = set()
        duplicate_ids = set()
        invalid_ids = []
        for term in onto.terms():
            if term.id in ids:
                duplicate_ids.add(term.id)
            ids.add(term.id)
            if not re.match(id_pattern, term.id):
                invalid_ids.append(term.id)
        if invalid_ids:
            msg = 'Terms should have ids that follow the pattern `{}`. The following ids are invalid:\n  {}'.format(
                id_pattern, '\n  '.join(sorted(invalid_ids)))
            raise ValueError(msg)
        if duplicate_ids:
            msg = 'Terms should have unique ids. The following ids are duplicated:\n  {}'.format(
                '\n  '.join(sorted(duplicate_ids)))
            raise ValueError(msg)

    def test_term_ids_are_normalizable(self):
        kisao = Kisao()
        onto = kisao.get_proto_ontology()
        invalid_ids = []
        for term_id in onto:
            try:
                kisao_id = kisao.get_normalized_id(term_id.split('#')[1], dialect=IdDialect.kisao)
            except Exception:
                invalid_ids.append(term_id)
        if invalid_ids:
            msg = 'The following terms have invalid ids:\n  {}'.format(
                '\n  '.join(sorted(invalid_ids)))
            raise ValueError(msg)

    def test_terms_resolvable(self):
        kisao = Kisao()
        onto = kisao.get_proto_ontology()
        for term in onto.terms():
            for dialect in IdDialect.__members__.values():
                id = kisao.get_normalized_id(kisao.get_term_id(term, dialect), dialect)
                kisao.get_term(id)

    def test_terms_have_names(self):
        kisao = Kisao()
        onto = kisao.get_proto_ontology()
        invalid_terms = []
        for term in onto.terms():
            if not term.name:
                invalid_terms.append(term.id)
        if invalid_terms:
            msg = 'Each term must have a name (`rdfs:label`). The following terms do not have names:\n  {}'.format(
                '\n  '.join(sorted(invalid_terms)))
            raise ValueError(msg)

    def test_term_names_are_lowercased(self):
        kisao = Kisao()
        onto = kisao.get_proto_ontology()
        invalid_terms = []
        for term in onto.terms():
            if term.name and term.name[0].lower() != term.name[0]:
                invalid_terms.append(term.id)
        if invalid_terms:
            msg = (
                'Names of term should start with lowercase letters '
                '(except for proper names such as "Gillespie''s algorithm). '
                'The following terms may have invalid names:\n  {}'
            ).format('\n  '.join(sorted(invalid_terms)))
            warnings.warn(msg)
