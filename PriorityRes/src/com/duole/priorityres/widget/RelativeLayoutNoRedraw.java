package com.duole.priorityres.widget;

import android.content.Context;
import android.widget.RelativeLayout;

public class RelativeLayoutNoRedraw extends RelativeLayout{

	public RelativeLayoutNoRedraw(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
		if(changed){
			super.onLayout(true, l, t, r, b);
		}
	}
}
