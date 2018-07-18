package pt.runtime;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class CloudTaskOneArg<R, T1> extends AbstractCloudTask<R> {
	
	private T1 paramOne = null;

	public CloudTaskOneArg(boolean hasNoReturn, String remoteIP, String remotePort, String userName, String password,
			String namingFactory, Class<?> remoteInterface, Method invokedMethod) {
		super(hasNoReturn, remoteIP, remotePort, userName, password, namingFactory, remoteInterface, invokedMethod);
	}
	
	public TaskID<R> start(T1 param){
		paramOne = param;
		return this.submitToTaskPool();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void customizedExecution(Object proxy) throws Throwable {
		Object p1 = (paramOne instanceof TaskID<?>) ? ((TaskID<?>)paramOne).getReturnResult() : paramOne;
		futureResult = (Future<R>) this.invokedMethod.invoke(this.remoteInterface.cast(proxy), p1);		
	}
}
