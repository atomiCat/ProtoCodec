package org.jd.proto.codec.codec;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 处理包装类、String、Number、日期
 * Boolean
 * Byte
 * Short
 * Character
 * Integer
 * Long
 * Float
 * Double
 * <p>
 * AtomicInteger
 * AtomicLong
 * BigDecimal
 * BigInteger
 *
 * @author cuijd    2021/1/21 15:15
 */
public class StandardCodec{


    @Override
    public Object decode(ByteBuffer buffer, Class<?> c) {
        if (c.isAssignableFrom(Boolean.class))
            return buffer.get() == 1;
        if (c.isAssignableFrom(Byte.class))
            return buffer.get();
        if (c.isAssignableFrom(Short.class))
            return buffer.getShort();
        if (c.isAssignableFrom(Character.class))
            return buffer.getChar();
        if (c.isAssignableFrom(Integer.class))
            return buffer.getInt();
        if (c.isAssignableFrom(Long.class))
            return buffer.getLong();
        if (c.isAssignableFrom(Float.class))
            return buffer.getFloat();
        if (c.isAssignableFrom(Double.class))
            return buffer.getDouble();

        if (c.isAssignableFrom(AtomicInteger.class))
            return new AtomicInteger(buffer.getInt());
        if (c.isAssignableFrom(AtomicLong.class))
            return new AtomicLong(buffer.getLong());

        int length = buffer.getInt();//不定长数据，读length
        if (c.isAssignableFrom(BigInteger.class)) {
            return decodeBigInteger(buffer, length);
        }
        if (c.isAssignableFrom(BigDecimal.class)) {
            int scale = buffer.getInt();
            return new BigDecimal(decodeBigInteger(buffer, length), scale);
        }

        if (c.isAssignableFrom(String.class)) {
            int oldLimit = buffer.limit();
            buffer.limit(buffer.position() + length);
            String s = StandardCharsets.UTF_8.decode(buffer).toString();
            buffer.limit(oldLimit);
            return s;
        }

        throw new IllegalArgumentException("unsupported class: " + c.getName());
    }

    private BigInteger decodeBigInteger(ByteBuffer buffer, int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new BigInteger(bytes);
    }

    @Override
    public void encode(Object o, Consumer<ByteBuffer> bufferConsumer) {

    }
}
