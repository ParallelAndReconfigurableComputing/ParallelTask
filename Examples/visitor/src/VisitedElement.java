package spoon.examples.visitor.src;



public interface VisitedElement {

    void accept(PrintExpressionVisitor visitor);

}