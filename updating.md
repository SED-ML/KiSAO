# What needs to be updated when making a new release

Put new version into:
* libkisao/python/kisao/_version.py
* kisao.owl (i.e. <owl:versionInfo rdf:datatype="http://www.w3.org/2001/XMLSchema#decimal">2.34</owl:versionInfo>)
* kisao_full.owl (i.e. <owl:versionInfo rdf:datatype="http://www.w3.org/2001/XMLSchema#decimal">2.34</owl:versionInfo>)
* .github/workflows/BioPortal-submission.json

Summarize changes in:
* CHANGELOG.md
