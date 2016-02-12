/**
 * Copyright (C) 2006-2015 INRIA and contributors
 * Spoon - http://spoon.gforge.inria.fr/
 *
 * This software is governed by the CeCILL-C License under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-C license as
 * circulated by CEA, CNRS and INRIA at http://www.cecill.info.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the CeCILL-C License for more details.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 */
package spoon.support.reflect.code;

import spoon.reflect.code.CtCodeElement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.CtVisitor;

public class CtThrowImpl extends CtStatementImpl implements CtThrow {
	private static final long serialVersionUID = 1L;

	CtExpression<? extends Throwable> throwExpression;

	@Override
	public void accept(CtVisitor visitor) {
		visitor.visitCtThrow(this);
	}

	@Override
	public CtExpression<? extends Throwable> getThrownExpression() {
		return throwExpression;
	}

	@Override
	public <T extends CtThrow> T setThrownExpression(CtExpression<? extends Throwable> expression) {
		if (expression != null) {
			expression.setParent(this);
		}
		this.throwExpression = expression;
		return (T) this;
	}

	@Override
	public Void S() {
		return null;
	}

	public CtCodeElement getSubstitution(CtType<?> targetType) {
		return getFactory().Core().clone(this);
	}

}
