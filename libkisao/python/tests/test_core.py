from kisao.core import Kisao
from kisao.data_model import IdDialect
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

    def test_get_term(self):
        kisao = Kisao()
        kisao.get_term('KISAO_0000029')
        with self.assertRaises(ValueError):
            kisao.get_term('KISAO_9999999')
        with self.assertRaises(ValueError):
            kisao.get_term('KISAO_0000245')

    def test_get_relationship(self):
        kisao = Kisao()
        kisao.get_relationship('KISAO_0000245')
        with self.assertRaises(ValueError):
            kisao.get_relationship('KISAO_9999999')
        with self.assertRaises(ValueError):
            kisao.get_relationship('KISAO_0000029')

    def test_get_normalized_id(self):
        self.assertEqual(Kisao.get_normalized_id('KISAO_0000029'), 'KISAO_0000029')
        self.assertEqual(Kisao.get_normalized_id('KISAO:0000029'), 'KISAO_0000029')
        self.assertEqual(Kisao.get_normalized_id('29'), 'KISAO_0000029')
        self.assertEqual(Kisao.get_normalized_id(29), 'KISAO_0000029')
        with self.assertRaisesRegex(ValueError, 'is not an id'):
            Kisao.get_normalized_id('X')

    def test_get_term_id(self):
        kisao = Kisao()
        term = kisao.get_term('KISAO_0000450')

        self.assertEqual(kisao.get_term_id(term), 'KISAO_0000450')
        self.assertEqual(kisao.get_term_id(term, dialect=IdDialect.kisao), 'KISAO_0000450')
        self.assertEqual(kisao.get_term_id(term, dialect=IdDialect.sedml), 'KISAO:0000450')
        self.assertEqual(kisao.get_term_id(term, dialect=IdDialect.integer), 450)
        with self.assertRaises(NotImplementedError):
            kisao.get_term_id(term, dialect=None)

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

if __name__ == "__main__":
    unittest.main()
