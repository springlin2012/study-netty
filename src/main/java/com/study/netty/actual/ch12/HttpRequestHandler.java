/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.study.netty.actual.ch12;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 功能描述: Http请求处理器
 * <p/>
 * 创建人: Moker
 * <p/>
 * 创建时间: 2018/08/05.
 * <p/>
 * Copyright (c) 凌霄阁-版权所有
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
        }

        RandomAccessFile file = new RandomAccessFile(INDEX, "r");
        HttpResponse response = new DefaultHttpResponse(
                request.getProtocolVersion(), HttpResponseStatus.OK
        );
        response.headers().set(
                HttpHeaders.Names.CONTENT_TYPE,
                "text/plain;charset=UTF-8");
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        if (keepAlive) { // 如果请求了 keep-alive,则添加所需要的HTTP头信息
            response.headers().set(
                    HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(response); // 将HttpResponse写到客户端
        if (ctx.pipeline().get(SslHandler.class) == null) { // 将index.html写到客户端
            ctx.write(new DefaultFileRegion(
                    file.getChannel(), 0, file.length()
            ));
        } else {
            ctx.write(new ChunkedNioFile(file.getChannel()));
        }
        // 写LastHttpContent并冲刷至客户端
        ChannelFuture future = ctx.writeAndFlush(
                LastHttpContent.EMPTY_LAST_CONTENT);

        // 如果没有请求 keep-alive,则在写操作完成后关闭Channel
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE
        );
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}