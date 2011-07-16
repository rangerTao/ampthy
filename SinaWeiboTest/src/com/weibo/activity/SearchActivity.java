package com.weibo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.weibo.BaseActivity;
import com.weibo.R;

public class SearchActivity extends BaseActivity{

	Spinner spSearchType;
	Button btnSearch;
	EditText etSearchContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchall);
		
		spSearchType = (Spinner)findViewById(R.id.spChoose);
		btnSearch = (Button)findViewById(R.id.btnSearch);
		etSearchContent = (EditText)findViewById(R.id.etSearch);
		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				switch(spSearchType.getSelectedItemPosition()){
				case 0:
					Log.v("TAG", "1");
					break;
				case 1:
					Log.v("TAG", "2");
					break;
				case 2:
					Log.v("TAG", "4");
					break;
				}
			}
		});
	}

	
}
