package org.example.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪就会出发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送消息到服务器
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收客户端返回的消息
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("Client receive from: " +
                ctx.channel().remoteAddress() + ", message: " +
                byteBuf.toString(CharsetUtil.UTF_8));
    }
}
