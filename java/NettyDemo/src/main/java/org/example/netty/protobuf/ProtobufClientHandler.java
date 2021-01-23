package org.example.netty.protobuf;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.netty.protobuf.pojo.StudentOuter;

public class ProtobufClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用Builder模式生成传输的POJO对象
        StudentOuter.Student student = StudentOuter.Student.newBuilder().setId(1).setName("haha").build();
        ChannelFuture channelFuture = ctx.writeAndFlush(student);

        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
