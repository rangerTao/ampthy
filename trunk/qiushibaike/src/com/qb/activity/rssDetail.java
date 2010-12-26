package com.qb.activity;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qb.R;
import com.qb.qbMain;
import com.qb.R.id;
import com.qb.R.layout;
import com.qb.activity.viewer.FeedViewer;
import com.qb.activity.viewer.NewsViewer;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class rssDetail extends Activity implements OnTouchListener,
		OnGestureListener {

	private SharedPreferences mysh;
	private static rssDetail appref;
	GestureDetector mGestureDetector;
	// The view on the layout
	private TextView tvTitle;
	private TextView tvDate;
	private TextView tvAuthor;
	private TextView tvDetail;
	private TextView tvUrl;

	// THe index of the listview
	int index = 0;

	// OnCreate

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bIndex = this.getIntent().getExtras();
		index = bIndex.getInt("index");
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail);
		mGestureDetector = new GestureDetector(this, new RssDetailGestureListener(
				this));
		RelativeLayout rldeLayout = (RelativeLayout) findViewById(R.id.rlDetail);
		rldeLayout.setOnTouchListener(this);
		rldeLayout.setLongClickable(true);
		appref = this;
		setTextViewContent(index);
	}

	public void setTextViewContent(int index) {

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		tvDetail = (TextView) findViewById(R.id.tvDetail);
		tvUrl = (TextView) findViewById(R.id.tvUrl);

		// get the intent of the qbMain
		final FeedViewer am = FeedViewer.getApp();
		Node el = am.nodeList.get(index);
		NodeList nlmeta = el.getChildNodes();
		Log.v("debug", index + "       " + nlmeta.getLength());
		for (int i = 0; i < nlmeta.getLength(); i++) {
			Node elmeta = nlmeta.item(i);
			if (elmeta.getNodeName().equalsIgnoreCase("title")) {
				String temp = elmeta.getFirstChild() == null ? "" : elmeta
						.getFirstChild().getNodeValue();
				tvTitle.setText(temp);
				setTitle(temp);
			} else if (elmeta.getNodeName().equalsIgnoreCase("author")) {
				tvAuthor.setText(elmeta.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("description")) {
				tvDetail.setText(elmeta.getFirstChild() == null ? "" : elmeta
						.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("pubDate")) {
				tvDate.setText(elmeta.getFirstChild() == null ? "" : elmeta
						.getFirstChild().getNodeValue());
			} else if (elmeta.getNodeName().equalsIgnoreCase("link")) {
				tvUrl.setText(elmeta.getFirstChild() == null ? "" : elmeta
						.getFirstChild().getNodeValue());
			}
		}

		tvUrl.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String urlString = tvUrl.getText().toString();

				Bundle bundle = new Bundle();
				bundle.putString("url", urlString);
				Intent newIntent = new Intent(getApp(), NewsViewer.class);
				newIntent.putExtras(bundle);
				getApp().startActivity(newIntent);
			}

		});
	}

	public void viewLast() {
		if (index >= 1) {
			setTextViewContent(index - 1);
		} else {
			Toast.makeText(this, "This is the first rss", 2000);
		}
	}

	public static rssDetail getApp() {
		return appref;
	}

	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		Toast.makeText(this, "test1", 2000);

	}

	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		Toast.makeText(this, "test12", 2000);

	}

	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
		Toast.makeText(this, "test13", 2000);

	}

	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		Toast.makeText(this, "test14", 2000);

	}

	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

}

class RssDetailGestureListener extends GestureDetector.SimpleOnGestureListener {

	private Context context;

	public RssDetailGestureListener(Context context) {
		this.context = context;
	}

	public boolean onSingleTapUp(MotionEvent ev) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent ev) {
	}

	@Override
	public void onLongPress(MotionEvent ev) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public boolean onDown(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 10 && Math.abs(velocityX) > 10) {
			// Fling left
			rssDetail rDetail = rssDetail.getApp();
			FeedViewer am = FeedViewer.getApp();
			if (rDetail.index <= am.nodeList.size() - 1) {
				int intindex = rDetail.index + 1;
				rDetail.index -= 1;
				Bundle bIndex = new Bundle();
				bIndex.putInt("index", intindex);
				Intent new_detail = new Intent(am, rssDetail.class);
				new_detail.putExtras(bIndex);
				am.startActivity(new_detail);
				rDetail.finish();
			} else {
				Toast.makeText(context, "已经是最后一条了！", Toast.LENGTH_SHORT).show();
			}
		} else if (e2.getX() - e1.getX() > 10 && Math.abs(velocityX) > 10) {
			// Fling right
			
			
			rssDetail rDetail = rssDetail.getApp();
			FeedViewer am = FeedViewer.getApp();
			if (rDetail.index >= 1) {
				int intindex = rDetail.index - 1;
				rDetail.index -= 1;
				Bundle bIndex = new Bundle();
				bIndex.putInt("index", intindex);
				Intent new_detail = new Intent(am, rssDetail.class);
				new_detail.putExtras(bIndex);
				am.startActivity(new_detail);
				rDetail.finish();
			} else {
				Toast.makeText(context, "已经是第一条了！", Toast.LENGTH_SHORT).show();
			}

		}
		return false;
	}
}