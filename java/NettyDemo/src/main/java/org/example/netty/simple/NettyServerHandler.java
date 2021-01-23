package org.example.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 自定义的Handler需要继承规定的HandlerAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据的事件
     * 读取客户端的数据
     * @param ctx 上下文对象，含有管道Pipeline、通道、客户端连接地址
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("Thread: " + Thread.currentThread().getName());
//        System.out.println(ctx);
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("Receive client: " +
//                ctx.channel().remoteAddress() + ". Message: " +
//                byteBuf.toString(CharsetUtil.UTF_8));

         // TaskQueued 用户线程自定义普通任务, 提交的任务放在channel的taskQueued上
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5 * 1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        // 一起执行
//        ctx.channel().eventLoop().execute(() -> {
//            try {
//                Thread.sleep(5 * 10);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello World!2", CharsetUtil.UTF_8));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        System.out.println("Go on...");


        // TaskQueued 提交定时任务, 提交的任务放到channel的scheduleTaskQueued上
        ctx.channel().eventLoop().schedule(() -> {
            ctx.writeAndFlush(Unpooled.copiedBuffer("Schedule Task Queued", CharsetUtil.UTF_8));
        }, 5L, TimeUnit.SECONDS);
        System.out.println("Go on ...");
    }

    // 读取完数据后
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 发送消息到客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("Server receive a message.", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 发生异常关闭通道
        ctx.close();
    }
}
