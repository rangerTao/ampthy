package com.highjump.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class BackGroundView extends SurfaceView implements Callback{

	public BackGroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
	}

}
