package com.highjump.thread;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.highjump.R;
import com.highjump.control.SynchronizedData;
import com.highjump.util.ActionEnum;
import com.highjump.util.CanvasControl;
import com.highjump.view.BackGroundView;

public class DrawCarrot extends Thread {
	
	private Canvas canvas;
	
	Bitmap bitmap;
	
	Resources res;
	
	public DrawCarrot(Resources res){
		bitmap = BitmapFactory.decodeResource(res, R.drawable.carrot);
	}
	
	public void run() {
		while (!(canvas == null)) {

			canvas = CanvasControl.getCanvas();
			Random random = new Random();
			int intX = random.nextInt() / 240;
			Paint paint = new Paint();
			if (SynchronizedData.getAction()[0].equals(ActionEnum.actionJump)) {
				canvas.drawBitmap(bitmap, intX, -bitmap.getHeight(), paint);
			}
		}
		CanvasControl.releaseCanvas();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
