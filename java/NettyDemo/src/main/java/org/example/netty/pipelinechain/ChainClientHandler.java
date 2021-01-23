package org.example.netty.pipelinechain;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.Random;

public class ChainClientHandler extends SimpleChannelInboundHandler<Long> {
    private static Random random = new Random();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(random.nextLong());

        // 以下代码是不会经过自定义的编码器，因为在编码器的write方法内部会检测数据是否是出站数据且处理的类型
        // 是否和SimpleChannelInboundHandler的泛型一致，如果一致则做编码处理，否则不做编码处理
        // 发送到服务端后以ASCII编码的字符串会被认为16个字节的数据，服务器经过解码器的时候会分解为两个long类型数据
        // ctx.writeAndFlush(Unpooled.copiedBuffer("abcdefghjkqertyz", CharsetUtil.US_ASCII));

        /*
        以下是MessageToByteEncoder的write方法内的代码，他会判断数据是否为出站数据，如果是则处理，否则就会不处理
        if (acceptOutboundMessage(msg)) {
            @SuppressWarnings("unchecked")
            I cast = (I) msg;
            buf = allocateBuffer(ctx, cast, preferDirect);
            try {
                encode(ctx, cast, buf);
            } finally {
                ReferenceCountUtil.release(cast);
            }

            if (buf.isReadable()) {
                ctx.write(buf, promise);
            } else {
                buf.release();
                ctx.write(Unpooled.EMPTY_BUFFER, promise);
            }
            buf = null;
        } else {
            ctx.write(msg, promise);
        }
         */
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("receive: " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
