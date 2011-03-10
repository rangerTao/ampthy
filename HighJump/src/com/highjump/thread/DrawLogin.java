package com.highjump.thread;

import android.graphics.Rect;

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

		// update the surface
		// the sub thread cannot operate the variable initialized in
		// the main thread.
		GData.handler.post(new Runnable() {
			public void run() {
				BackGroundView.releaseHolder(GData.holder);
			}
		});

		while (!GData.ingame) {
			GData.canvas = GData.holder.lockCanvas(new Rect());
		}
	}
}
