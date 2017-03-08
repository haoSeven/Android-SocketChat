package com.example.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天页面碎片
 */

public class TalkLayoutFragment extends Fragment{

    private View view;

    private List<TalkData> mList = new ArrayList<TalkData>();
    private ListView listView;
    private TextView textView;

    private TalkDataAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.talk_layout, container, false);
        adapter = new TalkDataAdapter(getActivity(), R.layout.talk_item, mList);
        listView = (ListView) view.findViewById(R.id.talk_listView);
        listView.setAdapter(adapter);

        initEvent();

        return view;
    }

    private void initEvent(){
      listView.setOnItemClickListener(new OnItemClickListener(){

          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              TalkData talkData = mList.get(position);
              TalkingActivity.actionStart(getActivity(), talkData.getImageView(), talkData.getTalkFriendName());
              getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
          }
      });
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        boolean change = sharedPreferences.getBoolean("Change", false);
        if (change){
            String content = sharedPreferences.getString("Content", "");
            String name = sharedPreferences.getString("SentTo", "");
            int headIcon = sharedPreferences.getInt("headIcon", 0);
            int hint = sharedPreferences.getInt(name + "hint", 0);
            boolean noFound = true;
            if (mList.isEmpty()){
                TalkData newTalkData = new TalkData(headIcon, name, content, hint);
                mList.add(0, newTalkData);
                noFound = false;
            } else {
               for (int i = 0; i < mList.size(); i++){
                    TalkData talkData = mList.get(i);
                    if (talkData.getTalkFriendName().equals(name)){
                        mList.remove(talkData);
                        TalkData newTalkData = new TalkData(headIcon, name, content, hint);
                        mList.add(0, newTalkData);
                        noFound = false;
                        break;
                    }
                }
            }
            if (noFound){
                TalkData newTalkData = new TalkData(headIcon, name, content, hint);
                mList.add(0, newTalkData);
            }
            adapter.notifyDataSetChanged();
            SharedPreferences.Editor editor = getActivity().
                    getSharedPreferences("Data", Context.MODE_PRIVATE).edit();
            editor.putBoolean("Change", false);

            editor.commit();
        }
    }
}
