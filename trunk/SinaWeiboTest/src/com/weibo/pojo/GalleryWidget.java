package com.weibo.pojo;

import com.weibo.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;

public class GalleryWidget extends AppWidgetProvider{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.gallery);
		
		appWidgetManager.updateAppWidget(appWidgetIds[0], views);
	}
	
	

}
