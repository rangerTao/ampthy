package com.duole.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DuoleNetUtils {

	public static String connect(String url) {

		try {
			URL urlCon = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) urlCon
					.openConnection();

			conn.setRequestMethod("GET");

			conn.setConnectTimeout(5 * 1000);

			InputStream inStream = conn.getInputStream();
			byte[] data = DuoleUtils.readFromInput(inStream);

			String html = new String(data, "gbk");

			return html;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
