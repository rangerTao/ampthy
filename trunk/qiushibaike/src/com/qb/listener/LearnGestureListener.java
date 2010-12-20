package com.qb.listener;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class LearnGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private Context context;
	
	public LearnGestureListener(Context context){
		this.context = context;
	}
	
	public boolean onSingleTapUp(MotionEvent ev) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent ev) {
	}

	@Override
	public void onLongPress(MotionEvent ev) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public boolean onDown(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 20
	            && Math.abs(velocityX) > 100) {
	        // Fling left
	        Toast.makeText(context, "Fling Left", Toast.LENGTH_SHORT).show();
	    } else if (e2.getX() - e1.getX() > 20
	            && Math.abs(velocityX) > 100) {
	        // Fling right
	        Toast.makeText(context, "Fling Right", Toast.LENGTH_SHORT).show();
	    }
	    return false;
	}
}