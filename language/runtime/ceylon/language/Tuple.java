/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language;

import static java.lang.System.arraycopy;
import static org.eclipse.ceylon.compiler.java.Util.toInt;
import static org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel.getTypeDescriptor;
import static org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor.NothingType;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Annotation;
import org.eclipse.ceylon.compiler.java.metadata.Annotations;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.FunctionalParameter;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes;
import org.eclipse.ceylon.compiler.java.metadata.Transient;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.compiler.java.runtime.serialization.$Serialization$;
import org.eclipse.ceylon.compiler.java.runtime.serialization.Serializable;

import ceylon.language.impl.BaseIterator;
import ceylon.language.impl.MemberImpl;
import ceylon.language.impl.rethrow_;
import ceylon.language.meta.declaration.ClassDeclaration;
import ceylon.language.meta.declaration.ValueDeclaration;
import ceylon.language.serialization.Member;
import ceylon.language.serialization.ReachableReference;

@Ceylon(major = 8)
@Class(extendsType = "ceylon.language::Object", 
       basic = false, identifiable = false)
@Annotations({
        @Annotation(
                value = "doc",
                arguments = {"A _tuple_ is a typed linked list..."}),
        @Annotation(
                value = "by",
                arguments = {"Gavin"}),
        @Annotation("shared"),
        @Annotation("final")})
@SatisfiedTypes({
        "ceylon.language::Sequence<Element>"})
@TypeParameters({
        @TypeParameter(
                value = "Element",
                variance = Variance.OUT,
                satisfies = {},
                caseTypes = {}),
        @TypeParameter(
                value = "First",
                variance = Variance.OUT,
                satisfies = {"Element"},
                caseTypes = {}),
        @TypeParameter(
                value = "Rest",
                variance = Variance.OUT,
                satisfies = {"ceylon.language::Sequential<Element>"},
                caseTypes = {},
                defaultValue = "ceylon.language::Empty")})
