package com.qb.viewer;

import com.qb.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsViewer extends Activity {

	// The main of the activity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.newsviewer);

		setProgressBarIndeterminateVisibility(true);

		WebView wView = (WebView) findViewById(R.id.wvNews);
		Bundle bundle = this.getIntent().getExtras();
		String urlString = bundle.getString("url");
		setTitle(urlString);
		// wView.loadUrl(urlString);

		setProgressBarIndeterminateVisibility(false);


		wView.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		wView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});
		wView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});

		wView.loadUrl(urlString);
	}

}
