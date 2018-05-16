package pt.runtime;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class CloudTaskNoArgs<R> extends CloudTask<R> {
	
	public CloudTaskNoArgs(boolean hasNoReturn, String remoteIP, String remotePort, String userName, String password, String namingFactory, Class<?> remoteInterface, Method invokedMethod) {
		super(hasNoReturn, remoteIP, remotePort, userName, password, namingFactory, remoteInterface, invokedMethod);
	}	
	
	@SuppressWarnings("unchecked")
	public void executeCloudTask() throws Throwable{
		futureResult = (Future<R>) this.invokedMethod.invoke(this.remoteInterface.cast(this.getRemoteObject()));		
	}
}
