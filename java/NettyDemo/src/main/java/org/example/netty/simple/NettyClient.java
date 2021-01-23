package org.example.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) {
        // 客户端需要一个事件循环组
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        // 创建并配置客户端启动器
        Bootstrap bootstrap = new Bootstrap();
        // 设置事件循环组
        bootstrap.group(nioEventLoopGroup)
                // 设置客户端连接通道的类型
                .channel(NioSocketChannel.class)
                // 初始化通道
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });

        System.out.println("Client ready!");
        System.out.println("Connect to 127.0.0.1:6668");

        try {
            // 连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            // 监听关闭通道
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程组
            nioEventLoopGroup.shutdownGracefully();
        }
    }
}
