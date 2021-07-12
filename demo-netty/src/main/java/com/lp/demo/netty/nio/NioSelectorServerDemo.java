package com.lp.demo.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @create 2021/4/4 19:29
 * @auther lp
 * @description
 **/
public class NioSelectorServerDemo {

    /**
     * NIO标准版连接
     * selector多路复用器，只感知有事件的连接，放到列表
     *
     * 弊端：只能处理有限个连接，大量连接可能会导致连接超时
     * 真正用nio还是要用=>netty
     */
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        // 设置ServerSocketChannel为非阻塞，true为阻塞
        serverSocketChannel.configureBlocking(false);

        // 打开Selector处理Channel，即创建epoll
        Selector selector = Selector.open();

        // 把ServerSocketChannel注册到Selector上，并且selector对客户端accept连接操作感兴趣
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(">>>>>>>>>>服务启动成功<<<<<<<<<<");

        while (true) {
            // 阻塞等待需要处理的事件发生
            selector.select();

            // 获取selector中注册的全部事件的SelectionKey实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

            // 遍历SelectionKey对事件进行处理
            while (selectionKeyIterator.hasNext()) {
                SelectionKey key = selectionKeyIterator.next();
                // 如果是OP_ACCEPT事件，则进行连接获取和事件注册
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    // 这里只注册了读事件，如果需要给客户端发送数据需要注册写事件
                    SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println(">>>>>>>>>>客户端连接成功<<<<<<<<<<");
                } else if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = socketChannel.read(byteBuffer);
                    // 如果有数据，打印出来
                    if (len > 0) {
                        System.out.println(">>>>>>>>>>接收到消息："+ new String(byteBuffer.array()));
                    } else if (len == -1) {
                        System.out.println(">>>>>>>>>>客户端断开连接<<<<<<<<<<");
                        socketChannel.close();
                    }
                }
                // 从事件集合中删除本次处理的key，防止下次selector重复处理
                selectionKeyIterator.remove();
            }

        }
    }
}
