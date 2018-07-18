package pt.runtime;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class CloudTaskTwelveArgs<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> extends AbstractCloudTask<R> {
	
	private T1 paramOne = null;			private T2 paramTwo = null;			private T11 paramEleven = null;
	private T3 paramThree = null;		private T4 paramFour = null;		private T12 paramTwelve = null;
	private T5 paramFive = null;		private T6 paramSix = null;
	private T7 paramSeven = null;		private T8 paramEight = null;
	private T9 paramNine = null;		private T10 paramTen = null;		
	
	public CloudTaskTwelveArgs(boolean hasNoReturn, String remoteIP, String remotePort, String userName,
			String password, String namingFactory, Class<?> remoteInterface, Method invokedMethod) {
		super(hasNoReturn, remoteIP, remotePort, userName, password, namingFactory, remoteInterface, invokedMethod);
	}
	
	public TaskID<R> start(T1 paramOne, T2 paramTwo, T3 paramThree, T4 paramFour, T5 paramFive, T6 paramSix, T7 paramSeven, T8 paramEight, T9 paramNine,
			T10 paramTen, T11  paramEleven, T12 paramTwelve){
		this.paramOne = paramOne;		this.paramTwo = paramTwo;			this.paramEleven = paramEleven;
		this.paramThree = paramThree;	this.paramFour = paramFour;			this.paramTwelve = paramTwelve;
		this.paramFive = paramFive;		this.paramSix = paramSix;
		this.paramSeven = paramSeven;	this.paramEight = paramEight;
		this.paramNine = paramNine;		this.paramTen = paramTen;		
		
		return this.submitToTaskPool();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void customizedExecution(Object proxy) throws Throwable {
		Object p1 = (paramOne instanceof TaskID<?>) ? ((TaskID<?>)paramOne).getReturnResult() : paramOne;
		Object p2 = (paramTwo instanceof TaskID<?>) ? ((TaskID<?>)paramTwo).getReturnResult() : paramTwo;
		Object p3 = (paramThree instanceof TaskID<?>) ? ((TaskID<?>)paramThree).getReturnResult() : paramThree;
		Object p4 = (paramFour instanceof TaskID<?>) ? ((TaskID<?>)paramFour).getReturnResult() : paramFour;
		Object p5 = (paramFive instanceof TaskID<?>) ? ((TaskID<?>)paramFive).getReturnResult() : paramFive;
		Object p6 = (paramSix instanceof TaskID<?>) ? ((TaskID<?>)paramSix).getReturnResult() : paramSix;
		Object p7 = (paramSeven instanceof TaskID<?>) ? ((TaskID<?>)paramSeven).getReturnResult() : paramSeven;
		Object p8 = (paramEight instanceof TaskID<?>) ? ((TaskID<?>)paramEight).getReturnResult() : paramEight;
		Object p9 = (paramNine instanceof TaskID<?>) ? ((TaskID<?>)paramNine).getReturnResult() : paramNine;
		Object p10 = (paramTen instanceof TaskID<?>) ? ((TaskID<?>)paramTen).getReturnResult() : paramTen;
		Object p11 = (paramEleven instanceof TaskID<?>) ? ((TaskID<?>)paramEleven).getReturnResult() : paramEleven;
		Object p12 = (paramTwelve instanceof TaskID<?>) ? ((TaskID<?>)paramTwelve).getReturnResult() : paramTwelve;
		this.futureResult = (Future<R>) this.invokedMethod.invoke(this.remoteInterface.cast(proxy), p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
	}
}
