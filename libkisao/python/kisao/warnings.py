""" Standard warnings

:Author: Jonathan Karr <karr@mssm.edu>
:Date: 2021-04-29
:Copyright: 2021, SED-ML Editors
:License: Apache 2.0
"""

__all__ = [
    'AlgorithmSubstitutedWarning',
]


class AlgorithmSubstitutedWarning(UserWarning):
    """ Warning that an alternative algorithm was used rather than the requested algorithm """
    pass  # pragma: no cover
