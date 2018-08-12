/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package netty.authority.ch02.simulationnio;

import netty.authority.ch02.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 功能描述: 伪异步I/O模型
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
            // 创建I/O任务线程池
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);

            while (true) {
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}