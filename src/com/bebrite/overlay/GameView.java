package com.bebrite.overlay;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;

	Paint paint2 = new Paint();
	private Float azDegrees;
	Float linediff;
	Rect r = new Rect(10, 10, 50, 50);

	// QUANDO CRIEI UM SURFACEVIEW ACIMA TEM QUE CRIAR UM HOLDER!
	public GameView(Context context) {
		super(context);
		holder = this.getHolder();
//		gameLoopThread = new GameLoopThread(null,null);

		paint2.setColor(android.graphics.Color.WHITE);
		paint2.setStyle(Style.STROKE);
		paint2.setStrokeWidth(SetPx((float) 30.0));
		paint2.setAntiAlias(true);

		holder.addCallback(new Callback() {
			public void surfaceDestroyed(SurfaceHolder holder) {
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

			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});

	}

	protected void onDraw(Canvas canvas) {

		int width = getWidth();
		int height = getHeight();
		int centerx = width / 2;
		int centery = height / 2;
		if (azDegrees != null && linediff == null) {
			linediff = azDegrees;
		}

		if (azDegrees != null && linediff != null) {
			canvas.rotate(azDegrees - linediff, centerx, centery);// rad*360/2pi
		}

		for (int y = -1000; y < height + 1000; y += 120) {
			canvas.drawLine(centerx, y, centerx, y + 90, paint2);
		}
	}

	public void ResetPos() {
		if (azDegrees != null) {
			linediff = azDegrees;
		}
	}

	public void SetAzi(Float azi) {
		this.azDegrees = azi;
	}

	// Implementacao de DP pra PX
	public Float SetPx(Float dip) {
		Resources r = getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
	}

}
