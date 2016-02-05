package spoon.examples.nton.template;

import spoon.reflect.reference.CtTypeReference;
import spoon.template.Local;
import spoon.template.Parameter;
import spoon.template.Template;

public class NtonCodeTemplate implements Template, _TargetType_ {
	@Parameter
	static int _n_;

	@Parameter
	CtTypeReference<?> _TargetType_;

	static _TargetType_[] instances = new _TargetType_[_n_];

	static int instanceCount = 0;

	@Local
	public NtonCodeTemplate(CtTypeReference<?> targetType, int n) {
		_n_ = n;
		_TargetType_ = targetType;
	}

	@Local
	public void initializer() {
		if (instanceCount >= _n_) {
			throw new RuntimeException("max number of instances reached");
		}
		instances[instanceCount++] = this;
	}

	public int getInstanceCount() {
		return instanceCount;
	}

	public _TargetType_ getInstance(int i) {
		if (i > _n_)
			throw new RuntimeException("instance number greater than " + _n_);
		return instances[i];
	}

	public int getMaxInstanceCount() {
		return _n_;
	}

	class Test {
		public void _name_() {}
	}
}

interface _TargetType_ {

}