package com.highjump.thread;

import java.util.Random;

import com.highjump.control.GData;
import com.highjump.view.GameView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class DrawJump extends Thread {

	@Override
	public void run() {

		if (GData.frameCount == 0) {
			GData.charX = GData.bgLeft.getWidth();
		}
		Canvas tmCanvas = new Canvas();
		for (int i = 0; i < GData.frequency; i++) {

			try {
				GData.canvas2 = GData.holder.lockCanvas(null);
				tmCanvas = GData.canvas2;
			} catch (Exception e) {
				tmCanvas = GData.canvas;
			}
			for (int j = 0; j < GData.cloudMax; j++) {
				
				GData.cloudY[j] += GData.length;
				if (GData.cloudY[j] > GData.screenY) {
					GData.cloudX[j] = new Random().nextInt() % 320;
					GData.cloudX[j] = GData.cloudX[j] > 0 ? GData.cloudX[j] : 0 - GData.cloudX[j];
					GData.cloudY[j] = 0 - GData.bmpCloud.getHeight();
				}

			}

			Paint paint = new Paint();

			GameView.setCharaPos();

			if (i == GData.frequency - 1) {
				if (GData.isLeft) {
					GData.isLeft = !GData.isLeft;
				} else {
					GData.isLeft = true;
				}
			}
			GameView.DrawScreen(tmCanvas, paint);

			GData.frameCount++;
			GData.holder.unlockCanvasAndPost(tmCanvas);

			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
