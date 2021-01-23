package org.example.netty.protobuf_multi;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.netty.protobuf_multi.pojo.GameOuter;

public class Protobuf2ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GameOuter.Game game = (GameOuter.Game) msg;

        switch (game.getDataType()) {
            case Download:
                System.out.println("Download " + game.getDownload().getFilename() + " " + game.getDownload().getSize());
                break;
            case Upload:
                System.out.println("Upload " + game.getUpload().getId() + " " + game.getUpload().getFilename());
                break;
            default:
                System.out.println("Unknow type");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
