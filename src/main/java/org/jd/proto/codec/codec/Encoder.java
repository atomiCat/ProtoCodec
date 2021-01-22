package org.jd.proto.codec.codec;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public interface Encoder {
    void encode(Object t, Consumer<ByteBuffer> bufferConsumer);
}
