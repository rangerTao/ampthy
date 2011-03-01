package com.highjump.view;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import com.highjump.R;
import com.highjump.control.SynchronizedData;
import com.highjump.thread.DrawCarrot;
import com.highjump.util.ActionEnum;
import com.highjump.util.CanvasControl;

public class BackGroundView extends SurfaceView implements Callback {

	static SurfaceHolder holder;
	Canvas canvas;
	Resources res;
	Bitmap bitmap;
	Paint charPaint;
	CanvasControl cc;
	int screenX = 0;
	int screenY = 0;

	public BackGroundView(Context context, Resources res) {
		super(context);
		holder = this.getHolder();
		cc = new CanvasControl(context, holder);
		while (!(canvas == null)) {
			canvas = cc.getCanvas();
		}
		holder.addCallback(this);
		charPaint = new Paint();
		this.setFocusable(true);
		this.res = res;
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setKeepScreenOn(true);
		bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.bg);
	}

	public BackGroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = this.getHolder();
		canvas = holder.lockCanvas(null);
		res = this.getResources();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		new Thread(new DrawThread()).start();
		// new Thread(new DrawCarrot(res)).start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	class DrawThread extends Thread {

		@Override
		public void run() {
			Paint paint = new Paint();
			try {
				canvas = holder.lockCanvas();
				paint.setColor(Color.BLACK);

				DrawCarrot(canvas);

				

				DrawCloud(canvas);

				canvas.drawBitmap(bitmap, 0, 0, paint);
				DrawCharc(canvas);


				holder.unlockCanvasAndPost(canvas);
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private boolean DrawCarrot(Canvas canvas) {
		screenX = canvas.getWidth();
		screenY = canvas.getHeight();
		charPaint.setARGB(200,255,0,96);
		charPaint.setStyle(Paint.Style.FILL);
		canvas.drawBitmap(BitmapFactory.decodeResource(res, R.drawable.icon),
				0, 0, charPaint);
		return true;
	}

	private boolean DrawCharc(Canvas canvas) {

		return true;
	}

	private boolean DrawCloud(Canvas canvas) {

		return true;
	}
}
