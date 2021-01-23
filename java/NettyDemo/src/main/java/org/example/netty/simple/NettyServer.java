package org.example.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.CopyOnWriteArrayList;

public class NettyServer {

    public static final CopyOnWriteArrayList<SocketChannel> SOCKET_CHANNEL_LIST = new CopyOnWriteArrayList<SocketChannel>();

    public static void main(String[] args) {
        // 创建BossGroup和WorkerGroup
        // 创建两个线程组：BossGroup和WorkerGroup
        // BossGroup仅仅处理连接请求
        // WorkerGroup与客户端进行业务处理
        // 两个组都是无限循环
        // 默认的线程数是CPU的 核数 * 2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建服务器端的启动对象，配置启动参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 设置BossGroup和WorkerGroup
        bootstrap.group(bossGroup, workerGroup)
                // 设置服务器通道的实现类型，
                .channel(NioServerSocketChannel.class)
                // 设置线程队列等待连接的个数
                .option(ChannelOption.SO_BACKLOG, 128)
                // 设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 设置WorkerGroup中的事件循环中的管道(Pipeline)的处理器(Handler)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 给Pipeline管道设置处理器
                        ch.pipeline().addLast(new NettyServerHandler());
                        // 保留连接客户端的channel集合
                        SOCKET_CHANNEL_LIST.add(ch);
                    }
                });

        System.out.println("Bootstrap config finish!");
        System.out.println("Starting server at 6668...");

        // 绑定端口并启动服务器
        try {
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            // 添加服务器启动后的监听事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("Listen port success!");
                    } else {
                        System.out.println("Cannot bind port!");
                    }
                }
            });

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 无论如何都要优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
