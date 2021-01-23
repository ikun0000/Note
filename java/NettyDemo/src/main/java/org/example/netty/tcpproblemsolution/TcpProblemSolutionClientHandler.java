package org.example.netty.tcpproblemsolution;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class TcpProblemSolutionClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            byte[] msg = ("Message" + i + "!").getBytes("UTF-8");
            MessageProtocol messageProtocol = new MessageProtocol(msg.length, msg);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println(new String(msg.getContent(), 0, msg.getLen(), "UTF-8"));
    }
}
