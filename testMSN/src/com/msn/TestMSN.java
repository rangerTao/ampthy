package com.msn;

public class TestMSN {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String user = "taoliang1985@126.com";
		String pas = "taolovesun";
		
		MSNConnection msnConnection = new MSNConnection();
		
		int result = msnConnection.connect(user, pas);

	}

}
