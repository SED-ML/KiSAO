package net.biomodels.ontology.impl;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;

import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 16-May-2011
 *         Time: 16:29:02
 */
interface IPropertyValueBySubjectVisitor extends OWLClassExpressionVisitor {

    void clear();

    Set<IRI> getValues();
}