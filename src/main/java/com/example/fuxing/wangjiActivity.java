/*
package com.example.fuxing;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class wangjiActivity extends AppCompatActivity {
    EditText code;
    Button resultButton;
    String phone;//需要接受验证码的手机号码
    TextView regetTextviewCode;//再次获取验证码
    TextView log, timelog, phoneTextview;
    ImageView back;
    String APPKEY = "自己申请的appkey";
    String APPSECRET = "自己去官网申请密码";
    private String PASSWORD = "[a-zA-Z0-9]{6,16}";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_verification_code);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        initSMSSDK();
        SMSSDK.getVerificationCode("86", phone);//发送短信验证码到手机号
        initView();
        initData();
        timer.start();

    }

    private void initView() {
        regetTextviewCode = (TextView) findViewById(R.id.regetcode);
        back = (ImageView) findViewById(R.id.write_back);
        code = (EditText) findViewById(R.id.verfication_code_edittext);
        resultButton = (Button) findViewById(R.id.submit_verfication);
        phoneTextview = (TextView) findViewById(R.id.verfication_phone);
        log = (TextView) findViewById(R.id.write_log);
        timelog = (TextView) findViewById(R.id.submit_log);
    }

    private void initData() {
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wangjiActivity.this.finish();
            }
        } );
        phoneTextview.setText( "+86  " + phone );
        log.setText( Html.fromHtml( "我们已经发送<font color='#45C01A'>验证码</font>短信到整个号码:" ) );
        resultButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeString = code.getText().toString().trim();
                SMSSDK.submitVerificationCode( "86", phone, codeString );
            }
        } );

        regetTextviewCode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToast.showToast( wangjiActivity.this, "请求已发送" );
                SMSSDK.getVerificationCode( "86", phone );//发送短信验证码到手机号
                timer.start();
            }
        } );
    }


    */
/**
     * 使用计时器来限定验证码
     * 在发送验证码的过程 不可以再次申请获取验证码 在指定时间之后没有获取到验证码才能重新进行发送
     * 这里限定的时间是30s**//*

    private CountDownTimer timer = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timelog.setText(("接收短信大约需要" + millisUntilFinished / 1000) + "秒");
        }

        @Override
        public void onFinish() {
            regetTextviewCode.setEnabled(true);
            regetTextviewCode.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止使用短信验证 产生内存溢出问题
        SMSSDK.unregisterAllEventHandler();
    }


    private void initSMSSDK() {
        //初始化短信验证
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        //注册短信回调
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交成功

                    try {
                        if (data != null) {
                            HashMap<String, Object> hashMap = (HashMap) data;//提交验证码之后得到返回的数据（返回的数据是手机，和国家代码）
                            String getphone = (String) hashMap.get("phone");
                            if (getphone.equals(phone)) {
                                Message message = new Message();//验证成功
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                        } else {
                            Message message = new Message();//提交的验证码错误
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    } catch (Exception e) {
                        Message message = new Message();//提交的验证码错误
                        message.what = 1;
                        handler.sendMessage(message);
                    }

                    startActivity(new Intent(wangjiActivity.this,ModifyPassword.class));
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        boolean smart = (Boolean) data;
                        if (smart) {
                            Message message = new Message();//智能验证成功
                            message.what = 2;
                            handler.sendMessage(message);
                            new Handler() {
                                public void publish(LogRecord record) {

                                }

                                @Override
                                public void flush() {

                                }

                                @Override
                                public void close() throws SecurityException {

                                }
                            }.postDelayed( new Runnable() {
                                public void run() {
                                    startActivity(new Intent(wangjiActivity.this,ModifyPassword.class));
                                    wangjiActivity.this.finish();
                                }
                            }, 2000);


                        } else {

                        }
                    }
                } else {
                    Message message = new Message();//得到验证码错误
                    message.what = 3;
                    handler.sendMessage(message);
                }


            }
        });
    }

    */
/**
     * 需要开启一个主线程来显示提示**//*

    Handler handler = new Handler() {

        @Override
        public void publish(LogRecord record) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
        public void handleMessage(Message msg) {
            int code1 = msg.what;
            switch (code1) {
                case 0:
                    Toast.makeText(wangjiActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    //执行验证成功的操作
                    break;
                case 1:
                    ShowToast.showToast(wangjiActivity.this, "您提交的验证码有误");
                    break;
                case 2:
                    resultButton.setText("该号码为可信任号码");
                    ShowToast.showToast(wangjiActivity.this, "智能验证成功，即将为您跳转页面");
                    showDialog();
                    break;
                case 3:
                    ShowToast.showToast(wangjiActivity.this, "验证码获取失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };}*/
