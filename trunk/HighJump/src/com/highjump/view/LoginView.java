package com.highjump.view;

import java.util.Random;

import com.highjump.R;
import com.highjump.control.GData;
import com.highjump.thread.DrawLogin;
import com.highjump.util.CanvasControl;
import com.highjump.util.MyDraw;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class LoginView extends SurfaceView implements Callback{

	public LoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		GData.holder = this.getHolder();
		GData.cc = new CanvasControl(context, GData.holder);
		while (!(GData.canvas == null)) {
			GData.canvas = GData.cc.getCanvas();
		}
		GData.holder.addCallback(this);
		GData.charPaint = new Paint();
		this.setFocusable(true);
		// this.res = res;
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setKeepScreenOn(true);
		GData.res = this.getResources();
	}
	
	public LoginView(Context context) {
		super(context);
		GData.holder = this.getHolder();
		GData.cc = new CanvasControl(context, GData.holder);
		while (!(GData.canvas == null)) {
			GData.canvas = GData.cc.getCanvas();
		}
		GData.holder.addCallback(this);
		GData.charPaint = new Paint();
		this.setFocusable(true);
		// this.res = res;
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setKeepScreenOn(true);
		GData.res = this.getResources();
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		MyDraw.initialize(this);
		new Thread(new DrawLogin()).start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	
	

}
