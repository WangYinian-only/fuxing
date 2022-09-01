package com.example.fuxing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fuxing.service.UserService;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity{

    EditText username;
    EditText password;
    EditText email;
    EditText phonenumber;
    EditText phonepass;
    EditText password2;
    private String phone_number;
    private String phone_pass;
    Button register;
    Button login;
    Button yanzheng;
    EventHandler eventHandler;
    private int time=60;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        phonenumber = findViewById( R.id.reg_phone ); //你的手机号
        phonepass = findViewById( R.id.reg_phone_pass );//你的验证码
        yanzheng = findViewById( R.id.reg_btn_yanzheng );//获取验证码按钮
        username = findViewById( R.id.reg_username );
        password = findViewById( R.id.reg_password );
        password2 = findViewById( R.id.reg_password2 );
        email = findViewById( R.id.reg_mail );
        register = findViewById( R.id.reg_btn_sure );
        login = findViewById( R.id.reg_btn_login );//登陆按钮
        register.setOnClickListener( new RegisterButton() );
        login.setOnClickListener( new RegisterButton() );
        yanzheng.setOnClickListener( new RegisterButton() );
        //初始化操作
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();//创建了一个对象
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                //handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler( eventHandler );//注册短信回调（记得销毁，避免泄露内存）
    }

    protected void onDestroy() {//销毁
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler=new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                if(result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    boolean smart = (Boolean)data;
                    if(smart) {
                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",
                                Toast.LENGTH_LONG).show();
                        phonenumber.requestFocus();//焦点
                        return;
                    }
                }
            }
            //回调完成
            if (result==SMSSDK.RESULT_COMPLETE){
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "验证码输入正确",
                            Toast.LENGTH_LONG).show();
                }
            }else {//其他出错情况
                if(flag)
                {
                    yanzheng.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    phonenumber.requestFocus();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                }

            }
        }

    };

    private boolean judPhone() {//判断手机号是否正确

        //不正确的情况
        if(TextUtils.isEmpty(phonenumber.getText().toString().trim()))
            //对于字符串处理Android为我们提供了一个简单实用的TextUtils类，如果处理比较简单的内容不用去思考正则表达式不妨试试这个在android.text.TextUtils的类，主要的功能如下:
            // 是否为空字符 boolean android.text.TextUtils.isEmpty(CharSequence str)
        {
            Toast.makeText(RegisterActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            phonenumber.requestFocus();
            //设置是否获得焦点。若有requestFocus()被调用时，后者优先处理。注意在表单中想设置某一个如EditText获取焦点，光设置这个是不行的，需要将这个EditText前面的focusable都设置为false才行。
            return false;
        }
        else if(phonenumber.getText().toString().trim().length()!=11)
        {
            Toast.makeText(RegisterActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            phonenumber.requestFocus();
            return false;
        }
        //正确的情况
        else
        {
            phone_number=phonenumber.getText().toString().trim();
            String num="[1][358]\\d{9}";
            if(phone_number.matches(num))
                return true;
            else
            {
                Toast.makeText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean judCord() {//判断验证码是否正确
        String name=username.getText().toString().trim();
        String pass=password.getText().toString().trim();
        String pass2=password2.getText().toString().trim();
        String em=email.getText().toString().trim();
        String phone=phonenumber.getText().toString().trim();
        Log.i("TAG",name+"_"+pass+"_"+em+"_"+phone);
        UserService uService=new UserService(RegisterActivity.this);
        User user=new User();
        user.setUsername(name);
        user.setPassword(pass);
        user.setEmail(em);
        user.setPhonenumber(phone);
        uService.register(user);
        //注册开始，判断注册条件
        judPhone();//先执行验证手机号码正确与否
        if(TextUtils.isEmpty(phonepass.getText().toString().trim()))//验证码
        {
            Toast.makeText(RegisterActivity.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            phonepass.requestFocus();//聚集焦点
            return flag=false;
        }
        else if(phonepass.getText().toString().trim().length()!=6)
        {
            Toast.makeText(RegisterActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            phonepass.requestFocus();
            return flag=false;
        }
        else
        {
            phone_pass=phonepass.getText().toString().trim();
            return flag=true;
        }
    }

    public class RegisterButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String phone_number=phonenumber.getText().toString().trim();//1
            String phone_pass=yanzheng.getText().toString().trim();//1
            String name=username.getText().toString().trim();
            String pass=password.getText().toString().trim();
            String pass2=password2.getText().toString().trim();
            String em=email.getText().toString().trim();
            String phone=phonenumber.getText().toString().trim();
            Log.i("TAG",name+"_"+pass+"_"+em+"_"+phone);
            UserService uService=new UserService(RegisterActivity.this);
            User user=new User();
            user.setUsername(name);
            user.setPassword(pass);
            user.setEmail(em);
            user.setPhonenumber(phone);
            uService.register(user);
            //注册开始，判断注册条件
            switch (v.getId()) {
                //注册开始，判断注册条件
                case R.id.reg_btn_sure:
                    if (TextUtils.isEmpty( name ))
                    {
                        Toast.makeText(RegisterActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty( pass )||TextUtils.isEmpty( pass2 ))
                    {
                        Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty( em )){
                        Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty( phone )) {
                        Toast.makeText(RegisterActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (TextUtils.equals(pass, pass2)) {
                            //执行注册操作
                            if(judCord())//判断验证码
                            {
                                SMSSDK.submitVerificationCode("86",phone_number,phone_pass);//提交手机号和验证码
                                if(flag=true){
                                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);}
                                else break;
                            }
                            //flag=false;
                        } else {
                            Toast.makeText(RegisterActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.reg_btn_login:
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.reg_btn_yanzheng:
                    if(judPhone())//去掉左右空格获取字符串，是正确的手机号
                    {
                        SMSSDK.getVerificationCode("86", phone_number);//获取你的手机号的验证码
                        phonepass.requestFocus();//判断是否获得焦点
                    }
                    //  获取后要提交你的验证码以判断是否正确，并登陆成功
            }
        }
    }
}