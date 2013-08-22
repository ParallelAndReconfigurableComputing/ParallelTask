/* 
 *  Copyright (C) 2010 Nasser Giacaman, Oliver Sinnen
 *
 *  This file is part of Parallel Task. 
 * 
 *  Parallel Task has been developed based on the Java 1.5 parser and
 *  Abstract Syntax Tree as a foundation. This file is part of the original
 *  Java 1.5 parser and Abstract Syntax Tree code, but has been extended
 *  to support features necessary for Parallel Task. Below is the original
 *  Java 1.5 parser and Abstract Syntax Tree license. 
 *
 *  Parallel Task is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or (at 
 *  your option) any later version.
 *
 *  Parallel Task is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along 
 *  with Parallel Task. If not, see <http://www.gnu.org/licenses/>.
 */


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
package paratask.compiler.parser.ast.visitor;

import paratask.compiler.parser.ast.BlockComment;
import paratask.compiler.parser.ast.CompilationUnit;
import paratask.compiler.parser.ast.ImportDeclaration;
import paratask.compiler.parser.ast.LineComment;
import paratask.compiler.parser.ast.Node;
import paratask.compiler.parser.ast.PackageDeclaration;
import paratask.compiler.parser.ast.TypeParameter;
import paratask.compiler.parser.ast.body.AnnotationDeclaration;
import paratask.compiler.parser.ast.body.AnnotationMemberDeclaration;
import paratask.compiler.parser.ast.body.ClassOrInterfaceDeclaration;
import paratask.compiler.parser.ast.body.ConstructorDeclaration;
import paratask.compiler.parser.ast.body.EmptyMemberDeclaration;
import paratask.compiler.parser.ast.body.EmptyTypeDeclaration;
import paratask.compiler.parser.ast.body.EnumConstantDeclaration;
import paratask.compiler.parser.ast.body.EnumDeclaration;
import paratask.compiler.parser.ast.body.FieldDeclaration;
import paratask.compiler.parser.ast.body.InitializerDeclaration;
import paratask.compiler.parser.ast.body.JavadocComment;
import paratask.compiler.parser.ast.body.MethodDeclaration;
import paratask.compiler.parser.ast.body.Parameter;
import paratask.compiler.parser.ast.body.TaskDeclaration;
import paratask.compiler.parser.ast.body.VariableDeclarator;
import paratask.compiler.parser.ast.body.VariableDeclaratorId;
import paratask.compiler.parser.ast.expr.ArrayAccessExpr;
import paratask.compiler.parser.ast.expr.ArrayCreationExpr;
import paratask.compiler.parser.ast.expr.ArrayInitializerExpr;
import paratask.compiler.parser.ast.expr.AssignExpr;
import paratask.compiler.parser.ast.expr.BinaryExpr;
import paratask.compiler.parser.ast.expr.BooleanLiteralExpr;
import paratask.compiler.parser.ast.expr.CastExpr;
import paratask.compiler.parser.ast.expr.CharLiteralExpr;
import paratask.compiler.parser.ast.expr.ClassExpr;
import paratask.compiler.parser.ast.expr.ConditionalExpr;
import paratask.compiler.parser.ast.expr.DoubleLiteralExpr;
import paratask.compiler.parser.ast.expr.EnclosedExpr;
import paratask.compiler.parser.ast.expr.FieldAccessExpr;
import paratask.compiler.parser.ast.expr.InstanceOfExpr;
import paratask.compiler.parser.ast.expr.IntegerLiteralExpr;
import paratask.compiler.parser.ast.expr.IntegerLiteralMinValueExpr;
import paratask.compiler.parser.ast.expr.LongLiteralExpr;
import paratask.compiler.parser.ast.expr.LongLiteralMinValueExpr;
import paratask.compiler.parser.ast.expr.MarkerAnnotationExpr;
import paratask.compiler.parser.ast.expr.MemberValuePair;
import paratask.compiler.parser.ast.expr.MethodCallExpr;
import paratask.compiler.parser.ast.expr.NameExpr;
import paratask.compiler.parser.ast.expr.NormalAnnotationExpr;
import paratask.compiler.parser.ast.expr.NullLiteralExpr;
import paratask.compiler.parser.ast.expr.ObjectCreationExpr;
import paratask.compiler.parser.ast.expr.QualifiedNameExpr;
import paratask.compiler.parser.ast.expr.SingleMemberAnnotationExpr;
import paratask.compiler.parser.ast.expr.StringLiteralExpr;
import paratask.compiler.parser.ast.expr.SuperExpr;
import paratask.compiler.parser.ast.expr.SuperMemberAccessExpr;
import paratask.compiler.parser.ast.expr.TaskClauseExpr;
import paratask.compiler.parser.ast.expr.ThisExpr;
import paratask.compiler.parser.ast.expr.UnaryExpr;
import paratask.compiler.parser.ast.expr.VariableDeclarationExpr;
import paratask.compiler.parser.ast.stmt.AssertStmt;
import paratask.compiler.parser.ast.stmt.BlockStmt;
import paratask.compiler.parser.ast.stmt.BreakStmt;
import paratask.compiler.parser.ast.stmt.CatchClause;
import paratask.compiler.parser.ast.stmt.ContinueStmt;
import paratask.compiler.parser.ast.stmt.DoStmt;
import paratask.compiler.parser.ast.stmt.EmptyStmt;
import paratask.compiler.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import paratask.compiler.parser.ast.stmt.ExpressionStmt;
import paratask.compiler.parser.ast.stmt.ForStmt;
import paratask.compiler.parser.ast.stmt.ForeachStmt;
import paratask.compiler.parser.ast.stmt.IfStmt;
import paratask.compiler.parser.ast.stmt.LabeledStmt;
import paratask.compiler.parser.ast.stmt.ReturnStmt;
import paratask.compiler.parser.ast.stmt.SwitchEntryStmt;
import paratask.compiler.parser.ast.stmt.SwitchStmt;
import paratask.compiler.parser.ast.stmt.SynchronizedStmt;
import paratask.compiler.parser.ast.stmt.ThrowStmt;
import paratask.compiler.parser.ast.stmt.TryStmt;
import paratask.compiler.parser.ast.stmt.TypeDeclarationStmt;
import paratask.compiler.parser.ast.stmt.WhileStmt;
import paratask.compiler.parser.ast.type.ClassOrInterfaceType;
import paratask.compiler.parser.ast.type.PrimitiveType;
import paratask.compiler.parser.ast.type.ReferenceType;
import paratask.compiler.parser.ast.type.VoidType;
import paratask.compiler.parser.ast.type.WildcardType;

