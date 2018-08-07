/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.study.netty.actual.ch12;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 功能描述: 处理文本帧
 * <p/>
 * 创建人: Moker
 * <p/>
 * 创建时间: 2018/08/05.
 * <p/>
 * Copyright (c) 凌霄阁-版权所有
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
    private final ChannelGroup group;


    public TextWebSocketFrameHandler (ChannelGroup group) {
        this.group = group;
    }


    /**
     * 重写 userEventTriggered 方法以处理自定义事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler
                .ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 如果该事件表示握手成功，则从该Channelipeline中移除 HttpRequestHandler,
            // 因为将不会接收到任何HTTP消息了
            ctx.pipeline().remove(HttpRequestHandler.class);
            // 将新的 WebSocket Channel 添加到 ChannelGroup 中，以便它可以接收到所有的消息
            group.writeAndFlush(new TextWebSocketFrame("Client "+ ctx.channel() +" joined"));
            group.add(ctx.channel());
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 增加消息的引用计数，并将它写到 ChannelGroup 中所有已连接的客户端
        group.writeAndFlush(msg.retain());
    }

}