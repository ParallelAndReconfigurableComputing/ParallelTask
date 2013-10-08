package pt.examples.paramessage;

import pt.examples.DisplayRemoteMessage.R;
import pt.examples.DisplayRemoteMessage.R.id;
import pt.examples.DisplayRemoteMessage.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DisplayRemoteMessage extends Activity {
    private Button disconnectButton;
    private Button queryButton;
    private TextView messageTextView;
    private RemoteMessageServiceServiceConnection remoteServiceConnection;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        remoteServiceConnection = new RemoteMessageServiceServiceConnection(this);
        disconnectButton = (Button)findViewById(R.id.disconnectButton);
        queryButton = (Button)findViewById(R.id.queryButton);
        messageTextView = (TextView)findViewById(R.id.messageTextView);
        
        disconnectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				remoteServiceConnection.safelyDisconnectTheService();
			}
		});
        
        queryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				remoteServiceConnection.safelyQueryMessage();
			}
		});
    }
    
    void theMessageWasReceivedAsynchronously(String message) {
    	messageTextView.setText(message);
    }
}