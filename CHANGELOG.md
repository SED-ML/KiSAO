# Changelog
## 2.30 (OWL 2)
- Added and unified aggregation functions for SED-ML L1V4
  - `maximum ignoring NaN` (KISAO_0000828)
  - `minimum ignoring NaN` (KISAO_0000829
  - `mean ignoring NaN` (KISAO_0000825)
  - `standard deviation ignoring NaN` (KISAO_0000826)
  - `standard error ignoring NaN` (KISAO_0000827)
  - `maximum` (KISAO_0000830)
  - `minimum` (KISAO_0000840)
  - `mean` (KISAO_0000841)
  - `standard deviation` (KISAO_0000842)
  - `standard error`  (KISAO_0000843)
  - `sum ignoring NaN` (KISAO_0000844)
  - `sum` (KISAO_0000845) 
  - `product ignoring NaN` (KISAO_0000846)
  - `product` (KISAO_0000847)
  - `cumulative sum` (KISAO_0000848)
  - `cumulative sum  ignoring NaN` (KISAO_0000849)
  - `cumulative product ignoring NaN` (KISAO_0000850)
  - `cumulative product` (KISAO_0000851)
  - `count ignoring NaN` (KISAO_0000852): number of non-zero elements, ignoring NaN entries
  - `count` (KISAO_0000853): number of non-zero elements, ignoring NaN entries
  - `length ignoring NaN` (KISAO_0000854): number of elements, ignoring NaN entries
  - `length` (KISAO_0000855): number of elements, ignoring NaN entries
  - `median ignoring NaN` (KISAO_0000856)
  - `median` (KISAO_0000857)
  - `variance ignoring NaN` (KISAO_0000858)
  - `variance` (KISAO_0000859)

## 2.29 (OWL 2)
- Added term for number of simulation steps per simulation output needed by BioNetGen

## 2.28 (OWL 2)
- Improved performance of algorithm substitution in Python library

## 2.27 (OWL 2)
- Added algorithm parameters for Gillespie and NLEQ2 in RoadRunner

## 2.26 (OWL 2)
- Added RKQS method in XPP
- Added documentation for XPP methods

## 2.25 (OWL 2)
- Added algorithm parameters for libRoadRunner/CVODE

## 2.24 (OWL 2)
- Added algorithm for Resource Balance Analysis
- Aligned Identifiers.org URIs to http:// rather than https://

## 2.23 (OWL 2)
- Added algorithms and paramters for XPP
  - Gear-like method for stiff ODE systems

## 2.22 (OWL 2)
- Added algorithms and paramters for XPP
  - Maximum number of iterations for root finding
  - Jacobian epsilon
  - Second order backward method for Volterra equations
  - Second order backward method for Volterra equations memory size

## 2.21 (OWL 2)
- Corrected ids for terms whose ids begin with `_`
- Corrected names and synonyms of terms to start with lowercase letters
- Started guidelines for conventions such as the rules above
- Added tests
  - URIs of terms follow the above pattern
  - URIs of terms are unique
  - Terms have names
  - Names start with lower case letters (warning)

## 2.20 (OWL 2)
- Generalized label and description of KISAO_0000216
- Added label for citation for KISAO_0000216

## 2.19 (OWL 2)
- Added algorithms for logical modeling
  - Sequential simulation
  - Stable state search
  - Trap space identification

## 2.18 (OWL 2)
- Added parameter for adaptive time steps

## 2.17 (OWL 2)
- Added terms for simulation outputs (formerly `urn:sed:symbol`)
- Added method for getting the type of a KiSAO term

## 2.16 (OWL 2)
- Added terms for sub-methods of CVODE

## 2.15.3 (OWL 2)
- Added more functionality to Python package

## 2.15.2 (OWL 2)
- Debugged CI action

## 2.15.1 (OWL 2)
- Added Python package for getting sets of similar algorithms
- Added unit tests for the above
- Added unit tests to CI action

## 2.15 (OWL 2)
Improved organization and relationships to facilitate the inference of similar simulation algorithms
- Nested dynamic flux balance (KISAO_0000499) analysis under hybrid method (KISAO_0000352)
- Moved 'has characteristic' some 'ordinary differential equation problem' from DA-DFBA (KISAO_0000502) to parent (dynamic flux balance analysis (KISAO_0000499))
- Corrected relationship for stochastic Runge-Kutta method (KISAO_0000564) `'has characteristic' max 1 'stochastic differential equation problem' to `'has characteristic' some 'stochastic differential equation problem'`
- Added relationship for stochastic Runge-Kutta method (KISAO_0000564) `'has characteristic' some 'stochastic system behaviour'` (KISAO_0000104)
- Added relationship for stochastic Runge-Kutta method (KISAO_0000564) `not ('has characteristic' some 'ordinary differential equation problem')` (KISAO_0000374)
- Added `is hybrid of` relationships for VCell hybrid methods (KISAO_0000598, KISAO_0000599, KISAO_0000600)
- Added more detail to `is hybrid of` relationships for Pahle hybrid methods (children of KISAO_0000231)
- Added `'is hybrid of' some (LSODA and LSODAR)` to hybrid LSODA/LSODAR method (KISAO_0000560)
- Relabeled hybrid LSODA/LSODAR method (KISAO_0000560)
- Added alt label `Gillespie's` algorithm to Gillespie direct algorithm  (KISAO_0000029)
- Added relationship 'has characteristic' some hybridity to BKMC (KISAO_0000581)
- Nested `Composite-rejection stochastic simulation algorithm` (KISAO_0000610) under accelerated SSA (KISAO_0000333)
- Added updating policy chacteristics to logical simulation methods (children of KISAO_0000448)
- Added parent term for parsimonious FBA methods (children of KISAO_0000620)
- Nested methods under generalized SSA (KISA_0000335)
    - bunker (KISAO_0000618)
    - EMC (KISAO_0000619)
    - iSSA (KISAO_0000611)
    - NMC (KISAO_0000613)
    - Bortz-Kalos-Lebowitz algorithm (KISAO_0000051)
    - Gillespie multi-particle method (KISAO_0000075)
- Added organizational term for stochastic simulation leaping methods (KISAO_0000621)
- Added missing exact (KISAO_0000236) vs approximate (KISAO_0000237) characteristics for non-generalized Gillespie-like methods
- Added organizational term for flux balance methods (KISAO_0000622)
- Added 'flux balance problem' (KISAO_0000623)

## 2.14 (OWL 2)
- Added algorithms for iBioSim bunker and emc-sim methods
- Added algorithms for VCell finite volume methods and VCell hybrid IDA/CVODE method

## 2.13 (OWL 2)
- Added algorithm and parameter terms for iBioSim methods

## 2.12 (OWL 2)
- Added missing subclass annotation for KISAO_0000435 of KISAO_0000302

## 2.11 (OWL 2)
- Added algorithm and parameter terms for Salis hybrid methods implemented in VCell

## 2.10 (OWL 2)
- Corrected capitalization of OptFlux to Optflux
- Added BioSimulators identifiers for simulation tools

## 2.9 (OWL 2)
- added term suggested in ticket 85
- added algorithm and parameter terms for COBRA Toolbox, Optflux, RAVEN, The Cell Collective, MaBoSS, E-Cell 4, JSim, SBSCL, BioUML, COBRApy
- organized order and tolerance parameter terms
- fixed data types of created dates

## 2.8 (OWL 2)
-  added terms suggested in tickets 45-83

## 2.7 (OWL 2)
-  added gFBA, pFBA, FVA and related parameters (tickets 37-44)

## 2.6 (OWL 2)
-  added patitioned leaping (KISAO_0000524) and stop condition (KISAO_0000525) (tickets 35-36)

## 2.5 (OWL 2)
-  added optimisation methods (ticket 33)

## 2.4 (OWL 2)
-  added dynamic FBA methods (ticket 32)

## 2.3.113 (OWL 2)
- 'number of runs' added (ticket 31)

## 2.3.12 (OWL 2)
- KLU added (ticket 30)

## 2.3.12 (OWL 2)
- KLU added (ticket 30)

## 2.3.11 (OWL 2)
- several terms updated (tickets 21-29)

## 2.3.10 (OWL 2)
- several algorithm parameters updated (ticket 20)

## 2.3.9 (OWL 2)
- several algorithm parameters added (tickets 15-19)

## 2.3.8 (OWL 2)
- several algorithm parameters added (tickets 12-13)

## 2.3.7 (OWL 2)
- 'optimization algorithm' branch added (ticket 6) including 2 child terms:
 -- 'local optimization algorithm' added (ticket 7)
 -- 'global optimization algorithm' added (ticket 8)
- 'bayesian inference algorithm' added (ticket 9)

## 2.3.6 (OWL 2)
- 'maximal timestep method' added

## 2.3.5 (encoded in OWL 2)
- 'maximum step size' parameter is added
- hierarchical relationships for 'Newton-type method' is updated

## 2.3.4 (encoded in OWL 2)
- COAST method is added
- 'logical model simulation method' sub-branch is added
- 'type of updating policy' sub-brach is added to characteristics

## 2.3.3 (encoded in OWL 2)
- 'flux balance analysis' is added.

## 2.3.2 (encoded in OWL 2)
- 'Embedded Runge-Kutta method' branch is updated: 'Dormand-Prince 8(5,3) method' and 'Higham-Hall method' are added.

## 2.3.1 (encoded in OWL 2)
- KINSOL and IDA solvers are moved to the 'Newton-type method' subbranch of the algorithm hierarchy tree.
- MIRIAM URNs in 'seeAlso' annotations are replaced with corresponding identifiers.org URLs.

## 2.3 (encoded in OWL 2)
- steady state method branch is added
- PLSR method branch is added
- 'kinetic simulation algorithm' term is renamed to 'modeling and simulation algorithm'.

## 2.2 (encoded in OWL 2)
- more Krylov subspace projection methods
- 'type of problem' characteristic is added
- skos:definition annotation is now used to represent definitions (instead of rdfs:comment)
- skos:altLabel annotation is now used to represent synonyms (instead of oboInOwl:Synonym)
- consists of two parts:
 -- kisao.owl: the core version containing all but deprecated terms
 -- kisao_full.owl: the full version which imports the core KiSAO and also contains deprecated terms.

## 2.1 (encoded in OWL 2)
- two times more simulation algorithms, than in the previous versions
- characteristic and parameter branches, linked to the algorithms which possess them, 
e.g. 'tau-leaping method' 'has parameter' 'tau-leaping epsilon'
- both hierarchical relations between simulation methods,
e.g. 'Poisson tau-leaping method' subClassOf 'tau-leaping method',
and ones representing a complex method switching between several algorithms,
e.g. 'LSODA' uses 'backward differentiation formula'
- consists of two parts:
 -- kisao.owl: the core version containing all but deprecated terms
 -- kisao_full.owl: the full version which imports the core KiSAO and also contains deprecated terms.

## 2.0 (encoded in OWL 2)
- the first of the KiSAO versions representing algorithm characteristics as a separate branch of OWL class hierarchy
- algorithms are linked to the characteristics they possess using 'has property' relationship,
e.g. 'Gillespie direct method' 'has property' 'stochastic system behaviour'

## 1.0 (encoded in OBO, OWL export is provided)
- an old version of KiSAO
- algorithm parameters are not covered
- algorithm subclassing is based on algorithm types, derivation (algorithm derived from another pre-existing one), and characteristics.
- characteristics are represented as subsumptions in algorithm branch, e.g. 'algorithm using adaptive time steps'
- multiple inheritance of algorithms (subsumptions as parents)
