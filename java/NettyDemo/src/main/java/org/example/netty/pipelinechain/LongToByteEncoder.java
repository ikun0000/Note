package org.example.netty.pipelinechain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LongToByteEncoder extends MessageToByteEncoder<Long> {

    /**
     *
     * @param ctx ChannelPipeline上下文
     * @param msg 后一个handler处理完发送的数据
     * @param out 出站的字节数据
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        out.writeLong(msg);
    }
}
