package com.weibo.activity;

import com.weibo.R;
import com.weibo.pojo.ViewGallery;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;

public class FriendsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.gallery);
		
		ViewGallery vg = (ViewGallery)findViewById(R.id.gallery);
//		vg.setAdapter(new HomeTimeLineAdapter());
	}
	
	

}
