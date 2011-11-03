/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.andconsd;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andconsd.adapter.PicViewAdapter;
import com.andconsd.constants.Constants;
import com.andconsd.widget.DuoleCountDownTimer;
import com.andconsd.widget.DuoleVideoView;
import com.andconsd.widget.FlingGallery;


public class PicViewer extends Activity implements OnTouchListener{

	private int mIndex;

	public static PicViewer appref;
	private int mItemwidth;
	private int mItemHerght;

	DuoleVideoView vv;
	private ArrayList<String> pathes;

	private ProgressBar mProgressBar;
	DuoleCountDownTimer autoPlayerCountDownTimer;
	
	int screen_off_timeout = 0;

	private Bitmap zoomBitmap;
	
	PicViewAdapter pva;

	private FlingGallery mFlingGallery;
	
	static RelativeLayout rlController;
	RelativeLayout rlSlidShow;
	ImageView ivSlidShow;
	public Handler handler = new Handler();
	
	RotateAnimation rotate; 
	ScaleAnimation scale; 
	AlphaAnimation alpha; 
	TranslateAnimation translate;
	
	boolean slidshow = false;
	TextView tvIndex;

	public int getmIndex() {
		return mIndex;
	}


	public void updateState(int visibility) {
		mProgressBar.setVisibility(visibility);
		mFlingGallery.setCanTouch(View.GONE == visibility);
	}
	
	public void updateIndex(int index){
		if (tvIndex != null) {
			tvIndex.setText(index + 1 + "/"
					+ Constants.files.size());
		}
	}


	private boolean isViewIntent() {
		String action = getIntent().getAction();
		return Intent.ACTION_VIEW.equals(action);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appref = this;
		Intent intent = getIntent();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mItemwidth = dm.widthPixels;
		mItemHerght = dm.heightPixels;
		// mInflater = LayoutInflater.from(this);
		if (!isViewIntent()) {
			pathes = intent.getStringArrayListExtra("pathes");
			mIndex = intent.getIntExtra("index", 0);
		} else {
			pathes = new ArrayList<String>();
			pathes.add(intent.getData().getPath());
			mIndex = 0;
		}
		
		try {
			screen_off_timeout = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 1000 * 60 * 4);

		setContentView(R.layout.picview);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
		mFlingGallery = (FlingGallery) findViewById(R.id.horizontalview);

		// hsv = (MyHorizontalScrollView)
		
		pva = new PicViewAdapter(Constants.files,mItemwidth,mItemHerght);
		mFlingGallery.setAdapter(pva, mIndex);
		
		mFlingGallery.setClickable(true);
		initPopupController();
		
		initAutoPlayCountDown();
	}
	
	private void initAutoPlayCountDown(){

		autoPlayerCountDownTimer = new DuoleCountDownTimer(Constants.apCountDown,1000) {
			
			@Override
			public void onTick(long millisUntilFinished, int percent) {
			}
			
			@Override
			public void onFinish() {
				//Start to auto play

				mFlingGallery.setVisibility(View.INVISIBLE);
				rlController.setVisibility(View.INVISIBLE);
				
				new Thread(){

					@Override
					public void run() {
						initAnimations();
						
						rlSlidShow = (RelativeLayout) findViewById(R.id.rlSlidShow);
						rlSlidShow.setOnTouchListener(appref);
						
						ivSlidShow = (ImageView) findViewById(R.id.ivSlidShow);
						slidshow = true;
						
						final BitmapFactory.Options options = new Options();
						options.inSampleSize = 3;
						
						while(slidshow){
							
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									rlSlidShow.setVisibility(View.VISIBLE);
									
									try{
//										ivSlidShow.setImageURI(Uri
//												.parse(Androsd.appref.files.get(getNextIndex())
//														.getAbsolutePath()));
										ivSlidShow.setImageBitmap(getDrawable(Constants.files.get(getNextIndex())
												.getAbsolutePath()));
									}catch(Exception e){
										System.gc();
										ivSlidShow.setImageURI(Uri
												.parse(Constants.files.get(getNextIndex())
														.getAbsolutePath()));
									}
									
									
									int anIndex = 0;
									anIndex = new Random().nextInt() % 4;
									anIndex = Math.abs(anIndex);
									switch (anIndex) {
									case 0:
										ivSlidShow.startAnimation(rotate);
										break;
									case 1:
										ivSlidShow.startAnimation(scale);
										break;
									case 2:
										ivSlidShow.startAnimation(alpha);
										break;
									case 3:
										ivSlidShow.startAnimation(translate);
										break;
									default:
										break;
									}
								}
							});
							
							try {
								Thread.sleep(15000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						resumeCountDown();
					}
					
				}.start();
				
			}
		};
		
		autoPlayerCountDownTimer.start();
	}

