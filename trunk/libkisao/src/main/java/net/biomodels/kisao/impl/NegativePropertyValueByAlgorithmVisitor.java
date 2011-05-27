package net.biomodels.kisao.impl;

import net.biomodels.kisao.visitors.IPropertyValueByAlgorithmVisitor;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 13-May-2011
 *         Time: 18:23:01
 */
class NegativePropertyValueByAlgorithmVisitor extends OWLClassExpressionVisitorAdapter
        implements IPropertyValueByAlgorithmVisitor {
    private final IPropertyValueByAlgorithmVisitor visitor;

    public NegativePropertyValueByAlgorithmVisitor(IRI iri) {
        assert iri != null;
        visitor = new PropertyValueByAlgorithmVisitor(iri);
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


