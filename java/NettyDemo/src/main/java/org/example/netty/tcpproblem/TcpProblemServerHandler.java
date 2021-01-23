package org.example.netty.tcpproblem;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class TcpProblemServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 接收客户端数据，不一定督导完整的数据
        byte[] array = new byte[msg.readableBytes()];
        msg.readBytes(array);
        System.out.println(new String(array, 0, array.length));

        ctx.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString(), Charset.forName("UTF-8")));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
