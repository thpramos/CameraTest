package com.bebrite.overlay;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.TypedValue;
import android.view.View;

public class LineView extends View {

	Paint paint2 = new Paint();
	private Float azDegrees;
	Float linediff;
	Rect r = new Rect(10, 10, 50, 50);

	public LineView(Context context) {
		super(context);

		paint2.setColor(android.graphics.Color.WHITE);
		paint2.setStyle(Style.STROKE);
		paint2.setStrokeWidth(SetPx((float) 30.0));
		paint2.setAntiAlias(true);
	}

	public void SetAzi(Float azi) {
		this.azDegrees = azi;
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

	// Implementacao de DP pra PX
	public Float SetPx(Float dip) {
		Resources r = getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
	}

}
