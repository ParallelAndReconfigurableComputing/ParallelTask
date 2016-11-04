package pt.examples.paramessage;

import android.os.RemoteException;

public class TimeMessageService extends IRemoteMessageService.Stub {

	private final ParaMessageService service;
	
	public TimeMessageService(ParaMessageService service) {
		this.service = service;
	}
	
	@Override
	public String getMessage() throws RemoteException {
		return service.getStringForRemoteService();
	}

}
