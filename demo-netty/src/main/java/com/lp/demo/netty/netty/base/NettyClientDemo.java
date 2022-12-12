package com.lp.demo.netty.netty.base;

import com.lp.demo.common.util.ConsoleColorUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @create 2021/4/4 22:23
 * @author lp
 * @description
 **/
public class NettyClientDemo {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandlerDemo());
                        }
                    });

            ConsoleColorUtil.printDefaultColor(">>>>>>>>>>netty client start...<<<<<<<<<<");

            // 连接到远程节点，阻塞等待直到连接完成
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 9000).sync();
            // 阻塞，直到Channel 关闭
            cf.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池并且释放所有的资源
            group.shutdownGracefully();
        }
    }
}
