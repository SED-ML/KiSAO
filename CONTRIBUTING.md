# Contributing to KiSAO

We enthusiastically welcome contributions to KiSAO!

## Coordinating contributions

Please use GitHub issues to announce your plans to the community so that other developers can provide input into your plans and coordinate their own work. As the development community grows, we will institute additional infrastructure as needed such as a leadership committee and regular online meetings.

## Repository organization

This repository is organized as follows:

* `README.md`: Overview of this repository
* `kisao.owl`: Definition of the ontology in OWL format
* `kisao_full.owl`: Basic meta data about the ontology in OWL format
* `CHANGELOG.txt`: Log of the changes with each version
* `docs`: Documentation about KiSAO
* `libkisao`: Libraries for working with KiSAO
    * `python`: Python library for working with KiSAO
* `LICENSE`: License for this package
* `CONTRIBUTING.md`: Guide to contributing to this package (this document)
* `CODE_OF_CONDUCT.md`: Code of conduct for developers of this package

## Submitting changes

Below are instructions for submitting changes:

1. Install the [Protégé](https://protege.stanford.edu/) ontology editor.
2. Fork this repository.
3. Use Protégé to edit `kisao.owl`.
4. Save and commit your changes.
5. Create a pull request for your changes. In the body of the pull request, please provide a brief overview of your requested changes.

## Releasing new versions

Below are instructions for releasing a new version:

1. Revise `kisao.owl`.
2. Add description of changes to `CHANGELOG.txt`.
3. Increment the version in `kisao.owl` and `kisao_full.owl`.
3. Increment the version in `libkisao/python/kisao/_version.py`.
4. Commit and push the new version (e.g., `git add kisao.owl kisao_full.owl; git commit -m "..."; git push`).
5. Tag the new version (e.g., `git tag 2.14`).
6. Push the new tag (`git push --tags`).
7. Pushing this tag will trigger a GitHub action which will merge the changes into the deploy branch and create a new GitHub release.
8. Log into BioPortal and create a new release -- this is not yet automated into the GitHub action.

## Reporting issues

Please use [GitHub issues](https://github.com/SED-ML/KiSAO/issues) to report any issues to the development community.

## Getting help

Please use [GitHub issues](https://github.com/SED-ML/KiSAO/issues) to post questions, comments, or bugs.