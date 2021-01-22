package org.jd.proto.codec;

import org.jd.proto.codec.codec.Decoder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * @author cuijd    2021/1/22 16:07
 */
public class TypeInfo {
    private final Class<?> type;
    /**
     * 根据类型分组,顺序为 Type 指定的顺序
     */
    private final Field[][] fieldss;
    Decoder decoder;


    public TypeInfo(Class<?> type) {
        this.type = type;
        fieldss = new Field[Type.size][];
        ArrayList<Field>[] fieldsLists = new ArrayList[Type.size];
        for (int i = 0; i < Type.size; i++) {
            fieldsLists[i] = new ArrayList<>();
        }
        //储存Field
        for (Field field : type.getFields()) {
            if (Modifier.isTransient(field.getModifiers()))
                continue;
            field.setAccessible(true);
            fieldsLists[Type.valueOf(field.getType()).ordinal()].add(field);
        }
        //排序
        for (int i = 0; i < fieldsLists.length; i++) {
            Field[] fields = fieldsLists[i].toArray(new Field[0]);
            Arrays.sort(fields, Comparator.comparing(Field::getName));
            fieldss[i] = fields;
        }
    }

    public void fieldsEach(BiConsumer<Field, Type> consumer) {
        Type[] types = Type.values();
        for (int i = 0; i < fieldss.length; i++) {
            Type type = types[i];
            for (Field field : fieldss[i]) {
                consumer.accept(field, type);
            }
        }
    }
}
