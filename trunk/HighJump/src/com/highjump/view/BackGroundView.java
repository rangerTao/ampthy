package com.highjump.view;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import com.highjump.R;
import com.highjump.util.CanvasControl;

/**
 * The main class of the game
 * 
 * @author ranger
 * 
 */
public class BackGroundView extends SurfaceView implements Callback {

	// The surfaceholder
	static SurfaceHolder holder;
	// the canvas
	Canvas canvas;
	// the system resources
	Resources res;
	// whether game is running
	boolean ingame = true;
	// to count the time of draw
	int drawCount = 0;
	// the frequency of the refresh
	int period = 1500;
	int frequency = 1500 / 40;
	// the length of jump
	int length = 200 / frequency;

	// the left part of the bg
	Bitmap bgLeft;
	// the right part of the bg
	Bitmap bgRight;
	// the bottom part of the bg
	Bitmap bgBottom;
	// the bitmap of the button jump
	Bitmap bmButton;
	// the radius of the button
	int radius = 60;
	// the bitmap of the cloud
	Bitmap bmpCloud;
	// the area of the jump button

	Paint charPaint;
	CanvasControl cc;
	// the width of the screen
	int screenX = 0;
	// the height of the screen
	int screenY = 0;

	// the paint user for bg
	Paint bgPaint;

	// the location of the character
	int charX = 0;
	int charY = 0;
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

	// the handle of the view
	Handler handler;

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
		// init the view
		initalize();
		new Thread(new DrawThread()).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	/**
	 * init the elements and postions used in the view
	 */
	public void initalize() {
		handler = this.getHandler();
		canvas = holder.lockCanvas();

		// get the size of the screen
		screenX = canvas.getWidth();
		screenY = canvas.getHeight();

		// init the paints
		bgPaint = new Paint();
		bgPaint.setAntiAlias(true);
		bgPaint.setStyle(Paint.Style.FILL);

		// the paint for character
		charPaint.setColor(Color.RED);
		charPaint.setStyle(Paint.Style.FILL);

		// the paint for cloud
		cloudPaint = new Paint();
		cloudPaint.setAntiAlias(true);
		cloudPaint.setARGB(255, 254, 255, 213);

		// init the bitmap of bg
		bgLeft = BitmapFactory.decodeResource(res, R.drawable.bg_left);
		bgRight = BitmapFactory.decodeResource(res, R.drawable.bg_right);
		bgBottom = BitmapFactory.decodeResource(res, R.drawable.bg_bottom);
		bmButton = BitmapFactory.decodeResource(res, R.drawable.btn_jump);

		// init the bitmap of cloud
		bmpCloud = BitmapFactory.decodeResource(res, R.drawable.cloud);

		// the location of the character
		charX = screenX / 2;
		charY = screenY;
		charY = ((charY - bgBottom.getHeight()) / 10) * 9;

		// the location of cloud
		int cloudDis = new Random().nextInt() % 20;
		for (int i = 0; i < cloudMax; i++) {
			cloudX[i] = new Random().nextInt() % 320;
			cloudX[i] = cloudX[i] > 0 ? cloudX[i] : 0 - cloudX[i];
			cloudY[i] = new Random().nextInt() % 480 - bgBottom.getHeight()
					+ cloudDis;
		}
		//holder.unlockCanvasAndPost(canvas);
	}

	/**
	 * release the holder
	 * 
	 */
	public void releaseHolder(SurfaceHolder holder) {
		holder.unlockCanvasAndPost(canvas);
		Log.v("TAG",drawCount+"");

	}

	/**
	 * Main thread to draw the surface
	 * 
	 * @author ranger
	 * 
	 */
	class DrawThread extends Thread {

		@Override
		public void run() {
			Paint paint = new Paint();
			try {
				//while (ingame) {

					
					paint.setColor(Color.BLACK);
					canvas.drawARGB(255, 254, 255, 213);

					// Draw the background
					DrawBG(canvas);
					// Draw the cloud
					DrawCloud(canvas);
					// Draw the carrot
					DrawCarrot(canvas);
					// draw the character
					DrawCharc(canvas);
					// update the surface
					// the sub thread cannot operate the variable initialized in
					// the
					// main thread.
					handler.post(new Runnable() {
						public void run() {
							releaseHolder(holder);
						}
					});
					Thread.sleep(40);
				//}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Draw the background
	 * 
	 * @param canvas
	 */
	private void DrawBG(Canvas canvas) {
		// draw the left part
		canvas.drawBitmap(bgLeft, 0, 0, bgPaint);
		// draw the right part
		canvas.drawBitmap(bgRight, screenX - bgRight.getWidth(), 0, bgPaint);
		// draw the bottom part
		canvas.drawBitmap(bgBottom, 0, screenY - bgBottom.getHeight(), bgPaint);
		// Set the color of the button
		bgPaint.setARGB(255, 254, 255, 213);
		// draw the button
		canvas.drawBitmap(bmButton, 0, screenY - bmButton.getHeight(), bgPaint);
	}

	/**
	 * Draw the
	 * 
	 * @param canvas
	 */
	private void DrawCarrot(Canvas canvas) {

	}

	/**
	 * Draw the character
	 * 
	 * @param canvas
	 */
	private void DrawCharc(Canvas canvas) {
		// draw the character
		canvas.drawRect(charX - 25, charY - 25, charX + 25, charY + 25,
				charPaint);
	}

	/**
	 * draw the clouds on the surface
	 * 
	 * @param canvas
	 */
	private void DrawCloud(Canvas canvas) {
		for (int i = 0; i < cloudMax; i++) {
			canvas.drawBitmap(bmpCloud, cloudX[i], cloudY[i], cloudPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.v("TAG", event.getX() + "");
			Log.v("TAG", event.getY() + "");
			Log.v("TAG",
					(event.getX() * event.getX() + (screenY - event.getY())
							* (screenY - event.getY()))
							+ "");
			if ((event.getX() * event.getX() + (screenY - event.getY())
					* (screenY - event.getY())) < radius * radius) {
				for (int i = 0; i < frequency; i++) {
					for (int j = 0; j < cloudMax; j++) {
						cloudY[j] += length;
						Paint paint = new Paint();
						canvas = holder.lockCanvas();
						paint.setColor(Color.BLACK);
						canvas.drawARGB(255, 254, 255, 213);
						// Draw the background
						DrawBG(canvas);
						// Draw the cloud
						DrawCloud(canvas);
						// Draw the carrot
						DrawCarrot(canvas);
						// draw the character
						DrawCharc(canvas);
						holder.unlockCanvasAndPost(canvas);
					}
				}
			}
			break;
		}
		return true;
	}

}
