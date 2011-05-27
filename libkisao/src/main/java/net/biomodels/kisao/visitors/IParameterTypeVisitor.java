package net.biomodels.kisao.visitors;

import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;

/**
 * @author Anna Zhukova
 *         Date: 17-May-2011
 *         Time: 14:29:40
 */
public interface IParameterTypeVisitor extends OWLClassExpressionVisitor {
    
    Class getResult();
}
