/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.study.netty.actual.ch02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能描述: 多线程Netty客户端
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/06/27.
 * <p/>
 * Copyright (c) 凌霄阁工作室-版权所有
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient (String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(final String number) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler(number));
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            group.shutdownGracefully().sync();
        }

    }

    public static void main(final String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: "+ EchoClient.class.getSimpleName() + "<host> <port>");
            return;
        }

        try {
            new EchoClient(args[0], Integer.parseInt(args[1])).start("1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i=1; i<=1; i++) {
            final String number = String.valueOf(i);
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        new EchoClient(args[0], Integer.parseInt(args[1])).start(number);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
    }
}