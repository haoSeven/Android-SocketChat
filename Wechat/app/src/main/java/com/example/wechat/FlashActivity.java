package com.example.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class FlashActivity extends Activity {
private static int SPLASH_TIME_OUT = 2000;  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.flash);
		
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		})*/
		
		new Handler().postDelayed(new Runnable() {  
            public void run() {  
                // This method will be executed once the timer is over  
                // Start your app main activity  
                Intent i = new Intent(FlashActivity.this,LoginActivity.class);  
                startActivity(i);  
   
                // close this activity  
                finish();  
            }  
        }, SPLASH_TIME_OUT);  
		}
	

}
