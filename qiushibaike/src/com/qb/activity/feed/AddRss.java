package com.qb.activity.feed;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.qb.R;
import com.qb.qbMain;
import com.qb.parse.parseXML;

public class AddRss extends Activity{
	
	Button btnSaveButton;
	Button btnCancel;
	EditText etName;
	EditText etUrl;
	private qbMain qbtemp;
	private int id = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.addfeed);
		Bundle eindex = getIntent().getExtras();

		
		initContent();
		
		setButtonListener();
		
		if(eindex != null && eindex.getString("editIndex")!=null){
			int index  = Integer.parseInt(eindex.getString("editIndex"));
			qbtemp = qbMain.getApp();
			Log.v("test", index+"");
			Log.v("test", qbtemp.feedList[index].toString());
			etName.setText(qbtemp.feedList[index].split(";")[0].toString());
			etUrl.setText(qbtemp.feedList[index].split(";")[1].toString());
			id = Integer.parseInt(qbtemp.feedList[index].split(";")[2].toString());
		}

	}
	
	public void initContent() {
		btnSaveButton = (Button) findViewById(R.id.btnSave);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		etName = (EditText) findViewById(R.id.etName);
		etUrl = (EditText) findViewById(R.id.etUrl);
	}
	
	public void setButtonListener(){
		btnSaveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String name = etName.getText().toString();
				String url = etUrl.getText().toString();
				String input[] = {name,url,id+""};

				AssetManager aManager = getAssets();
				
				InputStream is;
				try {
					is = aManager.open("feed.xml");
					
					if(id == -1){
						parseXML.addNode(input);
					}else{
						parseXML.editNode(input);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				etName.setText("");
				etUrl.setText("");
				finish();				
			}
		});
	}
}
