package com.bebrite.overlay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ResultActivity extends Activity {

	TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.result);
		Log.d("RESULT", "Activity Created");
		
		result = (TextView) findViewById(R.id.textResult);
		
		Bundle bundle = getIntent().getExtras();

		String line;
		
		if ((line = bundle.getString("Result")) != null) {
			result.setText(line+"%");
			result.setVisibility(0);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
