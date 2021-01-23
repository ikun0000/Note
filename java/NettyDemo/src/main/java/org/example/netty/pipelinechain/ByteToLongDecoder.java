package org.example.netty.pipelinechain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * 对入站的数据进行解码
     * @param ctx ChannelPipeline上下文
     * @param in 接收到的字节数据
     * @param out 交给下一个handler的数据，一个element就会调用一次下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
