package org.eclipse.ceylon.compiler.java.language;

import ceylon.language.Annotated;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.Nonbean;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
@org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes({"ceylon.language::OptionalAnnotation<java.lang::Nonbean,ceylon.language.meta.declaration::ClassDeclaration,ceylon.language::Anything>"})
public class Nonbean
        implements org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType,
            ceylon.language.OptionalAnnotation<Nonbean, Annotated, Object>,
            java.io.Serializable {

    private static final long serialVersionUID = -5466420866210193001L;

    @Ignore
    public Nonbean(Nonbean$annotation$ ignored) {
    }
    public Nonbean() {
    }
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return Nonbean.$TypeDescriptor$;
    }
    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Nonbean.class);

    @Override
    @Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Nonbean$annotation$.class;
    }
}