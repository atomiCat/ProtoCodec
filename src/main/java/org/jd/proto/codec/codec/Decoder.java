package org.jd.proto.codec.codec;

import java.nio.ByteBuffer;

/**
 * @author cuijd    2021/1/22 15:40
 */
public interface Decoder {
    Object decode(ByteBuffer buffer, Class<?> c);
}
