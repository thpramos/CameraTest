package com.bebrite.overlay;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/*
 * VIEW (LAYOUT) CUSTOMIZADO PARA O DESENHO DA LINHA
 */
public class LineView extends View {

	//CLASS VARS//
	Paint paint2 = new Paint();
	Float startingAzimuth;
	private Float rotateDegrees;


	Rect r = new Rect(10, 10, 50, 50);
	ArrayList<Double> movingAverage;


	public LineView(Context context) {
		super(context);
		paint2.setColor(android.graphics.Color.WHITE);
		paint2.setStyle(Style.STROKE);
		paint2.setStrokeWidth(SetPx((float) 30.0));
		paint2.setAntiAlias(true);
		
		//Moving Average
		movingAverage = new ArrayList<Double>();
		for(int i=0;i<20;i++){
			movingAverage.add(0.00);			
		}
	}
	
	
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		int centerx = width / 2;
		int centery = height / 2;
		
		if (rotateDegrees != null && startingAzimuth != null) {
			canvas.rotate(rotateDegrees, centerx, centery);// rad*360/2pi
			Log.d("DRAW","Degrees Rotated:"+(rotateDegrees));
		}		
		for (int y = -1000; y < height + 1000; y += 120) {
			canvas.drawLine(centerx, y, centerx, y + 90, paint2);
		}
	}

	// Implementacao de DP pra PX
	public Float SetPx(Float dip) {
		Resources r = getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
	}

	public void setRotation(Float azi) {
			if (startingAzimuth == null) {
				startingAzimuth = azi;
			}
			movingAverage.remove(0);
			movingAverage.add((double) (azi - startingAzimuth));
			double sum = 0;
			StringBuffer values = new StringBuffer();
			for (double valor : movingAverage) {
				sum += valor;
				values.append(valor + ",");
			}
			Log.d("Average", "Items: " + values);
			rotateDegrees = (float) (sum / movingAverage.size());

	}	
	

	public Float getRotateDegrees() {
		return rotateDegrees;
	}
}
