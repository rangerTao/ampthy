package com.highjump.view;

import com.highjump.R;
import com.highjump.control.SynchronizedData;
import com.highjump.util.ActionEnum;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class BackGroundView extends SurfaceView implements Callback{

	SurfaceHolder holder;
	Canvas canvas;
	Resources res;
	
	public BackGroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = this.getHolder();
		canvas = holder.lockCanvas();
		res = this.getResources();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		new Thread(new DrawThread()).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
	}
	
	class DrawThread extends Thread {

		@Override
		public void run() {
			try {
				Paint paint = new Paint();
				Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.bg);
				canvas.drawBitmap(bitmap, 0, 0, paint);
				
				holder.unlockCanvasAndPost(canvas);
				
				SynchronizedData.addAction(ActionEnum.actionJump, "50");
				
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
