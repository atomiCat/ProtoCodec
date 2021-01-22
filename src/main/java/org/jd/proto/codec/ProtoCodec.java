package org.jd.proto.codec;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author cuijd    2021/1/21 11:32
 */
public class ProtoDecoder {

    private Map<Class, TypeInfo> typeInfos;
    private Map<Class, TypeInfo> decoders;

    public <T> T merge(byte[] data, int offset, int length, T t) {
        return merge(ByteBuffer.wrap(data, offset, length), t);
    }

    /**
     * @return fields[0] 占用1位，1/8字节 boolean
     * fields[1] 1字节 byte
     * fields[2] 2字节 char
     * fields[3] 4字节 int
     * fields[4] 4字节 float
     * fields[5] 8字节 long
     * fields[6] 8字节 double
     * fields[7] object array
     */
    public <T> T merge(ByteBuffer data, T t) {
        TypeInfo typeInfo = typeInfos.computeIfAbsent(t.getClass(), TypeInfo::new);
        typeInfo.fieldsEach(((field, type) -> {
            if (Modifier.isFinal(field.getModifiers()))
                return;
            try {
                switch (type) {
                    case boolean_:
                        field.setBoolean(t, data.get() == 1);
                        break;
                    case byte_:
                        field.setByte(t, data.get());
                        break;
                    case short_:
                        field.setShort(t, data.getShort());
                        break;
                    case char_:
                        field.setChar(t, data.getChar());
                        break;
                    case int_:
                        field.setInt(t, data.getInt());
                        break;
                    case float_:
                        field.setFloat(t, data.getFloat());
                        break;
                    case long_:
                        field.setLong(t, data.getLong());
                        break;
                    case double_:
                        field.setDouble(t, data.getDouble());
                        break;
                    case object_:
                        Class<?> fieldType = field.getType();
                        Object value = field.get(t);
                        value = value == null ? newInstance(fieldType, data) : merge(data, value);
                        field.set(t, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("error set field " + field.getName() + " in " + t.getClass().getName(), e);
            }
        }));
        return t;
    }

    private Object newInstance(Class<?> c, ByteBuffer data) {
        int length = data.getInt();
        if (c.isArray()) {
            if (length == -1)
                return null;
            Class<?> componentType = c.getComponentType();
            Object array = Array.newInstance(componentType, length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, newInstance(componentType, data));
            }
            return array;
        }
        if (length == 0)
            return null;

        Constructor<?> constructor = null;
        for (Constructor<?> cons : c.getConstructors()) {
            int mod = cons.getModifiers();
            if (!Modifier.isPublic(mod))
                continue;

        }
    }
}
