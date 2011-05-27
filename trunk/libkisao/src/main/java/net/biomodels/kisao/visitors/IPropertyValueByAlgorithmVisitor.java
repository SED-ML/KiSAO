package net.biomodels.kisao.visitors;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;

import java.util.Set;

/**
 * @author Anna Zhukova
 *         Date: 16-May-2011
 *         Time: 16:29:02
 */
public interface IPropertyValueByAlgorithmVisitor extends OWLClassExpressionVisitor {

    void clear();

    Set<IRI> getValues();
}

