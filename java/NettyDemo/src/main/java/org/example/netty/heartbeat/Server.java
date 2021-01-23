package org.example.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class Server {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 加入Netty提供的IdleStateHandler
                        // IdleStateHandler是处理空闲状态的处理器
                        // 参数readerIdleTime表示多久时间没有读事件，如果达到这个时间就会发送一个心跳检测包，检测是否连接
                        // 参数writerIdleTime表示多久没有写事件，也会发送心跳检测表
                        // allIdleTime表示多长时间没有读写操作，也会发送心跳检测包/
                        // 除了发送心跳检测包以外还会出发IdleStateEvent事件，由pipeline下一个Handler处理(调用userEventTrigger方法)
                        ch.pipeline().addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                        // 加入对空闲检测进一步处理的自定义Handler
                        ch.pipeline().addLast(new ServerHandlerIdle());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(6000).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
