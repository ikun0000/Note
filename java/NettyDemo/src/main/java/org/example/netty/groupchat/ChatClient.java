package org.example.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class ChatClient {
    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast(new ChatClientHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("Connect to " + host + ":" + port);
                    } else {
                        System.out.println("Cannot to connect" + host + ":" + port);
                    }
                }
            });

            // 如果加了这句话会阻塞在这里，不会执行后续的代码
//            channelFuture.channel().closeFuture().sync();

            Scanner scanner = new Scanner(System.in);

            // 判断用户输入，如果有输入则通过channelFuture发送消息到服务端
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                channelFuture.channel().writeAndFlush(s + "\r\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ChatClient("127.0.0.1", 7000).run();
    }
}
