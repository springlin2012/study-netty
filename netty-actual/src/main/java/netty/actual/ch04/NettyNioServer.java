/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package netty.actual.ch04;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 功能描述: Netty非阻塞IO版本
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/08/05.
 * <p/>
 * Copyright (c) 凌霄阁工作室-版权所有
 */
public class NettyNioServer {

    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n",
                Charset.forName("UTF-8"));

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(
                                                            ChannelFutureListener.CLOSE
                                                    );
                                        }
                                    }
                            );
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}