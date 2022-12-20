package com.lp.demo.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lp
 * @date 2022/8/31 23:52
 * @desc
 **/
public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            System.out.println("等待连接....");

            // 阻塞连接
            Socket clientSocket = serverSocket.accept();
            System.out.println("有客户端连接了....");

            // 方式1，多客户端连接时会阻塞。
//            handler(clientSocket);

            // 方式2，开启多线程去处理客户端连接，存在c10k问题，很多客户端连接时资源不够。
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 方式3，使用线程池，但是会降低并发处理能力。

        }
    }

    private static void handler(Socket clientSocket) throws IOException {
        byte[] bytes = new byte[1024];
        System.out.println("准备读取....");

        // 接收客户端的数据，阻塞方法，没有数据可读时就阻塞等待
        int read = clientSocket.getInputStream().read();

        System.out.println("读取完成....");

        if (read != -1) {
            System.out.println("接收到客户端的数据：" + new String(bytes, 0, read));
        }


    }
}
