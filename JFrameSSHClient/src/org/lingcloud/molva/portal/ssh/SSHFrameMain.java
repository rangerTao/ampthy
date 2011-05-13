package org.lingcloud.molva.portal.ssh;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SSHFrameMain extends Frame implements KeyListener,Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SSHFrameMain(String title){
		this.setTitle(title);
		this.setSize(800,600);
		this.setLocation(200, 200);
		
		FlowLayout fl = new FlowLayout();
		this.setLayout(fl);
		
		
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == 'q'){
			System.exit(0);
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
		
	public static void main(String args[]){
		SSHFrameMain sfm = new SSHFrameMain("test");
		sfm.setVisible(true);
	}

}
