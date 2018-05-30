package pt.runtime;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class CloudTaskThreeArgs<R, T1, T2, T3> extends AbstractCloudTask<R> {
	
	private T1 paramOne = null;
	private T2 paramTwo = null;
	private T3 paramThree = null;

	public CloudTaskThreeArgs(boolean hasNoReturn, String remoteIP, String remotePort, String userName,
			String password, String namingFactory, Class<?> remoteInterface, Method invokedMethod) {
		super(hasNoReturn, remoteIP, remotePort, userName, password, namingFactory, remoteInterface, invokedMethod);
	}
	
	public TaskID<R> start(T1 paramOne, T2 paramTwo, T3 paramThree){
		this.paramOne = paramOne;
		this.paramTwo = paramTwo;
		this.paramThree = paramThree;
		return this.submitToTaskPool();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void customizedExecution(Object proxy) throws Throwable {
		this.futureResult = (Future<R>) this.invokedMethod.invoke(this.remoteInterface.cast(proxy), paramOne, paramTwo, paramThree);
	}

}
