package pt.runtime;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class CloudTaskEightArgs<R, T1, T2, T3, T4, T5, T6, T7, T8> extends AbstractCloudTask<R> {

	private T1 paramOne = null;			private T2 paramTwo = null;
	private T3 paramThree = null;		private T4 paramFour = null;
	private T5 paramFive = null; 		private T6 paramSix = null;
	private T7 paramSeven = null;		private T8 paramEight = null;
	
	public CloudTaskEightArgs(boolean hasNoReturn, String remoteIP, String remotePort, String userName,
			String password, String namingFactory, Class<?> remoteInterface, Method invokedMethod) {
		super(hasNoReturn, remoteIP, remotePort, userName, password, namingFactory, remoteInterface, invokedMethod);
	}
	
	public TaskID<R> start(T1 paramOne, T2 paramTwo, T3 paramThree, T4 paramFour, T5 paramFive, T6 paramSix, T7 paramSeven, T8 paramEight){
		this.paramOne = paramOne;		this.paramTwo = paramTwo;
		this.paramThree = paramThree;	this.paramFour = paramFour;
		this.paramFive = paramFive;		this.paramSix = paramSix;
		this.paramSeven = paramSeven;	this.paramEight = paramEight;
		
		return this.submitToTaskPool();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void customizedExecution(Object proxy) throws Throwable {
		this.futureResult = (Future<R>) this.invokedMethod.invoke(this.remoteInterface.cast(proxy), this.paramOne, this.paramTwo, this.paramThree, this.paramFour,
				this.paramFive, this.paramSix, this.paramSeven, this.paramEight);
	}

}
