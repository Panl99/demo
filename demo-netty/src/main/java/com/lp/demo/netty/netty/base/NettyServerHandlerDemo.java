package com.lp.demo.netty.netty.base;

import com.lp.demo.common.util.ConsoleColorUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author lp
 * @date 2021/4/4 21:05
 * @desc 接收、处理客户端数据
 **/
@ChannelHandler.Sharable // 标示一个ChannelHandler 可以被多个 Channel 安全地共享
public class NettyServerHandlerDemo extends ChannelInboundHandlerAdapter {

    /**
     * 客户端连接服务端完成时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>>客户端连接通道建立完成<<<<<<<<<<");
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
        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>接收到客户端消息：" + buf.toString(CharsetUtil.UTF_8));
        // 将接收到的消息写给发送者，而不冲刷出站消息
//        ctx.write(buf);
    }

    /**
     * 数据读取完后处理方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer("hello client".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(buf);

        // 将未决消息冲刷到远程节点，并且关闭该 Channel
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//           .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭该Channel
        ctx.close();
    }
}
