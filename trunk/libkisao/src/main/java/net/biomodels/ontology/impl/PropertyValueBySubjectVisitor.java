package net.biomodels.ontology.impl;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 16-May-2011
 *         Time: 16:30:36
 */
class PropertyValueBySubjectVisitor extends OWLClassExpressionVisitorAdapter
        implements IPropertyValueBySubjectVisitor {
    private final OWLPropertyExpression property;
    private final Set<IRI> values = new HashSet<IRI>();

    public PropertyValueBySubjectVisitor(OWLPropertyExpression property) {
        assert property != null;
        this.property = property;
    }

    public void visit(OWLObjectSomeValuesFrom ce) {
        OWLObjectPropertyExpression prop = ce.getProperty();
        if (property.equals(prop) && !ce.getFiller().isAnonymous()) {
            this.values.add(ce.getFiller().asOWLClass().getIRI());
        }
    }

    public void visit(OWLObjectAllValuesFrom ce) {
        OWLObjectPropertyExpression prop = ce.getProperty();
        if (property.equals(prop) && !ce.getFiller().isAnonymous()) {
            this.values.add(ce.getFiller().asOWLClass().getIRI());
        }
    }

    public void visit(OWLObjectExactCardinality ce) {
        OWLObjectPropertyExpression prop = ce.getProperty();
        if (property.equals(prop) && !ce.getFiller().isAnonymous()) {
            this.values.add(ce.getFiller().asOWLClass().getIRI());
        }
    }

    public void visit(OWLDataSomeValuesFrom ce) {
        OWLDataPropertyExpression prop = ce.getProperty();
        if (property.equals(prop) && ce.getFiller().isDatatype()) {
            this.values.add(ce.getFiller().asOWLDatatype().getIRI());
        }
    }

    public void visit(OWLDataExactCardinality ce) {
        OWLDataPropertyExpression prop = ce.getProperty();
        if (property.equals(prop) && ce.getFiller().isDatatype()) {
            this.values.add(ce.getFiller().asOWLDatatype().getIRI());
        }
    }

    public void visit(OWLDataAllValuesFrom ce) {
        OWLDataPropertyExpression prop = ce.getProperty();
        if (property.equals(prop) && ce.getFiller().isDatatype()) {
            this.values.add(ce.getFiller().asOWLDatatype().getIRI());
        }
    }

    public void clear() {
        this.values.clear();
    }

    public Set<IRI> getValues() {
        return Collections.unmodifiableSet(values);
    }
}