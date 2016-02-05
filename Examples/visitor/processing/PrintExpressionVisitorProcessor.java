package spoon.examples.visitor.processing;

import spoon.examples.visitor.src.PrintExpressionVisitor;
import spoon.examples.visitor.src.VisitedElement;
import spoon.examples.visitor.src.expression.Expression;
import spoon.reflect.reference.CtTypeReference;

public class PrintExpressionVisitorProcessor extends AbstractVisitorProcessor {

    @Override
    public CtTypeReference<?> getVisitedRootType() {
        return getFactory().Type().createReference(Expression.class);
    }

    @Override
    public CtTypeReference<?> getVisitedInterface() {
        return getFactory().Type().createReference(VisitedElement.class);
    }

    @Override
    public CtTypeReference<?> getVisitorType() {
        return getFactory().Type().createReference(PrintExpressionVisitor.class);
    }
    
}