/**
 * 
 */
package pt.test;

import java.lang.reflect.Method;

/**
 * @author Kingsley
 * 
 */
public class Benchmark {
	private Object instance;

	private Method method;

	private Object[] arguments;

	public Benchmark(Object instance, Method method, Object[] arguments) {
		super();
		this.instance = instance;
		this.method = method;
		this.arguments = arguments;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
}
