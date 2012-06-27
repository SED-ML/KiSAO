LibKiSAO

LibKiSAO is a java library that provides methods to query Kinetic Simulation Algorithm Ontology (KiSAO).
More information about KiSA Ontology and libKiSAO is available at <http://www.biomodels.net/kisao/>.

LibKiSAO is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

Copies of the GNU General Public License and the GNU Lesser General Public License have been included with this distribution in the file gpl.txt and lgpl-3.0.txt, respectively. An online version is available at <http://www.gnu.org/licenses/>.

LibKiSAO uses the following libraries in unmodified form:

1) The OWL API, http://owlapi.sourceforge.net
   released under GNU Lesser General Public License, see owlapi.LICENSE in Licenses/ folder of libKiSAO.jar
2) HermiT, http://www.hermit-reasoner.com
   released under the GNU Lesser General Public License, see HermiT.LICENSE in Licenses/ folder of libKiSAO.jar

The libKiSAO.zip archive is organized as follows:

- libKiSAO.jar is a stand-alone version of libKiSAO that can be used from within other Java programs. 
It contains all required libraries.
- libKiSAO-src.jar contains libKiSAO source code.
- javadoc/* folder contains java documentation for libKiSAO.
- example/* folder contains Main.java example class, showing how libKiSAO might be used.

This version is a 1.0.3-rc release candidate.

libKiSAO 1.0.3-rc
- similar algorithm finding methods improved by not taking into account subsumption-like (organizational) algorithms
- KiSAO id to identifiers.org URL conversion supported
- identifiers.org URLs supported by searchById method

libKiSAO 1.0.2-rc5
- general ontology querying functionality was separated from KiSAO-specific one and extracted to a separate package.

libKiSAO 1.0.2-rc4
- getNMostSimilarAlgorithms method was added
- getComplexAlgorithms and isComplex methods were added

libKiSAO 1.0.2-rc3
- return type of getAllAlgorithms/Parameters/Characteristics methods was changed to Set<IRI> (was List<IRI>)
- getMiriamURIByIRI method was renamed to getMiriamURI
- getIdByIRI method was renamed to getId
- getIRIByName and getIRIByNameOrSynonym methods were replaced with searchByName
- getIRIbyMiriamURIorId method was replaced with searchById

libKiSAO 1.0.2-rc2
- annotation IRIs for definitions and synonyms were updated to be compatible with KiSAO 2.2

libKiSAO 1.0.2-rc1
- more methods for retrieving similar algorithms
- more code examples

libKiSAO 1.0.1-rc1
- compatible with java 1.5

libKiSAO 1.0-rc1
- compatible with java 1.6

Please send bug-reports, suggestions and comments to the KiSAO tracker at <https://sourceforge.net/tracker/?group_id=293617>
or to biomodels-net-support@lists.sf.net.

Thank you

BioModel.net Team
 
