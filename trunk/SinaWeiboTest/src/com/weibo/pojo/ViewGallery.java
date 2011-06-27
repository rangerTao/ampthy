package com.weibo.pojo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.Gallery;

public class ViewGallery extends AbsSpinner implements GestureDetector.OnGestureListener {

	//The position of selected item.
	int mSelectedPosition;
	//The sum of item.
	int mItemCount;
	//the position of first item.
	int mFirstPosition;
	//whether data changed.
	boolean mDataChanged;
	
	
	public ViewGallery(Context context) {
		super(context, null);
	}
	
	public ViewGallery(Context context, AttributeSet attrs) {
		super(context, attrs,android.R.attr.galleryStyle);
	}
	
	public ViewGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new Gallery.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
	}
	
	@Override
	protected boolean checkLayoutParams(LayoutParams p) {
		// TODO Auto-generated method stub
		return p instanceof LayoutParams;
	}
	

	@Override
	protected int computeHorizontalScrollExtent() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	protected int computeHorizontalScrollOffset() {
		// TODO Auto-generated method stub
		return mSelectedPosition;
	}

	@Override
	protected int computeHorizontalScrollRange() {
		// TODO Auto-generated method stub
		return mItemCount;
	}
	
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new LayoutParams(getContext(),attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		// TODO Auto-generated method stub
		return new LayoutParams(p);
	}
	
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		int selectedIndex = mSelectedPosition - mFirstPosition;
		
		if(selectedIndex < 0) return i;
		
		if(i == childCount - 1){
			// Draw the selected child last
			return selectedIndex;
		}else if(i >= selectedIndex){
			// Move the children to the right of the selected child earlier one
			return i + 1;
		}else{
			// Keep the children to the up of the selected child the same
			return i;
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		layout();
	}
	
	/**
	 * The method used to creates and positions view.
	 */
	public void layout(){
		
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
