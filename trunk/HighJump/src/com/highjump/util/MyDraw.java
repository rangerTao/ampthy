package com.highjump.util;

import java.util.Random;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.highjump.R;
import com.highjump.control.GData;

public class MyDraw {

	public static void initialize(View view){

		GData.handler = view.getHandler();
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
	 * Draw the background
	 * 
	 * @param canvas
	 */
	public static void DrawBG(Canvas canvas) {
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
	public static void DrawCarrot(Canvas canvas) {

	}

	/**
	 * Draw the character
	 * 
	 * @param canvas
	 */
	public static void DrawCharc(Canvas canvas) {
		// draw the character
		canvas.drawBitmap(GData.bmpChar, GData.charX, GData.charY, GData.charPaint);
	}

	/**
	 * draw the clouds on the surface
	 * 
	 * @param canvas
	 */
	public static void DrawCloud(Canvas canvas) {
		for (int i = 0; i < GData.cloudMax; i++) {
			canvas.drawBitmap(GData.bmpCloud, GData.cloudX[i], GData.cloudY[i], GData.cloudPaint);
		}
	}
}
