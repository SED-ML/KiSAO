package net.biomodels.ontology.impl;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

import java.util.Set;


/**
 * @author Anna Zhukova
 *         Date: 13-May-2011
 *         Time: 18:23:01
 */
class NegativePropertyValueBySubjectVisitor extends OWLClassExpressionVisitorAdapter
        implements IPropertyValueBySubjectVisitor {
    private final IPropertyValueBySubjectVisitor visitor;

    public NegativePropertyValueBySubjectVisitor(OWLPropertyExpression property) {
        assert property != null;
        visitor = new PropertyValueBySubjectVisitor(property);
    }

    public void visit(OWLObjectComplementOf ce) {
        OWLClassExpression complement = ce.getComplementNNF();
        if (complement.isAnonymous()) {
            complement.accept(visitor);
        }
    }

    public void clear() {
        visitor.clear();
    }

    public Set<IRI> getValues() {
        return visitor.getValues();
    }
}