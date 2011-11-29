package com.bebrite.overlay;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/*
 * ACTIVITY QUE ADMINISTRA A TELA DO JOGO
 * CRIA AS VIEWS, CRIA UM THREAD E TRABALHA O SENSOR
 */
public class GameActivity extends Activity implements SensorEventListener {


	LayoutInflater controlInflater = null;
	private GameLoopThread gameLoopThread;
	Button startBut;

	// Sensor vars
	private SensorManager mSensorManager;
	float[] mGeomagnetic;
	Sensor accelerometer;
	Sensor magnetometer;
	LineView lineView;
	float[] mGravity;
	View viewControl;
	Float azimut;
	int accurac;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new CameraView(this));
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFormat(PixelFormat.UNKNOWN);

		// SENSORS STUFF
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		controlInflater = LayoutInflater.from(getBaseContext());
		// ABAIXO CRIA Linha!
		// ### OS ENGENHEIROS DA GOOGLE NÃO RECOMENDAM COLOCAR 2 SURFACEVIEW NA
		// MESMA TELA! ENTAO TIVE QUE FAZER COM VIEW QUE É MAIS LENTA.
		lineView = new LineView(this);
		LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(lineView, layoutParamsControl);

		// ABAIXO CRIA A VIEW CONTROL.
		viewControl = controlInflater.inflate(R.layout.control, null);
		this.addContentView(viewControl, layoutParamsControl);

		gameLoopThread = new GameLoopThread(lineView,viewControl,handler);

	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		
		boolean retry = true;
		gameLoopThread.setRunning(false);
		while (retry) {
			try {
				gameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values;
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimut = orientation[0]; // orientation contains: azimut, pitch
				float degrees = -azimut * 360 / (2 * 3.14159f);
				gameLoopThread.setDegrees(degrees);
			}
		}
	}

	//NAO IMPLEMENTEI NADA PORQUE ACCURACY PARECE SEMPRE SER 3....
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {	}

	
	public void onClickStart(View v) {
		//PRA SUMIR O BOTAO
		startBut = (Button)findViewById(R.id.buttonStart);
		startBut.getHandler().post(new Runnable() {
		    public void run() {
		        startBut.setVisibility(View.GONE);
		    }
		});		
		//INICIA A THREAD
		gameLoopThread.setRunning(true);
		gameLoopThread.start();		
	}
	
	//HANDLER CRIADO POIS O INICIO DA RESULT ACTIVITY SEMPRE DEVE PARTIR DE UMA ACTIVITY. A THREAD USA ELE.
	private Handler handler = new Handler() {
	    public void handleMessage(Message msg) {
	    	double resultado = gameLoopThread.getResultado();
			Intent intent = new Intent("com.bebrite.overlay.RESULT");
			DecimalFormat decFor = new DecimalFormat("0");
			if (resultado > 100) {
				intent.putExtra("Result", "0");
				startActivity(intent);
			} else {
				intent.putExtra("Result", decFor.format(100 - Math.round(resultado)));
				
				startActivity(intent);
			}
			finish();
			
			
	    }
	};
	
	
}