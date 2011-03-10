package com.highjump.thread;

import android.graphics.Paint;

import com.highjump.control.GData;
import com.highjump.view.BackGroundView;

public class DrawBG extends Thread{
	

	@Override
	public void run() {
		Paint paint = new Paint();
		try {

			BackGroundView.DrawScreen(GData.canvas, paint);
			// update the surface
			// the sub thread cannot operate the variable initialized in
			// the main thread.
			GData.handler.post(new Runnable() {
				public void run() {
					BackGroundView.releaseHolder(GData.holder);
				}
			});
			Thread.sleep(40);
			// }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}