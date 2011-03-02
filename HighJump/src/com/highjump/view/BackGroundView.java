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
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import com.highjump.R;
import com.highjump.util.CanvasControl;

public class BackGroundView extends SurfaceView implements Callback {

	static SurfaceHolder holder;
	Canvas canvas;
	Resources res;
	// the bitmap of resources
	Bitmap bitmap;
	// the left part of the bg
	Bitmap bgLeft;
	// the right part of the bg
	Bitmap bgRight;
	// the bottom part of the bg
	Bitmap bgBottom;
	// the bitmap of the button jump
	Bitmap bmButton;
	// the bitmap of the cloud
	Bitmap bmpCloud;
	Paint charPaint;
	CanvasControl cc;
	// the width of the screen
	int screenX = 0;
	// the height of the screen
	int screenY = 0;

	/**
	 * The cloud
	 */
	// the total sum of the cloud in the screen
	int cloudMax = 20;
	// the default position of the cloud
	int[] cloudX = new int[cloudMax];
	int[] cloudY = new int[cloudMax];
	// the paint used for cloud
	Paint cloudPaint;

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

		// this.setBackgroundResource(R.drawable.bg);
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
				screenX = canvas.getWidth();
				screenY = canvas.getHeight();
				paint.setColor(Color.BLACK);
				canvas.drawARGB(255, 254, 255, 213);
				DrawBG(canvas);

				DrawCloud(canvas);

				DrawCarrot(canvas);

				DrawCharc(canvas);

				holder.unlockCanvasAndPost(canvas);
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void DrawBG(Canvas canvas) {
		Paint bgPaint = new Paint();
		bgPaint.setAntiAlias(true);
		bgPaint.setStyle(Paint.Style.FILL);
		bgLeft = BitmapFactory.decodeResource(res, R.drawable.bg_left);
		canvas.drawBitmap(bgLeft, 0, 0, bgPaint);

		bgRight = BitmapFactory.decodeResource(res, R.drawable.bg_right);
		canvas.drawBitmap(bgRight, screenX - bgRight.getWidth(), 0, bgPaint);

		bgBottom = BitmapFactory.decodeResource(res, R.drawable.bg_bottom);
		canvas.drawBitmap(bgBottom, 0, screenY - bgBottom.getHeight(), bgPaint);

		bgPaint.setARGB(255, 254, 255, 213);
		bmButton = BitmapFactory.decodeResource(res, R.drawable.btn_jump);
		canvas.drawBitmap(bmButton, 0, screenY - bmButton.getHeight(), bgPaint);
	}

	private void DrawCarrot(Canvas canvas) {
		charPaint.setColor(Color.RED);
		charPaint.setStyle(Paint.Style.FILL);
		int x = screenX / 2;
		int y = screenY;
		y = ((y - bgBottom.getHeight()) / 10) * 9;
		canvas.drawRect(x - 25, y - 25, x + 25, y + 25, charPaint);
	}

	private boolean DrawCharc(Canvas canvas) {
		return true;
	}

	private boolean DrawCloud(Canvas canvas) {
		bmpCloud = BitmapFactory.decodeResource(res, R.drawable.cloud);
		cloudPaint = new Paint();
		cloudPaint.setAntiAlias(true);
		cloudPaint.setARGB(255, 254, 255, 213);
		int cloudDis = new Random().nextInt() % 20;
		for (int i = 0; i < cloudMax; i++) {
			cloudX[i] = new Random().nextInt() % 320;
			cloudX[i] = cloudX[i] > 0 ? cloudX[i] : 0 - cloudX[i];
			Log.v("TAG", cloudX[i] + "");
			// cloudY[i] = 0 - bmpCloud.getHeight() + cloudDis;
			cloudY[i] = new Random().nextInt() % 480 - bgBottom.getHeight()
					+ cloudDis;
			canvas.drawBitmap(bmpCloud, cloudX[i], cloudY[i], cloudPaint);
		}

		return true;
	}
}
