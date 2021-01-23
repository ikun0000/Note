package org.example.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 基于HTTP协议，所以需要HTTP的编码/解码器
                        ch.pipeline().addLast(new HttpServerCodec());
                        // 以块的方式读写，加上ChunkedWriteHandler处理器
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        // HTTP数据在传输过程中是分段的，这个处理器用于多个HTTP分段聚合起来
                        ch.pipeline().addLast(new HttpObjectAggregator(8192));
                        // WebSocket的数据是以帧（Frame）的方式进行传输的
                        // WebSocketFrame有6个子类
                        // 客户端请求：ws://localhost:7070/hello
                        // 将一个HTTP协议通过Upgrade升级为WS协议
                        // 这里配置的websocketPath参数要与客户端的URL匹配
                        // WebSocketServerProtocalHandler的核心功能是把HTTP协议升级为WebSocket协议
                        // 连接建立的时候客户端会发送一个HTTP包使用Set-WebSocket-*开启WebSocket
                        // 服务端受到后会返回101(Switching Protocal)状态码表示切换协议，同时返回头包含Upgrade: websocket更改协议为WebSocket
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/hello"));
                        // 自定义Handler，处理业务逻辑
                        ch.pipeline().addLast(new WebSocketServerHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(7070).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
