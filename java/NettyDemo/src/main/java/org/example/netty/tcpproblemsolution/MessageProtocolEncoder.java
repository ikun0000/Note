package org.example.netty.tcpproblemsolution;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageProtocolEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        /**
        协议格式
          4byte         unknow
        +----------+-----------------+
        |len       |      data       |
        +----------+-----------------+
        第一个字段为4字节，表示之后的数据长度
         */
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
