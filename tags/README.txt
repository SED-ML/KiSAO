This folder contains releases of KiSAO.

KiSAO 2.3.9 (OWL 2)
- several algorithm parameters added (tickets 15-19)

KiSAO 2.3.8 (OWL 2)
- several algorithm parameters added (tickets 12-13)

KiSAO 2.3.7 (OWL 2)
- 'optimization algorithm' branch added (ticket 6) including 2 child terms:
 -- 'local optimization algorithm' added (ticket 7)
 -- 'global optimization algorithm' added (ticket 8)
- 'bayesian inference algorithm' added (ticket 9)

KiSAO 2.3.6 (OWL 2)
- 'maximal timestep method' added

KiSAO 2.3.5 (encoded in OWL 2)
- 'maximum step size' parameter is added
- hierarchical relationships for 'Newton-type method' is updated

KiSAO 2.3.4 (encoded in OWL 2)
- COAST method is added
- 'logical model simulation method' sub-branch is added
- 'type of updating policy' sub-brach is added to characteristics

KiSAO 2.3.3 (encoded in OWL 2)
- 'flux balance analysis' is added.

KiSAO 2.3.2 (encoded in OWL 2)
- 'Embedded Runge-Kutta method' branch is updated: 'Dormand-Prince 8(5,3) method' and 'Higham-Hall method' are added.

KiSAO 2.3.1 (encoded in OWL 2)
- KINSOL and IDA solvers are moved to the 'Newton-type method' subbranch of the algorithm hierarchy tree.
- MIRIAM URNs in 'seeAlso' annotations are replaced with corresponding identifiers.org URLs.

KiSAO 2.3 (encoded in OWL 2)
- steady state method branch is added
- PLSR method branch is added
- 'kinetic simulation algorithm' term is renamed to 'modeling and simulation algorithm'.

KiSAO 2.2 (encoded in OWL 2)
- more Krylov subspace projection methods
- 'type of problem' characteristic is added
- skos:definition annotation is now used to represent definitions (instead of rdfs:comment)
- skos:altLabel annotation is now used to represent synonyms (instead of oboInOwl:Synonym)
- consists of two parts:
 -- kisao.owl: the core version containing all but deprecated terms
 -- kisao_full.owl: the full version which imports the core KiSAO and also contains deprecated terms.

KiSAO 2.1 (encoded in OWL 2)
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

KiSAO 2.0 (encoded in OWL 2)
- the first of the KiSAO versions representing algorithm characteristics as a separate branch of OWL class hierarchy
- algorithms are linked to the characteristics they possess using 'has property' relationship,
e.g. 'Gillespie direct method' 'has property' 'stochastic system behaviour'

KiSAO 1.0 (encoded in OBO, OWL export is provided)
- an old version of KiSAO
- algorithm parameters are not covered
- algorithm subclassing is based on algorithm types, derivation (algorithm derived from another pre-existing one), and characteristics.
- characteristics are represented as subsumptions in algorithm branch,
e.g. 'algorithm using adaptive time steps'
- multiple inheritance of algorithms (subsumptions as parents)
