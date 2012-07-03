package com.duole.pres;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.duole.pres.gif.GifView;
import com.duole.pres.pojos.PreSource;
import com.duole.pres.util.PRApplication;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BaseActivity extends Activity {

	PRApplication pra;
	PreSource ps;

	private static MediaPlayer mp;

	static {
		mp = new MediaPlayer();
		mp.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});
	}

	public final static int PLAY_MUSIC = 1;
	public final static int PREPARE_MP = 2;
	public final static int VICTORY = 3;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String path;
			switch (msg.what) {
			case PLAY_MUSIC:

				path = (String) msg.obj;
				try {
					// mp = MediaPlayer.create(getApplicationContext(),
					// Uri.parse(pra.getBasePath() + "/" +path));
					// mp.start();
					mp.reset();
					mp.setDataSource(pra.getBasePath() + "/" + path);
					mp.prepare();
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case VICTORY:

				path = (String) msg.obj;

				if (path != null && !path.equals("")) {
					try {
						mp.reset();
						mp.setDataSource(pra.getBasePath() + "/" + path);
						mp.prepare();
					} catch (Exception e) {
						e.printStackTrace();
					}

					mp.setOnCompletionListener(new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							exitwhenright();
						}
					});
				} else {
					exitwhenright();
				}

				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

	public void exitwhenright() {
		new Thread() {

			@Override
			public void run() {
				try {
					Thread.sleep(3500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				PRApplication pra = (PRApplication) getApplication();
				String pkgname = pra.getPkgname();

				Log.d("TAG", "pkg name" + pra.getPkgname());
				if (pkgname != null && !pkgname.trim().equals("")) {
					if (PriorityResActivity.appref != null) {
						PriorityResActivity.appref.startActivityByPkgName(pkgname);
					} else if (PResViewActivity.appref != null) {
						PResViewActivity.appref.startActivityByPkgName(pkgname);
					}
				} else {
					// if (PriorityResActivity.appref != null) {
					// Log.d("TAG", "priority");
					// PriorityResActivity.appref.setResult(2);
					// PriorityResActivity.appref.finish();
					// }

					if (PRWordActivity.appref != null) {
						PRWordActivity.appref.setResult(2);
						PRWordActivity.appref.finish();
					}
					if (PResViewActivity.appref != null) {
						Log.d("TAG", "PResViewActivity");
						// PResViewActivity.appref.setResult(2);
						// PResViewActivity.appref.finish();
					}
				}
				android.os.Process.killProcess(android.os.Process.myPid());
				super.run();
			}

		}.start();

	}

	public void playVictorySoundAndTip(View parent) {

		Message msg = new Message();
		msg.what = VICTORY;
		msg.obj = ps.getAudio();
		mHandler.sendMessage(msg);

		if (ps.getvPic() != null && !ps.getvPic().equals("")) {
			String path = ps.getvPic();
			GifView gv = null;
			ImageView iv = null;
			if (path.toLowerCase().endsWith("gif")) {
				gv = new GifView(getApplicationContext());
				try {
					gv.setGifImage(new FileInputStream(new File(pra.getBasePath() + path)));

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				iv = new ImageView(getApplicationContext());
				iv.setImageDrawable(Drawable.createFromPath(pra.getBasePath() + path));
			}

			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View view = inflater.inflate(R.layout.vpopup, null);
			RelativeLayout rlmain = (RelativeLayout) view.findViewById(R.id.llMain);

			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_IN_PARENT);

			rlmain.addView(gv == null ? iv : gv, lp);

			PopupWindow popup = new PopupWindow(view, 800, 600, false);
			popup.showAtLocation(parent, Gravity.CENTER, 0, 0);
		}
	}

}
