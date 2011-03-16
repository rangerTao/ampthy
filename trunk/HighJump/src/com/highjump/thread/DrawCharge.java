package com.highjump.thread;

import java.util.Random;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.highjump.R;
import com.highjump.control.GData;
import com.highjump.view.GameView;

/**
 * Draw the animation of charging.
 * @author ranger
 * @since 2011.03.15
 */
public class DrawCharge extends Thread{

	public void run() {

		Canvas tmCanvas = new Canvas();

		try {
			//wait 500 ms ,then draw the second bitmap of char
			Thread.sleep(500);
			if (GData.isLeft) {
				GData.bmpChar = BitmapFactory.decodeResource(GData.res, R.drawable.char_jump_right);
				GData.charX = GData.bgLeft.getWidth();
			} else {
				GData.bmpChar = BitmapFactory.decodeResource(GData.res, R.drawable.char_jump);
				GData.charX = GData.screenX - GData.bgRight.getWidth() - GData.bmpChar.getWidth();
			}
			GData.canvas2 = GData.holder.lockCanvas(null);
			tmCanvas = GData.canvas2;
		} catch (Exception e) {
			tmCanvas = GData.canvas;
		}

		Paint paint = new Paint();

		//GameView.setCharaPos();

		GameView.DrawScreen(tmCanvas, paint);

		GData.frameCount++;
		GData.holder.unlockCanvasAndPost(tmCanvas);

		
		
	}
	
	
	
}
