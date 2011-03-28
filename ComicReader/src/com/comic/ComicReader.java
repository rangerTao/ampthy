package com.comic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ComicReader extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final TextView mTextField = (TextView)findViewById(R.id.tv);
        
        Button btnStart = (Button)findViewById(R.id.btnStart);
        Button btnStop = (Button)findViewById(R.id.btnStop);
        
        btnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startService(new Intent("com.comic.MusicService"));
			}
        	
        });
        
        btnStop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				stopService(new Intent("com.comic.MusicService"));
				
			}
        	
        });
    }
}