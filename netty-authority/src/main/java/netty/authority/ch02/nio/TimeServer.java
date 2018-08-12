/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package netty.authority.ch02.nio;

/**
 * 功能描述: NIO实现时间服务器
 * <p/>
 * 创建人: Moker
 * <p/>
 * 创建时间: 2018/08/12.
 * <p/>
 * Copyright (c) 凌霄阁-版权所有
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 启动处理线程

    }
}