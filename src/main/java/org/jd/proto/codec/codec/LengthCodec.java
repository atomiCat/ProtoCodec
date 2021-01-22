package org.jd.proto.codec.codec;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author cuijd    2021/1/22 15:08
 */
public interface LengthCodec extends Codec {
    @Override
    default void encode(Object t, Consumer<ByteBuffer> bufferConsumer) {
        encodeL(t, buffer -> {
            ByteBuffer length = ByteBuffer.allocate(4);
            length.putInt(buffer.remaining());
            bufferConsumer.accept(length);
            bufferConsumer.accept(buffer);
        });
    }

    @Override
    default Object decode(ByteBuffer buffer, Class<?> c) {
        return decodeL(buffer, c, buffer.getInt());
    }

    void encodeL(Object t, Consumer<ByteBuffer> bufferConsumer);

    Object decodeL(ByteBuffer buffer, Class<?> c, int length);

    class Impl implements LengthCodec {
        private final BiConsumer<Object, Consumer<ByteBuffer>> encoder;
        private final Decoder decoder;

        public Impl(BiConsumer<Object, Consumer<ByteBuffer>> encoder, Decoder decoder) {
            this.encoder = encoder;
            this.decoder = decoder;
        }

        @Override
        public void encodeL(Object t, Consumer<ByteBuffer> bufferConsumer) {
            encoder.accept(t, bufferConsumer);
        }

        @Override
        public Object decodeL(ByteBuffer buffer, Class<?> c, int length) {
            return decoder.accept(buffer, c, length);
        }

        public interface Decoder {
            Object accept(ByteBuffer buffer, Class<?> c, int length);
        }
    }
}
