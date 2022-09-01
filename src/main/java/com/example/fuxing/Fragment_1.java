package com.example.fuxing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_1 extends Fragment {
    @Nullable
    public Button mcreat;
    public Button mjoin;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建Fragment的布局
        View view = inflater.inflate(R.layout.activity_fragment_1,container,false);

        mcreat=view.findViewById( R.id.btn_chuangjianhuiyi );
        mcreat.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),CreateActivity.class);
                startActivity( intent );
            }
        } );
        mjoin=view.findViewById( R.id.btn_jiaruhuiyi );
        mjoin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),JoinActivity.class);
                startActivity( intent );
            }
        } );
        return view;
    }
}