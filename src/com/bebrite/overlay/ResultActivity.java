package com.bebrite.overlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ACTIVITY QUE CONTROLA A TELA DO RESULTADO
 */
public class ResultActivity extends Activity {

	TextView resultView;
	TextView resultText;
	LinearLayout buttons;
	int result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.result);
		Log.d("RESULT", "Activity Created");
		
		resultView = (TextView) findViewById(R.id.textResult);
		resultText = (TextView) findViewById(R.id.resultText);
		buttons = (LinearLayout)findViewById(R.id.linearLayout1);
		
		Bundle bundle = getIntent().getExtras();

		String line;
		
		if ((line = bundle.getString("Result")) != null) {
			resultView.setText(line+"%");
			resultView.setVisibility(0);
			Log.d("TA", line);
			result = Integer.parseInt(line);
		}
		
		if (result>99){
			resultText.setText("Come on! Tell me the truth, you were not walking right!");
		}else if(result > 90){
			resultText.setText("Okay Okay, You're alright to go home, but take care, you're still not 100%");			
		}else if(result > 60){			
			resultText.setText("Many things could describe that performance but sober isn't one of them. Try putting something other than alcohol into your belly.");			
		}else if(result>40){
			resultText.setText("It may be best to call it a night, or better yet, call a taxi.");			
		}else{			
			resultText.setText("Your coordination seems impaired. We're guessing your speech is as well so it might be best to limit who you talk to.");			
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
