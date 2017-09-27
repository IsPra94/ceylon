package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import org.eclipse.ceylon.javax.lang.model.type.ArrayType;
import org.eclipse.ceylon.javax.lang.model.type.DeclaredType;
import org.eclipse.ceylon.javax.lang.model.type.ErrorType;
import org.eclipse.ceylon.javax.lang.model.type.ExecutableType;
import org.eclipse.ceylon.javax.lang.model.type.IntersectionType;
import org.eclipse.ceylon.javax.lang.model.type.NoType;
import org.eclipse.ceylon.javax.lang.model.type.NullType;
import org.eclipse.ceylon.javax.lang.model.type.PrimitiveType;
import org.eclipse.ceylon.javax.lang.model.type.TypeMirror;
import org.eclipse.ceylon.javax.lang.model.type.TypeVariable;
import org.eclipse.ceylon.javax.lang.model.type.TypeVisitor;
import org.eclipse.ceylon.javax.lang.model.type.UnionType;
import org.eclipse.ceylon.javax.lang.model.type.WildcardType;

public class TypeVisitorWrapper<R, P> implements TypeVisitor<R, P> {

    private javax.lang.model.type.TypeVisitor<R, P> d;

    public TypeVisitorWrapper(javax.lang.model.type.TypeVisitor<R, P> d) {
        this.d = d;
    }

    @Override
    public R visit(TypeMirror t, P p) {
        return d.visit(Facades.facade(t), p);
    }

    @Override
    public R visit(TypeMirror t) {
        return d.visit(Facades.facade(t));
    }

    @Override
    public R visitPrimitive(PrimitiveType t, P p) {
        return d.visitPrimitive(Facades.facade(t), p);
    }

    @Override
    public R visitNull(NullType t, P p) {
        return d.visitNull(Facades.facade(t), p);
    }

    @Override
    public R visitArray(ArrayType t, P p) {
        return d.visitArray(Facades.facade(t), p);
    }

    @Override
    public R visitDeclared(DeclaredType t, P p) {
        return d.visitDeclared(Facades.facade(t), p);
    }

    @Override
    public R visitError(ErrorType t, P p) {
        return d.visitError(Facades.facade(t), p);
    }

    @Override
    public R visitTypeVariable(TypeVariable t, P p) {
        return d.visitTypeVariable(Facades.facade(t), p);
    }

    @Override
    public R visitWildcard(WildcardType t, P p) {
        return d.visitWildcard(Facades.facade(t), p);
    }

    @Override
    public R visitExecutable(ExecutableType t, P p) {
        return d.visitExecutable(Facades.facade(t), p);
    }

    @Override
    public R visitNoType(NoType t, P p) {
        return d.visitNoType(Facades.facade(t), p);
    }

    @Override
    public R visitUnknown(TypeMirror t, P p) {
        try{
            return d.visitUnknown(Facades.facade(t), p);
        }catch(javax.lang.model.type.UnknownTypeException x){
            throw Wrappers.wrap(x);
        }
    }

    @Override
    public R visitUnion(UnionType t, P p) {
        return d.visitUnion(Facades.facade(t), p);
    }

    @Override
    public R visitIntersection(IntersectionType t, P p) {
        // FIXME: J8 issue
//        return d.visitIntersection(Facades.facade(t), p);
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TypeVisitorWrapper == false)
            return false;
        return d.equals(((TypeVisitorWrapper)obj).d);
    }
    
    @Override
    public int hashCode() {
        return d.hashCode();
    }
    
    @Override
    public String toString() {
        return d.toString();
    }
}
