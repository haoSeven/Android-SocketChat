package com.example.wechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 80561 on 2016/12/14.
 */

public class MessageAdepter extends ArrayAdapter<TalkData>{
    private int resourceId;


    public MessageAdepter(Context context, int resource, List<TalkData> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        TalkData talkData = getItem(position);
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_msg_layout);
            viewHolder.leftImage = (ImageView) view.findViewById(R.id.left_msg_icon);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_msg_layout);
            viewHolder.rightImage = (ImageView) view.findViewById(R.id.right_msg_icon);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (talkData.getType() == TalkData.TYPE_RECEIVED){
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftImage.setImageResource(talkData.getImageView());
            viewHolder.leftMsg.setText(talkData.getTalkContent());
        }else if (talkData.getType() == TalkData.TYPE_SENT){
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightImage.setImageResource(talkData.getImageView());
            viewHolder.rightMsg.setText(talkData.getTalkContent());
        }

        return view;
    }

    class ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        ImageView leftImage;
        ImageView rightImage;
        TextView leftMsg;
        TextView rightMsg;
    }
}
