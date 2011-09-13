package com.duole.player;

import android.os.Bundle;
import android.widget.VideoView;

import com.duole.R;
import com.duole.activity.PlayerBaseActivity;

public class VideoPlayerActivity extends PlayerBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.videoplayer);
		
		VideoView vvPlayer = (VideoView)findViewById(R.id.vvVideoPlayer);
		
		vvPlayer.setVideoPath("/sdcard/DuoleCache/video/ ”∆µ∫œ≥….avi");
	
		vvPlayer.start();
	}
	
	

	
	
}
