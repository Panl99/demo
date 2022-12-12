package com.lp.demo.netty.netty.base;

import com.lp.demo.common.util.ConsoleColorUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @create 2021/4/4 21:03
 * @author lp
 * @description
 **/
public class NettyServerDemo {
    public static void main(String[] args) {
        // 创建两个线程组，含有的子线程NioEventLoop的个数默认为cpu核数的2倍
        // masterGroup只处理连接请求，workerGroup真正处理客户端业务
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            // 创建服务器端启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 使用链式编程配置参数
            serverBootstrap.group(bossGroup, workerGroup)
                    // 使用NioServerSocketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 初始化服务器连接队列大小，服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接。
                    // 多个客户端同时来的时候，服务端将不能处理的客户端连接请求放到队列中等待处理。
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 创建通道初始化对象，设置初始化参数，在Sock
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 对workerGroup的SocketChannel设置处理器，业务在NettyServerHandlerDemo处理
                            socketChannel.pipeline().addLast(new NettyServerHandlerDemo());
                        }
                    });

            ConsoleColorUtil.printDefaultColor(">>>>>>>>>>netty server start...<<<<<<<<<<");

            // 绑定一个端口并且同步，生成一个ChannelFuture异步对象，通过isDone()等方法可以判断异步事件执行情况
            // bind是异步操作，sync()是等待异步操作执行完毕
            ChannelFuture cf = serverBootstrap.bind(9000).sync();

            // 给cf注册监听器，监听关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (cf.isSuccess()) {
                        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>监听端口9000成功<<<<<<<<<<");
                    } else {
                        ConsoleColorUtil.printSingleColor(">>>>>>>>>>监听端口9000失败<<<<<<<<<<", 31, 2);
                    }
                }
            });
            //(7) 获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
