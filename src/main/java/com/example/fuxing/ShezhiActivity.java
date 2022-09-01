package com.example.fuxing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ShezhiActivity extends AppCompatActivity {

    private Button mBtnTui,mBtnQie;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shezhi );

        mBtnTui=findViewById( R.id.btn_tuichu );
        mBtnTui.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShezhiActivity.this,LoginActivity.class);
                startActivity( intent );
            }
        } );

        mBtnQie=findViewById( R.id.btn_qiehuan );
        mBtnQie.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShezhiActivity.this,LoginActivity.class);
                startActivity( intent );
            }
        } );


    }
}
