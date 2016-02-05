package spoon.examples.fieldaccess.processing;

import spoon.examples.fieldaccess.annotation.Getter;
import spoon.examples.fieldaccess.annotation.Setter;
import spoon.examples.fieldaccess.template.FieldAccessTemplate;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.template.Substitution;
import spoon.template.Template;
import spoon.template.TemplateMatcher;

/**
 * This processor replaces all the read accesses of the form <code>field</code>
 * or <code>expr.field</code> by calls to the getters (<code>getField()</code>
 * or <code>expr.getField()</code>). It uses the template matching facility ({@link spoon.template.TemplateMatcher})
 * to avoid doing this transformation when the field access is done within a
 * getter or a setter implementation (to avoid infinite runtime recursions).
 */
public class ReadAccessProcessor<_FieldType_> extends
        AbstractProcessor<CtFieldAccess<_FieldType_>> {

    public boolean isToBeProcessed(CtFieldAccess<_FieldType_> candidate) {
        return true;
    }

    public void process(CtFieldAccess<_FieldType_> access) {
        CtFieldReference<_FieldType_> field = access.getVariable();
        if (ServerAccessProcessor.fields.contains(field)) {
            CtClass<FieldAccessTemplate<?>> templateType = getFactory().Class()
                    .get(FieldAccessTemplate.class);
            TemplateMatcher matcher = new TemplateMatcher(templateType);
            CtMethod<?> setter = (CtMethod<?>) templateType.getAnnotatedChildren(
                    Setter.class).get(0);
            CtMethod<?> getter = (CtMethod<?>) templateType.getAnnotatedChildren(
                    Getter.class).get(0);
            if (!matcher.match(access.getParent(CtMethod.class), setter)
                    && !matcher.match(access.getParent(CtMethod.class), getter)) {
                Template t = new FieldAccessTemplate<_FieldType_>(field
                        .getType(), field.getSimpleName(), access, null);
                CtBlock<?> b = Substitution.substituteMethodBody(access
                        .getParent(CtClass.class), t, "getterInvocation");
                access.replace(b.getStatements().get(0));
            }
        }
    }
}