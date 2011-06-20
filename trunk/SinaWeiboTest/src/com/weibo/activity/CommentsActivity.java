package com.weibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;

import com.weibo.pojo.ViewGallery;
import com.weibo.pojo.adapter.HomeTimeLineAdapter;

public class CommentsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewGallery vg = new ViewGallery(this);
		vg.setAdapter(new HomeTimeLineAdapter());
		vg.setSpacing(0);
		//vg.setGravity(Gravity.CENTER_HORIZONTAL);
		//vg.setUnselectedAlpha(50);
		this.setContentView(vg);
	}
}