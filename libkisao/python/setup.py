import re
import setuptools
import subprocess
import sys
try:
    result = subprocess.run(
        [sys.executable, "-m", "pip", "show", "pkg_utils"],
        check=True, capture_output=True)
    match = re.search(r'\nVersion: (.*?)\n', result.stdout.decode(), re.DOTALL)
    assert match and tuple(match.group(1).split('.')) >= ('0', '0', '5')
except (subprocess.CalledProcessError, AssertionError):
    subprocess.run(
        [sys.executable, "-m", "pip", "install", "-U", "pkg_utils"],
        check=True)
import os
import pkg_utils

name = 'kisao'
dirname = os.path.dirname(__file__)
package_data = {
    name: [
        'kisao.owl',
    ],
}

# get package metadata
md = pkg_utils.get_package_metadata(dirname, name, package_data_filename_patterns=package_data)

# install package
setuptools.setup(
    name=name,
    version=md.version,
    description="Utilities for working with the Kinetic Simulation Algorithm Ontology (KiSAO)",
    long_description=md.long_description,
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
    package_data=md.package_data,
    install_requires=md.install_requires,
    extras_require=md.extras_require,
    tests_require=md.tests_require,
    dependency_links=md.dependency_links,
    classifiers=[
        'Development Status :: 3 - Alpha',
        'Intended Audience :: Science/Research',
        'License :: OSI Approved :: Apache Software License',
        'Topic :: Scientific/Engineering :: Bio-Informatics',
    ],
)
