package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NIO实现聊天室客户端
 * @author root
 */
public class NioClient {
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(3,
            Runtime.getRuntime().availableProcessors() + 1,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(Runtime.getRuntime().availableProcessors()),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws IOException {
        // 连接服务器端
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 12345));

        // 接收服务端的相应
        // 客户端线程，专门接收服务器相应信息
        // selector, socketChannel, 注册
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        THREAD_POOL_EXECUTOR.execute(() -> {
            try {
                for (;;) {
                    // 获取可用channel数量
                    int readyChannels = selector.select();

                    // 如果没有可读channel不执行之后操作
                    if (readyChannels == 0) {
                        continue;
                    }

                    // 获取可用channel集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();

                    while (iterator.hasNext()) {
                        // 遍历每一个SelectionKey实例
                        SelectionKey selectionKey = iterator.next();
                        // 移除Set中当前的SelectionKey实例
                        iterator.remove();

                        // 如果是可读事件
                        if (selectionKey.isReadable()) {
                            // 从SelectionKey中获取就绪的channel
                            SocketChannel innerSocketChannel = (SocketChannel) selectionKey.channel();

                            // 创建Buffer
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                            // 循环读取服务端相应的数据
                            String response = "";
                            while (innerSocketChannel.read(byteBuffer) > 0) {
                                // 切换buffer为读模式
                                byteBuffer.flip();
                                response += Charset.forName("UTF-8").decode(byteBuffer);
                            }

                            // 将channel再次注册到selector上，监听可读事件
                            innerSocketChannel.register(selector, SelectionKey.OP_READ);
                            // 将服务器相应的数据打印到控制台
                            if (response.length() > 0) {
                                System.out.println(response);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
        

        // 向服务器发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String request = scanner.nextLine();
            if (request != null && request.length() > 0) {
                socketChannel.write(Charset.forName("UTF-8").encode(request));
            }
        }
    }

}
