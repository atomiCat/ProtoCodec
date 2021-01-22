package org.jd.proto.codec.codec;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface Codec {
    void encode(Object t, Consumer<ByteBuffer> bufferConsumer);

    Object decode(ByteBuffer buffer, Class<?> c);

    class Impl<T> implements Codec {
        private final BiConsumer<T, Consumer<ByteBuffer>> encoder;
        private final BiFunction<ByteBuffer, Class<?>, ?> decoder;

        public Impl(BiConsumer<T, Consumer<ByteBuffer>> encoder, BiFunction<ByteBuffer, Class<?>, ?> decoder) {
            this.encoder = encoder;
            this.decoder = decoder;
        }

        @Override
        public void encode(Object t, Consumer<ByteBuffer> bufferConsumer) {
            encoder.accept((T) t, bufferConsumer);
        }

        @Override
        public Object decode(ByteBuffer buffer, Class<?> c) {
            return decoder.apply(buffer, c);
        }
    }
}
