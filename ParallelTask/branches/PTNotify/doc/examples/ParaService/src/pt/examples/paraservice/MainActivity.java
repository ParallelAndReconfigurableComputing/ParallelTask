package pt.examples.paraservice;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	private final static String LOG_TAG = MainActivity.class.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	// Start the  service
	public void startNewService(View view) {
		Log.i(LOG_TAG, "ParaTask startNewService");
		startService(new Intent(this, ParaService.class));
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
