package com.bebrite.overlay;

import android.graphics.Canvas;
import android.util.Log;


public class GameLoopThread extends Thread {
	static final long FPS = 10;
	private GameView view;
	private boolean running = false;
	

	
	
	public GameLoopThread(GameView view){
		this.view = view;
		
	}
	
	public void setRunning(boolean run){
		running = run;
		
	}
	
	@Override
	public void run() {
		long ticksPS = 1000 / FPS;
		long startTime;
		long sleepTime;
		
		//Metodo de tempo, calcula o tempo para fazer um frame per second padrao.
		while (running){
			Canvas c = null;
			startTime = System.currentTimeMillis();
			try {
				c = view.getHolder().lockCanvas();
				synchronized(view.getHolder()) {
					view.onDraw(c);
				}
			} finally {
				if (c != null) {
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			
			sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
			
			try {
				if(sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {}
			Log.d("Thread!", "LOOP!");
		}
	}


	
	
	
	
	
}
