package pt.runtime;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.naming.Context;
import javax.naming.NamingException;

public abstract class AbstractCloudTask<R> extends TaskInfo<R> {
	protected String remoteIP = "";
	protected String remotePort = "";
	protected String userName = "";
	protected String password = "";
	protected String appName = ""; //EAR name
	protected String moduleName = ""; // EJB jar name
	protected String beanName = ""; //bean name
	protected String qualifiedRemoteInterfaceName = "";
	protected String javaNamingFactory = "";
	protected Class<?> remoteInterface = null;
	protected Method invokedMethod = null;
	protected boolean ejbSet = false;
	protected Future<R> futureResult = null;
	protected Throwable exception = null;
	protected boolean hasException = false;
	
	protected AbstractCloudTask(boolean hasNoReturn, String remoteIP, String remotePort, String userName, String password, String namingFactory, Class<?> remoteInterface, Method invokedMethod) {
		this.hasNoReturn = hasNoReturn;
		this.isCloudTask = true;
		this.remoteIP = remoteIP;
		this.remotePort = remotePort;
		this.userName = userName;
		this.password = password;
		this.javaNamingFactory = namingFactory;
		 //remember method "isInterface" indicates if the specified class is an interface
		// if(!remoteInterface.isInterface()) --> maybe this is better to be checked during the annotation processing phase
		this.remoteInterface = remoteInterface;
		this.invokedMethod = invokedMethod;
		//invokedMethod.invoke(obj, args...); --> obj is the instance on which the invocation is made, and args are the arguments
		//invokedMethod.getReturnType()
	}
	
	public void setEJB(String appName, String moduleName, String ejbName, String qualifiedRemoteInterfaceName) {
		this.appName = appName;
		this.moduleName = moduleName;
		this.beanName = ejbName;
		this.qualifiedRemoteInterfaceName = qualifiedRemoteInterfaceName;
		ejbSet = true;
	}
	
	/**
	 * This method does not perform any tasks for Cloud tasks. A Cloud task just needs to implement this method because
	 * it extends TaskInfo.
	 */
	@Override
	public R execute() throws Throwable {
		return null;
	}
	
	/**
	 * This is the method that actually needs to be implemented by every cloud task for executing its task.
	 */
	public void executeCloudTask() {
		try {
			Object proxy = this.getRemoteObject();
			if(proxy != null)
				customizedExecution(proxy);
			else
				throw new RuntimeException("ERROR MUST HAVE OCCURRED DURING LOOKING UP REMOTE OBJECT IN @PT RUNTIME!");	
		}catch(Throwable e) {
			this.setException(e);
		}
	}
	
	protected abstract void customizedExecution(Object proxy) throws Throwable;
	
	public void setRemoteIP(String IP) {
		this.remoteIP = IP;
	}
	
	public void setRemotePort(String port) {
		this.remotePort = port;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRemoteInterface(Class<?> remoteInterface) {
		this.remoteInterface = remoteInterface;
	}
	
	public void setInvokedMethod(Method method) {
		this.invokedMethod = method;
	}
	
	public String getRemoteIP() {
		return this.remoteIP;
	}
	
	public String getRemotePort() {
		return this.remotePort;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Class<?> getRemoteInterface(){
		return this.remoteInterface;
	}
	
	public Method getInvokedMethod() {
		return this.invokedMethod;
	}
	
	public String getAppName() {
		return this.appName;
	}
	
	public String getModuleName() {
		return this.moduleName;
	}
	
	public String getBeanName() {
		return this.beanName;
	}
	
	public String getQualifiedRemoteInterfaceName() {
		return this.qualifiedRemoteInterfaceName;
	}
	
	public boolean isEjbSet() {
		return this.ejbSet;
	}
	
	public boolean resultIsReady() {
		if(this.hasNoReturn()) {
			return true;
		}
		else {
			return this.futureResult.isDone();
		}			
	}
	
	public R getResult() throws InterruptedException, ExecutionException {
		if(this.hasNoReturn())
			return null;
		
		if(resultIsReady())
			return this.futureResult.get();
		else
			return null;
	}
	
	public boolean hasException() {
		return this.hasException;
	}
	
	public Throwable getException() {
		return this.exception;
	}
	
	protected void setException(Throwable exception) {
		this.exception = exception;
		this.hasException = true;
	}
	
	protected Object getRemoteObject(){
		Context initialContext = null;
		String lookupString = "";
		try {
			if(this.javaNamingFactory.isEmpty())
				initialContext = AtPtCloudClient.getInitialContext(remoteIP, remotePort, userName, password);
			else
				initialContext = AtPtCloudClient.getInitialContext(javaNamingFactory, remoteIP, remotePort, userName, password);
			
			lookupString = AtPtCloudClient.getLookupString(appName, moduleName, beanName, qualifiedRemoteInterfaceName);
			return initialContext.lookup(lookupString);
		}catch(NamingException e) {
			System.err.println("ERROR OCCURRED WHILE LOOKING UP " + lookupString + " IN @PT RUNTIME!");
			e.printStackTrace();
		}
		return null;
	}
	
	protected TaskID<R> submitToTaskPool(){
		return TaskpoolFactory.getTaskpool().enqueue(this);
	}
}
