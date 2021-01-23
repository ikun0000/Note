package org.example.netty.tcpproblemsolution;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;


public class TcpProblemSolutionServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println(new String(msg.getContent(), 0, msg.getLen(), "UTF-8"));

        byte[] data = UUID.randomUUID().toString().getBytes("UTF-8");
        ctx.writeAndFlush(new MessageProtocol(data.length, data));
    }
}
