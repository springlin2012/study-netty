/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.study.netty.actual.ch04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 功能描述: 阻塞IO实现（OIO）
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/07/01.
 * <p/>
 * Copyright (c) 凌霄阁工作室-版权所有
 */
public class PlainOioServer {

    public void server(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);

        try {
            for(;;) {
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from "+ clientSocket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("Hi! \r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();

                            out.close();
                            clientSocket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        PlainOioServer plainOioServer = new PlainOioServer();
        plainOioServer.server(8080);
        while (true) {

        }
    }
}