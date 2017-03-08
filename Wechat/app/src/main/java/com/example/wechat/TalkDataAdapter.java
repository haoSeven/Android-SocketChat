package com.example.wechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 80561 on 2016/12/10.
 */

public class TalkDataAdapter extends ArrayAdapter<TalkData>{
    private int resource;

    public TalkDataAdapter(Context context, int resource, List<TalkData> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TalkData talkData = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.TalkImage = (ImageView) view.findViewById(R.id.head_icon);
            viewHolder.TalkName = (TextView) view.findViewById(R.id.talk_friend_name);
            viewHolder.lastTalkContent = (TextView) view.findViewById(R.id.last_talk);
            viewHolder.msgHint = (FrameLayout) view.findViewById(R.id.msg_hint);
            viewHolder.hintNum = (TextView) view.findViewById(R.id.msg_hint_num);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.TalkImage.setImageResource(talkData.getImageView());
        viewHolder.TalkName.setText(talkData.getTalkFriendName());
        viewHolder.lastTalkContent.setText(talkData.getTalkContent());
        if (talkData.getHintNum() != 0){
            viewHolder.msgHint.setVisibility(View.VISIBLE);
            viewHolder.hintNum.setText(talkData.getHintNum() + "");
        }else{
            viewHolder.msgHint.setVisibility(View.GONE);
        }

        return view;
    }

    class ViewHolder{
        ImageView TalkImage;
        TextView TalkName;
        TextView lastTalkContent;
        FrameLayout msgHint;
        TextView hintNum;
    }
}
