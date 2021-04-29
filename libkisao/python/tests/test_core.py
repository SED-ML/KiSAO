from kisao.core import Kisao
import unittest


class CoreTestCase(unittest.TestCase):
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

    def test_get_term_ids(self):
        kisao = Kisao()
        terms = [
            kisao.get_term('KISAO_0000450'),
            kisao.get_term('KISAO_0000581'),
            kisao.get_term('KISAO_0000449'),
        ]
        term_ids = kisao.get_term_ids(terms)

        self.assertIn('KISAO_0000450', term_ids)
        self.assertIn('KISAO_0000581', term_ids)
        self.assertIn('KISAO_0000449', term_ids)
