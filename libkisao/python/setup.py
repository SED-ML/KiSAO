import setuptools
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

_version = open(_version_fname).readline().strip().split("'")[1]

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
    package_data={'kisao': ['kisao.owl']},
    install_requires=open("requirements.txt").read().strip(),
    extras_require={"all": ["natsort", "numpy", "pandas"]},
    tests_require={"all": ["natsort", "numpy", "pandas"]},
    classifiers=[
        'Development Status :: 3 - Alpha',
        'Intended Audience :: Science/Research',
        'License :: OSI Approved :: Apache Software License',
        'Topic :: Scientific/Engineering :: Bio-Informatics',
    ],
)
