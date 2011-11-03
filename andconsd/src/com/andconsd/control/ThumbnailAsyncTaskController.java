package com.andconsd.control;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;


public class ThumbnailAsyncTaskController {

	private static ArrayList<Object[]> taskList = new ArrayList<Object[]>();
	private static HashMap<Integer, asyncLoadImage> threadPool = new HashMap<Integer, asyncLoadImage>();
	private static int threadSize = 16;
	private static int currentThreadSum = 0;
	private static boolean dealTaskListRunning = false;
	
	public static void doTask(Object...objects){
		Log.v("TAG","thread sum" + currentThreadSum);
		if(currentThreadSum < threadSize){
			Log.v("TAG","about to do task");
			new asyncLoadImage().execute(objects);
			newAThread();
		}else{
			Log.v("TAG", "thread up to 16,add to list");
			addTask(objects);
			if(false == dealTaskListRunning){
				startDealWithTaskList();
			}
		}
		
	}

	private static void startDealWithTaskList(){
		new Thread(){

			@Override
			public void run() {
				while(taskList.size() > 0){
					dealTaskListRunning = true;
					if(currentThreadSum < threadSize){
						Log.v("TAG", "do task from list");
						new asyncLoadImage().execute(taskList.get(taskList.size() - 1));
						removeTask();
					}
				}
				dealTaskListRunning = false;
				super.run();
			}
			
		}.start();
		
	}
	
	private static void addTask(Object... objects) {
		Log.v("TAG","add a task from list");
//		synchronized (taskList) {
			taskList.add(objects);
//		}
	}

	public static void removeTask() {
		Log.v("TAG","remove a task from list");
//		synchronized (taskList) {
		try{
			taskList.remove(taskList.size() - 1);
			removeAThread();
		}catch (Exception e) {
			e.printStackTrace();
		}
			
//		}

	}
	
	private synchronized static void newAThread(){
		Log.v("TAG","increase thread sum");
		currentThreadSum ++;
	}
	
	public synchronized static void removeAThread(){
		Log.v("TAG","decrease thread sum           " + currentThreadSum);
		currentThreadSum --;
	}
	
}
