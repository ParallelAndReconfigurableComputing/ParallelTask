package spoon.examples.dbaccess.processing;

import spoon.examples.dbaccess.annotation.DBAccess;
import spoon.examples.dbaccess.annotation.DBType;
import spoon.examples.dbaccess.template.DBCodeTemplate;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.processing.Severity;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.template.Substitution;

public class DBAccessProcessor extends
		AbstractAnnotationProcessor<DBAccess, CtClass<?>> {

	public void process(DBAccess dbAccess, CtClass<?> target) {
		Factory f = target.getFactory();
		DBType t = dbAccess.type();
		if (t != DBType.RELATIONAL)
			f.getEnvironment().report(
					this,
					Severity.ERROR,
					target.getAnnotation(f.Type().createReference(
							DBAccess.class)), "unsupported DB system");

		DBCodeTemplate template = new DBCodeTemplate(f, dbAccess.database(),
				dbAccess.username(), dbAccess.password(), dbAccess.tableName());
		Substitution.insertField(target, template, f.Class().get(
				DBCodeTemplate.class).getField("connection"));
		for (CtConstructor<?> c : target.getConstructors()) {
			c.getBody().insertBegin((CtStatement)
					Substitution.substituteMethodBody(target, template,
							"initializerCode"));
		}
		for (CtMethod<?> m : target.getMethods()) {
			template._columnName_ = m.getSimpleName().substring(3)
					.toLowerCase();
			m.getBody().replace(
					Substitution.substituteMethodBody(target, template,
							"accessCode"));
		}
	}

}