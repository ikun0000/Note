package org.example.netty.pipelinechain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

// ReplayingDecoder并不是支持所有ByteBuf的方法，如果调用了一个不支持的方法
// 会抛出一个UnsupportedOperationException
public class ByteToLongReplayingDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Replaying不需要判断数据是否完整或是否足够，它内部会进行管理
        out.add(in.readLong());
    }
}
