package pt.runtime;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class CloudTaskFourArgs<R, T1, T2, T3, T4> extends AbstractCloudTask<R> {

	private T1 paramOne = null;
	private T2 paramTwo = null;
	private T3 paramThree = null;
	private T4 paramFour = null;
	
	public CloudTaskFourArgs(boolean hasNoReturn, String remoteIP, String remotePort, String userName,
			String password, String namingFactory, Class<?> remoteInterface, Method invokedMethod) {
		super(hasNoReturn, remoteIP, remotePort, userName, password, namingFactory, remoteInterface, invokedMethod);
	}
	
	public TaskID<R> start(T1 paramOne, T2 paramTwo, T3 paramThree, T4 paramFour){
		this.paramOne = paramOne;
		this.paramTwo = paramTwo;
		this.paramThree = paramThree;
		this.paramFour = paramFour;
		return this.submitToTaskPool();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void customizedExecution(Object proxy) throws Throwable {
		Object p1 = (paramOne instanceof TaskID<?>) ? ((TaskID<?>)paramOne).getReturnResult() : paramOne;
		Object p2 = (paramTwo instanceof TaskID<?>) ? ((TaskID<?>)paramTwo).getReturnResult() : paramTwo;
		Object p3 = (paramThree instanceof TaskID<?>) ? ((TaskID<?>)paramThree).getReturnResult() : paramThree;
		Object p4 = (paramFour instanceof TaskID<?>) ? ((TaskID<?>)paramFour).getReturnResult() : paramFour;
		this.futureResult = (Future<R>) this.invokedMethod.invoke(this.remoteInterface.cast(proxy), p1, p2, p3, p4);
	}

}
