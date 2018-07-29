/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.study.netty.authority.ch02.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 功能描述: 同步阻塞I/O (BIO)
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/06/23.
 * <p/>
 * Copyright (c) 凌霄阁工作室-版权所有
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        int prot = 8080;
        if (args != null && args.length > 0) {
            try {
                prot = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(prot);
            System.out.println("The time server start in port:"+ prot);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}