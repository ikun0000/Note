package org.example.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBufTest02 {

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);

        if (byteBuf.hasArray()) {
            byte[] array = byteBuf.array();
            String s = new String(array, 0, array.length, CharsetUtil.UTF_8);
            System.out.println(s);
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());
            System.out.println(byteBuf.readableBytes());

            System.out.println(byteBuf.getCharSequence(1, 3, CharsetUtil.UTF_8));
        }
    }
}
