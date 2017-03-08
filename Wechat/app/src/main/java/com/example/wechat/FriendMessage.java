package com.example.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendMessage extends Activity {
	 private TextView topBackBtn;
	 private TextView messageName;
	 private TextView topTitle;
	 private ImageView messageHead;
	 private Button btnSend;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.friend_message);
        initView();
        initEvent();
	}
	  private void initView(){
		  topBackBtn = (TextView) findViewById(R.id.top_back_btn);
		  messageName = (TextView) findViewById(R.id.message_name);
		  messageHead = (ImageView) findViewById(R.id.message_head);
		  btnSend = (Button) findViewById(R.id.message_send);
		  topTitle = (TextView) findViewById(R.id.top_textView);
	  }
	    private void initEvent(){
			topTitle.setText("详细信息");
	        messageName.setText(getIntent().getStringExtra("Name"));
			messageHead.setImageResource(getIntent().getIntExtra("Icon", 0));
	        topBackBtn.setText("<");
	        topBackBtn.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                finish();
	                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	            }
	        });
	        btnSend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					TalkingActivity.actionStart(FriendMessage.this,
							getIntent().getIntExtra("Icon", 0),
							getIntent().getStringExtra("Name"));
					finish();
				}
			});
	    }
	    public static void actionStart(Context context, int icon, String name){
	        Intent intent = new Intent(context, FriendMessage.class);
			intent.putExtra("Icon", icon);
			intent.putExtra("Name", name);
	        context.startActivity(intent);
	    }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}
}