@SharedAnnotation$annotation$
@FinalAnnotation$annotation$
@SerializableAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public final class Tuple<Element, First extends Element, 
                Rest extends Sequential<? extends Element>>
        implements Sequence<Element>, Serializable, 
                   ReifiedType, java.io.Serializable {
    
    private static final long serialVersionUID = 6194999680327535668L;
    
    /** 
     * A backing array. May be shared between many Tuple instances
     * (Flyweight pattern).
     */
    @Ignore
    final java.lang.Object[] array;
    
    @Ignore
    private TypeDescriptor $reifiedElement;
    
    /** 
     * The rest of the elements, after those in the array.
     * This should never be another Tuple instance though.
     */
    private Sequential<? extends Element> rest;

    /**
     * The ultimate constructor.
     * @param array A backing array
     * @param rest The tail of the tuple (not itself a Tuple)
     * @param copy Whether the backing array should be copied 
     * (i.e. false only if the caller can guarantee the passed 
     * array will <strong>never</strong> be modified after the call)
     */
    @SuppressWarnings("unchecked")
    @Ignore(handWritten = true)
    public Tuple(@Ignore TypeDescriptor $reifiedElement, 
            java.lang.Object[] array, 
            Sequential<? extends Element> rest, 
            boolean copy) {
//        super($reifiedElement);
        if (array.length + rest.getSize() == 0) {
            throw new AssertionError("Tuple may not have zero elements");
        }
        int length = array.length;
        this.$reifiedElement = $reifiedElement;
        if (copy) {
            this.array = (Element[])
                    Arrays.copyOfRange(array, 0, Util.toInt(length));
            
        } else {
            this.array = (Element[])array;
        }
        this.rest = rest;
        
        if (this.array == null) {
            throw new AssertionError("");
        }
        if (this.rest == null) {
            throw new AssertionError("");
        }
    }
    
    /**
     * The Ceylon initializer constructor
     */
    public Tuple(@Ignore
            final TypeDescriptor $reifiedElement, 
            @Ignore
            final TypeDescriptor $reifiedFirst, 
            @Ignore
            final TypeDescriptor $reifiedRest, 
            @Name("first")
            @TypeInfo("First")
            final First first, 
            @Name("rest")
            @TypeInfo("Rest")
            final Rest rest) {
        this($reifiedElement, makeArray(first, rest), makeRest(rest));
    }
    
    @Ignore(handWritten = true)
    private Tuple(TypeDescriptor $reifiedElement, java.lang.Object[] array,
            Sequential<? extends Element> rest) {
        this($reifiedElement, array, rest, false);
    }
    
    private static java.lang.Object[] makeArray(java.lang.Object first,
            Sequential<?> rest) {
        final java.lang.Object[] array;
        if (rest instanceof Tuple) {
            Tuple<?,?,?> other = (Tuple<?,?,?>) rest;
            array = new java.lang.Object[1 + other.array.length];
            array[0] = first;
            System.arraycopy(other.array, 0, array, 1, other.array.length);
            rest = other.rest;
        } else {
            array = new java.lang.Object[] { first };
        }
        return array;
    }
    
    private static <Element> Sequential<? extends Element> 
    makeRest(Sequential<? extends Element> rest) {
        if (rest instanceof Tuple) {
            @SuppressWarnings("unchecked")
            Tuple<? extends Element,? extends Element,? extends Sequential<? extends Element>> other = 
                    (Tuple<? extends Element,? extends Element,? extends Sequential<? extends Element>>) rest;
            return other.rest;
        }
        return rest;
    }
    
    @SuppressWarnings("unchecked")
    @Ignore(handWritten = true)
    public Tuple(TypeDescriptor $reifiedElement, 
            java.lang.Object[] elements) {
        this($reifiedElement, elements, 
                (Sequential<? extends Element>) empty_.get_(), false);
    }
    

    /**
     * The constructor used by the transformation of {@code [foo, bar *baz]}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    public static Tuple<?,?,?> 
    instance(TypeDescriptor $reifiedElement, 
            java.lang.Object[] elements, 
            Sequential<?> tail) {
        Sequential<?> rest;
        java.lang.Object[] array;
        if (tail instanceof Tuple) {
            Tuple<?,?,?> other = (Tuple<?,?,?>) tail;
            array = new java.lang.Object[elements.length + other.array.length];
            System.arraycopy(elements, 0, array, 0, elements.length);
            System.arraycopy(other.array, 0, array, elements.length, other.array.length);
            rest = other.rest;
        } else {
            array = elements;
            rest = tail;
        }
        return new Tuple($reifiedElement, array, rest, false);
    }
    
    /**
     * The constructor used by the transformation of {@code [foo, bar]}
     */
    @Ignore
    public static Tuple<?,?,?> 
    instance(TypeDescriptor $reifiedElement, 
            java.lang.Object[] elements) {
        return instance($reifiedElement, elements, empty_.get_());
    }
    
    @Ignore
    protected TypeDescriptor $getReifiedElement$() {
        return ((TypeDescriptor.Class)$getType$()).getSequenceElement();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("First")
    @SuppressWarnings("unchecked")
    public final First getFirst() {
        return (First)getFromFirst(0);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("Rest")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Rest getRest() {
        if (getSize()==1) {
            return (Rest) empty_.get_();
        }
        else if (this.array.length == 1) {
            return (Rest)rest;
        }
        else {
            TypeDescriptor typeArg = ((TypeDescriptor.Class)
                    ((TypeDescriptor.Class)$getType$()).getTupleRest())
                    .getSequenceElement();
            java.lang.Object[] copy = 
                    Arrays.copyOfRange(this.array, 1, this.array.length);
            return (Rest) new Tuple(typeArg, copy, rest, false);
        }
    }

    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Integer")
    @Transient
    public final long getSize() {
        return array.length + rest.getSize();
    }
    
    @SuppressWarnings("unchecked")
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public final Element getFromFirst(@Name("index") final long index) {
        if (index < 0) {
            return null;
        } else if (index >= array.length) {
            return (Element)rest.getFromFirst(index-array.length);
        }
        else {
            return (Element)array[toInt(index)];
        }
    }
    
    @SuppressWarnings("unchecked")
    @Ignore
    @Override
    public Element getFromLast(long index) {
        if (index < 0) {
            return null;
        } else if (index >= array.length) {
            return (Element)rest.getFromLast(index - this.array.length);
        }
        return (Element)array[array.length-1-toInt(index)];
    }

//    @Ignore
//    @Override
//    public final Element get(Integer index) {
//        return getFromFirst(index.value);
//    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Integer")
    @Transient
    public final ceylon.language.Integer getLastIndex() {
        return Integer.instance(array.length + rest.getSize() - 1);
    }
    
    @SuppressWarnings("unchecked")
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("Element")
    @Transient
    public final Element getLast() {
        if (!rest.getEmpty()) {
            return (Element)rest.getLast();
        }
        return (Element)array[array.length - 1];
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> 
    measure(@Name("from")
    @TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer from, @Name("length")
    @TypeInfo("ceylon.language::Integer")
    long length) {
        long fromIndex = from.longValue();
        if (fromIndex < 0) {
            length = length+fromIndex;
            fromIndex = 0;
        }
        final long lastIndex = getSize()-1;
        
        if (fromIndex > lastIndex || length <= 0) {
            return getEmptyTuple();
        }
        long l;
        if (length > lastIndex-fromIndex) {
            l = lastIndex-fromIndex+1;
        } else {
            l = length;
        }
        if (fromIndex+l<array.length) {
            // just chop the array, discarding rest
            java.lang.Object[] copy = fromIndex+l==array.length ? 
                    this.array : Arrays.copyOfRange(this.array, 
                            toInt(fromIndex), toInt(fromIndex+l));
            return new Tuple<Element,Element,Sequential<? extends Element>>
                    ($reifiedElement, copy, getEmptyTuple(), false);    
        } else if (fromIndex >= array.length) {
            // chop the rest
            return rest.measure(Integer.instance(fromIndex-array.length), l);
        } else {
            // we need the trailing elements of the array and the 
            // leading elements of rest
            java.lang.Object[] copy = Arrays.copyOfRange(this.array, 
                    toInt(fromIndex), toInt(this.array.length));
            Sequential<? extends Element> rest =
                    this.rest.measure(Integer.instance(0), 
                            l-(this.array.length-toInt(fromIndex)));
            return new Tuple<Element,Element,Sequential<? extends Element>>
                    ($reifiedElement, copy, rest, false);
        }
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> 
    span(@Name("from") final ceylon.language.Integer from, 
         @Name("end") final ceylon.language.Integer end) {
        long fromIndex = Util.toInt(from.longValue());
        long toIndex = end==null ? getSize() : end.longValue();
        long lastIndex = getSize()-1;
        boolean reverse = toIndex<fromIndex;
        if (reverse) {
            long tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }
        if (toIndex<0 || fromIndex>lastIndex) {
            return getEmptyTuple();
        }
        // fromIndex < toIndex
        fromIndex= Math.max(fromIndex, 0);
        toIndex = Math.min(toIndex, lastIndex);
        if (toIndex < array.length 
                || rest.getEmpty()) {
            // just chop the array, discarding rest
            java.lang.Object[] newArray;
            if (reverse) {
                int fromInt = Util.toInt(fromIndex);
                int toInt = Util.toInt(toIndex);
                newArray = new java.lang.Object[toInt-fromInt+1];
                for (int ii = toInt, jj = 0; ii >= fromIndex; ii--, jj++) {
                    newArray[jj] = getFromFirst(ii);
                }
            } else {
                newArray = Arrays.copyOfRange(this.array, 
                        toInt(fromIndex), toInt(toIndex)+1);
            }
            return new Tuple<Element, First, Rest>($getReifiedElement$(), 
                    newArray, getEmptyTuple(), false);
        } else if (fromIndex >= array.length) {
            // chop the rest
            if (reverse) {
                return rest.span(Integer.instance(toIndex-array.length), 
                        Integer.instance(fromIndex-array.length));
            } else {
                return rest.span(Integer.instance(fromIndex-array.length), 
                        Integer.instance(toIndex-array.length));
            }
        } else {// fromIndex < array.length <= toIndex
            // we need the trailing elements of the array and the 
            // leading elements of rest
            java.lang.Object[] newArray = Arrays.copyOfRange(this.array, 
                    toInt(fromIndex), this.array.length);
            Sequential<? extends Element> rest = 
                    (Sequential)this.rest.span(
                        Integer.instance(0), 
                        Integer.instance(toIndex-array.length));
            Tuple<Element, First, Rest> result = 
                    new Tuple<Element, First, Rest>($getReifiedElement$(), 
                            newArray, rest, false);
            return reverse ? result.getReversed() : result;
        }
    }
    
    @SuppressWarnings("unchecked")
    private Sequential<? extends Element> getEmptyTuple() {
        return (Sequential<? extends Element>) empty_.get_();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> 
    spanTo(@Name("to") final ceylon.language.Integer to) {
        return to.longValue() < 0 ? 
                getEmptyTuple() : 
                span(Integer.instance(0), to);
    }

    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> 
    spanFrom(@Name("from") final ceylon.language.Integer from) {
        long size = getSize();
        return from.longValue() >= size ? 
                getEmptyTuple() : 
                span(from, Integer.instance(size-1));
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element,First,Rest>")
    @NonNull
    public final Tuple<Element, ? extends First, ? extends Rest> $clone() {
        return this;
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Iterator<Element>")
    public ceylon.language.Iterator<Element> iterator() {
        return new TupleIterator();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    public boolean contains(@Name("element")
    @TypeInfo("ceylon.language::Object")
    final java.lang.Object element) {
        for (int ii = 0; ii < array.length; ii++) {
            java.lang.Object x = array[ii];
            if (x!=null && element.equals(x)) return true;
        }
        return rest.contains(element);
    }
    
    @Ignore
    private volatile SoftReference<TypeDescriptor> $cachedType = null;
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        SoftReference<TypeDescriptor> cachedType = $cachedType;
        TypeDescriptor type = cachedType!=null ? cachedType.get() : null;
        if (type==null) {
            synchronized (this) {
                cachedType = $cachedType;
                type = cachedType!=null ? cachedType.get() : null;
                if (type==null) {
                    type = computeType();
                    $cachedType = new SoftReference<TypeDescriptor>(type);
                }
            }
        }
        return type;
    }
    
    @Annotations({
        @Annotation("shared"),
        @Annotation("actual")})
    @TypeInfo("ceylon.language::Tuple<Element,First,Rest>")
    @Override
    @NonNull
    public Sequence<? extends Element> tuple() {
        if (rest instanceof Empty) {
            return this;
        }
        else {
            int offset = array.length;
            int size = (int) rest.getSize();
            java.lang.Object[] arr = new java.lang.Object[offset + size];
            System.arraycopy(array, 0, arr, 0, offset);
            for (int i=0; i<size; i++) {
                arr[i + offset] = rest.getFromFirst(i);
            }
            return (Sequence<? extends Element>) 
                    instance($reifiedElement, arr);
        }
    }

    private TypeDescriptor computeType() {
        TypeDescriptor restType = getTypeDescriptor(rest);
        TypeDescriptor elementType = 
                Metamodel.getIteratedTypeDescriptor(restType);
        for (int i=array.length-1; i>=0; i--) {
            TypeDescriptor elemType = $getElementType(i);
            elementType = TypeDescriptor.union(elementType, elemType);
            restType = TypeDescriptor.klass(Tuple.class, 
                    elementType, elemType, restType);
        }
        return restType;
    }
    
    /*
    @Ignore
    private TypeDescriptor $getType(int offset) {
        if (offset < getSize()) {
            return TypeDescriptor.klass(Tuple.class, 
                    $getUnionOfAllType(offset), 
                    $getElementType(offset), 
                    $getType(offset + 1));
        } else {
            return empty_.$TypeDescriptor$;
        }
    }
    
    @Ignore
    private TypeDescriptor $getUnionOfAllType(int offset) {
        TypeDescriptor[] types = 
                new TypeDescriptor[Util.toInt(getSize() - offset)];
        for (int i = 0; i < getSize() - offset; i++) {
            types[i] = $getElementType(offset + i);
        }
        return TypeDescriptor.union(types);
    }
    */
    
    @Ignore
    private TypeDescriptor $getElementType(int index) {
        return getTypeDescriptor(array[index]);
    }
    
    // The array length is the first element in the array
    @Ignore
    private static final long USE_ARRAY_SIZE = -10L;
    
    @Ignore
    @Override
    public boolean defines(@Name("key") Integer key) {
        long ind = key.longValue();
        return ind>=0 && ind<array.length || 
                rest.defines(Integer.instance(ind-this.array.length));
    }

    @Ignore
    private class TupleIterator 
            extends BaseIterator<Element> {
        
        private TupleIterator() {
            super($getReifiedElement$());
        }
        
        private long idx = 0;
        
        private Iterator<? extends Element> restIter = rest.iterator(); 
        
        @Override
        public java.lang.Object next() {
            if (idx < array.length) {
                return array[Util.toInt(idx++)];
            }
            else {
                return restIter.next();
            }
        }
        
        @Override
        public java.lang.String toString() {
            return "TupleIterator";
        }
        
    }

    @Ignore
    @Override
    public long count(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            @Name("selecting")
            @FunctionalParameter("(element)") 
            Callable<? extends Boolean> f) {
        int count=0;
        for (int ii = 0; ii < array.length; ii++) {
            java.lang.Object x = array[ii];
            if (x!=null && f.$call$(x).booleanValue()) count++;
        }
        return count + rest.count(f);
    }

    @Override
    @Ignore
    public Sequence<? extends Element> sequence() {
        return this; //$ceylon$language$Sequence$this.sequence();
    }

    @Override @Ignore
    public boolean longerThan(long length) {
        return this.array.length+rest.getSize() > length;
    }
    @Override @Ignore
    public boolean shorterThan(long length) {
        return this.array.length+rest.getSize() < length;
    }
    
    @Annotations({
        @Annotation("shared"),
        @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element|Other,Other,ceylon.language::Tuple<Element,First,Rest>>")
    public final <Other> 
    Tuple<java.lang.Object, ? extends Other, Tuple<Element,? extends First,? extends Rest>>
    withLeading(@Ignore TypeDescriptor $reifiedOther, 
                @Name("element") Other e) {
        int length = this.array.length;
        java.lang.Object[] array = new java.lang.Object[length+1];
        array[0] = e;
        arraycopy(this.array, 0, array, 1, length);
        return new Tuple<java.lang.Object, Other, Tuple<Element,? extends First,? extends Rest>>
                (TypeDescriptor.union($reifiedElement,$reifiedOther), array, rest);
    }
    
    @Annotations({
        @Annotation("shared"),
        @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element|Other,First,ceylon.language::Sequence<Element|Other>>")
    public <Other> 
    Tuple<java.lang.Object, First, ? extends Sequential<?>>
    withTrailing(@Ignore TypeDescriptor $reifiedOther,
                 @Name("element") Other e) {
        if (rest.getEmpty()) {
            int length = this.array.length;
            java.lang.Object[] array = new java.lang.Object[length+1];
            arraycopy(this.array, 0, array, 0, length);
            array[length] = e;
            return new Tuple<java.lang.Object, First, Sequential<?>>
                    (TypeDescriptor.union($reifiedElement,$reifiedOther), array);
        } else {
            return new Tuple<java.lang.Object, First, Sequential<?>>
                    (TypeDescriptor.union($reifiedElement,$reifiedOther), array, 
                            rest.withTrailing($reifiedOther, e));
        }
    }
    
    @Annotations({
        @Annotation("shared"),
        @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element|Other,First,ceylon.language::Sequential<Element|Other>>")
    public <Other> Tuple<java.lang.Object,First,Sequential<?>>
    append(@Ignore TypeDescriptor $reifiedOther, 
           @Name("elements") Sequential<? extends Other> es) {
        if (rest.getEmpty()) {
            int length = this.array.length;
            java.lang.Object[] array = 
                    new java.lang.Object[length+Util.toInt(es.getSize())];
            arraycopy(this.array, 0, array, 0, length);
            int ii = length;
            Iterator<?> iter = es.iterator();
            java.lang.Object o;
            while (!((o = iter.next()) instanceof Finished)) {
                array[ii++] = o;
            }
            return new Tuple<java.lang.Object,First,Sequential<?>>
                    (TypeDescriptor.union($reifiedElement,$reifiedOther), array);
        } else {
            return new Tuple<java.lang.Object,First,Sequential<?>>
                    (TypeDescriptor.union($reifiedElement,$reifiedOther), 
                            array, rest.append($reifiedOther, es));
        }
    }

    /** Gets the underlying array. Used for iteration using a C-style for */
    @Ignore
    public java.lang.Object[] $getArray$() {
        if (rest instanceof Empty) {
            return array;
        } 
        return null;
    }
    
    /** Gets the underlying first index. Used for iteration using a C-style for */
    @Ignore
    public int $getFirst$() {
        return 0;
    }
    
    /** Gets the underlying length. Used for iteration using a C-style for */
    @Ignore
    public int $getLength$() {
        return Util.toInt(array.length + rest.getSize());
    }
    
    @Ignore
    public Tuple($Serialization$ ignored, 
            TypeDescriptor $reifiedElement,
            TypeDescriptor $reifiedFirst, 
            TypeDescriptor $reifiedRest) {
        this.$reifiedElement = $reifiedElement;
        this.array = null;
        this.rest = null;
    }
    @Ignore
    @Override
    public java.util.Collection<ReachableReference> $references$() {
        // could put the elements with int indexes and the rest as a String!
        ArrayList<ReachableReference> s = new ArrayList<ReachableReference>(2);
        ClassDeclaration cd = (ClassDeclaration)Metamodel.getOrCreateMetamodel(Tuple.class);
        s.add(new MemberImpl(Util.assertExists(cd.<ValueDeclaration>getMemberDeclaration(ValueDeclaration.$TypeDescriptor$, "first"))));
        s.add(new MemberImpl(Util.assertExists(cd.<ValueDeclaration>getMemberDeclaration(ValueDeclaration.$TypeDescriptor$, "rest"))));
        return s;
    }
    @Ignore
    @Override
    public void $set$(ReachableReference indexOrAttr, java.lang.Object ref) {
        // Because of the tuple optimization, the array field depends on both 
        // the first and rest values. Because of this PartialImpl handles
        // Tuples as a special case and invokes $completeInit$() instead of $set$()
    }

    @Ignore
    public void $completeInit$(java.lang.Object first, java.lang.Object rest) {
        Lookup lookup = MethodHandles.lookup();
        try {
            java.lang.Object[] array = makeArray(first, (Sequential)rest);
            Util.setter(lookup, "array").invokeExact(this, array);
            Rest rest2 = (Rest)makeRest((Sequential)rest);
            Util.setter(lookup, "rest").invokeExact(this, rest2);
        } catch (java.lang.Throwable t) {
            rethrow_.rethrow(t);
        }
    }
    
    @Ignore
    @Override
    public java.lang.Object $get$(ReachableReference indexOrAttr) {
        if (indexOrAttr instanceof Member) {
            switch (((Member)indexOrAttr).getAttribute().getQualifiedName()){
            case "ceylon.language::Tuple.first":
                return getFirst();
            case "ceylon.language::Tuple.rest":
                return getRest();
            default:
                throw new AssertionError("unknown attribute " + indexOrAttr);
            }
        } else {
            throw new AssertionError("unknown reference " + indexOrAttr);
        }
    }
    
    
    
    //MIXINS:

    @Override @Ignore
    public Sequential$impl<? extends Element> $ceylon$language$Sequential$impl() {
        return new Sequential$impl<Element>($reifiedElement,this);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override @Ignore
    public Sequential<? extends Element> initial(long length) {
        if (length<=0) {
            return (Sequential<? extends Element>) empty_.get_();
        }
        long restSize = rest.getSize();
        if (length>=array.length+restSize) {
            return this;
        }
        if (length>=array.length) {
            return new Tuple($reifiedElement, array, 
                    rest.initial(length-array.length));
        }
        int len = (int) length;
        java.lang.Object[] initialArray = new java.lang.Object[len];
        System.arraycopy(array, 0, initialArray, 0, len);
        return new Tuple($reifiedElement, initialArray, 
                rest.initial(length-array.length));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override @Ignore
    public Sequential<? extends Element> terminal(long length) {
        if (length<=0) {
            return (Sequential<? extends Element>) empty_.get_();
        }
        long restSize = rest.getSize();
        if (length>=array.length+restSize) {
            return this;
        }
        if (length > restSize) {
            int len = (int) (length - restSize);
            java.lang.Object[] initialArray = new java.lang.Object[len];
            System.arraycopy(array, array.length-len, initialArray, 0, len);
            return new Tuple($reifiedElement, initialArray, rest);
        }
        else {
            return rest.terminal(length);
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override @Ignore
    public Sequential<? extends Element> trim(Callable<? extends Boolean> f) {
        if (!rest.getEmpty()) {
            return $ceylon$language$Sequential$impl().trim(f);
        }
        int size = array.length;
        int j = 0;
        while (j<size) {
            if (!f.$call$(array[j]).booleanValue()) {
                break;
            }
            j++;
        }
        int i = 0;
        while (i<size) {
            if (!f.$call$(array[size-1-i]).booleanValue()) {
                break;
            }
            i++;
        }
        if (i==0 && j==0) return this;
        java.lang.Object[] trimmedArray = new java.lang.Object[size-i-j];
        System.arraycopy(array, j, trimmedArray, 0, size-i);
        return new Tuple($reifiedElement, trimmedArray);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override @Ignore
    public Sequential<? extends Element> trimLeading(
            Callable<? extends Boolean> f) {
        if (!rest.getEmpty()) {
            return $ceylon$language$Sequential$impl().trimLeading(f);
        }
        int size = array.length;
        int i = 0;
        while (i<size) {
            if (!f.$call$(array[i]).booleanValue()) {
                break;
            }
            i++;
        }
        if (i==0) return this;
        java.lang.Object[] trimmedArray = 
                new java.lang.Object[size-i];
        System.arraycopy(array, i, trimmedArray, 0, size-i);
        return new Tuple($reifiedElement, trimmedArray, rest);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override @Ignore
    public Sequential<? extends Element> trimTrailing(
            Callable<? extends Boolean> f) {
        if (!rest.getEmpty()) {
            return $ceylon$language$Sequential$impl().trimTrailing(f);
        }
        int size = array.length;
        int i = 0;
        while (i<size) {
            if (!f.$call$(array[size-1-i]).booleanValue()) {
                break;
            }
            i++;
        }
        if (i==0) return this;
        java.lang.Object[] trimmedArray = 
                new java.lang.Object[size-i];
        System.arraycopy(array, 0, trimmedArray, 0, size-i);
        return new Tuple($reifiedElement, trimmedArray);
    }

    @Override @Ignore
    public List$impl<? extends Element> $ceylon$language$List$impl() {
        return new List$impl<Element>($reifiedElement, this);
    }

    @Override @Ignore
    public boolean endsWith(List<? extends java.lang.Object> list) {
        if (!rest.getEmpty()) {
            return $ceylon$language$List$impl().endsWith(list);
        }
        long size = list.getSize();
        if (size>array.length) return false;
        if (size<=0) return true;
        int offset = array.length - (int) size;
        if (offset<0) return false;
        for (int i=0; i<size; i++) {
            java.lang.Object x = array[i+offset];
            java.lang.Object y = list.getFromFirst(i);
            if (x!=y) {
                if (x==null || y==null || !x.equals(y)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override @Ignore
    public boolean startsWith(List<? extends java.lang.Object> list) {
        if (!rest.getEmpty()) {
            return $ceylon$language$List$impl().startsWith(list);
        }
        long size = list.getSize();
        if (size>array.length) return false;
        if (size<=0) return true;
        int offset = array.length - (int) size;
        if (offset<0) return false;
        for (int i=0; i<size; i++) {
            java.lang.Object x = array[i];
            java.lang.Object y = list.getFromFirst(i);
            if (x!=y) {
                if (x==null || y==null || !x.equals(y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Integer adjustIndex(Integer index) {
        return Integer.instance(index.longValue()+array.length);
    }

    @Override @Ignore
    public Integer firstIndexWhere(Callable<? extends Boolean> f) {
        for (int i=0; i<array.length; i++) {
            if (f.$call$(array[i]).booleanValue()) {
                return Integer.instance(i);
            }
        }
        Integer index = rest.firstIndexWhere(f);
        return index==null ? null : adjustIndex(index);
    }

    @Override @Ignore
    public Integer lastIndexWhere(Callable<? extends Boolean> f) {
        Integer index = rest.lastIndexWhere(f);
        if (index!=null) {
            return adjustIndex(index);
        }
        for (int i=array.length-1; i>=0; i--) {
            if (f.$call$(array[i]).booleanValue()) {
                return Integer.instance(i);
            }
        }
        return null;
    }
    
    @Override @Ignore
    public Element get(Integer index) {
        return getFromFirst(index.value);
    }
    
    /*@Override @Ignore
    public Entry<? extends Boolean,? extends Element> 
    lookup(Integer index) {
        boolean defined = defines(index);
        Element item = defined ? 
                getFromFirst(index.value) : null;
        return new Entry<Boolean,Element>(
                Boolean.$TypeDescriptor$,
                $getReifiedElement$(),
                Boolean.instance(defined), 
                item);
    }*/

    @Override @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> 
    indexesWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$impl().indexesWhere(f);
    }
    
    @Override @Ignore
    public <Other> List patch(TypeDescriptor $reifiedOther, List<? extends Other> list) {
        return $ceylon$language$List$impl().patch($reifiedOther, list);
    }

    @Override @Ignore
    public <Other> List patch(TypeDescriptor $reifiedOther, List<? extends Other> list,
            long index) {
        return $ceylon$language$List$impl().patch($reifiedOther, list, index);
    }

    @Override @Ignore
    public <Other> List patch(TypeDescriptor $reifiedOther, List<? extends Other> list,
            long index, long len) {
        return $ceylon$language$List$impl().patch($reifiedOther, list, index, len);
    }

    @Override @Ignore
    public <Other> long patch$from(TypeDescriptor $reifiedOther,
            List<? extends Other> list) {
        return list.getSize();
    }

    @Override @Ignore
    public <Other> long patch$length(TypeDescriptor arg0,
            List<? extends Other> list, long from) {
        return 0;
    }

    @Override @Ignore
    public Sequential<? extends Element> sublist(long from, long to) {
        return $ceylon$language$Sequence$impl().sublist(from, to);
    }

    @Override @Ignore
    public Sequential<? extends Element> sublistFrom(long from) {
        return $ceylon$language$Sequence$impl().sublistFrom(from);
    }

    @Override @Ignore
    public Sequential<? extends Element> sublistTo(long to) {
        return $ceylon$language$Sequence$impl().sublistTo(to);
    }

    @Override @Ignore
    public Collection$impl<? extends Element> $ceylon$language$Collection$impl() {
        return new Collection$impl<Element>
                ($reifiedElement,this);
    }

    @Override @Ignore
    public Iterable$impl<? extends Element, ? extends java.lang.Object> $ceylon$language$Iterable$impl() {
        return new Iterable$impl<Element,java.lang.Object>
                ($reifiedElement,NothingType,this);
    }

    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        for (int i=0; i<array.length; i++) {
            if (f.$call$(array[i]).booleanValue()) {
                return true;
            }
        }
        return rest.any(f);
    }

    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        for (int i=0; i<array.length; i++) {
            if (!f.$call$(array[i]).booleanValue()) {
                return false;
            }
        }
        return rest.every(f);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> by(long step) {
        return $ceylon$language$Iterable$impl().by(step);
    }

    @Override @Ignore
    public <Other, OtherAbsent> Iterable chain(TypeDescriptor $reifiedOther,
            TypeDescriptor $reifiedOtherAbsent,
            Iterable<? extends Other, ? extends OtherAbsent> it) {
        return $ceylon$language$Iterable$impl().chain($reifiedOther, $reifiedOtherAbsent, it);
    }

    @Override @Ignore
    public <Default> Iterable defaultNullElements(TypeDescriptor $reified$Default,
            Default defaultValue) {
        return $ceylon$language$Iterable$impl().defaultNullElements($reified$Default, defaultValue);
    }

    @Override @Ignore
    public java.lang.Object each(Callable<? extends java.lang.Object> f) {
        for (int i=0; i<array.length; i++) {
            f.$call$(array[i]);
        }
        rest.each(f);
        return null;
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> filter(
            Callable<? extends Boolean> selecting) {
        return $ceylon$language$Iterable$impl().filter(selecting);
    }

    @SuppressWarnings("rawtypes")
    @Override @Ignore
    public <Type> Iterable 
    narrow(@Ignore TypeDescriptor $reifiedType) {
        return  $ceylon$language$Iterable$impl().narrow($reifiedType);
    }

    @Override @Ignore
    public <Result, OtherAbsent> Iterable flatMap(
            TypeDescriptor $reified$Result,
            TypeDescriptor $reified$OtherAbsent,
            Callable<? extends Iterable<? extends Result, ? extends OtherAbsent>> collecting) {
        return $ceylon$language$Iterable$impl().flatMap($reified$Result, $reified$OtherAbsent, collecting);
    }

    @Override @Ignore
    public <Result> Result fold(TypeDescriptor $reified$Result,
            Result initial, Callable<? extends Result> fun) {
        return $ceylon$language$Iterable$impl().fold($reified$Result, initial, fun);
    }

    @Override @Ignore
    public <Other> Iterable follow(TypeDescriptor $reified$Other, Other head) {
        return $ceylon$language$Iterable$impl().follow($reified$Other, head);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> getCoalesced() {
        return $ceylon$language$Iterable$impl().getCoalesced();
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> getCycled() {
        return $ceylon$language$Iterable$impl().getCycled();
    }

    @Override @Ignore
    public Sequential<? extends Element> getExceptLast() {
        return $ceylon$language$Sequence$impl().getExceptLast();
    }

    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, ? extends java.lang.Object> getIndexed() {
        return $ceylon$language$Iterable$impl().getIndexed();
    }

    @Override @Ignore
    public Iterable<? extends Sequence<? extends Element>, ? extends java.lang.Object> getPaired() {
        return $ceylon$language$Iterable$impl().getPaired();
    }

    @Override @Ignore
    public <Other> Iterable interpose(TypeDescriptor $reifiedOther, Other o) {
        return $ceylon$language$Iterable$impl().interpose($reifiedOther, o);
    }

    @Override @Ignore
    public <Other> Iterable interpose(TypeDescriptor $reifiedOther, Other o, long step) {
        return $ceylon$language$Iterable$impl().interpose($reifiedOther, o, step);
    }

    @Override @Ignore
    public <Other> long interpose$step(TypeDescriptor arg0, Other arg1) {
        return 1;
    }

    @Override @Ignore
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> map(
            TypeDescriptor $reified$Result, Callable<? extends Result> collecting) {
        return $ceylon$language$Iterable$impl().map($reified$Result, collecting);
    }

    @Override @Ignore
    public java.lang.Object max(Callable<? extends Comparison> f) {
        java.lang.Object result = array[0];
        for (int i=1; i<array.length; i++) {
            java.lang.Object object = array[i];
            if (f.$call$(object, result)==larger_.get_()) {
                result = object;
            }
        }
        if (!rest.getEmpty()) {
            java.lang.Object object = rest.max(f);
            if (f.$call$(object, result)==larger_.get_()) {
                result = object;
            }
        }
        return result;
    }

    @Override @Ignore
    public Iterable<? extends Sequence<? extends Element>, ? extends java.lang.Object> partition(
            long length) {
        return $ceylon$language$Iterable$impl()
        .partition(length);
    }

    @Override @Ignore
    public <Other, OtherAbsent> Iterable product(TypeDescriptor $reified$Other,
            TypeDescriptor $reified$OtherAbsent,
            Iterable<? extends Other, ? extends OtherAbsent> other) {
        return $ceylon$language$Iterable$impl().product($reified$Other, $reified$OtherAbsent, other);
    }

    @Override @Ignore
    public <Result> java.lang.Object reduce(TypeDescriptor $reifiedResult,
            Callable<? extends Result> f) {
        return $ceylon$language$Iterable$impl().reduce($reifiedResult, f);
    }

    @Override @Ignore
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> scan(
            TypeDescriptor $reified$Result, Result initial, Callable<? extends Result> fun) {
        return  $ceylon$language$Iterable$impl().scan($reified$Result, initial, fun);
    }

    @Override @Ignore
    public Sequential<? extends Element> select(Callable<? extends Boolean> selecting) {
        return $ceylon$language$Iterable$impl().select(selecting);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> skip(long skipping) {
        return $ceylon$language$Iterable$impl().skip(skipping);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> skipWhile(
            Callable<? extends Boolean> skipping) {
        return $ceylon$language$Iterable$impl().skipWhile(skipping);
    }

    @Override @Ignore
    public <Result, Args extends Sequential<? extends java.lang.Object>> 
    Callable<? extends Iterable<? extends Result, ? extends java.lang.Object>> 
    spread(TypeDescriptor $reified$Result, TypeDescriptor $reified$Args,
            Callable<? extends Callable<? extends Result>> method) {
        return $ceylon$language$Iterable$impl().spread($reified$Result, $reified$Args, method);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> take(long taking) {
        return $ceylon$language$Iterable$impl().take(taking);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> takeWhile(
            Callable<? extends Boolean> taking) {
        return $ceylon$language$Iterable$impl().takeWhile(taking);
    }

    @Override @Ignore
    public Category$impl<? super java.lang.Object> $ceylon$language$Category$impl() {
        return new Category$impl<java.lang.Object>
                ($reifiedElement,this);
    }

    @Override @Ignore
    public boolean containsAny(Iterable<? extends java.lang.Object, ? extends java.lang.Object> it) {
        return $ceylon$language$Category$impl().containsAny(it);
    }

    @Override @Ignore
    public boolean containsEvery(
            Iterable<? extends java.lang.Object, ? extends java.lang.Object> it) {
        return $ceylon$language$Category$impl().containsEvery(it);
    }

    @Override @Ignore
    public Correspondence$impl<? super Integer, ? extends Element> $ceylon$language$Correspondence$impl() {
        return new Correspondence$impl<Integer, Element>
                (Integer.$TypeDescriptor$,$reifiedElement,this);
    }

    @Override @Ignore
    public boolean definesAny(Iterable<? extends Integer, ? extends java.lang.Object> it) {
        return $ceylon$language$Correspondence$impl().definesAny(it);
    }

    @Override @Ignore
    public boolean definesEvery(
            Iterable<? extends Integer, ? extends java.lang.Object> it) {
        return $ceylon$language$Correspondence$impl().definesEvery(it);
    }

    @Override @Ignore
    public <Absent> Iterable<? extends Element, ? extends Absent> getAll(
            TypeDescriptor $reified$Absent,
            Iterable<? extends Integer, ? extends Absent> keys) {
        return $ceylon$language$Correspondence$impl().getAll($reified$Absent, keys);
    }

    @Override @Ignore
    public Sequence$impl<? extends Element> $ceylon$language$Sequence$impl() {
        return new Sequence$impl<Element>
                ($reifiedElement,this);
    }

    @Override @Ignore
    public <Result> Sequence<? extends Result> collect(TypeDescriptor $reified$Result,
            Callable<? extends Result> collecting) {
        return $ceylon$language$Sequence$impl().collect($reified$Result, collecting);
    }

    @Override @Ignore
    public Element find(Callable<? extends Boolean> f) {
        for (int i=0; i<array.length; i++) {
            java.lang.Object object = array[i];
            if (f.$call$(object).booleanValue()) {
                return (Element) object;
            }
        }
        return rest.find(f);
    }

    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        Element element = rest.findLast(f);
        if (element!=null) return element;
        for (int i=array.length-1; i>=0; i--) {
            java.lang.Object object = array[i];
            if (f.$call$(object).booleanValue()) {
                return (Element) object;
            }
        }
        return null;
    }

    private Entry<Integer, Element> adjustEntry
            (Entry<? extends Integer, ?> entry) {
        return new Entry<Integer,Element>(
                Integer.$TypeDescriptor$,
                $reifiedElement,
                Integer.instance(entry.getKey().longValue()+array.length), 
                (Element) entry.getItem());
    }

    @Override @Ignore
    public Entry<? extends Integer,? extends Element> 
    locate(Callable<? extends Boolean> f) {
        for (int i=0; i<array.length; i++) {
            java.lang.Object object = array[i];
            if (f.$call$(object).booleanValue()) {
                return new Entry<Integer,Element>
                        (Integer.$TypeDescriptor$, $reifiedElement, 
                        Integer.instance(i), 
                        (Element) object);
            }
        }
        Entry<? extends Integer, ?> entry = rest.locate(f);
        return entry==null ? null : adjustEntry(entry);
    }

    @Override @Ignore
    public Entry<? extends Integer,? extends Element> 
    locateLast(Callable<? extends Boolean> f) {
        Entry<? extends Integer,? extends Element> entry = 
                rest.locateLast(f);
        if (entry!=null) {
            return adjustEntry(entry);
        }
        for (int i=array.length-1; i>=0; i--) {
            java.lang.Object object = array[i];
            if (f.$call$(object).booleanValue()) {
                return new Entry<Integer,Element>(
                        Integer.$TypeDescriptor$, $reifiedElement, 
                        Integer.instance(i), 
                        (Element) object);
            }
        }
        return null;
    }

    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, ? extends java.lang.Object> 
    locations(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$impl().locations(f);
    }

    @Override @Ignore
    public boolean getEmpty() {
        return false;
    }

    @Override @Ignore
    public Range getKeys() {
        return $ceylon$language$Sequence$impl().getKeys();
    }

    @Override @Ignore
    public Range indexes() {
        return $ceylon$language$Sequence$impl().indexes();
    }

    @Override @Ignore
    public Sequence<? extends Element> getReversed() {
        return $ceylon$language$Sequence$impl().getReversed();
    }

    @Override @Ignore
    public <Other> Sequence prepend(TypeDescriptor $reifiedOther,
            Sequential<? extends Other> elements) {
        return $ceylon$language$Sequence$impl().prepend($reifiedOther,elements);
    }

    @Override @Ignore
    public Sequential<? extends Element> repeat(long times) {
        //TODO: optimize this one, returning a Tuple!
        return $ceylon$language$Sequence$impl().repeat(times);
    }

    @Override @Ignore
    public Sequence slice(long index) {
        //TODO: optimize this one, returning a Tuple!
        return $ceylon$language$Sequence$impl().slice(index);
    }

    @Override @Ignore
    public Sequence<? extends Element> sort(Callable<? extends Comparison> comparing) {
        return $ceylon$language$Sequence$impl().sort(comparing);
    }
    
    @Override @Ignore
    public Iterable<? extends Sequence<? extends Element>, ? extends java.lang.Object>
    getPermutations() {
        return $ceylon$language$Collection$impl().getPermutations();
    }
    
    @Override @Ignore
    public Iterable<? extends Sequence<? extends Element>, ? extends java.lang.Object>
    combinations(long length) {
        return $ceylon$language$Collection$impl().combinations(length);
    }
    
    @Override @Ignore
    public <Group> Map<? extends Group, ? extends Sequence<? extends Element>>
    group(TypeDescriptor $reifiedGroup, Callable<? extends Group> fun) {
        return $ceylon$language$Iterable$impl().group($reifiedGroup, fun);
    }
    
    @Override @Ignore
    public <Group,Result> Map<? extends Group, ? extends Result>
    summarize(TypeDescriptor $reifiedGroup, TypeDescriptor $reifiedResult, 
            Callable<? extends Group> fun, Callable<? extends Result> fold) {
        return $ceylon$language$Iterable$impl().summarize($reifiedGroup, $reifiedResult, fun, fold);
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object>
    getDistinct() {
        return $ceylon$language$Iterable$impl().getDistinct();
    }
    
    @Override @Ignore
    public <Item> Map<? extends Element, ? extends Item>
    tabulate(TypeDescriptor arg0, Callable<? extends Item> arg1) {
        return $ceylon$language$Iterable$impl().tabulate(arg0, arg1);
    }

    @Override @Ignore
    public Map<? extends Element, ? extends Integer>
    frequencies() {
        return $ceylon$language$Iterable$impl().frequencies();
    }

    /*@Override @Ignore
    public Set<? extends Element> elements() {
        return $ceylon$language$Iterable$impl().elements();
    }*/
    
    @Override @Ignore
    public java.lang.String toString() {
        StringBuilder sb = new StringBuilder();
        sb.appendCharacter('[');
        boolean first = true;
        for (int i=0; i<array.length; i++) {
            if (first) {
                first = false;
            }
            else {
                sb.append(", ");
            }
            java.lang.Object object = array[i];
            sb.append(object==null ? "<null>" : object.toString());
        }
        if (!rest.getEmpty()) {
            sb.append(", *").append(rest.toString());
        }
        sb.appendCharacter(']');
        return sb.toString();
    }
    
    @Override @Ignore
    public boolean equals(java.lang.Object obj) {
        return $ceylon$language$List$impl().equals(obj);
    }
    
    @Override @Ignore
    public int hashCode() {
        return $ceylon$language$List$impl().hashCode();
    }
    
    @Override @Ignore
    public <Result> List<? extends Result> mapElements(
            TypeDescriptor $reified$Result, Callable<? extends Result> collecting) {
        return $ceylon$language$List$impl().mapElements($reified$Result, collecting);
    }

}
