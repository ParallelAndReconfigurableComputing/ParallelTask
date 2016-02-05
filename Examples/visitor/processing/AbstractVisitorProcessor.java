package spoon.examples.visitor.processing;

import spoon.examples.visitor.template.VisitorTemplate;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.template.Substitution;
import spoon.template.Template;

/**
 * The abstract processor {@link AbstractVisitorProcessor} introduces an
 * {@link spoon.examples.visitor.template.VisitorTemplate#accept(_Visitor_)}
 * template method in all the classes that are a sub-type of a root type (the
 * root node of the visited hierarchy, given by {@link #getVisitedRootType()}).
 * 
 * <p>
 * By automatically generating the <code>accept</code> method, this processor
 * makes the visitor pattern more generic an easier to use. In particular, any
 * change in the visited hierarchy will be automatically detected when spooning
 * the program with this processor.
 * 
 * <p>
 * Note that this abstract class must be subclassed to tune it to a particular
 * visitor pattern (see {@link #getVisitedInterface()},
 * {@link #getVisitedRootType()}, {@link #getVisitorType()}, and an example
 * implementation
 * {@link spoon.examples.visitor.processing.PrintExpressionVisitorProcessor}).
 * 
 * @see spoon.examples.visitor.template.VisitorTemplate
 */
public abstract class AbstractVisitorProcessor extends
        AbstractProcessor<CtType<?>> {

    /**
     * Defines the root node of the visited type hierarchy.
     */
    public abstract CtTypeReference<?> getVisitedRootType();

    /**
     * Defines the actual visitor type (typically a root interface for all the
     * visitor).
     */
    public abstract CtTypeReference<?> getVisitorType();

    /**
     * Defines the actual visited type.
     */
    public abstract CtTypeReference<?> getVisitedInterface();

    public boolean isToBeProcessed(CtType<?> candidate) {
        return getVisitedRootType().isAssignableFrom(
                getFactory().Type().createReference(candidate));
    }

    public void process(CtType<?> target) {
        // insert visited interface
        if (target == getVisitedRootType().getDeclaration()) {
            target.addSuperInterface(getVisitedInterface());
        }
        // insert the accept method
        if (target instanceof CtClass
                && !target.hasModifier(ModifierKind.ABSTRACT)) {
            Template t = new VisitorTemplate(target.getSimpleName(),
                    getVisitorType().getActualClass());
            Substitution.insertAll(target, t);
        }
    }

}