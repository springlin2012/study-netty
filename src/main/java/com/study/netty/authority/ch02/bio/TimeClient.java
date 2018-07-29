/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.study.netty.authority.ch02.bio;

import java.io.*;
import java.net.Socket;

/**
 * 功能描述: BIO客户端
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/06/23.
 * <p/>
 * Copyright (c) 凌霄阁工作室-版权所有
 */
public class TimeClient {

    public static void main(String[] args) {
        int prot = 8080;
        if (args != null && args.length > 0) {
            try {
                prot = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Socket socket = null;
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            socket = new Socket("127.0.0.1", prot);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("QUERY TIME ORDER");
            out.flush();
            System.out.println("Send order 2 server succeed.");


            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String rlt = in.readLine();
            System.out.println("Now is:"+ rlt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                in = null;
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}