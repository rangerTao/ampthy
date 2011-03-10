package com.highjump.thread;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.highjump.control.GData;
import com.highjump.util.MyDraw;
import com.highjump.view.BackGroundView;

/**
 * Draw the view of login
 * 
 * @author Taoliang
 * @since 2011.03.10
 * 
 */
public class DrawLogin extends Thread {

	@Override
	public void run() {

		
		Rect charR = new Rect();
		double left = GData.screenX * 0.3;
		double right = GData.screenX * 0.7;
		double top = GData.screenY * 0.45;
		charR.set((int)left, (int)top,
				(int)right, (int)top
						+ GData.char_login.getHeight()
						+ GData.bmpCloud.getHeight());
		int intLeft = (int)left;
		boolean booLeft = true;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Canvas tmCanvas = new Canvas();
		while (true) {
			try {
				GData.canvas2 = GData.holder.lockCanvas(null);
				tmCanvas = GData.canvas2;
			} catch (Exception e) {
				tmCanvas = GData.canvas;
			}

			tmCanvas.drawARGB(255, 254, 255, 213);

			// draw the left part
			tmCanvas.drawBitmap(GData.bgLeft, 0, 0, GData.bgPaint);
			// draw the right part
			tmCanvas.drawBitmap(GData.bgRight, GData.screenX
					- GData.bgRight.getWidth(), 0, GData.bgPaint);
			// draw the bottom part
			tmCanvas.drawBitmap(GData.bgBottom, 0, GData.screenY
					- GData.bgBottom.getHeight(), GData.bgPaint);
			// canvas.save(SaveFlags.bgComplete);
			// Draw the cloud
			MyDraw.DrawCloud(tmCanvas);
			// Draw the carrot
			MyDraw.DrawCarrot(tmCanvas);

			//tmCanvas = GData.holder.lockCanvas(charR);
			if(booLeft){
				tmCanvas.drawBitmap(GData.char_login, intLeft, (int)top, paint);
				tmCanvas.drawBitmap(GData.bmpCloud, intLeft+=2, (int)top + GData.char_login.getHeight(),paint);
			}else{
				tmCanvas.drawBitmap(GData.char_login, intLeft, (int)top, paint);
				tmCanvas.drawBitmap(GData.bmpCloud, intLeft-=2, (int)top + GData.char_login.getHeight(),paint);
			}
			if(intLeft >= (int)right - GData.char_login.getWidth() && booLeft){
				booLeft = false;
			}
			if(intLeft <= (int)left && !booLeft){
				booLeft = true;
			}

			GData.holder.unlockCanvasAndPost(tmCanvas);
			//tmCanvas = null;
			//tmCanvas = GData.holder.lockCanvas();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		


	}
}
