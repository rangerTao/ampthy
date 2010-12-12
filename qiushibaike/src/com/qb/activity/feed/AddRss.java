package com.qb.activity.feed;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.qb.R;
import com.qb.parse.parseXML;

public class AddRss extends Activity{
	
	Button btnSaveButton;
	Button btnCancel;
	EditText etName;
	EditText etUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.addfeed);
		
		initContent();
		
		setButtonListener();

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
				String input[] = {name,url};
				parseXML.addNode(input);
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
