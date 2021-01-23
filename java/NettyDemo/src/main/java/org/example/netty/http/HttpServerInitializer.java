package org.example.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // HttpServerCodec是netty提供的一个HTTP编码/解码器
        ch.pipeline().addLast("HttpServerEncodeDecode", new HttpServerCodec());
        ch.pipeline().addLast("HttpServerHandler", new HttpServerHandler());
    }
}
