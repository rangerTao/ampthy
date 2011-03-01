package com.highjump.util;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class CanvasControl {
	
	Context context;
	
	static Canvas canvas;
	
	static SurfaceHolder holder;
	
	public static boolean used = false;
	public CanvasControl(Context context,SurfaceHolder holder){
		this.context = context;
		this.holder = holder;
		canvas = holder.lockCanvas();
	}
	
	public synchronized static Canvas getCanvas(){
		if(used == false){
			used = true;
			return canvas;
		}else{
			return null;
		}
		
	}
	
	public synchronized static SurfaceHolder getHolder(){
		if(used == false){
			used = true;
			return holder;
		}else{
			return null;
		}
	}
	
	public static void releaseCanvas(){
		//holder.unlockCanvasAndPost(canvas);
		used = false;
	}
	
	
}
