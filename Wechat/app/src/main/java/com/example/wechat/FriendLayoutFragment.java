package com.example.wechat;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 *   朋友页面碎片
 */

public class FriendLayoutFragment extends Fragment{

    private View view;
    private List<TalkData> friend_List = new ArrayList<TalkData>();
    private ListView friend_listView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.friend_layout, container, false);
        initFriendData();
        FriendDataAdapter adapter = new FriendDataAdapter(getActivity(), R.layout.friend_item,friend_List);
        friend_listView = (ListView) view.findViewById(R.id.friend_listView);
        friend_listView.setAdapter(adapter);
        initEvent();
        return view;
    }
    private void initEvent(){
        friend_listView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TalkData friendData = friend_List.get(position);
                FriendMessage.actionStart(getActivity(), friendData.getImageView()
                        ,friendData.getTalkFriendName());
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
      }
    private void initFriendData(){
    	TalkData td1 = new TalkData(R.drawable.me, "lhr");
        friend_List.add(td1);
        TalkData td2 = new TalkData(R.drawable.me, "lh");
        friend_List.add(td2);
    }
}
