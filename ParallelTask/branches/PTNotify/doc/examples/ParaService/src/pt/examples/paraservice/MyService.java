package pt.examples.paraservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
    public void onCreate() {
        Toast.makeText(this, "The new ParaService was Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
    	// For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, "ParaService Started", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "ParaService Destroyed", Toast.LENGTH_LONG).show();

    }
}
