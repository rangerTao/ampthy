package com.highjump.thread;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.highjump.R;
import com.highjump.control.GData;

public class DrawTimeCountDown extends Thread{

	public void run() {
		
		Canvas tmCanvas = new Canvas();

		try {
			GData.canvas2 = GData.holder.lockCanvas(null);
			tmCanvas = GData.canvas2;
			
			//the pro
			if(GData.isLeft){
				GData.bmpProcess = BitmapFactory.decodeResource(GData.res, R.drawable.pro_left);
				GData.bmpSword = BitmapFactory.decodeResource(GData.res, R.drawable.sword_left);
			}else{
				GData.bmpProcess = BitmapFactory.decodeResource(GData.res, R.drawable.pro_right);
				GData.bmpSword = BitmapFactory.decodeResource(GData.res, R.drawable.sword_right);
			}
			
		} catch (Exception e) {
			tmCanvas = GData.canvas;
		}
		Paint paint = new Paint();
		tmCanvas.drawBitmap(GData.bmpProcess, GData.charX + 5, GData.charY - 5, paint);
		
		tmCanvas.drawBitmap(GData.bmpSword, GData.charX + 5, GData.charY - 5, paint);
		
		GData.holder.unlockCanvasAndPost(tmCanvas);
		
	}

	
}
