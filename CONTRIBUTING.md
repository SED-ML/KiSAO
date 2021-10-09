# Contributing to KiSAO

We enthusiastically welcome contributions to KiSAO!

## Coordinating contributions

Please use GitHub issues to announce your plans to the community so that other developers can provide input into your plans and coordinate their own work. As the development community grows, we will institute additional infrastructure as needed such as a leadership committee and regular online meetings.

## Repository organization

This repository is organized as follows:

* [`README.md`](README.md): Overview of this repository
* [`kisao.owl`](kisao.owl): Definition of the ontology in OWL format
* [`kisao_full.owl`](kisao_full.owl): Basic meta data about the ontology in OWL format
* [`CHANGELOG.md`](CHANGELOG.md): Log of the changes with each version
* [`docs`](docs): Documentation about KiSAO
* [`libkisao`](libkisao): Libraries for working with KiSAO
    * `python`](python): Python library for working with KiSAO
* [`LICENSE`](LICENSE): License for this package
* [`CONTRIBUTING.md`](CONTRIBUTING.md): Guide to contributing to this package (this document)
* [`CODE_OF_CONDUCT.md`](CODE_OF_CONDUCT.md): Code of conduct for developers of this package

## Modifying the ontology

The ontology should be edited using the [Protégé](https://protege.stanford.edu/) ontology editor.

1. Fork this repository.
2. Use Protégé to edit [`kisao.owl`](kisao.owl).
3. The following attributes should be specified for each new term (with the data types in parantheses):
   - `rdfs:label` (`language:en`): primary name of the term  
   - `skos:definition` (`language:en`): description of the term
   - `dcterms:creator`: (`language:en`): initial of the investigator who created the term (e.g., `JRK`)
   - `dcterms:created`: (`xsd:date`): date the term was created (e.g., `2021-06-03`)
4. The following attributes can also be used to describe new terms:
   - `isOrganizational` (`xsd:boolean`): set this to `true` if the term is an abstract concept (i.e. shouldn't be used in a SED-ML document; only its children should be used in SED-ML documents)
   - `skos:altLabel` (`language:en`): synonym for the term
   - `rdfs:seeAlso` (`xsd:anyURI`): URL for more information about the term (e.g., `https://identifiers.org/doi/XYZ`)
      - `rdfs:comment` (`language:en`): human-readable citation for the URL
   - `isImplementedIn` (`xsd:anyURI`): URL for a simulation tool which supports the term (e.g., `https://identifiers.org/biosimulators/tellurium`)
      - `rdfs:comment` (`language:en`): name of the simulation tool (e.g., `tellurium`)
5. Save your changes to `kisao.owl`.

## Conventions

Contributions to KiSAO should adhere to the following conventions:

- URIs of terms should follow the pattern `http://www.biomodels.net/kisao/KISAO#KISAO_\d{7,7}`
- URIs should be assigned sequentially, starting from the greatest id
- Labels should start with a lowercase letter, except for proper names such as the name of a person (e.g., `Gillespie's Algorithm`)
- Labels should not contain special characters or brackets

## Submitting changes

Below are instructions for submitting changes:

1. Save and commit your changes.
2. Create a pull request for your changes. In the body of the pull request, please provide a brief overview of your requested changes.

## Releasing new versions

Below are instructions for releasing a new version:

1. Revise [`kisao.owl`](kisao.owl).
2. Add description of changes to [`CHANGELOG.md`](CHANGELOG.md).
3. Increment the version in [`kisao.owl`](kisao.owl) and [`kisao_full.owl`](kisao_full.owl).
3. Increment the version in [`libkisao/python/kisao/_version.py`](libkisao/python/kisao/_version.py).
4. Commit and push the new version (e.g., `git add kisao.owl kisao_full.owl; git commit -m "..."; git push`).
5. Tag the new version (e.g., `git tag 2.14`).
6. Push the new tag (`git push --tags`).
7. Pushing this tag will trigger a GitHub action which will perform the following tasks: 
   * Merge the changes into the deploy branch
   * Create a new GitHub release
   * Submit the new version to BioPortal
   * Submit the new version of the Python package to PyPI
   * Compile the documentation of the Python package and push it to this repository.

## Reporting issues

Please use [GitHub issues](https://github.com/SED-ML/KiSAO/issues) to report any issues to the development community.

## Getting help

Please use [GitHub issues](https://github.com/SED-ML/KiSAO/issues) to post questions, comments, or bugs.
