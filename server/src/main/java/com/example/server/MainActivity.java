package com.example.server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.server.model.NetUtils;
import com.example.server.model.ServerSocketModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvServerIp;
    private EditText mEtText;
    private Button mBtnSend;
    private TextView mTvReceiveMsg;

    private ServerSocketModel serverSocketModel;
    private int port = 7071;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvServerIp = findViewById(R.id.ip);
        mEtText = findViewById(R.id.send_text);
        mTvReceiveMsg = findViewById(R.id.text_receive_msg);
        mBtnSend = findViewById(R.id.send);
        mBtnSend.setOnClickListener(this);

        serverSocketModel = new ServerSocketModel(handler, port);
        serverSocketModel.openListen();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                sendMessage();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvServerIp.setText("IP: " + NetUtils.getIPAddress(this) + ":" + port);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTvReceiveMsg.setText(msg.obj.toString());
        }
    };

    private void sendMessage() {
        String msg = mEtText.getText().toString();
        serverSocketModel.sendMessage(msg);
    }
}
