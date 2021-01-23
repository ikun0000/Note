package org.example.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHandlerIdle extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        assert evt instanceof IdleStateEvent;

        IdleStateEvent event = (IdleStateEvent) evt;
        switch (event.state()) {
            case READER_IDLE:
                System.out.println(ctx.channel().remoteAddress() + "read idle");
                break;
            case WRITER_IDLE:
                System.out.println(ctx.channel().remoteAddress() + "write idle");
                break;
            case ALL_IDLE:
                System.out.println(ctx.channel().remoteAddress() + "all idle");
                break;
        }
    }
}
