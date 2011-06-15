package com.weibo.pojo;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class ViewGallery extends Gallery{
	
	private Camera mCamera = new Camera();
    private int mMaxRotationAngle = 50;
    private int mMaxZoom = -500;
    private int mCoveflowCenter;
    private boolean mAlphaMode = true;
    private boolean mCircleMode = false;

	public ViewGallery(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
	}
	
	public ViewGallery(Context context,AttributeSet attrs){
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}
	
	public ViewGallery(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {

		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();
		int rotationAngle = 0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		 if (childCenter == mCoveflowCenter) {
	            transformImageBitmap(child, t, 0, 0);
	        } else {
	            rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);

	            if (Math.abs(rotationAngle) > mMaxRotationAngle) {
	                rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
	                        : mMaxRotationAngle;
	            }
	            transformImageBitmap(child, t, rotationAngle,
	                    (int) Math.floor((mCoveflowCenter - childCenter)/ (childWidth==0?1:childWidth)));
	        }
	        return true;
		
	}
	
	public int getCenterOfView(View childView){
		return (getWidth() - getPaddingLeft() - getPaddingRight()) /2 + getPaddingLeft();
	}
	
	private void transformImageBitmap(View child, Transformation t,
            int rotationAngle, int d) {
        mCamera.save();
        final Matrix imageMatrix = t.getMatrix();
        final int imageHeight = child.getLayoutParams().height;
        final int imageWidth = child.getLayoutParams().width;
        final int rotation = Math.abs(rotationAngle);
        mCamera.translate(0.0f, 0.0f, 100.0f);
        // As the angle of the view gets less, zoom in
        if (rotation <= mMaxRotationAngle) {
            float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
            mCamera.translate(0.0f, 0.0f, zoomAmount);
            if (mCircleMode) {
                if (rotation < 40)
                    mCamera.translate(0.0f, 155, 0.0f);
                else
                    mCamera.translate(0.0f, (255 - rotation * 2.5f), 0.0f);
            }
            if (mAlphaMode) {
               // (child.setAlpha((int) (255 - rotation * 2.5));
            }
        }
        mCamera.rotateY(rotationAngle);
        mCamera.getMatrix(imageMatrix);

        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
        mCamera.restore();
    }

}
