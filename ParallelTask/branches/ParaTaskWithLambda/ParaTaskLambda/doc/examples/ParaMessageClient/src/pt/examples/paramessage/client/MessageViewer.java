package pt.examples.paramessage.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MessageViewer extends Activity {
    private Button disconnectButton;
    private Button queryButton;
    private TextView messageTextView;
    private MessageServiceConnection remoteServiceConnection;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        remoteServiceConnection = new MessageServiceConnection(this);
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
    
    public void theMessageWasReceivedAsynchronously(String message) {
    	messageTextView.setText(message);
    }
}
