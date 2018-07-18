package pt.runtime;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AtPtCloudClient {
	
	public static Context getInitialContext(String namingFactory, String url, String port, String userName, String password) {
		String t3URL = "t3://" + url + ":" + port;
		Properties prop = new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, namingFactory);
		prop.put(Context.PROVIDER_URL, t3URL);
		prop.put(Context.SECURITY_PRINCIPAL, userName);
		prop.put(Context.SECURITY_CREDENTIALS, password);
		Context initialContext = null;
		try {
			initialContext = new InitialContext(prop);
		} catch (NamingException e) {
			System.err.println("ERROR OCCURED WHILE TRYING TO BUILD THE INITIAL CONTEXT FOR ATPT!");
			e.printStackTrace();
		}
		return initialContext;
	}
	
	public static Context getInitialContext(String url, String port, String userName, String password) {
		String namingFactory = ParaTask.getJavaNamingFactory();
		return getInitialContext(namingFactory, url, port, userName, password);
	}	
	
	public static String getLookupString(String appName, String moduleName, String beanName, String qualifiedRemoteInterfaceName) {
		String lookupString = "";
		String protocol = "java:global";
		lookupString = protocol + "/" + appName + "/" + moduleName + "/" + beanName + "!" + qualifiedRemoteInterfaceName;
		return lookupString;
	}
}
