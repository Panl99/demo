package com.lp.demo.netty.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @create 2021/4/4 22:24
 * @author lp
 * @description
 **/
public class NettyClientHandlerDemo extends ChannelInboundHandlerAdapter {

    /**
     * 当客户端连接服务端完成时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer("hello server".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(buf);
    }

    /**
     * 当通道有读取事件时会触发，即服务端发送数据给客户端
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(">>>>>>>>>>收到服务端的消息："+ buf.toString(CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
