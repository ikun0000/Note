package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO实现聊天室服务端
 * @author root
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        // 创建Selector
        Selector selector = Selector.open();

        // 通过ServerSocketChannel创建channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 为channel绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(12345));

        // 设置channel为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 将channel注册到selector上，监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started...");

        // 循环等待新接入的链接
        for (;;) {
            // TODO 获取可用channel数量
            int readyChannels = selector.select();

            // TODO 以后解释
            if (readyChannels == 0) {
                continue;
            }

            // 获取可用channel的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                // 处理SelectionKey实例
                SelectionKey selectionKey = iterator.next();
                // 移除Set中当前的SelectionKey
                // 每次调用Selector的selectedKeys()方法都会把可用的channel放入Set中
                // 所以一旦获取了SelectionKey就把他从当前集合移除，以免集合有过多SelectionKey
                iterator.remove();

                // 根据就绪状态调用对应的方法处理业务逻辑
                // 如果是接入事件
                if (selectionKey.isAcceptable()) {
                    acceptHandle(serverSocketChannel, selector);
                }

                // 如果是可读事件
                if (selectionKey.isReadable()) {
                    readHandle(selectionKey, selector);
                }
            }
        }
    }

    /**
     * 接入事件处理
     * @param serverSocketChannel 需要一个ServerSocketChannel接收连接
     * @param selector 需要一个Selector用来注册接入的连接
     */
    private static void acceptHandle(ServerSocketChannel serverSocketChannel,
                              Selector selector) throws IOException {
        // 如果是接入事件，创建SocketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 将channel设置为非阻塞模式
        socketChannel.configureBlocking(false);

        // 注册到Selector上
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 回复客户端提示信息
        socketChannel.write(Charset.forName("UTF-8").encode("connected..."));
    }

    /**
     * 可读事件处理
     * @param selectionKey 通过SelectionKey获取客户端channel
     * @param selector 到时候注册会Selector上
     */
    private static void readHandle(SelectionKey selectionKey,
                            Selector selector) throws IOException {
        // 从SelectionKey中获取已经就绪的channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        // 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 循环读取客户端请求消息
        String request = "";
        while (socketChannel.read(byteBuffer) > 0) {
            // 切换Buffer为读模式
            byteBuffer.flip();

            // 读取buffer中的数据
            request += Charset.forName("UTF-8").decode(byteBuffer);
        }

        // 将channel再次注册到Selector中，监听他的可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 将客户端发送的信息广播给其他客户端
        if (request.length() > 0) {
            // 广播消息
            System.out.println("server: " + request);
            broadCast(selector, socketChannel, request);
        }

    }

    /**
     * 广播给其他客户端
     * @param selector 通过Selector获取所有接入的客户端
     * @param socketChannel 发送消息的客户端的channel
     * @param message 需要广播的消息
     */
    private static void broadCast(Selector selector,
                                  SocketChannel socketChannel,
                                  String message) {
        // 获取所有已接入的客户端channel
        // 循环向channel广播信息
        selector.keys().forEach((selectionKey) -> {
            Channel channel = selectionKey.channel();

            // 剔除ServerSocketChannel和发送消息的channel
            if (!(channel instanceof ServerSocketChannel)
                    && channel != socketChannel) {
                // 将消息发送给其他客户端
                try {
                    ((SocketChannel) channel).write(Charset.forName("UTF-8").encode(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
