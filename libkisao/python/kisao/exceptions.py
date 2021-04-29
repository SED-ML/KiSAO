""" Standard exceptions

:Author: Jonathan Karr <karr@mssm.edu>
:Date: 2021-04-29
:Copyright: 2021, SED-ML Editors
:License: Apache 2.0
"""


__all__ = [
    'AlgorithmCannotBeSubstitutedException',
]


class AlgorithmCannotBeSubstitutedException(Exception):
    """ Exception that the algorithm substitution policy does not allow an algorithm to be substituted """
    pass  # pragma: no cover
