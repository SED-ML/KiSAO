# KiSAO

Python package for working with the the [Kinetic Simulation Algorithm Ontology](http://co.mbine.org/standards/kisao) (KiSAO), an ontology of algorithms for simulating and analyzing biological models, as well as the characteristics of these algorithms, their input parameters, and their outputs.

## Installing this package

### Requirements

* Python >= 3.7
* pip

### Installation
Please run the following command to install this package:
```
pip install kisao
```

To generate a matrix of the substitutability among algorithms, install this package with the `substitutability-matrix` option:
```
pip install kisao[substitutability-matrix]
```

## Tutorial

```python
from kisao import Kisao
from kisao import utils
from kisao.data_model import AlgorithmSubstitutionPolicy

# load the ontology
kisao = Kisao()

# get a term
term = kisao.get_term('KISAO_0000019')

# get the name of the term
term.name
>> 'CVODE'

# get sets of methods
algs = utils.get_ode_algorithms()
sorted([alg.name for alg in algs])[0:5]
>> [
      'Adams method',
      'Adams predictor-corrector method',
      'Adams-Bashforth method',
      'Adams-Moulton method',
      'Bader-Deuflhard method',
   ]

algs = utils.get_sde_algorithms()
algs = utils.get_pde_algorithms()
algs = utils.get_tau_leaping_algorithms()
algs = utils.get_ode_algorithms()
algs = utils.get_gillespie_like_algorithms()
algs = utils.get_tau_leaping_algorithms()
algs = utils.get_rule_based_algorithms()
algs = utils.get_sde_algorithms()
algs = utils.get_pde_algorithms()
algs = utils.get_flux_balance_algorithms()
algs = utils.get_logical_algorithms()
algs = utils.get_hybrid_algorithms()

# get a set of substitutable algorithms for a specific substitution policy
cvode = kisao.get_term('KISAO_0000019')
euler_forward = kisao.get_term('KISAO_0000030')
lsoda = kisao.get_term('KISAO_0000088')
lsodar = kisao.get_term('KISAO_0000089')
fba = kisao.get_term('KISAO_0000437')
fva = kisao.get_term('KISAO_0000526')

alt_algs = utils.get_substitutable_algorithms_for_policy(cvode,
    substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS)
sorted([alt_alg.name for alt_alg in alt_algs])[0:5]
>> [
      'Adams method',
      'Adams predictor-corrector method',
      'Adams-Bashforth method',
      'Adams-Moulton method',
      'Bader-Deuflhard method',
   ]

# get a preferred substitution for an algorithm
requested_alg = lsoda
implemented_algs_in_preferred_order = [cvode, lsoda, lsodar, euler_forward]
alt_alg = utils.get_perferred_substitute_algorithm(requested_alg, implemented_algs_in_preferred_order,
    substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS)
alt_alg.name
>> 'LSODA'

requested_alg = lsoda
implemented_algs_in_preferred_order = [cvode, euler_forward]
alt_alg = utils.get_perferred_substitute_algorithm(requested_alg, implemented_algs_in_preferred_order,
    substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS)
alt_alg.name
>> 'CVODE'

requested_alg = lsoda
implemented_algs_in_preferred_order = [fba, fva]
alt_alg = utils.get_perferred_substitute_algorithm(requested_alg, implemented_algs_in_preferred_order,
    substitution_policy=AlgorithmSubstitutionPolicy.SIMILAR_APPROXIMATIONS)
alt_alg
>> None
```

## Browsing KiSAO

KiSAO can be browsed through [BioPortal](https://bioportal.bioontology.org/ontologies/KISAO) and [OLS](https://www.ebi.ac.uk/ols/ontologies/kisao).

## Browsing the substitutability of algorithms catalogued by KiSAO

A matrix of the substitutability of algorithms catalogued by KiSAO is available [here](https://github.com/SED-ML/KiSAO/blob/dev/libkisao/python/docs/algorithm-substitutability.csv). The documentation for this package describes the queries and rules used to define this matrix.

## Contributing to KiSAO

Please see the [KiSAO repository](https://github.com/SED-ML/KiSAO/) for information about contributing to KiSAO and this package.

## License

This package is released under [Artistic License 2.0](https://github.com/SED-ML/KiSAO/blob/dev/LICENSE).
