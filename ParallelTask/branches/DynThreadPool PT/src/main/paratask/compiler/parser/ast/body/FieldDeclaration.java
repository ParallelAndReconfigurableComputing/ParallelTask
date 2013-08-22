/*
 * Copyright (C) 2007 J�lio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05/10/2006
 */
package paratask.compiler.parser.ast.body;

import paratask.compiler.parser.ast.expr.AnnotationExpr;
import paratask.compiler.parser.ast.type.Type;
import paratask.compiler.parser.ast.visitor.GenericVisitor;
import paratask.compiler.parser.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class FieldDeclaration extends BodyDeclaration {

    private final int modifiers;

    private final List<AnnotationExpr> annotations;

    private final Type type;

    private final List<VariableDeclarator> variables;

    public FieldDeclaration(int line, int column, JavadocComment javaDoc, int modifiers, List<AnnotationExpr> annotations, Type type, List<VariableDeclarator> variables) {
        super(line, column, javaDoc);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.variables = variables;
    }

    public int getModifiers() {
        return modifiers;
    }

    public List<AnnotationExpr> getAnnotations() {
        return annotations;
    }

    public Type getType() {
        return type;
    }

    public List<VariableDeclarator> getVariables() {
        return variables;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
