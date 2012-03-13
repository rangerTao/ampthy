package com.duole.priorityres;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.duole.priorityres.gif.GifView;
import com.duole.priorityres.pojos.ResourceItem;
import com.duole.priorityres.pojos.TargetZone;
import com.duole.priorityres.util.PRApplication;
import com.duole.priorityres.widget.RelativeLayoutNoRedraw;

public class PRWordActivity extends BaseActivity {

	RelativeLayout main;
	
	DisplayMetrics dm = new DisplayMetrics();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		pra = (PRApplication) getApplication();
		
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		ps = pra.getPs();

		main = new RelativeLayoutNoRedraw(getApplicationContext());
		RelativeLayout.LayoutParams lpmain = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
		main.setLayoutParams(lpmain);
		
		main.addView(getBgView());
		
		
		for(TargetZone tz : ps.getAlTarZone()){
			
			RelativeLayout.LayoutParams lpri = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			ImageView iv = new ImageView(getApplicationContext());
			
			if(!tz.getPic().equals("")){
				
				Bitmap bmp = BitmapFactory.decodeFile(pra.getBasePath() + tz.getPic());
				iv.setImageBitmap(bmp);
				
				int x = Integer.parseInt(tz.getPosx());
				int y = Integer.parseInt(tz.getPosy());
				tz.setWidth(bmp.getWidth() + "");
				tz.setHeight(bmp.getHeight() + "");
				
				lpri.setMargins(x, y, 0, 0);
				
				iv.setTag(tz);
				
				main.addView(iv,lpri);
			}
			
		}
		
		getRandomPositionRI();
		
		for(ResourceItem ri : ps.getAlResItems()){
			
			RelativeLayout.LayoutParams lpri = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			ImageView iv = new ImageView(getApplicationContext());
			
			Bitmap bmp = BitmapFactory.decodeFile(pra.getBasePath() + ri.getPic());
			iv.setImageBitmap(bmp);
			
			int x = Integer.parseInt(ri.getPosx());
			int y = Integer.parseInt(ri.getPosy());
			
			lpri.setMargins(x, y, 0, 0);
			
			iv.setTag(ri);
			
			if(Boolean.parseBoolean(ri.isDragable())){
				iv.setOnTouchListener(thumbOnTouchListener);
			}
			
			iv.setOnClickListener(thumbOnClickListener);

			main.addView(iv,lpri);
			
		}
		
