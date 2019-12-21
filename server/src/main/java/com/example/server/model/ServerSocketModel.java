package com.example.server.model;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Copyright (C), 2016-2020
 * FileName: ServerSocketModel
 * Author: wei.zheng
 * Date: 2019/12/21 14:12
 * Description: ServerSocket 模型封装类
 */
public class ServerSocketModel {
    private ExecutorService executorService;
    private Handler handler;
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String receivedMsg;

    public ServerSocketModel(Handler handler, int port) {
        this.handler = handler;
        this.port = port;

        executorService = ThreadPoolFactory.getExecutorService();
    }

    public void openListen() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);

                    socket = serverSocket.accept();
                    System.out.println("Accept socket client: " + socket.getInetAddress() + ", port:" + socket.getLocalPort());

                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

                    sendMessage("Connect success");

                    while (socket.isConnected()) {
                        if ((receivedMsg = bufferedReader.readLine()) != null) {
                            receiveMessage();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("exception:" + e.getMessage());
                }
            }
        });
    }

    public void receiveMessage() {
        Message msg = new Message();
        msg.obj = receivedMsg;
        handler.sendMessage(msg);
    }

    public void sendMessage(final String msg) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket != null && socket.isConnected()) {
                        bufferedWriter.write(System.currentTimeMillis() + "-" + msg + "\n");
                        bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
