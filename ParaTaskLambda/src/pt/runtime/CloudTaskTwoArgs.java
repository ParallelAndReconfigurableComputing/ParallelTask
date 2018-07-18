package pt.runtime;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class CloudTaskTwoArgs<R, T1, T2> extends AbstractCloudTask<R> {
	
	private T1 paramOne = null;
	private T2 paramTwo = null;

	public CloudTaskTwoArgs(boolean hasNoReturn, String remoteIP, String remotePort, String userName,
			String password, String namingFactory, Class<?> remoteInterface, Method invokedMethod) {
		super(hasNoReturn, remoteIP, remotePort, userName, password, namingFactory, remoteInterface, invokedMethod);
	}
	
	public TaskID<R> start(T1 paramOne, T2 paramTwo){
		this.paramOne = paramOne;
		this.paramTwo = paramTwo;
		return this.submitToTaskPool();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void customizedExecution(Object proxy) throws Throwable {
		Object p1 = (paramOne instanceof TaskID<?>) ? ((TaskID<?>)paramOne).getReturnResult() : paramOne;
		Object p2 = (paramTwo instanceof TaskID<?>) ? ((TaskID<?>)paramTwo).getReturnResult() : paramTwo;
		futureResult = (Future<R>) this.invokedMethod.invoke(this.remoteInterface.cast(proxy), p1, p2);		
	}
}
