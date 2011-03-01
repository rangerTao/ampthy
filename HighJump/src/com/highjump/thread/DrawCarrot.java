package com.highjump.thread;

import java.util.Random;

public class DrawCarrot extends Thread {
	
	public void run(){
		Random random = new Random();
		int intX = random.nextInt() / 240;
	}
}
