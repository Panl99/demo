package com.lp.demo.netty.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * @create 2021/4/4 21:05
 * @auther lp
 * @description
 **/
public class NettyServerHandlerDemo extends ChannelInboundHandlerAdapter {

    /**
     * 客户端连接服务端完成时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(">>>>>>>>>>>客户端连接通道建立完成<<<<<<<<<<");
    }

    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象，含有通道channel、管道pipeline
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链接，出站入站
        // 将msg转成一个ByteBuf，类似NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(">>>>>>>>>>接收到客户端消息："+ buf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完后处理方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer("hello client".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
