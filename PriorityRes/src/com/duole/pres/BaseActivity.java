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

}
