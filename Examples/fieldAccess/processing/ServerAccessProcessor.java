package spoon.examples.fieldaccess.processing;

import java.util.ArrayList;
import java.util.List;

import spoon.examples.fieldaccess.annotation.Access;
import spoon.examples.fieldaccess.template.FieldAccessTemplate;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.reference.CtFieldReference;
import spoon.template.Substitution;

/**
 * This processor inserts the getters and the setters typical implementations
 * for all the fields annotated with the
 * {@link spoon.examples.fieldaccess.annotation.Access} annotation.
 */
public class ServerAccessProcessor<_FieldType_> extends
        AbstractAnnotationProcessor<Access, CtField<_FieldType_>> {

    public static List<CtFieldReference<?>> fields = new ArrayList<CtFieldReference<?>>();

    public void process(Access access, CtField<_FieldType_> field) {
    	System.out.println("=============>"+field);
        fields.add(field.getReference());
        Substitution.insertAll(field.getParent(CtClass.class),
                new FieldAccessTemplate<_FieldType_>(field.getType(), field
                        .getSimpleName(), null, null));
    }
}