package pt.test;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MultiThreadProgressBarActivity extends Activity {
	ProgressBar myBarHorizontal;
	ProgressBar myBarCircular;
	TextView lblTopCaption;
	EditText txtDataBox;
	Button btnDoSomething;
	Button btnDoItAgain;
	int progressStep = 5;
	int globalVar = 0;
	int accum = 0;
	long startingMills = System.currentTimeMillis();
	boolean isRunning = false;
	String PATIENCE = "Some important data is being collected now. "
			+ "\nPlease be patient...wait... ";
	Handler myHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_para_task_progress_bar);
		lblTopCaption = (TextView) findViewById(R.id.lblTopCaption);
		myBarHorizontal = (ProgressBar) findViewById(R.id.myBarHor);
		myBarCircular = (ProgressBar) findViewById(R.id.myBarCir);
		txtDataBox = (EditText) findViewById(R.id.txtBox1);
		txtDataBox.setHint(" Foreground distraction\n Enter some data here...");

		btnDoItAgain = (Button) findViewById(R.id.btnDoItAgain);
		btnDoItAgain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStart();
			}// onClick
		});// setOnClickListener
		btnDoSomething = (Button) findViewById(R.id.btnDoSomething);
		btnDoSomething.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editable text = txtDataBox.getText();
				Toast.makeText(
						getBaseContext(),
						"You said >> \n" + text
								+ "\nSystem.getProperty(\"java.vm.name\"): "
								+ System.getProperty("java.vm.name"), 1).show();
			}// onClick
		});// setOnClickListener
	}// onCreate

	@Override
	protected void onStart() {
		super.onStart();
		// prepare UI components
		txtDataBox.setText("");
		btnDoItAgain.setEnabled(false);

		// reset and show progress bars
		accum = 0;
		myBarHorizontal.setMax(100);
		myBarHorizontal.setProgress(0);
		myBarHorizontal.setVisibility(View.VISIBLE);
		myBarCircular.setVisibility(View.VISIBLE);
		// create background thread were the busy work will be done
		Thread myBackgroundThread = new Thread(backgroundTask, "backAlias1");
		myBackgroundThread.start();
	}

	// FOREGROUND
	// this foreground Runnable works on behave of the background thread //
	// updating the main UI which is unreachable to it
	private Runnable foregroundRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				// update UI, observe globalVar is changed in back thread
				lblTopCaption.setText(PATIENCE + "\nPct progress: " + accum
						+ "  globalVar: " + globalVar);
				// advance ProgressBar
				myBarHorizontal.incrementProgressBy(progressStep);
				accum += progressStep;
				// are we done yet?
				if (accum >= myBarHorizontal.getMax()) {
					lblTopCaption.setText("Background work is OVER!");
					myBarHorizontal.setVisibility(View.INVISIBLE);
					myBarCircular.setVisibility(View.INVISIBLE);
					btnDoItAgain.setEnabled(true);
				}
			} catch (Exception e) {
				Log.e("<<foregroundTask>>", e.getMessage());
			}
		}
	}; // foregroundTask

	// BACKGROUND
	// this is the back runnable that executes the slow work
	private Runnable backgroundTask = new Runnable() {
		@Override
		public void run() {
			// busy work goes here...
			try {
				for (int n = 0; n < 20; n++) {
					// this simulates 1 sec. of busy activity
					Thread.sleep(1000);
					// change a global variable from here...
					globalVar++;
					// try: next two UI operations should NOT work
					// Toast.makeText(getApplication(), "Hi ", 1).show(); //
					// txtDataBox.setText("Hi ");

					// wake up foregroundRunnable delegate to speak for you
					myHandler.post(foregroundRunnable);
				}
			} catch (InterruptedException e) {
				Log.e("<<foregroundTask>>", e.getMessage());
			}
		}// run
	};// backgroundTask
}// ParaTaskProgressBarActivity

