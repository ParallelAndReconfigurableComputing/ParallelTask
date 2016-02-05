package spoon.examples.fieldaccess.processing;

import spoon.examples.fieldaccess.annotation.Setter;
import spoon.examples.fieldaccess.template.FieldAccessTemplate;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.template.Substitution;
import spoon.template.Template;
import spoon.template.TemplateMatcher;

/**
 * This processor replaces all the write accesses of the form
 * <code>field = expr</code> or <code>expr1.field = expr2</code> by calls to
 * the setters (<code>setField(expr)</code> or
 * <code>expr1.setField(expr2)</code>). It uses the template matching
 * facility ({@link spoon.template.TemplateMatcher}) to avoid doing
 * this transformation when the written field is done within a setter
 * implementation (to avoid infinite runtime recursions).
 */
public class WriteAccessProcessor<_FieldType_> extends
        AbstractProcessor<CtAssignment<_FieldType_, _FieldType_>> {

    public boolean isToBeProcessed(
            CtAssignment<_FieldType_, _FieldType_> candidate) {
        return true;
    }

    public void process(CtAssignment<_FieldType_, _FieldType_> assignment) {
        if (assignment.getAssigned() instanceof CtFieldAccess) {
            CtFieldAccess<_FieldType_> fa = ((CtFieldAccess<_FieldType_>) assignment
                    .getAssigned());
            CtFieldReference<_FieldType_> field = fa.getVariable();
            if (ServerAccessProcessor.fields.contains(field)) {

                // if (assignment.getParent(CtMethod.class).getSimpleName()
                // .compareToIgnoreCase("set" + field.getSimpleName()) == 0)
                // return;
                CtClass<FieldAccessTemplate<?>> templateType = getFactory().Class()
                        .get(FieldAccessTemplate.class);
                TemplateMatcher matcher = new TemplateMatcher(templateType);
                CtMethod<?> setter = (CtMethod<?>) templateType.getAnnotatedChildren(
                        Setter.class).get(0);
                if (!matcher
                        .match(assignment.getParent(CtMethod.class), setter)) {
                    Template t = new FieldAccessTemplate<_FieldType_>(field
                            .getType(), field.getSimpleName(), fa, assignment
                            .getAssignment());

                    CtBlock<?> b = Substitution.substituteMethodBody(assignment
                            .getParent(CtClass.class), t, "setterInvocation");
                    assignment.replace(b.getStatements().get(0));
                }
            }
        }
    }
}