package com.example.wechat;

/**
 *  @项目名称：简单聊
 *  @作者：浩然，皓哥
 *  @version：1.0
 *  增加：
 *  	增加动画，删除数据库
 *      能显示未读信息提示
 *      将接受到的消息显示出来
 *  	读取消息提示
 *      即时显示聊天内容
 *  	保存聊天记录
 *  问题：
 *      启动画面
 *      美化界面
 *      启动加载聊天列表
 *
 *  项目创建时间：2016年12月7日22:28:59
 *  最后更新时间：2016年12月22日21:21:46
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private TextView topTextView;
    private LinearLayout btnTalk;
    private ImageView btnTalkImage;
    private LinearLayout btnFriend;
    private ImageView btnFriendImage;
    private LinearLayout btnSet;
    private ImageView btnSetImage;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    SharedPreferences sharedPreferences;
    
    private MyOpenHelper dbHelper;                         //定义数据库
    private SQLiteDatabase db;     						//数据库

    public static Socket socket;
    //服务器server/IP地址
    private final String ADDRESS = "192.168.252.1";
    private String serverAddress;
    //服务器端口
    private final int PORT = 22236;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String msg = "";
    private String who = "";
    private String me="";
    private int count = 0;

    private Fragment mTab01 = new TalkLayoutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        WifiManager wifimanager=(WifiManager)getSystemService(Context.WIFI_SERVICE);//获取WifiManager
        //检查wifi是否开启
        if(!wifimanager.isWifiEnabled())  {
            wifimanager.setWifiEnabled(true);
        }
        DhcpInfo di = wifimanager.getDhcpInfo();
        serverAddress = intToIp(di.serverAddress);

        initSocket();
        initView();
        initViewPage();
        initEvent();
    }
    //将获取的int转为真正的ip地址,参考的网上的，修改了下
    private String intToIp(int i)  {
        return (i & 0xFF)+ "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) +"."+((i >> 24 ) & 0xFF );
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message m) {
            super.handleMessage(m);
            msg = m.getData().getString("Msg", "");
            int hint = sharedPreferences.getInt(who + "hint", 0);
            hint += 1;
            if (TalkingActivity.isOnStart){
                TalkingActivity.setChang(msg, TalkData.TYPE_RECEIVED);
            }

            dbHelper=new MyOpenHelper(MainActivity.this, me + who +".db", null, 1, 1);
            db = dbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
			values.put("name", who);
			values.put("message", msg);
			values.put("type", TalkData.TYPE_RECEIVED);
			db.insert("messageTable", null, values);
			values.clear();
            
            SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
            editor.putBoolean("Change", true);
            editor.putString("Content", msg);
            editor.putString("SentTo", who);
            editor.putInt("headIcon", R.drawable.me2);
            editor.putInt(who + "hint", hint);
            editor.commit();
            mTab01.onResume();
        }
    };

    private void initSocket() {
    	sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
    	me=sharedPreferences.getString("account", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(serverAddress, PORT);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!socket.isClosed()){
                    if (socket.isConnected()) {
                        if (!socket.isOutputShutdown()) {
                            out.println(me);
                        }
                    }
                }
                String me = sharedPreferences.getString("account", "");
                Log.d("hah", me);
                try {
                    while (socket.isClosed() == false && socket.isConnected() == true){
                        if (!socket.isInputShutdown()){
                            if ((msg = in.readLine()) != null){
                                if (count == 0){
                                    who = msg;
                                    count++;
                                }else if (count == 1){
                                    Message m = new Message();
                                    Bundle data = new Bundle();
                                    data.putString("Msg", msg);
                                    m.setData(data);
                                    handler.sendMessage(m);
                                    count = 0;
                                }
                            }
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(){
            public void run(){
                if (!socket.isClosed()){
                    if (socket.isConnected()) {
                        if (!socket.isOutputShutdown()) {
                            out.println("exit");
                            try {
                                in.close();
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        }.start();
    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.show_viewpager);
        btnTalk = (LinearLayout) findViewById(R.id.talk_button);
        btnFriend = (LinearLayout) findViewById(R.id.friend_button);
        btnSet = (LinearLayout) findViewById(R.id.set_button);
        topTextView = (TextView) findViewById(R.id.top_textView);
        btnTalkImage = (ImageView) findViewById(R.id.talk_button_icon);
        btnFriendImage = (ImageView) findViewById(R.id.friend_button_icon);
        btnSetImage = (ImageView) findViewById(R.id.set_button_icon);
    }

    private void initViewPage(){
    //    Fragment mTab01 = new TalkLayoutFragment();
        Fragment mTab02 = new FriendLayoutFragment();
        Fragment mTab03 = new SetLayoutFragment();

        mFragments.add(mTab01);
        mFragments.add(mTab02);
        mFragments.add(mTab03);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        mViewPager.setAdapter(mAdapter);
    }

    private void initEvent(){
        topTextView.setText("聊天");
        btnTalkImage.setImageResource(R.drawable.chat);
        btnTalk.setOnClickListener(this);
        btnFriend.setOnClickListener(this);
        btnSet.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.talk_button:
                mViewPager.setCurrentItem(0);
                resetBtn();
                topTextView.setText("聊天");
                btnTalkImage.setImageResource(R.drawable.chat);
                break;
            case R.id.friend_button:
                mViewPager.setCurrentItem(1);
                resetBtn();
                topTextView.setText("好友");
                btnFriendImage.setImageResource(R.drawable.friend);
                break;
            case R.id.set_button:
                mViewPager.setCurrentItem(2);
                resetBtn();
                topTextView.setText("我");
                btnSetImage.setImageResource(R.drawable.me);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        int currentItem = sharedPreferences.getInt("CurrentItem", -1);
        if (currentItem == 0) {
            mViewPager.setCurrentItem(currentItem);
            resetBtn();
            topTextView.setText("聊天");
            btnTalkImage.setImageResource(R.drawable.chat);
        }
    }

    /**
     * 把所有按键重设
     */
    private void resetBtn() {
        btnTalkImage.setImageResource(R.drawable.chat2);
        btnFriendImage.setImageResource(R.drawable.friend2);
        btnSetImage.setImageResource(R.drawable.me2);
    }
}