	@Override
	protected void onDestroy() {
		if (zoomBitmap != null) {
			zoomBitmap.recycle();
		}
		android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, 1000 * 60 * 4);
		super.onDestroy();
	}

	public static void showHideController() {
		
		if(rlController.getVisibility() == View.VISIBLE){
			rlController.setVisibility(View.INVISIBLE);
		}else{
			rlController.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void playVideo(int position){
		
		String pathString = Constants.files.get(mFlingGallery.getCurrentPosition()).getAbsolutePath();
		if(pathString.endsWith(".mp4") || pathString.endsWith(".3gp")){
			mFlingGallery.setVisibility(View.INVISIBLE);
			autoPlayerCountDownTimer.pause();

			vv = (DuoleVideoView) findViewById(R.id.vv);
			vv.videoAutoPlay();
			vv.setVideoPath(pathString);
			vv.setVisibility(View.VISIBLE);
			
			vv.setMediaController(new MediaController(appref));
		}
	}
	
	private void initPopupController() {

		rlController = (RelativeLayout)findViewById(R.id.rlController);
		Button btnBack = (Button) findViewById(R.id.btnBack);

		tvIndex = (TextView) findViewById(R.id.tvIndex);
		tvIndex.setTextColor(Color.WHITE);
		tvIndex.setText(mIndex + 1 + "/"
				+ Constants.files.size());
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(vv!=null && vv.isPlaying()){
					if (vv != null && vv.isPlaying()) {
						vv.pause();
						vv.setVisibility(View.INVISIBLE);

						mFlingGallery.setVisibility(View.VISIBLE);
						rlController.bringToFront();
						
						autoPlayerCountDownTimer.resume();
					}
				}else{
					appref.finish();
				}
				
			}
		});
	}


	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		autoPlayerCountDownTimer.seek(0);
		
		if(rlSlidShow != null && rlSlidShow.getVisibility() == View.VISIBLE){
			mFlingGallery.setVisibility(View.VISIBLE);
			rlController.setVisibility(View.VISIBLE);
			ivSlidShow.setVisibility(View.INVISIBLE);
			rlSlidShow.setVisibility(View.INVISIBLE);
			slidshow = false;
		}

		return false;
	}
	
	private void resumeCountDown(){
		autoPlayerCountDownTimer.seek(0);
		autoPlayerCountDownTimer.start();
	}
	
	private void initAnimations(){
		
		rotate = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF,0.5f, RotateAnimation.RELATIVE_TO_SELF,0.5f ); 
		scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF,0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f ); 
		alpha = new AlphaAnimation(0, 1); 
		
		rotate.setFillAfter(true); 
		rotate.setDuration(1000); 
		
		scale.setFillAfter(true); 
		scale.setDuration(500); 
		scale.setStartOffset(500); 
		
		alpha.setFillAfter(true); 
		alpha.setDuration(500); 
		alpha.setStartOffset(500); 

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		translate = new TranslateAnimation(dm.widthPixels, 0, dm.heightPixels, 0);
		translate.setFillAfter(true);
		translate.setDuration(500);
		translate.setStartOffset(500);
	}
	
	private int getNextIndex(){
		int resIndex = new Random().nextInt() % Constants.files.size();
		resIndex = Math.abs(resIndex);
		if(resIndex > Constants.files.size() -1){
			resIndex = 0;
		}
		return resIndex;
	}
	
	private Bitmap getDrawable(String path) {
		if (path == null || path.length() < 1)
			return null;
		File file = new File(path);
		Log.v("TAG", "file length " + file.length());
		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 数字越大读出的图片占用的heap越小 不然总是溢出
		if (file.length() < 1048576) { // 0-1m
			opts.inSampleSize = 1;
		} else if (file.length() < 2097152) { // 1-2m
			opts.inSampleSize = 2;
		} else if (file.length() < 4194304) { // 2-4m
			opts.inSampleSize = 4;
		} else if (file.length() < 8388608) { // 4-8m
			opts.inSampleSize = 6;
		} else {
			opts.inSampleSize = 10;
		}
		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		return resizeBmp;
	}
	

}