		setContentView(main);
	}
	
	private boolean getRandomPositionRI(){
		
		if(ps.getRandom()== null || ps.getRandom().equals("false")){
			return false;
		}
		
		ArrayList<String[]> alPos = new ArrayList<String[]>();
		
		for(ResourceItem ri : pra.getPs().getAlResItems()){
			String[] pos = {ri.getPosx(),ri.getPosy()};
			alPos.add(pos);
		}
		
		Random random = new Random();
		for(ResourceItem ri : pra.getPs().getAlResItems()){
			int index = 0;
			if(alPos.size() > 1){
				index = Math.abs(random.nextInt() % (alPos.size() - 1));
			}
			
			ri.setPosx(alPos.get(index)[0]);
			ri.setPosy(alPos.get(index)[1]);
			alPos.remove(index);
		}
		
		return true;
		
	}

	OnClickListener thumbOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			ResourceItem ri = (ResourceItem) v.getTag();
			
			Log.d("TAG", ri.getIsAnswer());
			if(ri.getIsAnswer().equals("true")){
				
				playVictorySoundAndTip(main);
				
			}else{
				
				if(!ri.getAudio().equals("")){
					Message msg = new Message();
					msg.obj = ri.getAudio();
					msg.what = PLAY_MUSIC;
					mHandler.sendMessage(msg);
				}
			}

			
			if(!ri.getPicClicked().equals("")){
				ImageView iv = (ImageView) v;
				iv.setImageDrawable(Drawable.createFromPath(pra.getBasePath() + ri.getPicClicked()));
			}
		}
	};
	
	OnTouchListener thumbOnTouchListener = new OnTouchListener() {

		int lastX, lastY;
		int orginalL,orginalT,orginalR,orginalB;
		int disX = 0;
		boolean isClick = false;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				orginalL = v.getLeft();
				orginalT = v.getTop();
				orginalR = v.getRight();
				orginalB = v.getBottom();
				disX = lastX;
				isClick = false;
				break;
			case MotionEvent.ACTION_MOVE:
				
				int dx = (int) event.getRawX() - lastX;
				int dy = (int) event.getRawY() - lastY;

				int left = v.getLeft() + dx;
				int top = v.getTop() + dy;
				int right = v.getRight() + dx;
				int bottom = v.getBottom() + dy;

				if (left < 0) {
					left = 0;
					right = left + v.getWidth();
				}

				if (right > main.getMeasuredWidth()) {
					right = main.getMeasuredWidth();
					left = right - v.getWidth();
				}

				if (top < 0) {
					top = 0;
					bottom = top + v.getHeight();
				}

				if (bottom > main.getMeasuredHeight()) {
					bottom = main.getMeasuredHeight();
					top = bottom - v.getHeight();
				}
				v.layout(left, top, right, bottom);

				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				v.postInvalidate();
				
				break;
			case MotionEvent.ACTION_UP:
				
				disX = Math.abs(lastX - disX);
				
				if (disX > 5) {
					isClick = true;
					if (!verifyTargetZone(v, event.getRawX(), event.getRawY())) {
						
						v.layout(orginalL, orginalT,orginalR ,
								orginalB);
						
						v.postInvalidate();
						
						RelativeLayout.LayoutParams lpri = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
						lpri.setMargins(orginalL, orginalT,orginalR ,
								orginalB);
						v.setLayoutParams(lpri);
					}
				}
				
				break;

			default:
				break;
			}
			return isClick;
		}
	};

	private boolean verifyTargetZone(View v,float f,float g){
		
		ResourceItem ri = (ResourceItem) v.getTag();
		boolean isIn = false;
		
		if(!ri.gettIndex().equals("")){
			
			for (TargetZone tz : ps.getAlTarZone()) {

				int tzx = Integer.parseInt(tz.getPosx());
				int tzy = Integer.parseInt(tz.getPosy());

				int width = Integer.parseInt(tz.getWidth());
				int height = Integer.parseInt(tz.getHeight());

				if (ri.gettIndex().equals(tz.getIndex()) && (tzx < (int) f && (int) f < (tzx + width))
						&& (tzy < (int) g && (int) g < (tzy + height))) {
					tz.setItemCount(tz.getItemCount() + 1);

					if(!tz.getAudio().equals("")){
						Message msg = new Message();
						msg.obj = tz.getAudio();
						msg.what = PLAY_MUSIC;
						mHandler.sendMessage(msg);
					}
					
					isIn = true;
				}
				
			}
			
			boolean flag = true;
			for(TargetZone tz : ps.getAlTarZone()){
				flag = flag && tz.getItemCount() == tz.getCount();
			}
			
			if(flag){
				playVictorySoundAndTip(main);
			}
			
			return isIn;
		}else{
			return true;
		}
		
		
		
	}
	
	
	private View getBgView(){
		
		String bg = ps.getBg();
		
		if(bg != null && !bg.equals("")){
			
			LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
			
			if(bg.toLowerCase().endsWith("gif")){
				FileInputStream fis;
				try {
					fis = new FileInputStream(new File(pra.getBasePath() + bg ));
					
					GifView gv = new GifView(getApplicationContext());
					gv.setGifImage(fis);
					gv.setShowDimension(dm.widthPixels, dm.heightPixels);
					gv.setLayoutParams(lp);
					
					return gv;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					
					return null;
				}
				
			}else{
				
				ImageView iv = new ImageView(getApplicationContext());

				Bitmap bmp = BitmapFactory.decodeFile(pra.getBasePath() + bg);
				
				iv.setImageBitmap(bmp);
				iv.setLayoutParams(lp);
				
				return iv;
				
			}
			
		}
		
		return null;
	}
}
