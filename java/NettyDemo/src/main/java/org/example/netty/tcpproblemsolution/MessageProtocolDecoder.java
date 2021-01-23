package org.example.netty.tcpproblemsolution;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

// 自定义协议的解码器
public class MessageProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MessageProtocol messageProtocol = new MessageProtocol();
        /**
        协议格式
          4byte         unknow
        +----------+-----------------+
        |len       |      data       |
        +----------+-----------------+
        第一个字段为4字节，表示之后的数据长度
         */
        // 首先判断查毒是否足够，不够则直接返回
        if (in.readableBytes() < 4) {
            return;
        }

        // 根据读取的长度生成字节数组并封装成MessageProtocol
        // 在读取之前记录当前readIndex位置，如果数据不足需要重置readIndex到这里
        in.markReaderIndex();
        messageProtocol.setLen(in.readInt());
        byte[] data = new byte[messageProtocol.getLen()];
        if (in.readableBytes() < messageProtocol.getLen()) {
            // 如果可读取的字节数小于之前读取到的数量，则恢复readIndex然后返回
            in.resetReaderIndex();
            // 可以使用markReadIndex()和resetReadIndex()配合使用
            // 也可以像下面这样直接回退之前读取的字节数
            // in.readerIndex(in.readerIndex() - 4);
            return;
        }
        in.readBytes(data);
        messageProtocol.setContent(data);

        out.add(messageProtocol);
    }
}
