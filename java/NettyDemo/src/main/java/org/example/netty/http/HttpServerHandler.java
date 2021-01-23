package org.example.netty.http;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

// 指定服务器和客户端之间的通信对象是HttpObject
// SimpleChannelInboundHandler是ChannelInboundHandlerAdapder的子类
// HttpObject表示客户端和服务端相互通信的数据
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // 当有读取事件就会执行这个函数
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断HttpObject是否是HttpRequest
        assert msg instanceof HttpRequest;

        HttpRequest request = (HttpRequest) msg;
        System.out.println(ctx.channel().remoteAddress() + " " + request.method() + " " + request.uri());

        if (request.uri().endsWith("favicon.ico")) {
            return;
        }

        // 恢复信息给浏览器
        ByteBuf content = Unpooled.copiedBuffer("<h1>Hello World!</h1>", CharsetUtil.UTF_8);
        // 构造HTTP响应(HttpResponse)
        HttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                content);
        // 设置HTTP响应的Content-Type
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
        // 设置HTTP响应Content-Length
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

        // 响应客户端
        ctx.writeAndFlush(httpResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ClassCastException) {
            return;
        }
        cause.printStackTrace();
    }
}
