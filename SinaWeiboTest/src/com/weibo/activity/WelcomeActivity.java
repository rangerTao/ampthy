package com.weibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.weibo.R;

public class WelcomeActivity extends Activity{

	Button btnNext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.welcome_view);
		
		btnNext = (Button)findViewById(R.id.btnNext);
		
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
			}
		});
	}
	
}
