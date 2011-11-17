package com.bebrite.overlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ACTIVITY QUE CONTROLA A TELA DO RESULTADO
 */
public class ResultActivity extends Activity {

	TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		super.onDestroy();
	}
	
	public void onClickRetry(View v){
		startActivity(new Intent("android.intent.action.GAME"));
		finish();
	}
	
	public void onClickCab(View v){
		Toast.makeText(this, "Sorry, No cabs available near you!", Toast.LENGTH_LONG).show();
	}

}
