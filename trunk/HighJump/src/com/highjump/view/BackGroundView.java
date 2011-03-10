package com.highjump.view;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import com.highjump.R;
import com.highjump.control.GData;
import com.highjump.thread.DrawBG;
import com.highjump.thread.DrawJump;
import com.highjump.util.CanvasControl;
import com.highjump.util.MyDraw;

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

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}


	public void surfaceCreated(SurfaceHolder arg0) {
		// init the view
		MyDraw.initialize(this);
		new Thread(new DrawBG()).start();
	}

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
		GData.charY = ((GData.charY - GData.bgBottom.getHeight()) / 10) * 7;

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
		MyDraw.DrawBG(canvas);
		// canvas.save(SaveFlags.bgComplete);
		// Draw the cloud
		MyDraw.DrawCloud(canvas);
		// Draw the carrot
		MyDraw.DrawCarrot(canvas);
		// draw the character
		MyDraw.DrawCharc(canvas);
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
			if (GData.charX >= GData.bgLeft.getWidth()) {
				GData.charX = GData.charX - GData.charLength - 1;
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
					new Thread(new DrawJump()).start();
				}
			}
			break;
		}
		return true;
	}
}
