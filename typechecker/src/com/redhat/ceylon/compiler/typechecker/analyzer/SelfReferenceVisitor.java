package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates that the initializer of a class does
 * not leak self-references to the instance being
 * initialized.
 * 
 * @author Gavin King
 *
 */
public class SelfReferenceVisitor extends Visitor {
        
    private Tree.Statement lastExecutableStatement;
    private Boolean declarationSection = null;
    private Boolean outerDeclarationSection = null;

    @Override
    public void visit(Tree.AnnotationList that) {}

    @Override
    public void visit(Tree.ExtendedTypeExpression that) {
        super.visit(that);
        Declaration member  = that.getDeclaration();
        if (member!=null && !inOuterDeclarationSection() && 
                !member.isToplevel() && 
                !member.isDefinedInScope(that.getScope().getContainer())) {
            //then it must be an inherited member class?
            //TODO: is that logic exactly correct?
            that.addError("inherited member class may not be extended in initializer: " + 
                            member.getName());
        }
    }

    @Override
    public void visit(Tree.BaseMemberExpression that) {
        super.visit(that);
        visitReference(that);
    }

    @Override
    public void visit(Tree.BaseTypeExpression that) {
        super.visit(that);
        visitReference(that);
    }

    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
    }

    @Override
    public void visit(Tree.QualifiedTypeExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
    }

    private boolean isSelfReference(Tree.Primary that) {
        return that instanceof Tree.This || that instanceof Tree.Outer;
    }

    private void visitReference(Tree.Primary that) {
        Declaration member  = that.getDeclaration();
        if (member!=null && !inDeclarationSection() && 
                !member.isToplevel() && 
                !member.isDefinedInScope(that.getScope())) {
            //then it must be an inherited member?
            //TODO: is that logic exactly correct?
            that.addError("inherited member may not be used in initializer: " + 
                            member.getName());
        }
    }
    
    @Override
    public void visit(Tree.InterfaceBody that) {
        Tree.Statement oles = lastExecutableStatement;
        Boolean ods = outerDeclarationSection;
        outerDeclarationSection = declarationSection;
        declarationSection = true;
        lastExecutableStatement = null;
        super.visit(that);
        declarationSection = outerDeclarationSection;
        outerDeclarationSection = ods;
        lastExecutableStatement = oles;
    }
    
    @Override
    public void visit(Tree.ClassBody that) {
        Tree.Statement les = null;
        for (Tree.Statement s: that.getStatements()) {
            if (s instanceof Tree.ExecutableStatement) {
                les = s;
            }
            else {
                if (s instanceof Tree.AttributeDeclaration) {
                    if ( ((Tree.AttributeDeclaration) s).getSpecifierOrInitializerExpression()!=null ) {
                        les = s;
                    }
                }
                if (s instanceof Tree.MethodDeclaration) {
                    if ( ((Tree.MethodDeclaration) s).getSpecifierExpression()!=null ) {
                        les = s;
                    }
                }
            }
        }
        Tree.Statement oles = lastExecutableStatement;
        Boolean ods = outerDeclarationSection;
        outerDeclarationSection = declarationSection;
        declarationSection = les==null;
        lastExecutableStatement = les;
        super.visit(that);
        lastExecutableStatement = oles;
        declarationSection = outerDeclarationSection;
        outerDeclarationSection = ods;
    }
    
    @Override
    public void visit(Tree.ExtendedType that) {
        Boolean ods = outerDeclarationSection;
        outerDeclarationSection = declarationSection;
        declarationSection = false;
        super.visit(that);
        declarationSection = outerDeclarationSection;
        outerDeclarationSection = ods;
    }
    
    boolean inClassOrInterfaceBody() {
        return declarationSection!=null;
    }
    
    boolean inNestedClassOrInterfaceBody() {
        return outerDeclarationSection!=null;
    }
    
    boolean inDeclarationSection() {
        return declarationSection!=null && declarationSection;
    }
    
    boolean inOuterDeclarationSection() {
        return outerDeclarationSection!=null && outerDeclarationSection;
    }
    
    @Override
    public void visit(Tree.Statement that) {
        super.visit(that);
        if (inClassOrInterfaceBody()) {
            declarationSection = declarationSection || 
                    that==lastExecutableStatement;
        }
    }
    
    @Override
    public void visit(Tree.Return that) {
        super.visit(that);
        if ( that.getExpression()!=null ) {
            if ( that.getExpression().getTerm() instanceof Tree.Super ) {
                that.addError("leaks super reference");
            }    
            if ( !inDeclarationSection() &&
                    that.getExpression().getTerm() instanceof Tree.This ) {
                that.addError("leaks this reference");
            }    
            if ( !inOuterDeclarationSection() &&
                    that.getExpression().getTerm() instanceof Tree.Outer ) {
                that.addError("leaks outer reference");
            }    
        }
    }

    @Override
    public void visit(Tree.SpecifierOrInitializerExpression that) {
        super.visit(that);
        if ( that.getExpression().getTerm() instanceof Tree.Super ) {
            that.addError("leaks super reference");
        }    
        if ( !inDeclarationSection() && 
                that.getExpression().getTerm() instanceof Tree.This ) {
            that.addError("leaks this reference");
        }
        if (!inOuterDeclarationSection() &&
                that.getExpression().getTerm() instanceof Tree.Outer ) {
            that.addError("leaks outer reference");
        }    
    }

    @Override
    public void visit(Tree.AssignmentOp that) {
        super.visit(that);
        if ( that.getRightTerm() instanceof Tree.Super ) {
            that.addError("leaks super reference");
        }    
        if ( !inDeclarationSection() &&
                that.getRightTerm() instanceof Tree.This ) {
            that.addError("leaks this reference");
        }
        if (!inOuterDeclarationSection() &&
                that.getRightTerm() instanceof Tree.Outer ) {
            that.addError("leaks outer reference");
        }    
    }

    @Override
    public void visit(Tree.PositionalArgumentList that) {
        super.visit(that);
        if ( inClassOrInterfaceBody() ) {
            for ( Tree.PositionalArgument arg: that.getPositionalArguments()) {
                if ( arg.getExpression().getTerm() instanceof Tree.Super ) {
                    that.addError("leaks super reference");
                }    
                if (arg.getExpression().getTerm() instanceof Tree.This
                        && !inDeclarationSection()) {
                    arg.addError("leaks this reference");
                }
                if (arg.getExpression().getTerm() instanceof Tree.Outer
                        && !inOuterDeclarationSection()) {
                    arg.addError("leaks outer reference");
                }
            }
        }
    }

    @Override
    public void visit(Tree.NamedArgumentList that) {
        super.visit(that);
        if ( inClassOrInterfaceBody() ) {
            for ( Tree.NamedArgument arg: that.getNamedArguments()) {
                if (arg instanceof Tree.SpecifiedArgument) {
                    SpecifierExpression se = ((Tree.SpecifiedArgument) arg).getSpecifierExpression();
                    if ( se.getExpression().getTerm() instanceof Tree.Super ) {
                        that.addError("leaks super reference");
                    }    
                    if (se.getExpression().getTerm() instanceof Tree.This
                            && !inDeclarationSection()) {
                        arg.addError("leaks this reference");
                    }
                    if (se.getExpression().getTerm() instanceof Tree.Outer
                            && !inOuterDeclarationSection()) {
                        arg.addError("leaks outer reference");
                    }
                }
            }
        }
    }

    @Override public void visit(Tree.Outer that) {
        if ( !inNestedClassOrInterfaceBody() ) {
            that.addError("outer appears outside a nested class or interface body");
        }
    }
    
    @Override public void visit(Tree.This that) {
        if ( !inClassOrInterfaceBody() ) {
            that.addError("this appears outside a class class or interface body");
        }
    }

}
