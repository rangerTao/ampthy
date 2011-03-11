package com.highjump;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.highjump.control.GData;
import com.highjump.view.GameView;
import com.highjump.view.LoginView;
import com.highjump.view.RecordView;

public class HighJump extends Activity {
    /** Called when the activity is first created. */
	
	Button btnStart;
	Button btnRecord;
	Button btnExit;
	Resources res;
	Context appRef;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		initButton();
    }
    
    public void initButton(){
    	res = this.getResources();
    	appRef = this.getApplicationContext();
    	btnStart = (Button)findViewById(R.id.btnStart);
		btnRecord = (Button)findViewById(R.id.btnRecord);
		btnExit = (Button)findViewById(R.id.btnExit);
		
		btnStart.setText(R.string.login_start);
		btnRecord.setText(R.string.login_record);
		btnExit.setText(R.string.login_exit);
		
		btnStart.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				GData.ingame = true;
				setContentView(new GameView(appRef,res));
			}
			
		});
		
		btnRecord.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				setContentView(new RecordView(appRef));
			}
			
		});
		
		btnExit.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				System.exit(0);
			}
			
		});
    }
    
    public void changeView(View view){
    	setContentView(view);
    }
}