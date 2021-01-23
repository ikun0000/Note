package org.example.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class ChatServer {
    private int port;

    public ChatServer(int port) {
        this.port = port;
    }

    public void run() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 客户端到服务端的信息先解码
                        ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                        // 服务端发送到客户端消息编码
                        ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                        // 自定义的业务处理
                        ch.pipeline().addLast(new ChatServerHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("Chat Server start at " + port + "...");
                    } else {
                        System.out.println("Chat Server start failed!");
                    }
                }
            });

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ChatServer(7000).run();
    }
}
