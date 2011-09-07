package com.duole.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.duole.R;

public class SystemConfigActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemconfig);
		
		ListView lvConfig = (ListView)findViewById(R.id.lvConfig);
		
		ArrayList<String> simple = new ArrayList<String>();
		
		simple.add("test");
		simple.add("test2");
	}
	
}
