package ceylon.language;

import static com.redhat.ceylon.compiler.java.Util.toInt;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import ceylon.language.impl.BaseIterator;
import ceylon.language.impl.BaseList;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.AbstractArrayIterable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.FunctionalParameter;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class(extendsType="ceylon.language::Object", basic = false, identifiable = false)
@TypeParameters(@TypeParameter("Element"))
@SatisfiedTypes({
    "ceylon.language::List<Element>",
    "ceylon.language::Ranged<ceylon.language::Integer,Element,ceylon.language::Array<Element>>"
})
public final class Array<Element>
        extends BaseList<Element>
        implements List<Element> {
    
    private final java.lang.Object array;
    private final int size;
    private final TypeDescriptor $reifiedElement;
    private final int elementType;

    
    @Ignore
    public Array(final TypeDescriptor $reifiedElement, 
            int size, Element element) {
        this($reifiedElement, 
                createArrayWithElement($reifiedElement, size, element));
    }
    
    public Array(@Ignore final TypeDescriptor $reifiedElement, 
            @Name("elements")
            @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
            final ceylon.language.Iterable<? extends Element,?> elements) {
        this($reifiedElement, 
                createArrayFromIterable($reifiedElement, elements));
    }

    @SuppressWarnings("unchecked")
    private static <Element> java.lang.Object createArrayFromIterable(
    		final TypeDescriptor $reifiedElement,
    		final Iterable<? extends Element,?> elements) {
    	
    	if (elements instanceof List) {
    		return createArrayFromList($reifiedElement, (List<? extends Element>) elements);
    	}
    	
    	final ArrayList<Element> list = new ArrayList<Element>();
    	Iterator<?> iterator = elements.iterator();
    	java.lang.Object elem;
    	while ((elem=iterator.next())!=finished_.get_()) {
    		list.add((Element) elem);
    	}
    	
    	final int size = list.size();

    	switch (elementType($reifiedElement)) {
    	case 13:
    	    //note: we don't unbox strings in an Array<String?>
    	    //      because it would break javaObjectArray()
    	    java.lang.String[] stringArray = new java.lang.String[size];
    	    for (int i=0; i<size; i++) {
    	        String string = (String) list.get(i);
    	        stringArray[i] = string==null ? null : string.value;
    	    }
    	    return stringArray;
    	case 0:
    	    long[] longPrecisionArray = new long[size];
    	    for (int i=0; i<size; i++) {
    	        longPrecisionArray[i] = ((Integer) list.get(i)).value;
    	    }
    	    return longPrecisionArray;
    	case 2:
    	    double[] doublePrecisionArray = new double[size];
    	    for (int i=0; i<size; i++) {
    	        doublePrecisionArray[i] = ((Float) list.get(i)).value;
    	    }
    	    return doublePrecisionArray;
    	case 4:
    	    int[] codepointArray = new int[size];
    	    for (int i=0; i<size; i++) {
    	        codepointArray[i] = ((Character) list.get(i)).codePoint;
    	    }
    	    return codepointArray;
    	case 8:
    	    boolean[] boolArray = new boolean[size];
    	    for (int i=0; i<size; i++) {
    	        boolArray[i] = ((Boolean) list.get(i)).booleanValue();
    	    }
    	    return boolArray;
    	case 6:
    	    byte[] bitsArray = new byte[size];
    	    for (int i=0; i<size; i++) {
    	        bitsArray[i] = ((Byte) list.get(i)).value;
    	    }
    	    return bitsArray;
    	case 9:
    	    boolean[] booleanArray = new boolean[size];
    	    for (int i=0; i<size; i++) {
    	        booleanArray[i] = (java.lang.Boolean) list.get(i);
    	    }
    	    return booleanArray;
    	case 10:
    	    char[] charArray = new char[size];
    	    for (int i=0; i<size; i++) {
    	        charArray[i] = (java.lang.Character) list.get(i);
    	    }
    	    return charArray;
    	case 12:
    	    float[] floatArray = new float[size];
    	    for (int i=0; i<size; i++) {
    	        floatArray[i] = (java.lang.Float) list.get(i);
    	    }
    	    return floatArray;
    	case 3:
    	    double[] doubleArray = new double[size];
    	    for (int i=0; i<size; i++) {
    	        doubleArray[i] = (java.lang.Double) list.get(i);
    	    }
    	    return doubleArray;
    	case 7:
    	    byte[] byteArray = new byte[size];
    	    for (int i=0; i<size; i++) {
    	        byteArray[i] = (java.lang.Byte) list.get(i);
    	    }
    	    return byteArray;
    	case 11:
    	    short[] shortArray = new short[size];
    	    for (int i=0; i<size; i++) {
    	        shortArray[i] = (java.lang.Short) list.get(i);
    	    }
    	    return shortArray;
    	case 5:
    	    int[] intArray = new int[size];
    	    for (int i=0; i<size; i++) {
    	        intArray[i] = (java.lang.Integer) list.get(i);
    	    }
    	    return intArray;
    	case 1:
    	    long[] longArray = new long[size];
    	    for (int i=0; i<size; i++) {
    	        longArray[i] =(java.lang.Long) list.get(i);
    	    }
    	    return longArray;
    	default:
    	    java.lang.Class<?> clazz = 
    	            $reifiedElement.getArrayElementClass();
    	    java.lang.Object[] array = (java.lang.Object[]) 
    	            java.lang.reflect.Array.newInstance(clazz, size);
    	    for (int i=0; i<size; i++) {
    	        array[i] = list.get(i);
    	    }
    	    return array;
    	}

    }
    
    private static <Element> java.lang.Object createArrayFromList(
            final TypeDescriptor $reifiedElement,
            final List<? extends Element> elements) {
        
    	if (elements instanceof Array) {
    		return createArrayFromArray($reifiedElement, 
    		        (Array<? extends Element>) elements);
    	}
    	
        int size = Util.toInt(elements.getSize());
        
        if (elements instanceof ceylon.language.String) {
            int[] array = new int[size];
            java.lang.String string = elements.toString();
            for (int i=0, offset = 0; i<size; i++) {
                int codePoint = string.codePointAt(offset);
                offset += java.lang.Character.charCount(codePoint);
                array[i] = codePoint;
            }
            return array;
        }

        switch (elementType($reifiedElement)) {
        case 13:
            //note: we don't unbox strings in an Array<String?>
            //      because it would break javaObjectArray()
            java.lang.String[] stringArray = new java.lang.String[size];
            for (int i=0; i<size; i++) {
                String string = (String) elements.getFromFirst(i);
                stringArray[i] = string==null ? null : string.value;
            }
            return stringArray;
        case 0:
            long[] longPrecisionArray = new long[size];
            for (int i=0; i<size; i++) {
                Integer e = (Integer) elements.getFromFirst(i);
                longPrecisionArray[i] = e.value;
            }
            return longPrecisionArray;
        case 2:
            double[] doublePrecisionArray = new double[size];
            for (int i=0; i<size; i++) {
                Float e = (Float) elements.getFromFirst(i);
                doublePrecisionArray[i] = e.value;
            }
            return doublePrecisionArray;
        case 4:
            int[] codepointArray = new int[size];
            for (int i=0; i<size; i++) {
                Character e = (Character) elements.getFromFirst(i);
                codepointArray[i] = e.codePoint;
            }
            return codepointArray;
        case 8:
            boolean[] boolArray = new boolean[size];
            for (int i=0; i<size; i++) {
                Boolean e = (Boolean) elements.getFromFirst(i);
                boolArray[i] = e.booleanValue();
            }
            return boolArray;
        case 6:
            byte[] bitsArray = new byte[size];
            for (int i=0; i<size; i++) {
                Byte e = (Byte) elements.getFromFirst(i);
                bitsArray[i] = e.value;
            }
            return bitsArray;
        case 9:
            boolean[] booleanArray = new boolean[size];
            for (int i=0; i<size; i++) {
                booleanArray[i] = (java.lang.Boolean)
                        elements.getFromFirst(i);
            }
            return booleanArray;
        case 10:
            char[] charArray = new char[size];
            for (int i=0; i<size; i++) {
                charArray[i] = (java.lang.Character)
                        elements.getFromFirst(i);
            }
            return charArray;
        case 12:
            float[] floatArray = new float[size];
            for (int i=0; i<size; i++) {
                floatArray[i] = (java.lang.Float)
                        elements.getFromFirst(i);
            }
            return floatArray;
        case 3:
            double[] doubleArray = new double[size];
            for (int i=0; i<size; i++) {
                doubleArray[i] = (java.lang.Double)
                        elements.getFromFirst(i);
            }
            return doubleArray;
        case 7:
            byte[] byteArray = new byte[size];
            for (int i=0; i<size; i++) {
                byteArray[i] = (java.lang.Byte)
                        elements.getFromFirst(i);
            }
            return byteArray;
        case 11:
            short[] shortArray = new short[size];
            for (int i=0; i<size; i++) {
                shortArray[i] = (java.lang.Short)
                        elements.getFromFirst(i);
            }
            return shortArray;
        case 5:
            int[] intArray = new int[size];
            for (int i=0; i<size; i++) {
                intArray[i] = (java.lang.Integer)
                        elements.getFromFirst(i);
            }
            return intArray;
        case 1:
            long[] longArray = new long[size];
            for (int i=0; i<size; i++) {
                longArray[i] = (java.lang.Long)
                        elements.getFromFirst(i);
            }
            return longArray;
        default:
            java.lang.Class<?> clazz = 
                    $reifiedElement.getArrayElementClass();
            java.lang.Object[] objectArray = (java.lang.Object[]) 
                    java.lang.reflect.Array.newInstance(clazz, size);
            for (int i=0; i<size; i++) {
                objectArray[i] = elements.getFromFirst(i);
            }
            return objectArray;
        }
    }

    private static <Element> java.lang.Object createArrayFromArray(
            final TypeDescriptor $reifiedElement,
            final Array<? extends Element> elements) {

        final int size = Util.toInt(elements.getSize());
        final java.lang.Class<?> clazz = 
                $reifiedElement.getArrayElementClass();

        switch (elementType($reifiedElement)) {
        case 13:  
            //note: we don't unbox strings in an Array<String?>
            //      because it would break javaObjectArray()
            java.lang.String[] stringArray = new java.lang.String[size];
            arraycopy(elements.array, 0, stringArray, 0, size);
            return stringArray;
        case 0:
            long[] longPrecisionArray = new long[size];
            arraycopy(elements.array, 0, longPrecisionArray, 0, size);
        case 2:
            double[] doublePrecisionArray = new double[size];
            arraycopy(elements.array, 0, doublePrecisionArray, 0, size);
            return doublePrecisionArray;
        case 4:
            int[] codepointArray = new int[size];
            arraycopy(elements.array, 0, codepointArray, 0, size);
            return codepointArray;
        case 8:
            boolean[] boolArray = new boolean[size];
            arraycopy(elements.array, 0, boolArray, 0, size);
            return boolArray;
        case 6:
            byte[] bitsArray = new byte[size];
            arraycopy(elements.array, 0, bitsArray, 0, size);
            return bitsArray;
        case 9:
            boolean[] booleanArray = new boolean[size];
            arraycopy(elements.array, 0, booleanArray, 0, size);
            return booleanArray;
        case 10:
            char[] charArray = new char[size];
            arraycopy(elements.array, 0, charArray, 0, size);
            return charArray;
        case 12:
            float[] floatArray = new float[size];
            arraycopy(elements.array, 0, floatArray, 0, size);
            return floatArray;
        case 3:
            double[] doubleArray = new double[size];
            arraycopy(elements.array, 0, doubleArray, 0, size);
            return doubleArray;
        case 7:
            byte[] byteArray = new byte[size];
            arraycopy(elements.array, 0, byteArray, 0, size);
            return byteArray;
        case 11:
            short[] shortArray = new short[size];
            arraycopy(elements.array, 0, shortArray, 0, size);
            return shortArray;
        case 5:
            int[] intArray = new int[size];
            arraycopy(elements.array, 0, intArray, 0, size);
            return intArray;
        case 1:
            long[] longArray = new long[size];
            arraycopy(elements.array, 0, longArray, 0, size);
            return longArray;
        default:
            java.lang.Object[] objectArray = (java.lang.Object[]) 
            java.lang.reflect.Array.newInstance(clazz, size);
            java.lang.Object otherArray = elements.array;
            if (otherArray.getClass()==objectArray.getClass()) {
                arraycopy(otherArray, 0, objectArray, 0, size);
            }
            else {
                for (int i=0; i<size; i++) {
                    objectArray[i] = elements.getFromFirst(i);
                }
            }
            return objectArray;
        }
    }
    
    private static <Element> java.lang.Object createArrayWithElement(
        final TypeDescriptor $reifiedElement,
        final int size, final Element element) {
        switch (elementType($reifiedElement)) {
        case 0:
            long[] longPrecisionArray = new long[size];
            if (element!=null) {
                long value = ((Integer) element).value;
                if (value!=0l) Arrays.fill(longPrecisionArray, value);
            }
            return longPrecisionArray;
        case 1:
            long[] longArray = new long[size];
            if (element!=null) {
                long longValue = (java.lang.Long) element;
                if (longValue!=0l) Arrays.fill(longArray, longValue);
            }
            return longArray;
        case 2:
            double[] doublePrecisionArray = new double[size];
            if (element!=null) {
                double value = ((Float) element).value;
                if (value!=0.0d) Arrays.fill(doublePrecisionArray, value);
            }
            return doublePrecisionArray;
        case 3:
            double[] doubleArray = new double[size];
            if (element!=null) {
                double value = (java.lang.Double) element;
                if (value!=0.0d) Arrays.fill(doubleArray, value);
            }
            return doubleArray;
        case 4:
            int[] codepointArray = new int[size];
            if (element!=null) {
                int value = ((Character) element).codePoint;
                if (value!=0) Arrays.fill(codepointArray, value);
            }
            return codepointArray;
        case 5:
            int[] intArray = new int[size];
            if (element!=null) {
                int intValue = (java.lang.Integer) element;
                if (intValue!=0) Arrays.fill(intArray, intValue);
            }
            return intArray;
        case 6:
            byte[] byteArray = new byte[size];
            if (element!=null) {
                byte value = ((Byte) element).value;
                if (value!=0.0d) Arrays.fill(byteArray, value);
            }
            return byteArray;
        case 7:
            byte[] bitsArray = new byte[size];
            byte value = (java.lang.Byte) element;
            if (value!=0) Arrays.fill(bitsArray, value);
            return bitsArray;
        case 8:
            boolean[] boolArray = new boolean[size];
            if (element!=null) {
                boolean boolValue = ((Boolean) element).booleanValue();
                if (boolValue!=false) Arrays.fill(boolArray, boolValue);
            }
            return boolArray;
        case 9:
            boolean[] booleanArray = new boolean[size];
            if (element!=null) {
                boolean booleanValue = (java.lang.Boolean) element;
                if (booleanValue!=false) Arrays.fill(booleanArray, booleanValue);
            }
            return booleanArray;
        case 10:
            char[] charArray = new char[size];
            if (element!=null) {
                char charValue = (java.lang.Character) element;
                if (charValue!=0) Arrays.fill(charArray, charValue);
            }
            return charArray;
        case 11:
            short[] shortArray = new short[size];
            if (element!=null) {
                short shortValue = (java.lang.Short) element;
                if (shortValue!=0) Arrays.fill(shortArray, shortValue);
            }
            return shortArray;
        case 12:
            float[] floatArray = new float[size];
            if (element!=null) {
                float floatValue = (java.lang.Float) element;
                if (floatValue!=0.0f) Arrays.fill(floatArray, floatValue);
            }
            return floatArray;
        case 13:
            //note: we don't unbox strings in an Array<String?>
            //      because it would break javaObjectArray()
            java.lang.String[] stringArray = new java.lang.String[size];
            if (element!=null) {
                String s = (String) element;
                Arrays.fill(stringArray, s.value);
            }
            return stringArray;
        default:
            java.lang.Class<?> elementClass = 
                    $reifiedElement.getArrayElementClass();
            java.lang.Object[] objectArray = (java.lang.Object[]) 
                    java.lang.reflect.Array.newInstance(elementClass, size);
            if (element!=null) {
                Arrays.fill(objectArray, element);
            }
            return objectArray;
        }
    }
        
    @Ignore
    private Array(@Ignore TypeDescriptor $reifiedElement, java.lang.Object array) {
        super($reifiedElement);
        this.$reifiedElement = $reifiedElement;
        assert(array.getClass().isArray());
        this.array = array;
        this.elementType = elementType($reifiedElement);
        this.size = java.lang.reflect.Array.getLength(array);
    }
    
    @Ignore
    public static <T> Array<T> instance(T[] array) {
        if (array == null) {
            return null;
        }
        java.lang.Class<?> componentType = array.getClass().getComponentType();
        return new Array<T>(TypeDescriptor.klass(componentType), array);
    }
    
    private static final TypeDescriptor CHAR_TYPE = 
            TypeDescriptor.klass(java.lang.Character.class);
    @Ignore
    public static Array<java.lang.Character> instance(char[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Character>(CHAR_TYPE, array);
    }

    private static final TypeDescriptor BYTE_TYPE = 
            TypeDescriptor.klass(java.lang.Byte.class);
    @Ignore
    public static Array<java.lang.Byte> instance(byte[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Byte>(BYTE_TYPE, array);
    }

    private static final TypeDescriptor SHORT_TYPE = 
            TypeDescriptor.klass(java.lang.Short.class);
    @Ignore
    public static Array<java.lang.Short> instance(short[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Short>(SHORT_TYPE, array);
    }
    
    private static final TypeDescriptor INT_TYPE = 
            TypeDescriptor.klass(java.lang.Integer.class);
    @Ignore
    public static Array<java.lang.Integer> instance(int[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Integer>(INT_TYPE, array);
    }
    
    private static final TypeDescriptor LONG_TYPE = 
            TypeDescriptor.klass(java.lang.Long.class);
    @Ignore
    public static Array<java.lang.Long> instance(long[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Long>(LONG_TYPE, array);
    }
    
    private static final TypeDescriptor FLOAT_TYPE = 
            TypeDescriptor.klass(java.lang.Float.class);
    @Ignore
    public static Array<java.lang.Float> instance(float[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Float>(FLOAT_TYPE, array);
    }
    
    private static final TypeDescriptor DOUBLE_TYPE = 
            TypeDescriptor.klass(java.lang.Double.class);
    @Ignore
    public static Array<java.lang.Double> instance(double[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Double>(DOUBLE_TYPE, array);
    }
    
    private static final TypeDescriptor BOOLEAN_TYPE = 
            TypeDescriptor.klass(java.lang.Boolean.class);
    @Ignore
    public static Array<java.lang.Boolean> instance(boolean[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Boolean>(BOOLEAN_TYPE, array);
    }
    
    @Ignore
    public static Array<Float> instanceForFloats(double[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Float>(Float.$TypeDescriptor$, array);
    }

    @Ignore
    public static Array<Boolean> instanceForBooleans(boolean[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Boolean>(Boolean.$TypeDescriptor$, array);
    }
    
    @Ignore
    public static Array<Byte> instanceForBytes(byte[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Byte>(Byte.$TypeDescriptor$, array);
    }
    
    @Ignore
    public static Array<Character> instanceForCodePoints(int[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Character>(Character.$TypeDescriptor$, array);
    }

    @Ignore
    public static Array<Integer> instanceForIntegers(long[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(Integer.$TypeDescriptor$, array);
    }

    @Override
    public Array<Element> spanFrom(@Name("from") Integer from) {
        return span(from, Integer.instance(size));
    }
    
    @Override
    public Array<Element> spanTo(@Name("to") Integer to) {
        return span(Integer.instance(0), to);
    }
    
    private static final java.lang.Object[] EMPTY_ARRAY = new java.lang.Object[0];
    
    @Override
    public Array<Element> span(@Name("from") Integer from,
            @Name("to") Integer to) {
        long fromIndex = from.longValue(); //inclusive
        long toIndex = to.longValue(); //inclusive
        boolean revert = toIndex < fromIndex;
        if (revert) {
            long swap = toIndex;
            toIndex = fromIndex;
            fromIndex = swap;
        }
        if (fromIndex<0) {
            fromIndex = 0;
        }
        if (toIndex>=size) {
            toIndex = size-1;
        }
        if (fromIndex>=size || toIndex<0 || toIndex<fromIndex) {
            return new Array<Element>($reifiedElement, EMPTY_ARRAY);
        }
        else {
            int resultFromIndex = toInt(fromIndex); //inclusive
            int resultToIndex = toInt(toIndex+1); //exclusive
            java.lang.Object newArray;
            if (array instanceof char[]) {
                char[] copy = copyOfRange((char[])array, 
                        resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        char temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof byte[]) {
                byte[] copy = copyOfRange((byte[])array, 
                        resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        byte temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof short[]) {
                short[] copy = copyOfRange((short[])array, 
                        resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        short temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof int[]) {
                int[] copy = copyOfRange((int[])array, 
                        resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        int temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof long[]) {
                long[] copy = copyOfRange((long[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        long temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof float[]) {
                float[] copy = copyOfRange((float[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        float temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof double[]) {
                double[] copy = copyOfRange((double[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        double temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof boolean[]) {
                boolean[] copy = copyOfRange((boolean[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        boolean temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else {
                java.lang.Object[] copy = 
                        copyOfRange((java.lang.Object[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        java.lang.Object temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            return new Array<Element>($reifiedElement, newArray);
        }
    }

    @Override
    public Array<Element> measure(@Name("from") Integer from,
            @Name("length") long length) {
        long fromIndex = from.longValue(); //inclusive
        long toIndex = fromIndex + length; //exclusive
        if (fromIndex<0) {
            fromIndex=0;
        }
        if (toIndex > size) {
            toIndex = size;
        }
        if (fromIndex>=size || toIndex<=0) {
            return new Array<Element>($reifiedElement, EMPTY_ARRAY);
        }
        else {
            int resultToIndex = toInt(toIndex);
            int resultFromIndex = toInt(fromIndex);
            java.lang.Object newArray;
            if (array instanceof char[]) {
                newArray = copyOfRange((char[])array, 
                    resultFromIndex, resultToIndex);
            }
            else if (array instanceof byte[]) {
                newArray = copyOfRange((byte[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof short[]) {
                newArray = copyOfRange((short[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof int[]) {
                newArray = copyOfRange((int[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof long[]) {
                newArray = copyOfRange((long[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof float[]) {
                newArray = copyOfRange((float[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof double[]) {
                newArray = copyOfRange((double[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof boolean[]) {
                newArray = copyOfRange((boolean[])array, 
                        resultFromIndex, resultToIndex);
            }
            else {
                newArray = copyOfRange((java.lang.Object[])array, 
                        resultFromIndex, resultToIndex);
            }
            return new Array<Element>($reifiedElement, newArray);
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer getLastIndex() {
        return getEmpty() ? null : Integer.instance(size - 1);
    }

    @Override
    public boolean getEmpty() {
        return size == 0;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean defines(@Name("index") Integer key) {
        long ind = key.longValue();
        return ind >= 0 && ind < size;
    }
    
    @Ignore
    private final class ArrayIterator extends BaseIterator<Element> {
        
        private int index = 0;
        // ok to cast here, since we know the size must fit in an int
        
        private ArrayIterator(TypeDescriptor $reified$Element) {
            super($reified$Element);
        }

        @Override
        public java.lang.Object next() {
            if (index<size) {
                return unsafeItem(index++);
            }
            else {
                return finished_.get_();
            }
        }

        @Override
        public java.lang.String toString() {
            return Array.this.toString() + ".iterator()";
        }
    }
    
    @Ignore
    final class ArrayIterable 
    extends AbstractArrayIterable<Element, java.lang.Object> {

        ArrayIterable() {
            // ok to cast here, since we know the size must fit in an int
            super($reifiedElement, array, (int)size);
        }
        
        protected ArrayIterable(java.lang.Object array, int start,
                int len, int step) {
            super($reifiedElement, array, start, len, step);
        }

        @Override
        protected ArrayIterable newInstance(java.lang.Object array, 
                int start, int len, int step) {
            return new ArrayIterable(array, start, len, step);
        }

        @Override
        protected Element get(java.lang.Object array, int index) {
            return unsafeItem(index);
        }
        
        /** 
         * If this is an Iterable over an {@code Array<Character>} 
         * (wrapping a {@code int[]}) with unit step size, then 
         * returns a String of those codepoints. 
         * Otherwise returns null.
         */
        @Ignore
        java.lang.String stringValue() {
            if (array instanceof int[]
                    && step == 1) {
                // ok to cast here, since we know the size must fit in an int
                return new java.lang.String((int[])array, start, 
                        (int)this.getSize());
            } 
            return null;
        }
    }
    
    @Override
    public Iterator<Element> iterator() {
        return new ArrayIterator($reifiedElement);
    }
    
//    @Override
//    @TypeInfo("ceylon.language::Null|Element")
//    public Element get(@Name("index") Integer key) {
//        return getFromFirst(key.longValue());
//    }

    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public Element getFromLast(@Name("index") long index) {
        if (index < 0 || index >= size) {
            return null;
        }
        else {
            //typecast is safe because we just checked
            return unsafeItem(size-1-(int) index);
        }
    }
    
    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public Element getFromFirst(@Name("index") long index) {
        if (index < 0 || index >= size) {
            return null;
        }
        else {
            //typecast is safe because we just checked
            return unsafeItem((int) index);
        }
    }
    
    // Used by the jvm backend code to avoid boxing the index
    @SuppressWarnings("unchecked")
    @Ignore
    public Element unsafeItem(int index) {
        switch (elementType) {
        case 0:
            return (Element) Integer.instance(((long[])array)[index]);
        case 1:
            return (Element) (java.lang.Long) ((long[])array)[index];
        case 2:
            return (Element) Float.instance(((double[])array)[index]);
        case 3:
            return (Element) (java.lang.Double) ((double[])array)[index];
        case 4:
            return (Element) Character.instance(((int[])array)[index]);
        case 5:
            return (Element) (java.lang.Integer) ((int[])array)[index];
        case 6:
            return (Element) Byte.instance(((byte[])array)[index]);
        case 7:
            return (Element) (java.lang.Byte) ((byte[])array)[index];
        case 8:
            return (Element) Boolean.instance(((boolean[])array)[index]);
        case 9:
            return (Element) (java.lang.Boolean) ((boolean[])array)[index];
        case 10:
            return (Element) (java.lang.Character) ((char[])array)[index];
        case 11:
            return (Element) (java.lang.Short) ((short[])array)[index];
        case 12:
            return (Element) (java.lang.Float) ((float[])array)[index];
        case 13:
            return (Element) String.instance(((java.lang.String[])array)[index]);
        case 14:
            return (Element) ((java.lang.String[])array)[index];
        case 15:
            return (Element) ((java.lang.Object[])array)[index];
        default: 
            throw new AssertionError("unknown element type");
        }
    }

    public void set(
            @Name("index") @TypeInfo("ceylon.language::Integer") long index,
            @Name("element") @TypeInfo("Element") Element element) {
        if (index<0) {
            throw new AssertionError("array index " + index + 
                    " may not be negative");
        }
        else if (index>=size) {
            throw new AssertionError("array index " + index + 
                    " must be less than size of array " + size);
        }
        else {
            int idx = (int) index; //typecast is safe 'cos we just checked above
            switch (elementType) {
            case 0:
                ((long[]) array)[idx] = ((Integer) element).value;
                break;
            case 1:
                ((long[]) array)[idx] = (java.lang.Long) element;
                break;
            case 2:
                ((double[]) array)[idx] = ((Float) element).value;
                break;
            case 3:
                ((double[]) array)[idx] = (java.lang.Double) element;
                break;
            case 4:
                ((int[]) array)[idx] = ((Character) element).codePoint;
                break;
            case 5:
                ((int[]) array)[idx] = (java.lang.Integer) element;
                break;
            case 6:
                ((byte[]) array)[idx] = ((Byte)element).value;
                break;
            case 7:
                ((byte[]) array)[idx] = (java.lang.Byte) element;
                break;
            case 8:
                ((boolean[]) array)[idx] = ((Boolean) element).booleanValue();
                break;
            case 9:
                ((boolean[]) array)[idx] = (java.lang.Boolean) element;
                break;
            case 10:
                ((char[]) array)[idx] = (java.lang.Character) element;
                break;
            case 11:
                ((short[]) array)[idx] = (java.lang.Short) element;
                break;
            case 12:
                ((float[]) array)[idx] = (java.lang.Float) element;
                break;
            case 13:
                ((java.lang.String[]) array)[idx] = ((String) element).value;
                break;
            case 14:
                ((java.lang.String[]) array)[idx] = (java.lang.String) element;
                break;
            case 15:
                ((java.lang.Object[]) array)[idx] = element;
                break;
            default:
                throw new AssertionError("unknown element type");
            }
        }
    }

    
    @Override
    public Array<Element> $clone() {
        return new Array<Element>($reifiedElement, copyArray());
    }
    
    private java.lang.Object copyArray() {
        if (array instanceof java.lang.Object[]) {
            return Arrays.copyOf((java.lang.Object[]) array, 
                    ((java.lang.Object[]) array).length);
        }
        else if (array instanceof long[]) {
            return Arrays.copyOf((long[]) array, 
                    ((long[]) array).length);
        }
        else if (array instanceof double[]) {
            return Arrays.copyOf((double[]) array, 
                    ((double[]) array).length);
        }
        else if (array instanceof boolean[]) {
            return Arrays.copyOf((boolean[]) array, 
                    ((boolean[]) array).length);
        }
        else if (array instanceof int[]) {
            return Arrays.copyOf((int[]) array, 
                    ((int[]) array).length);
        }
        else if (array instanceof byte[]) {
            return Arrays.copyOf((byte[]) array, 
                    ((byte[]) array).length);
        }
        else if (array instanceof short[]) {
            return Arrays.copyOf((short[]) array, 
                    ((short[]) array).length);
        }
        else if (array instanceof float[]) {
            return Arrays.copyOf((float[]) array, 
                    ((float[]) array).length);
        }
        else if (array instanceof char[]) {
            return Arrays.copyOf((char[]) array, 
                    ((char[]) array).length);
        }
        else {
            throw new AssertionError("impossible array type");
        }
    }

    @Ignore
    public java.lang.Object toArray() {
        return array;
    }

    @Override
    public boolean contains(@Name("element") 
    @TypeInfo("ceylon.language::Object")
    java.lang.Object element) {
        // FIXME Very inefficient for primitive types due to boxing
        for (int i=0; i<size; i++) {
            Element elem = getFromFirst(i);
            if (elem != null && elem.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long count(@Name("selecting")@FunctionalParameter("(element)") 
    @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
    Callable<? extends Boolean> selecting) {
        // FIXME Very inefficient for primitive types due to boxing
        int count=0;
        for (int i=0; i<size; i++) {
            Element elem = getFromFirst(i);
            if (elem != null && selecting.$call$(elem).booleanValue()) {
                count++;
            }
        }
        return count;
    }

    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Null|Element")
    public Element getFirst() {
        if (size>0) {
            return unsafeItem(0);
        }
        else {
            return null;
        }
    }
    
    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Null|Element")
    public Element getLast() {
        return size > 0 ? unsafeItem(size-1) : null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> 
    sort(@Name("comparing") @FunctionalParameter("(x,y)")
    @TypeInfo("ceylon.language::Callable<ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>") 
    Callable<? extends Comparison> comparing) {
        if (getEmpty()) {
            return (Sequential<? extends Element>) empty_.get_();
        }
        Array<Element> clone = $clone();
        clone.sortInPlace(comparing);
        return new ArraySequence<Element>($reifiedElement, clone);
    }
    /*@SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> 
    reverse() {
        if (getEmpty()) {
            return (Sequential<? extends Element>) empty_.get_();
        }
        Array<Element> clone = $clone();
        clone.reverseInPlace();
        return new ArraySequence<Element>($reifiedElement, clone);
    }*/
    
    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ?> 
    skip(@Name("skipping") long skipping) {
        int intSkip = toInt(skipping);
        // ok to cast here, since we know the size must fit in an int
        int length = (int) size;
        if (skipping <= 0) {
            return this;
        }
        return new ArrayIterable(this.array, intSkip, 
                Math.max(0, length-intSkip), 1);
    }

    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ?> 
    take(@Name("taking") long taking) {
        // ok to cast here, since we know the size must fit in an int
        int length = (int)size;
        if (taking >= length) {
            return this;
        }
        return new ArrayIterable(this.array, 0, 
                Math.max(toInt(taking), 0), 1);
    }

    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ?> 
    by(@Name("step") long step) {
        if (step <= 0) {
            throw new AssertionError("step size must be greater than zero");
        }
        else if (step == 1) {
            return this;
        }
        return new ArrayIterable(array, 0, 
                toInt((size+step-1)/step), 
                toInt(step));
    }
    
    @SuppressWarnings("unchecked")
    @Override 
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> sequence() {
        if (getEmpty()) {
            return (Sequential<? extends Element>) empty_.get_();
        }
        else {
            return new ArraySequence<Element>($reifiedElement, 
                    new Array<Element>($reifiedElement, copyArray()));
        }
    }
    
    @Ignore
    public int copyTo$sourcePosition(Element[] destination){
        return 0;
    }

    @Ignore
    public int copyTo$destinationPosition(Element[] destination, 
            int sourcePosition){
        return 0;
    }

    @Ignore
    public int copyTo$length(Element[] destination, 
            int sourcePosition, int destinationPosition){
        return java.lang.reflect.Array.getLength(array)-sourcePosition;
    }

    @Ignore
    public void copyTo(Array<Element> destination){
        copyTo(destination, 0, 0);
    }

    @Ignore
    public void copyTo(Array<Element> destination, int sourcePosition){
        copyTo(destination, sourcePosition, 0);
    }

    @Ignore
    public void copyTo(Array<Element> destination, 
            int sourcePosition, int destinationPosition){
        copyTo(destination, sourcePosition, destinationPosition, 
                java.lang.reflect.Array.getLength(array)-sourcePosition);
    }

    public void copyTo(@Name("destination") Array<Element> destination, 
                       @Name("sourcePosition") @Defaulted int sourcePosition, 
                       @Name("destinationPosition") @Defaulted int destinationPosition, 
                       @Name("length") @Defaulted int length){
        arraycopy(array, sourcePosition, destination.array, 
                        destinationPosition, length);
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(Array.class, $reifiedElement);
    }
    
    public void reverseInPlace() {
        if (array instanceof java.lang.Object[]) {
            for (int index=0; index<size/2; index++) {
                java.lang.Object[] arr = (java.lang.Object[]) array;
                java.lang.Object swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else if (array instanceof long[]) {
            for (int index=0; index<size/2; index++) {
                long[] arr = (long[]) array;
                long swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else if (array instanceof int[]) {
            for (int index=0; index<size/2; index++) {
                int[] arr = (int[]) array;
                int swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else if (array instanceof short[]) {
            for (int index=0; index<size/2; index++) {
                short[] arr = (short[]) array;
                short swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else if (array instanceof byte[]) {
            for (int index=0; index<size/2; index++) {
                byte[] arr = (byte[]) array;
                byte swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else if (array instanceof double[]) {
            for (int index=0; index<size/2; index++) {
                double[] arr = (double[]) array;
                double swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else if (array instanceof float[]) {
            for (int index=0; index<size/2; index++) {
                float[] arr = (float[]) array;
                float swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else if (array instanceof boolean[]) {
            for (int index=0; index<size/2; index++) {
                boolean[] arr = (boolean[]) array;
                boolean swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else if (array instanceof char[]) {
            for (int index=0; index<size/2; index++) {
                char[] arr = (char[]) array;
                char swap = arr[index];
                int indexFromLast = size-index-1;
                arr[index] = arr[indexFromLast];
                arr[indexFromLast] = swap;
            }
        }
        else {
            throw new AssertionError("illegal array type");
        }
    }
    
    public void sortInPlace(
            @Name("comparing") @FunctionalParameter("(x,y)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>") 
            final Callable<? extends Comparison> comparing) {
        java.util.List<Element> list = 
                new java.util.AbstractList<Element>() {
            @Override
            public Element get(int index) {
                return unsafeItem(index);
            }
            @Override
            public Element set(int index, Element element) {
                // Strictly this method should return the element that was at 
                // the given index, but that might require even more boxing 
                // and in practice the return value
                // doesn't seem to be used by the sorting algorithm
                Array.this.set(index, element);
                return null;
            }
            @Override
            public int size() {
                return (int)size;
            }
            @Override
            public java.lang.Object[] toArray() {
                if (array instanceof java.lang.Object[] &&
                    !(array instanceof java.lang.String[])) {
                    return (java.lang.Object[]) array;
                }
                else {
                    int size = size();
                    java.lang.Object[] result = 
                            new java.lang.Object[size];
                    for (int i=0; i<size; i++) {
                        result[i] = unsafeItem(i);
                    }
                    return result;
                }
            }
        };
        Comparator<Element> comparator = 
                new Comparator<Element>() {
            public int compare(Element x, Element y) {
                Comparison result = comparing.$call$(x, y);
                if (result==larger_.get_()) return 1;
                if (result==smaller_.get_()) return -1;
                return 0;
            }
        };
        Collections.<Element>sort(list, comparator);
    }
    
    private static int elementType(TypeDescriptor $reifiedElement) {
        if ($reifiedElement.containsNull()) {
            return 15;
        }
        
        java.lang.Class<?> arrayElementClass = 
                $reifiedElement.getArrayElementClass();
        if (arrayElementClass == Integer.class) {
            return 0;
        }
        else if (arrayElementClass == java.lang.Long.class) {
            return 1;
        }
        else if (arrayElementClass == Float.class) {
            return 2;
        }
        else if (arrayElementClass == java.lang.Double.class) {
            return 3;
        } 
        else if (arrayElementClass == Character.class) {
            return 4;
        }
        else if (arrayElementClass == java.lang.Integer.class) {
            return 5;
        } 
        else if (arrayElementClass == Byte.class) {
            return 6;
        }
        else if (arrayElementClass == java.lang.Byte.class) {
            return 7;
        } 
        else if (arrayElementClass == Boolean.class) {
            return 8;
        }
        else if (arrayElementClass == java.lang.Boolean.class) {
            return 9;
        } 
        else if (arrayElementClass == java.lang.Character.class) {
            return 10;
        } 
        else if (arrayElementClass == java.lang.Short.class) {
            return 11;
        } 
        else if (arrayElementClass == java.lang.Float.class) {
            return 12;
        } 
        else if (arrayElementClass == String.class) {
            return 13;
        }
        else if (arrayElementClass == java.lang.String.class) {
            return 14;
        } 
        else {
            return 15;
        }
    }
    
}
