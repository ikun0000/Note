package org.example.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    // 定义一个channel组管理所有的客户端channel, 全局事件执行器
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 连接建立后第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 通知其他客户端有新的客户端上线
        // 会向channel组中的所有channel执行writeAndFlush
        CHANNEL_GROUP.writeAndFlush("Client" + ctx.channel().remoteAddress() + " added.");
        CHANNEL_GROUP.add(ctx.channel());
    }

    // 表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " online");
    }

    // 当channel处于非活动状态则会触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " downline");
    }

    // 当连接断开之前会执行
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 其实不需要执行remove方法，因为在出发这个事件以后netty会自动把channel从channel group中移除
        CHANNEL_GROUP.remove(ctx.channel());
        CHANNEL_GROUP.writeAndFlush("Client " + ctx.channel().remoteAddress() + " removed.");
    }

    // 把该channel发送的消息推送到别的channel
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        CHANNEL_GROUP.forEach(ch -> {
            // 判断遍历的channel是否为自己，如果是自己则不转发消息
            if (ch == channel) {
                return;
            }
            ch.writeAndFlush("Client " + channel.remoteAddress() + ": " + msg);
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
