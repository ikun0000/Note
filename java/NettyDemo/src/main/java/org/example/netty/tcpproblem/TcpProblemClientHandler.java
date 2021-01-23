package org.example.netty.tcpproblem;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class TcpProblemClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用taskQueue发送10条数据给服务端，会出现拆包沾包问题
        // 服务器可能并不是一个个接收，有时候全部一起接收，有时候分开几个接收
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            ctx.channel().eventLoop().execute(() -> {
                ctx.writeAndFlush(Unpooled.copiedBuffer("Message" + finalI + " ", CharsetUtil.UTF_8));
            });
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        System.out.println(new String(bytes, 0, bytes.length));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
