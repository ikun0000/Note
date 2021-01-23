package org.example.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.netty.protobuf.pojo.StudentOuter;

public class ProtobufServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取客户端发送的StudentOuter.Student对象
        StudentOuter.Student student = (StudentOuter.Student) msg;
        System.out.println(student.getId());
        System.out.println(student.getName());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
