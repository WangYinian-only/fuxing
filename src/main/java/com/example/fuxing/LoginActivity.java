package com.example.fuxing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*import com.bkk.library.QQLoginManager;*/
import com.example.fuxing.service.UserService;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

public class LoginActivity extends AppCompatActivity /*implements View.OnClickListener*/ {

    private String APP_ID = "101950523";//官方获取的APPID
    private Tencent mTencent;
    private IUiListener listener;
    private UserInfo mUserInfo;
    private CheckBox rem_pw;
    private CheckBox auto_login;
    private EditText username;
    private EditText password;
    private Button login;
    private TextView register;
    private SharedPreferences sp;
    private String Scope = "all";
    private static final int SUCCESS = 0;
    private static final int FAILED = 1;
    private TextView btn_LoginQQ;
    private TextView user_nickname;
    private ImageView user_photo;
    private String nickName , figureurl;

    @SuppressLint("WrongConstant")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );//即activity_login.xml
        /*mTencent = Tencent.createInstance("101950523", this.getApplicationContext());*/
        /*initView();*/
        /* 初始化视图 */


        /*QQLoginManager.setQQLoginListener( new QQLoginManager.QQLoginListener() {
            @Override
            public void onQQLoginSuccess(JSONObject jsonObject) {
                // 登录成功
            }

            @Override
            public void onQQLoginCancel() {
                // 登录取消
            }

            @Override
            public void onQQLoginError(UiError uiError) {
                // 登录出错
            }
        });*/

        sp = this.getSharedPreferences( "userInfo", Context.MODE_PRIVATE );
            username = findViewById( R.id.username );
            password = findViewById( R.id.password );
            login = findViewById( R.id.btn_login );
            register = findViewById( R.id.btn_register );
            rem_pw = findViewById( R.id.checkbox_text );
            auto_login = findViewById( R.id.checkbox_text );
            btn_LoginQQ=findViewById( R.id.btn_LoginQQ );

        /*Map<String, String> map=User.Get(this);
        if(map!=null){
            username.setText(map.get("user"));
            password.setText(map.get("key"));
        }*/

       /* btn_LoginQQ.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_LoginQQ:
                            QQLoginManager.login( LoginActivity.this);
                            break;
                    }
            }
        } );*/





        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            rem_pw.setChecked(false);
            username.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动登录状态
                auto_login.setChecked(false);
                //跳转界面
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(intent);

            }
        }

        //登录事件
            login.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = username.getText().toString();
                    System.out.println( name );
                    String pass = password.getText().toString();
                    System.out.println( pass );

                    Log.i( "TAG", name + "_" + pass );
                    UserService uService = new UserService( LoginActivity.this );
                    boolean flag = uService.login( name, pass );

                    if (flag) {
                        Log.i( "TAG", "登录成功" );
                        Toast.makeText( LoginActivity.this, "登录成功", Toast.LENGTH_LONG ).show();
                        //登录成功和记住密码框为选中状态才保存用户信息
                        if(rem_pw.isChecked())
                        {
                            //记住用户名、密码、
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_NAME", name);
                            editor.putString("PASSWORD", pass);
                            editor.commit();
                        }
                        //跳转界面
                        Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                        startActivity( intent );
                    }
                    else if(TextUtils.isEmpty( name ) || TextUtils.isEmpty( pass )) {
                        Toast.makeText( LoginActivity.this, "密码或账号不能为空", Toast.LENGTH_SHORT ).show();
                    }
                    else {
                        Log.i( "TAG", "登录失败" );
                        Toast.makeText( LoginActivity.this, "登录失败", Toast.LENGTH_LONG ).show();
                    }
                }
            } );

        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (rem_pw.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

        //注册事件
        register.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent( LoginActivity.this, RegisterActivity.class );
                    startActivity( intent );
                }
            } );
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 回调
        QQLoginManager.onActivityResultData(requestCode, resultCode, data);
    }




  /*  private void initView() {
        btn_LoginQQ=findViewById(R.id.btn_LoginQQ);
        btn_LoginQQ.setOnClickListener(this);
       *//* user_photo= findViewById(R.id.user_logo);
        user_nickname=findViewById(R.id.user_nickname);*//*
    }*/



/*
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    String nick=(String) msg.obj;
                    user_nickname.setText(nick);
                    break;
                case FAILED:
                    Toast.makeText(LoginActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    };



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_LoginQQ:
                login();
                break;
        }
    }

//QQ第三方登录实现
public void Login(View v) {
    *//**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
     官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
     第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类
    mTencent = Tencent.createInstance( "101950523" , this.getApplicationContext());
    if(!mTencent.isSessionValid()){
        listener=new BaseUiListener(){
            @Override
            public void onComplete(Object object) {
                JSONObject json = (JSONObject) object;
                getUserInfoByVolley(json);
            }
        };
        mTencent.login(this, "all", listener);

    }
}

    /**
     * 使用Volley解析json数据
     * @param json
     *//*

    protected void getUserInfoByVolley(JSONObject json) {
        try {
            String openid=json.getString("openid");
            String access_token=json.getString("access_token");
            Log.e("TAG--->Openid",openid);
            Log.e("TAG--->Openid",access_token);

            String url="http://119.147.19.43/v3/user/get_info?openid="+openid+"&openkey="+access_token+"&pf=qzone&appid=1104812858&format=json&userip=10.0.0.1&sig=C3BGTm24S%2FZJdt1J%2BjfEzRpCLWA%3D";
            *//**
             * 使用Volley框架得到json数据
             * tag : https://github.com/adamrocker/volley
             *//*

            RequestQueue requestQueue=Volley.newRequestQueue(LoginActivity.this); //用于获取一个Volley的请求对象
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest( Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject json) {
                            *//**
                             * 解析json数据
                             *//*
                            try {
                                nickName=json.getString("nickname");  //昵称
                                figureurl=json.getString("figureurl");  //头像的url

                                //TODO 通过异步任务处理图片
                                new NewAsyncTask().execute(figureurl);
                                //

                                mHandler.obtainMessage(SUCCESS, nickName).sendToTarget();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError arg0) {
                            Log.e("error","问题~~");
                            mHandler.obtainMessage(FAILED).sendToTarget();
                        }
                    });
            requestQueue.add(jsonObjectRequest); //在请求队列中加入当前的请求

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    *//**
     * 异步任务加载图片
     * @author monster
     *//*
    class NewAsyncTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            String url=params[0];
            return getBitmapByUrl(url);
        }
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            user_photo.setImageBitmap(result);
        }

    }

    *//**
     * 通过url 得到bitmap对象
     * @param urlString
     * @return
     *//*
    public Bitmap getBitmapByUrl(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url=new URL(urlString);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(connection.getInputStream());
            bitmap= BitmapFactory.decodeStream(is);
            connection.disconnect(); //关闭连接
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/

}