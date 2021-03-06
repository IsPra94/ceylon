/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.decl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.eclipse.ceylon.common.Nullable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.AnnotationBearing;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.ModuleImport;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ImportImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ModuleImpl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class ImportImpl 
    implements ceylon.language.meta.declaration.Import,
        AnnotationBearing,
        ReifiedType {
    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ImportImpl.class);
    
    private final ModuleImpl module;
    
    private final ModuleImport moduleImport;
    
    public ImportImpl(ModuleImpl module, ModuleImport moduleImport) {
        this.module = module;
        this.moduleImport = moduleImport;
    }

    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        final Field field = getField();
        return field != null ? field.getAnnotations() : AnnotationBearing.NONE;
    }
    
    @Override
    @Ignore
    public boolean $isAnnotated$(Class<? extends Annotation> annotationType) {
        final Field field = getField();
        return field != null ? field.isAnnotationPresent(annotationType) : false;
    }
    
    @Override
    public <AnnotationType extends java.lang.annotation.Annotation> boolean annotated(TypeDescriptor reifed$AnnotationType) {
        return Metamodel.isAnnotated(reifed$AnnotationType, this);
    }

    @Nullable
    private Field getField() {
        Class<?> javaClass = Metamodel.getJavaClass(this.module.declaration);
        String fieldName = getName().replace('.', '$');
        final Field field;
        try {
            field = javaClass.getField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            return null;
        }
        return field;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return moduleImport.getModule().getNameAsString();
    }
    
    @Override
    @TypeInfo("ceylon.language::String")
    public String getVersion() {
        return moduleImport.getModule().getVersion();
    }

    @Override
    public ceylon.language.meta.declaration.Module getContainer(){
        return module;
    }
    
    @Override
    public boolean getShared() {
        return moduleImport.isExport();
    }

    @Override
    public boolean getOptional() {
        return moduleImport.isOptional();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getContainer().hashCode();
        result = 37 * result + getName().hashCode();
        result = 37 * result + getVersion().hashCode();
        result = 37 * result + (getShared() ? 1 : 0);
        result = 37 * result + (getOptional() ? 1 : 0);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.declaration.Import == false)
            return false;
        ceylon.language.meta.declaration.Import other = (ceylon.language.meta.declaration.Import) obj;
        return getContainer().equals(other.getContainer())
                && getName().equals(other.getName())
                && getVersion().equals(other.getVersion())
                && getShared() == other.getShared()
                && getOptional() == other.getOptional();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    public String toString() {
        return "import of " + getName() +"/" + getVersion() + " by " + getContainer();
    }
}
