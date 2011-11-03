package com.andconsd;

import java.io.File;
import java.util.ArrayList;

import com.andconsd.constants.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity{

	Handler handler = new Handler();
	WelcomeActivity appref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appref = this;
		setContentView(R.layout.welcome);
		
		Constants.files = new ArrayList<File>();
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {

				final File rootFile = new File(Constants.ROOT_DIR);
				if (!rootFile.exists()) {
					rootFile.mkdirs();
				}
				File[] filess = rootFile.listFiles();

				for (File temp : filess) {
					Constants.files.add(temp);
				}
				
				startActivity(new Intent(appref,Androsd.class));

				finish();
			}
		});
	}

	
	
}
