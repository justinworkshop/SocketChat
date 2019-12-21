package com.example.client.model;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

/**
 * Copyright (C), 2016-2020
 * FileName: ClientSocketModel
 * Author: wei.zheng
 * Date: 2019/12/21 14:47
 * Description: Socket 客户端模型封装类
 */
public class ClientSocketModel {
    private Handler handler;
    private int port;
    private ExecutorService executorService;

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String receivedMsg;

    public ClientSocketModel(Handler handler, int port) {
        this.handler = handler;
        this.port = port;

        executorService = ThreadPoolFactory.getExecutorService();
    }

    public void connect(final String host) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(host, port);
                    socket.setTcpNoDelay(true);
                    socket.sendUrgentData(0x44);
                    socket.setSoTimeout(5000);

                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

                    while (socket.isConnected()) {
                        if ((receivedMsg = bufferedReader.readLine()) != null) {
                            receiveMessage();
                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void receiveMessage() {
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
