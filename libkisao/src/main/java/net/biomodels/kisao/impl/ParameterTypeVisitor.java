package net.biomodels.kisao.impl;

import net.biomodels.kisao.KiSAOIRI;
import net.biomodels.kisao.visitors.IParameterTypeVisitor;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

/**
 * @author Anna Zhukova
 *         Date: 16-May-2011
 *         Time: 16:52:03
 */
class ParameterTypeVisitor extends OWLClassExpressionVisitorAdapter implements IParameterTypeVisitor {
    private Class result = null;

    public void visit(OWLDataSomeValuesFrom someValuesFrom) {
        visitData(someValuesFrom);
    }

    public void visit(OWLDataAllValuesFrom allValuesFrom) {
        visitData(allValuesFrom);
    }

    public Class getResult() {
        return result;
    }

    private void visitData(OWLQuantifiedDataRestriction someValuesFrom) {
        OWLDataProperty prop = someValuesFrom.getProperty().asOWLDataProperty();
        if (KiSAOIRI.HAS_TYPE.equals(prop.getIRI())) {
            OWLDatatype owlDatatype = someValuesFrom.getFiller().asOWLDatatype();
            if (owlDatatype.isBoolean()) {
                result = Boolean.class;
            } else if (owlDatatype.isDouble()) {
                result = Double.class;
            } else if (owlDatatype.isFloat()) {
                result = Float.class;
            } else if (owlDatatype.isInteger()) {
                result = Integer.class;
            }
        }
    }
}
