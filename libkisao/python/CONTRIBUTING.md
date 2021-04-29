# Contributing to the Python library for KiSAO

We enthusiastically welcome contributions!

## Coordinating contributions

Please use GitHub issues to announce your plans to the community so that other developers can provide input into your plans and coordinate their own work. As the development community grows, we will institute additional infrastructure as needed such as a leadership committee and regular online meetings.

## Repository organization

This package follows standard Python conventions:

* `README.md`: Overview of this package
* `kisao/`: Source code for this package
* `tests/`: Unit tests for this package
* `setup.py`: pip installation script for this package
* `setup.cfg`: Configuration for the pip installation script
* `requirements.txt`: Dependencies of this package
* `MANIFEST.in`: List of files to include when this package is bundled for distribution through PyPI
* `CONTRIBUTING.md`: Guide to contributing to this package (this document)

## Coding convention

This package follows standard Python style conventions:

* Class names: `UpperCamelCase`
* Function names: `lower_snake_case`
* Variable names: `lower_snake_case`

## Documentation convention

This package is documented using [reStructuredText](https://www.sphinx-doc.org/en/master/usage/restructuredtext/index.html) and the [napoleon Sphinx plugin](https://www.sphinx-doc.org/en/master/usage/extensions/napoleon.html). The documentation can be compiled with [Sphinx](https://www.sphinx-doc.org/) by running the following commands:

```
python -m pip install -r docs-src/requirements.txt
sphinx-apidoc . setup.py --output-dir docs-src/source --force --module-first --no-toc
sphinx-build docs-src docs
```
