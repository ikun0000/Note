package org.example.netty.protobuf_multi;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.netty.protobuf_multi.pojo.GameOuter;

import java.util.concurrent.TimeUnit;

public class Protobuf2ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ctx.channel().eventLoop().execute(() -> {
            GameOuter.Game game1 = GameOuter.Game
                    .newBuilder()
                    .setDataType(GameOuter.Game.DataType.Upload)
                    .setUpload(GameOuter.Upload.newBuilder().setFilename("a.php").setId(1).build())
                    .build();

            ctx.writeAndFlush(game1);
        });

        ctx.channel().eventLoop().schedule(() -> {
            GameOuter.Game game2 = GameOuter.Game
                    .newBuilder()
                    .setDataType(GameOuter.Game.DataType.Download)
                    .setDownload(GameOuter.Download.newBuilder().setFilename("a.php").setSize(22).build())
                    .build();

            ctx.writeAndFlush(game2);
        }, 1, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
