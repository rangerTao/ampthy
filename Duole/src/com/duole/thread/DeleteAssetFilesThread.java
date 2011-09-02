package com.duole.thread;

import java.io.File;
import java.util.ArrayList;

import android.util.Log;

import com.duole.pojos.asset.Asset;
import com.duole.utils.Constants;

/**
 * Delete useless asset.
 * 
 * @author taoliang1985@gmail.com
 * 
 */
public class DeleteAssetFilesThread extends Thread {

	ArrayList<Asset> assets;

	public DeleteAssetFilesThread(ArrayList<Asset> assets) {
		this.assets = assets;
	}

	@Override
	public void run() {

		for (int i = 0; i < assets.size(); i++) {
			Asset ass = assets.get(i);
			// Delete thumbnail.
			File file;
			
			if(!ass.getThumbnail().equals("")){
				file = new File(Constants.CacheDir
						+ Constants.RES_THUMB
						+ ass.getThumbnail().substring(
								ass.getThumbnail().lastIndexOf("/")));
				if (file.exists()) {
					file.delete();
				}
			}
			
			

			// if is a asset of audio.
			if (ass.getType().equals(Constants.RES_AUDIO)) {
				file = new File(Constants.CacheDir + Constants.RES_AUDIO
						+ ass.getUrl().substring(ass.getUrl().lastIndexOf("/")));
				if (file.exists()) {
					file.delete();
				}
			}

			// if is a asset of game.
			if (ass.getType().equals(Constants.RES_GAME)) {
				file = new File(Constants.CacheDir + Constants.RES_GAME
						+ ass.getUrl().substring(ass.getUrl().lastIndexOf("/")));
				if (file.exists()) {
					file.delete();
				}
			}

			// if is a asset of video.
			if (ass.getType().equals(Constants.RES_VIDEO)) {
				file = new File(Constants.CacheDir + Constants.RES_VIDEO
						+ ass.getUrl().substring(ass.getUrl().lastIndexOf("/")));
				if (file.exists()) {
					file.delete();
				}
			}
		}

	}

}
