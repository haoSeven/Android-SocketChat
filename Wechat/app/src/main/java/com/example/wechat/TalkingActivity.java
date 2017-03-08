package com.example.wechat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 80561 on 2016/12/11.
 */

public class TalkingActivity extends Activity {

    private TextView topBackBtn;
    private TextView topTitle;
    private EditText editMsg;
    private Button btnSend;
    private static ListView msgListView;
    private static MessageAdepter messageAdepter;
    private static List<TalkData> msgList = new ArrayList<TalkData>();
    private SendMessage sendMessage;
    private String me;
    private String sentToWho;
    private int headIcon;
    private SharedPreferences sharedPreferences;
    private MyOpenHelper dbHelper;                         //定义数据库
    private SQLiteDatabase db;     //数据库
    public static boolean isOnStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_talking);
        initMsg();
        initView();
        initEvent();
    }

    private void initMsg() {
        sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        me = sharedPreferences.getString("account", "");
        sentToWho = getIntent().getStringExtra("Name");
        headIcon = getIntent().getIntExtra("Icon", 0);
        messageAdepter = new MessageAdepter(TalkingActivity.this, R.layout.msg_item, msgList);
        dbHelper=new MyOpenHelper(TalkingActivity.this, me + sentToWho +".db", null, 1, 1);
        db = dbHelper.getReadableDatabase();

       
    }

    private void initView(){
        topBackBtn = (TextView) findViewById(R.id.top_back_btn);
        topTitle = (TextView) findViewById(R.id.top_textView);
        editMsg = (EditText) findViewById(R.id.edit_message);
        btnSend = (Button) findViewById(R.id.send_message);
        msgListView = (ListView) findViewById(R.id.message_list);
        
        msgListView.setAdapter(messageAdepter);
        isOnStart = true;
        // 读取聊天记录
        Cursor cursor = db.rawQuery(
				"select message,type from messageTable", null);
        if(cursor !=null){
        	while(cursor.moveToNext()){
        		String message=cursor.getString(cursor.getColumnIndex("message"));
        		int type=cursor.getInt(cursor.getColumnIndex("type"));
        		TalkingActivity.setChang(message, type);
        	}
        	cursor.close();  
        }
    }

    private void initEvent(){
        topTitle.setText(getIntent().getStringExtra("Name"));
        topBackBtn.setText("<");
        topBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                editor.putInt("CurrentItem", 0);
                editor.putInt(sentToWho + "hint", 0);
                editor.putBoolean("Change", true);
                editor.commit();

                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editMsg.getText().toString();

                if (!"".equals(content)){
                	// 将发送内容存入数据库
                	ContentValues values = new ContentValues();
    				values.put("name", me);
    				values.put("message", content);
    				values.put("type", TalkData.TYPE_SENT);
    				db.insert("messageTable", null, values);
    				values.clear();

                    // 将信息显示在聊天框中
                    setChang(content, TalkData.TYPE_SENT);
                    editMsg.setText("");

                    // 发送信息
                    sendMessage = new SendMessage(me, content, sentToWho);
                    sendMessage.sendMsg();

                    // 将信息返回聊天列表中
                    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    editor.putBoolean("Change", true);
                    editor.putString("Content", content);
                    editor.putString("SentTo", sentToWho);
                    editor.putInt("headIcon", headIcon);
                    editor.commit();
                }
            }
        });
    }

    public static void setChang(String content, int type){
        TalkData data = new TalkData(R.drawable.me, content, type);
        msgList.add(data);
        messageAdepter.notifyDataSetChanged();
        msgListView.setSelection(msgList.size());
    }

    public static void actionStart(Context context,int headIcon, String name){
        Intent intent = new Intent(context, TalkingActivity.class);
        intent.putExtra("Icon", headIcon);
        intent.putExtra("Name", name);

        context.startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch(item.getItemId()){
    	case R.id.delect_message:
    		dbHelper.deleteDatabase(TalkingActivity.this);
    	}
    	
    	return true;
    	
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
        editor.putInt("CurrentItem", 0);
        editor.putInt(sentToWho + "hint", 0);
        editor.putBoolean("Change", true);
        editor.commit();

        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOnStart = false;
        msgList.clear();
    }
}
