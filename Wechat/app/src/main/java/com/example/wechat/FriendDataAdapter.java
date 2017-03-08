package com.example.wechat;

import java.util.List;

import com.example.wechat.TalkDataAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendDataAdapter extends ArrayAdapter<TalkData>{
	 private int resource;

	    public FriendDataAdapter(Context context, int resource, List<TalkData> m_List) {
	        super(context, resource, m_List);
	        this.resource = resource;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        TalkData friendData= getItem(position);
	        View view;
	        ViewHolder viewHolder;
	        if(convertView == null){
	            view = LayoutInflater.from(getContext()).inflate(resource, null);
	            viewHolder = new ViewHolder();
	            viewHolder.FriendImage = (ImageView) view.findViewById(R.id.friend_head);
	            viewHolder.FriendName = (TextView) view.findViewById(R.id.friend_name);
	            view.setTag(viewHolder);
	        }else {
	            view = convertView;
	            viewHolder = (ViewHolder) view.getTag();
	        }
	        viewHolder.FriendImage.setImageResource(friendData.getImageView());
	        viewHolder.FriendName.setText(friendData.getTalkFriendName());

	        return view;
	    }

	    class ViewHolder{
	        ImageView FriendImage;
	        TextView FriendName;
	    }
}
