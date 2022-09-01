package com.example.fuxing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fuxing.widget.ItemGroup;

public class Fragment_3 extends Fragment {
    ItemGroup ziliao;
    ItemGroup xiaoxi;
    ItemGroup shezhi;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建Fragment的布局
        View view = inflater.inflate(R.layout.activity_fragment_3,container,false);
        ziliao=view.findViewById(R.id.ig_ziliao);
        ziliao.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserActivity.class);
                startActivity( intent );
            }
        } );

        xiaoxi=view.findViewById(R.id.ig_xiaoxi);
        xiaoxi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),XiaoxiActivity.class);
                startActivity( intent );
            }
        } );

        shezhi=view.findViewById(R.id.ig_shezhi);
        shezhi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ShezhiActivity.class);
                startActivity( intent );
            }
        } );


        return view;


    }

}
