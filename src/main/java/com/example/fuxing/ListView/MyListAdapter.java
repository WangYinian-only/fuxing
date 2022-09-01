package com.example.fuxing.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fuxing.R;
import com.example.fuxing.widget.RoundImageView;

public class MyListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    //构造方法
    public MyListAdapter(Context context){
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    static class ViewHolder{
        public RoundImageView imageView;
        public ImageView tvContent;
        public TextView tvTitle,tvTime;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_list_item,null);
            holder = new ViewHolder();
            holder.imageView=convertView.findViewById(R.id.iv);
            //holder.tvTitle=convertView.findViewById(R.id.tv_title);
            holder.tvTime=convertView.findViewById(R.id.tv_time);
            holder.tvContent=convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        //给控件赋值
        //holder.tvTitle.setText("标题");
        holder.tvTime.setText("内容");
        Glide.with( mContext ).load( "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fs9.sinaimg.cn%2Fbmiddle%2F5ceba31bg5d6503750788&refer=http%3A%2F%2Fs9.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617702100&t=70f3f864439d2b47f45258fcc00fd2f8").into(holder.tvContent);
        Glide.with(mContext).load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fs9.sinaimg.cn%2Fbmiddle%2F5ceba31bg5d6503750788&refer=http%3A%2F%2Fs9.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617702100&t=70f3f864439d2b47f45258fcc00fd2f8").into(holder.imageView);
        return convertView;
    }
}
