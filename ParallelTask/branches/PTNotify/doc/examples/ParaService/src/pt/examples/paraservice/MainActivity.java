package pt.examples.paraservice;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final static String LOG_TAG = MainActivity.class.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.i(LOG_TAG, "ParaTask onCreate");
		
		Button test = (Button) findViewById(R.id.button3);
		test.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "ParaTask test button3 onClick");
			}
		});
	}

	// Start the  service
	public void startNewService(View view) {
		Log.i(LOG_TAG, "ParaTask startNewService");
		startService(new Intent(this, ParaService.class));
		/*
		//final View v = view;
		final Context c = getBaseContext();
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Autod-generated method stub
				Toast.makeText(c, "Complete!", Toast.LENGTH_LONG).show();
			}});
		t.start();
		*/
	}

	// Stop the  service
	public void stopNewService(View view) {
		Log.i(LOG_TAG, "ParaTask stopNewService");
		stopService(new Intent(this, ParaService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
