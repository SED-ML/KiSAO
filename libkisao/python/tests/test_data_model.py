from kisao.data_model import AlgorithmSubstitutionPolicy, ALGORITHM_SUBSTITUTION_POLICY_LEVELS
import unittest


class DataModelTestCase(unittest.TestCase):
    def test(self):
        self.assertLess(
            ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.NONE],
            ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.ANY],
        )
        self.assertLess(
            ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.SAME_METHOD],
            ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.SAME_MATH],
        )
        self.assertLess(
            ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.SAME_MATH],
            ALGORITHM_SUBSTITUTION_POLICY_LEVELS[AlgorithmSubstitutionPolicy.DISTINCT_APPROXIMATIONS],
        )
