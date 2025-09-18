package com.example.test1;

import java.util.List;

import com.example.test1.R;
import com.example.test1.PeopleInfo;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MyAdapter extends BaseAdapter{
    private List<PeopleInfo> mData;
    private Context context;
    public void setmData(List mData) {
        this.mData = mData;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    //决定了列表item显示的个数
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }
    //根据position获取对应item的内容
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }
    //获取对应position的item的ID
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    //创建列表item视图
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View view=View.inflate(context, R.layout.adapter, null);
        //获取item对应的数据对象
        PeopleInfo people=mData.get(position);
        //初始化view
        ImageView ivAvatar=(ImageView) view.findViewById(R.id.iv_avatar);
        TextView tvUsername=(TextView) view.findViewById(R.id.tv_username);
        TextView tvMotto=(TextView) view.findViewById(R.id.tv_motto);
        //绑定数据到view

        Uri avatarUri = Uri.parse(people.getAvatar());
        ivAvatar.setImageURI(avatarUri);
        tvUsername.setText(people.getUsername());
        tvMotto.setText(people.getMotto());
        return view;
    }


}
