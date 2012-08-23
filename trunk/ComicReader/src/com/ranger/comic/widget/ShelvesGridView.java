package com.ranger.comic.widget;

import com.ranger.comic.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

public class ShelvesGridView extends GridView {

	private Bitmap background;

	public ShelvesGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_04);
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int count = getChildCount();
		int top = count > 0 ? getChildAt(0).getTop() : 0;

		int backgroundWidth = background.getWidth();
		int backgroundHeight = background.getHeight();

		int width = getWidth();
		int height = getHeight();

		for (int y = top; y < height; y += backgroundHeight) {
			for (int x = 0; x < width; x += backgroundWidth) {
				canvas.drawBitmap(background, x, y, null);
			}
		}

		super.dispatchDraw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

}
