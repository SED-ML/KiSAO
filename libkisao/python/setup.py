import re
import setuptools
import subprocess
import sys
import os

name = 'kisao'
dirname = os.path.dirname(__file__)
package_data = {
    name: [
        'kisao.owl',
    ],
}

_basedir = os.path.abspath(os.path.dirname(__file__))

_version_fname = os.path.join(_basedir, 'kisao', '_version.py')

_version = open(_version_fname).readline().strip().split(' ')[2]

_readme_fname = os.path.join(_basedir, '..', '..', 'README.md')


# install package
setuptools.setup(
    name=name,
    version=_version,
    description="Utilities for working with the Kinetic Simulation Algorithm Ontology (KiSAO)",
    long_description=open(_readme_fname).read().strip(),
    long_description_content_type="text/markdown",
    url="https://github.com/SED-ML/kisao",
    download_url='https://github.com/SED-ML/kisao',
    author='SED-ML Editors',
    author_email="sed-ml-editors@googlegroups.com",
    license="Apache 2.0",
    keywords=[
        'systems biology',
        'modeling',
        'simulation',
        'algorithm',
        'ontology',
        'KiSAO',
        'SED-ML',
        'SBML',
    ],
    packages=setuptools.find_packages(exclude=['tests', 'tests.*']),
    # package_data=md.package_data,
    install_requires=open("requirements.txt").read().strip(),
    extras_require=open("requirements.optional.txt").read().strip(),
    tests_require=open("requirements.optional.txt").read().strip(),
    classifiers=[
        'Development Status :: 3 - Alpha',
        'Intended Audience :: Science/Research',
        'License :: OSI Approved :: Apache Software License',
        'Topic :: Scientific/Engineering :: Bio-Informatics',
    ],
)
