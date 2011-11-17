package com.bebrite.overlay;


import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
/*
 * THREAD RESPONSAVEL PELA FUNCIONALIDADE DO JOGO
 */
public class GameLoopThread extends Thread {
	static final long FPS = 10;
	private LineView lineView;
	private View viewControl;
	private boolean running = false;
	private Float degrees;
	private long mStartTime = 0L;
	TextView counter;
	private String count;
	private double resultados = 0;
	private int resultcount = 0;
	private Handler handler;
	private double resultado;

	
	public GameLoopThread(LineView arg1, View arg2, Handler hand) {
		lineView = arg1;
		viewControl = arg2;
		this.handler = hand;
		counter = (TextView) viewControl.findViewById(R.id.counter);

	}

	public void setRunning(boolean run) {
		running = run;

	}

	@Override
	public void run() {
		long ticksPS = 1000 / FPS;
		long startTime;
		long sleepTime;
		mStartTime = System.currentTimeMillis();

		// Metodo de tempo, calcula o tempo para fazer um frame per second
		// padrao.
		while (running) {

			// TELA, JOGO E FPS
			startTime = System.currentTimeMillis();
			if (degrees != null)
				lineView.setRotation(degrees);
			lineView.postInvalidate();
			Log.d("Thread", "Loop'd");

			// RESULTADOS
			resultados += Math.abs(lineView.getRotateDegrees());
			resultcount++;

			// CONTADOR
			final long start = mStartTime + 15000;
			long millis = start - System.currentTimeMillis();
			int seconds = (int) (millis / 1000);
			int minutes = seconds / 60;
			seconds = seconds % 60;
			count = "";
			if ((((minutes * 60) + seconds) * 1000) >= 0) {
				if (seconds < 10) {
					count = "" + minutes + ":0" + seconds;
				} else {
					count = "" + minutes + ":" + seconds;
				}
				counter.getHandler().post(new Runnable() {
					public void run() {
						counter.setText(count);
					}
				});
			} else {
				setRunning(false);
			}

			// DORMIR ATE O PROXIMO LOOP
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {
			}
		}
		resultado = resultados / resultcount;
		Log.d("ENDGAME", "Result: " + resultado);
		handler.sendEmptyMessage(0);
	}

	public double getResultado() {
		return resultado;
	}

	public void setDegrees(Float azi) {
		degrees = azi;
	}

}
