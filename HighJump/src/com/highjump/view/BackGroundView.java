package com.highjump.view;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import com.highjump.R;
import com.highjump.control.GData;
import com.highjump.pojos.SaveFlags;
import com.highjump.thread.DrawBG;
import com.highjump.util.CanvasControl;

/**
 * The main class of the game
 * 
 * @author ranger
 * 
 */
public class BackGroundView extends SurfaceView implements Callback {


	public BackGroundView(Context context, Resources res) {
		super(context);
		GData.holder = this.getHolder();
		GData.cc = new CanvasControl(context, GData.holder);
		while (!(GData.canvas == null)) {
			GData.canvas = GData.cc.getCanvas();
		}
		GData.holder.addCallback(this);
		GData.charPaint = new Paint();
		this.setFocusable(true);
		GData.res = res;
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setKeepScreenOn(true);
	}

	public BackGroundView(Context context, AttributeSet attrs) {
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

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// init the view
		initalize();
		new Thread(new DrawBG()).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	/**
	 * init the elements and postions used in the view
	 */
	public void initalize() {
		GData.handler = this.getHandler();
		GData.canvas = GData.holder.lockCanvas();

		// get the size of the screen
		GData.screenX = GData.canvas.getWidth();
		GData.screenY = GData.canvas.getHeight();

		// init the paints
		GData.bgPaint = new Paint();
		GData.bgPaint.setAntiAlias(true);
		GData.bgPaint.setStyle(Paint.Style.FILL);

		// the paint for character
		GData.charPaint.setColor(Color.RED);
		GData.charPaint.setStyle(Paint.Style.FILL);

		// the paint for cloud
		GData.cloudPaint = new Paint();
		GData.cloudPaint.setAntiAlias(true);
		GData.cloudPaint.setARGB(255, 254, 255, 213);

		// init the bitmap of bg
		GData.bgLeft = BitmapFactory.decodeResource(GData.res, R.drawable.bg_left);
		GData.bgRight = BitmapFactory.decodeResource(GData.res, R.drawable.bg_right);
		GData.bgBottom = BitmapFactory.decodeResource(GData.res, R.drawable.bg_bottom);
		GData.bmButton = BitmapFactory.decodeResource(GData.res, R.drawable.btn_jump);
		GData.bmpChar = BitmapFactory.decodeResource(GData.res, R.drawable.char_jump);

		// the charLength
		GData.charLength = (GData.screenX - GData.bgLeft.getWidth() -GData. bgRight.getWidth() - GData.bmpChar
				.getWidth())
				/ GData.frequency;

		// init the bitmap of cloud
		GData.bmpCloud = BitmapFactory.decodeResource(GData.res, R.drawable.cloud);

		// the location of the character
		GData.charX = GData.screenX / 2;
		GData.charY = GData.screenY;
		GData.charY = ((GData.charY - GData.bgBottom.getHeight()) / 10) * 9;

		// the location of cloud
		int cloudDis = new Random().nextInt() % 20;
		for (int i = 0; i < GData.cloudMax; i++) {
			GData.cloudX[i] = new Random().nextInt() % 320;
			GData.cloudX[i] = GData.cloudX[i] > 0 ? GData.cloudX[i] : 0 - GData.cloudX[i];
			GData.cloudY[i] = new Random().nextInt() % 480 - GData.bgBottom.getHeight()
					+ cloudDis;
		}
		// holder.unlockCanvasAndPost(canvas);
	}

	/**
	 * release the holder
	 */
	public static void releaseHolder(SurfaceHolder holder) {
		holder.unlockCanvasAndPost(GData.canvas);
	}

	public static void DrawScreen(Canvas canvas, Paint paint) {

		// if (frameCount < 2) {
		paint.setColor(Color.BLACK);
		canvas.drawARGB(255, 254, 255, 213);

		// Draw the background
		DrawBG(canvas);
		// canvas.save(SaveFlags.bgComplete);
		// Draw the cloud
		DrawCloud(canvas);
		// Draw the carrot
		DrawCarrot(canvas);
		// draw the character
		DrawCharc(canvas);
	}

	/**
	 * Draw the background
	 * 
	 * @param canvas
	 */
	private static void DrawBG(Canvas canvas) {
		// draw the left part
		canvas.drawBitmap(GData.bgLeft, 0, 0, GData.bgPaint);
		// draw the right part
		canvas.drawBitmap(GData.bgRight, GData.screenX - GData.bgRight.getWidth(), 0, GData.bgPaint);
		// draw the bottom part
		canvas.drawBitmap(GData.bgBottom, 0, GData.screenY - GData.bgBottom.getHeight(), GData.bgPaint);
		// Set the color of the button
		GData.bgPaint.setARGB(255, 254, 255, 213);
		// draw the button
		canvas.drawBitmap(GData.bmButton, 0, GData.screenY - GData.bmButton.getHeight(), GData.bgPaint);
	}

	/**
	 * Draw the
	 * 
	 * @param canvas
	 */
	private static void DrawCarrot(Canvas canvas) {

	}

	/**
	 * Draw the character
	 * 
	 * @param canvas
	 */
	private static void DrawCharc(Canvas canvas) {
		// draw the character
		canvas.drawBitmap(GData.bmpChar, GData.charX, GData.charY, GData.charPaint);
	}

	/**
	 * draw the clouds on the surface
	 * 
	 * @param canvas
	 */
	private static void DrawCloud(Canvas canvas) {
		for (int i = 0; i < GData.cloudMax; i++) {
			canvas.drawBitmap(GData.bmpCloud, GData.cloudX[i], GData.cloudY[i], GData.cloudPaint);
		}
	}

	/**
	 * Get the position of chara
	 */
	public static void setCharaPos() {
		if (GData.isLeft) {
			if (GData.charX < GData.screenX - GData.bgRight.getWidth()) {
				GData.charX = GData.charX + GData.charLength;
			}
		} else {
			if (GData.charX > GData.bgLeft.getWidth()) {
				GData.charX = GData.charX - GData.charLength;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (GData.ingame) {
				if ((event.getX() * event.getX() + (GData.screenY - event.getY())
						* (GData.screenY - event.getY())) < GData.radius * GData.radius) {
					new Thread(new MoveThread()).start();
				}
			}

			break;
		}
		return true;
	}

	class MoveThread extends Thread {

		@Override
		public void run() {
			if (GData.frameCount == 0) {
				GData.charX = GData.bgLeft.getWidth();
			}
			for (int i = 0; i < GData.frequency; i++) {

				GData.canvas = GData.holder.lockCanvas();

				for (int j = 0; j < GData.cloudMax; j++) {
					Rect cRect = new Rect(GData.cloudX[j], GData.cloudY[j], GData.cloudX[j]
							+ GData.bmpCloud.getWidth(), GData.cloudY[j]
							+ GData.bmpCloud.getHeight());

					GData.cloudY[j] += GData.length;
					if (GData.cloudY[j] > GData.screenY) {
						GData.cloudX[j] = new Random().nextInt() % 320;
						GData.cloudX[j] = GData.cloudX[j] > 0 ? GData.cloudX[j] : 0 - GData.cloudX[j];
						GData.cloudY[j] = 0 - GData.bmpCloud.getHeight();
					}

				}

				Paint paint = new Paint();

				setCharaPos();

				if (i == GData.frequency - 1) {
					if (GData.isLeft) {
						GData.isLeft = !GData.isLeft;
					} else {
						GData.isLeft = true;
					}
				}
				DrawScreen(GData.canvas, paint);

				GData.frameCount++;
				GData.holder.unlockCanvasAndPost(GData.canvas);

				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