/**
 * @author Julio Vilmar Gesser
 */
public interface VoidVisitor<A> {

    public void visit(Node n, A arg);
    
    //- ParaTask --------------------
    public void visit(TaskClauseExpr tc, A arg);
    
    public void visit(TaskDeclaration n, A arg);

    //- Compilation Unit ----------------------------------

    public void visit(CompilationUnit n, A arg);

    public void visit(PackageDeclaration n, A arg);

    public void visit(ImportDeclaration n, A arg);

    public void visit(TypeParameter n, A arg);

    public void visit(LineComment n, A arg);

    public void visit(BlockComment n, A arg);

    //- Body ----------------------------------------------

    public void visit(ClassOrInterfaceDeclaration n, A arg);

    public void visit(EnumDeclaration n, A arg);

    public void visit(EmptyTypeDeclaration n, A arg);

    public void visit(EnumConstantDeclaration n, A arg);

    public void visit(AnnotationDeclaration n, A arg);

    public void visit(AnnotationMemberDeclaration n, A arg);

    public void visit(FieldDeclaration n, A arg);

    public void visit(VariableDeclarator n, A arg);

    public void visit(VariableDeclaratorId n, A arg);

    public void visit(ConstructorDeclaration n, A arg);

    public void visit(MethodDeclaration n, A arg);

    public void visit(Parameter n, A arg);

    public void visit(EmptyMemberDeclaration n, A arg);

    public void visit(InitializerDeclaration n, A arg);

    public void visit(JavadocComment n, A arg);

    //- Type ----------------------------------------------

    public void visit(ClassOrInterfaceType n, A arg);

    public void visit(PrimitiveType n, A arg);

    public void visit(ReferenceType n, A arg);

    public void visit(VoidType n, A arg);

    public void visit(WildcardType n, A arg);

    //- Expression ----------------------------------------

    public void visit(ArrayAccessExpr n, A arg);

    public void visit(ArrayCreationExpr n, A arg);

    public void visit(ArrayInitializerExpr n, A arg);

    public void visit(AssignExpr n, A arg);

    public void visit(BinaryExpr n, A arg);

    public void visit(CastExpr n, A arg);

    public void visit(ClassExpr n, A arg);

    public void visit(ConditionalExpr n, A arg);

    public void visit(EnclosedExpr n, A arg);

    public void visit(FieldAccessExpr n, A arg);

    public void visit(InstanceOfExpr n, A arg);

    public void visit(StringLiteralExpr n, A arg);

    public void visit(IntegerLiteralExpr n, A arg);

    public void visit(LongLiteralExpr n, A arg);

    public void visit(IntegerLiteralMinValueExpr n, A arg);

    public void visit(LongLiteralMinValueExpr n, A arg);

    public void visit(CharLiteralExpr n, A arg);

    public void visit(DoubleLiteralExpr n, A arg);

    public void visit(BooleanLiteralExpr n, A arg);

    public void visit(NullLiteralExpr n, A arg);

    public void visit(MethodCallExpr n, A arg);

    public void visit(NameExpr n, A arg);

    public void visit(ObjectCreationExpr n, A arg);

    public void visit(QualifiedNameExpr n, A arg);

    public void visit(SuperMemberAccessExpr n, A arg);

    public void visit(ThisExpr n, A arg);

    public void visit(SuperExpr n, A arg);

    public void visit(UnaryExpr n, A arg);

    public void visit(VariableDeclarationExpr n, A arg);

    public void visit(MarkerAnnotationExpr n, A arg);

    public void visit(SingleMemberAnnotationExpr n, A arg);

    public void visit(NormalAnnotationExpr n, A arg);

    public void visit(MemberValuePair n, A arg);

    //- Statements ----------------------------------------

    public void visit(ExplicitConstructorInvocationStmt n, A arg);

    public void visit(TypeDeclarationStmt n, A arg);

    public void visit(AssertStmt n, A arg);

    public void visit(BlockStmt n, A arg);

    public void visit(LabeledStmt n, A arg);

    public void visit(EmptyStmt n, A arg);

    public void visit(ExpressionStmt n, A arg);

    public void visit(SwitchStmt n, A arg);

    public void visit(SwitchEntryStmt n, A arg);

    public void visit(BreakStmt n, A arg);

    public void visit(ReturnStmt n, A arg);

    public void visit(IfStmt n, A arg);

    public void visit(WhileStmt n, A arg);

    public void visit(ContinueStmt n, A arg);

    public void visit(DoStmt n, A arg);

    public void visit(ForeachStmt n, A arg);

    public void visit(ForStmt n, A arg);

    public void visit(ThrowStmt n, A arg);

    public void visit(SynchronizedStmt n, A arg);

    public void visit(TryStmt n, A arg);

    public void visit(CatchClause n, A arg);

}
