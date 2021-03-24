# KiSAO

The Kinetic Simulation Algorithm Ontology (KiSAO: http://co.mbine.org/standards/kisao) is an ontology of algorithms for simulating and analyzing biological models, as well as the characteristics of these algorithms, their input parameters, and their outputs. In addition, KiSAO captures relationships among algorithms, their parameters, and their outputs.

![Overview of KiSAO](https://github.com/SED-ML/KiSAO/raw/dev/docs/overview.jpeg)

## Browsing KiSAO

KiSAO can be browsed through [BioPortal](https://bioportal.bioontology.org/ontologies/KISAO) and [OLS](https://www.ebi.ac.uk/ols/ontologies/kisao).

## Obtaining KiSAO in other formats

KiSAO is developed in the OWL format. The current release of KiSAO is available in OWL format [here](https://github.com/SED-ML/KiSAO/tree/dev/kisao.owl). KiSAO is available in CSV and RDF/XML formats from [BioPortal](https://bioportal.bioontology.org/ontologies/KISAO).

## Obtaining older versions of KiSAO

The current release of KiSAO is available in the OWL format [here](https://github.com/SED-ML/KiSAO/tree/deploy/kisao.owl). The current development version is available [here](https://github.com/SED-ML/KiSAO/tree/dev/kisao.owl). Older versions of KiSAO are available [here](https://github.com/SED-ML/KiSAO/releases). All versions are available in OWL format. Versions 1.0 is also available in OBO format.

## Requesting new terms and relationships, requesting changes to existing terms and relationships, and reporting bugs

Please use [GitHub issues](https://github.com/SED-ML/KiSAO/issues/new/choose) to submit requests for new terms and relationships, request modifications to existing terms and relationships, and report bugs.

## Contributing multiple changes to KiSAO

If you plan to request a significant number of terms, relationships, or changes, please submit your request as a pull request with the intended modifications to KiSAO:

1. Install the [Protégé](https://protege.stanford.edu/) ontology editor.
2. Fork this repository.
3. Use Protégé to edit `kisao.owl`.
4. Save and commit your changes.
5. Create a pull request for your changes. In the body of the pull request, please provide a brief overview of your requested changes.

## Releasing new versions

1. Revise `kisao.owl`.
2. Add description of changes to `CHANGELOG.txt`.
3. Increment the version in `kisao.owl` and `kisao_full.owl`.
4. Commit and push the new version (e.g., `git add kisao.owl kisao_full.owl; git commit -m "..."; git push`).
5. Tag the new version (e.g., `git tag 2.14`).
6. Push the new tag (`git push --tags`).
7. Pushing this tag will trigger a GitHub action which will merge the changes into the deploy branch and create a new GitHub release.
8. Log into BioPortal and create a new release -- this is not yet automated into the GitHub action.

## License

KiSAO is released under [Artistic License 2.0](LICENSE).
