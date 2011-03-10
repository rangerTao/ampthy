package com.highjump.thread;

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
		double right = GData.screenX * 0.6;
		double top = GData.screenY * 0.45;
		charR.set((int)left, (int)top,
				(int)right, (int)top
						+ GData.char_login.getHeight()
						+ GData.bmpCloud.getHeight());
		int intLeft = (int)left;
		boolean booLeft = true;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		while (true) {
			try{
				GData.canvas = GData.holder.lockCanvas();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			Log.v("TAG", "f");
			GData.canvas.drawARGB(255, 254, 255, 213);

			// draw the left part
			GData.canvas.drawBitmap(GData.bgLeft, 0, 0, GData.bgPaint);
			// draw the right part
			GData.canvas.drawBitmap(GData.bgRight, GData.screenX
					- GData.bgRight.getWidth(), 0, GData.bgPaint);
			// draw the bottom part
			GData.canvas.drawBitmap(GData.bgBottom, 0, GData.screenY
					- GData.bgBottom.getHeight(), GData.bgPaint);
			// canvas.save(SaveFlags.bgComplete);
			// Draw the cloud
			MyDraw.DrawCloud(GData.canvas);
			// Draw the carrot
			MyDraw.DrawCarrot(GData.canvas);

			//GData.canvas = GData.holder.lockCanvas(charR);
			if(booLeft){
				GData.canvas.drawBitmap(GData.char_login, intLeft, (int)top, paint);
				GData.canvas.drawBitmap(GData.bmpCloud, intLeft+=2, (int)top + GData.char_login.getHeight(),paint);
			}else{
				GData.canvas.drawBitmap(GData.char_login, intLeft, (int)top, paint);
				GData.canvas.drawBitmap(GData.bmpCloud, intLeft-=2, (int)top + GData.char_login.getHeight(),paint);
			}
			if(intLeft <= (int)right - GData.char_login.getWidth() && booLeft){
				booLeft = false;
			}else{
				booLeft = true;
			}
			Log.v("TAG", "test");

			// update the surface
			// the sub thread cannot operate the variable initialized in
			// the main thread.
//			GData.handler.post(new Runnable() {
//				public void run() {
//					BackGroundView.releaseHolder(GData.holder);
//				}
//			});
			GData.holder.unlockCanvasAndPost(GData.canvas);
			//GData.canvas = GData.holder.lockCanvas();
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		


	}
}
