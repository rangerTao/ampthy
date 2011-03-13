package com.highjump.thread;

import android.graphics.Paint;
import android.util.Log;

import com.highjump.control.GData;
import com.highjump.view.GameView;

public class DrawBG extends Thread{
	

	@Override
	public void run() {
		Paint paint = new Paint();
		GameView.DrawScreen(GData.canvas, paint);
	}
}
