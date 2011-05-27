package net.biomodels.kisao.impl;

import net.biomodels.kisao.visitors.IPropertyValueByAlgorithmVisitor;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 16-May-2011
 *         Time: 16:30:36
 */
class PropertyValueByAlgorithmVisitor extends OWLClassExpressionVisitorAdapter
        implements IPropertyValueByAlgorithmVisitor {
    private final IRI propertyIRI;
    private final Set<IRI> values = new HashSet<IRI>();

    public PropertyValueByAlgorithmVisitor(IRI propertyIRI) {
        assert propertyIRI != null;
        this.propertyIRI = propertyIRI;
    }

    public void visit(OWLObjectSomeValuesFrom ce) {
        OWLObjectPropertyExpression prop = ce.getProperty();
        if (!prop.isAnonymous() && propertyIRI.equals(prop.asOWLObjectProperty().getIRI())
                && !ce.getFiller().isAnonymous()) {
            this.values.add(ce.getFiller().asOWLClass().getIRI());
        }
    }

    public void visit(OWLObjectExactCardinality ce) {
        OWLObjectPropertyExpression prop = ce.getProperty();
        if (!prop.isAnonymous() && propertyIRI.equals(prop.asOWLObjectProperty().getIRI())
                && !ce.getFiller().isAnonymous()) {
            this.values.add(ce.getFiller().asOWLClass().getIRI());
        }
    }

    public void clear() {
        this.values.clear();
    }

    public Set<IRI> getValues() {
        return Collections.unmodifiableSet(values);
    }
}
