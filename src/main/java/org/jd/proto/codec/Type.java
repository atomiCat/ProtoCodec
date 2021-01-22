package org.jd.proto.codec;

/**
 * @author cuijd    2021/1/22 16:01
 */
public enum Type {
    boolean_,
    byte_,
    short_,
    char_,
    int_,
    float_,
    long_,
    double_,
    object_;
    public static final int size = values().length;

    public static Type valueOf(Class<?> type) {
        return type.isPrimitive() ? Enum.valueOf(Type.class, type.getName() + "_") : object_;
    }
}
