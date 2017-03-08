package com.example.wechat;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wechat.R;
import com.example.wechat.SetLayoutFragment;


public class About extends Activity implements OnClickListener {
	private TextView topBackBtn;
	private TextView topTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		topBackBtn = (TextView) findViewById(R.id.top_back_btn);
		topTitle = (TextView) findViewById(R.id.top_textView);

		topBackBtn.setText("<");
		topTitle.setText("关于");

		topBackBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.top_back_btn:
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				break;
		}
	}
}
