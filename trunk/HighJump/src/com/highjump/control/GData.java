package com.highjump.control;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.highjump.util.CanvasControl;

public class GData {
	
	// The surfaceholder
	public static SurfaceHolder holder;
	// the canvas
	public static Canvas canvas;
	public static Canvas canvas2;
	// the system resources
	public static Resources res;
	// whether game is running
	public static boolean ingame = true;
	// to count the time of draw
	public static int drawCount = 0;
	// the frequency of the refresh
	public static int period = 500;
	public static int frequency = period / 40;
	// the length of jump
	public static int length = 50 / frequency;

	// the left part of the bg
	public static Bitmap bgLeft;
	// the right part of the bg
	public static Bitmap bgRight;
	// the bottom part of the bg
	public static Bitmap bgBottom;
	// the bitmap of the button jump
	public static Bitmap bmButton;
	// the radius of the button
	public static Bitmap bmpChar;
	public static int radius = 60;
	// the bitmap of the cloud
	public static Bitmap bmpCloud;
	// the area of the jump button

	public static Paint charPaint;
	public static CanvasControl cc;
	// the width of the screen
	public static int screenX = 0;
	// the height of the screen
	public static int screenY = 0;

	// the paint user for bg
	public static Paint bgPaint;
	
	//the bitmap of char
	public static Bitmap char_login;

	/**
	 * The character
	 */
	public static boolean isLeft = true;
	// the location of the character
	public static int charX = 0;
	public static int charY = 0;
	public static int charLength = 0;
	/**
	 * The cloud
	 */
	// the total sum of the cloud in the screen
	public static int cloudMax = 8;
	// the default position of the cloud
	public static int[] cloudX = new int[cloudMax];
	public static int[] cloudY = new int[cloudMax];

	// the paint used for cloud
	public static Paint cloudPaint;

	// the handle of the view
	public static Handler handler;

	public static int frameCount = 0;
	
}
