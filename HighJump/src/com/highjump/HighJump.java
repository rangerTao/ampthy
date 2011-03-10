package com.highjump;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.highjump.view.LoginView;

public class HighJump extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.setContentView(new BackGroundView(this,this.getResources()));
		this.setContentView(new LoginView(this));
    }
    
    public void changeView(View view){
    	setContentView(view);
    }
}