package com.highjump.thread;

import java.util.Random;

import com.highjump.control.GData;
import com.highjump.view.BackGroundView;

import android.graphics.Paint;
import android.graphics.Rect;

public class DrawJump extends Thread {

	@Override
	public void run() {
		if (GData.frameCount == 0) {
			GData.charX = GData.bgLeft.getWidth();
		}
		for (int i = 0; i < GData.frequency; i++) {

			GData.canvas = GData.holder.lockCanvas();

			for (int j = 0; j < GData.cloudMax; j++) {
				Rect cRect = new Rect(GData.cloudX[j], GData.cloudY[j], GData.cloudX[j]
						+ GData.bmpCloud.getWidth(), GData.cloudY[j]
						+ GData.bmpCloud.getHeight());

				GData.cloudY[j] += GData.length;
				if (GData.cloudY[j] > GData.screenY) {
					GData.cloudX[j] = new Random().nextInt() % 320;
					GData.cloudX[j] = GData.cloudX[j] > 0 ? GData.cloudX[j] : 0 - GData.cloudX[j];
					GData.cloudY[j] = 0 - GData.bmpCloud.getHeight();
				}

			}

			Paint paint = new Paint();

			BackGroundView.setCharaPos();

			if (i == GData.frequency - 1) {
				if (GData.isLeft) {
					GData.isLeft = !GData.isLeft;
				} else {
					GData.isLeft = true;
				}
			}
			BackGroundView.DrawScreen(GData.canvas, paint);

			GData.frameCount++;
			GData.holder.unlockCanvasAndPost(GData.canvas);

			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
