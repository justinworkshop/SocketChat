package com.example.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.client.model.ClientSocketModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtIp;
    private Button mBtnConnect;
    private TextView mTvReceiveText;
    private EditText mEtSendText;
    private Button mBtnSend;

    private ClientSocketModel clientSocketModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtIp = findViewById(R.id.ip);
        mBtnConnect = findViewById(R.id.connect);
        mBtnConnect.setOnClickListener(this);

        mEtSendText = findViewById(R.id.send_text);
        mBtnSend = findViewById(R.id.send);
        mBtnSend.setOnClickListener(this);

        mTvReceiveText = findViewById(R.id.text_receive_msg);

        clientSocketModel = new ClientSocketModel(handler, 7071);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect:
                String host = mEtIp.getText().toString();
                clientSocketModel.connect(host);
                break;
            case R.id.send:
                String msg = mEtSendText.getText().toString();
                clientSocketModel.sendMessage(msg);
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTvReceiveText.setText(msg.obj.toString());
        }
    };
}
