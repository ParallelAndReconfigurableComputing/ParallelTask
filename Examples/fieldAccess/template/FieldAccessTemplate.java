package spoon.examples.fieldaccess.template;

import spoon.examples.fieldaccess.annotation.Getter;
import spoon.examples.fieldaccess.annotation.Setter;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.reference.CtTypeReference;
import spoon.template.Local;
import spoon.template.Parameter;
import spoon.template.Template;

/**
 * This template defines the needed template code for matching and/or generating
 * field accesses through getters and setters:
 */
public class FieldAccessTemplate<_FieldType_> implements Template {

	@Parameter
	CtTypeReference<?> _FieldType_;

	@Parameter("_field_")
	String __field_;

	@Parameter
	String _Field_;

	@Parameter
	CtExpression<_FieldType_> _setExpression_;

	@Parameter
	CtExpression<FieldAccessTemplate<_FieldType_>> _target_;

	@SuppressWarnings("unchecked")
	@Local
	public FieldAccessTemplate(CtTypeReference<?> type, String field,
			CtFieldAccess<_FieldType_> fieldAccess,
			CtExpression<_FieldType_> setExpression) {
		_FieldType_ = type;
		__field_ = field;
		char[] chars = field.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		_Field_ = new String(chars);
		if (fieldAccess != null) {
			_target_ = (CtExpression<FieldAccessTemplate<_FieldType_>>) fieldAccess
					.getTarget();
			if (!fieldAccess.getVariable().isStatic() && _target_ == null) {
				_target_ = (CtExpression<FieldAccessTemplate<_FieldType_>>) fieldAccess
						.getFactory().Code().createThisAccess(
								fieldAccess.getVariable().getDeclaringType());
			}
		}
		_setExpression_ = setExpression;
	}

	@Local
	_FieldType_ _field_;

	/**
	 * This template code defines the setter of a field {@link #_field_}. Note
	 * the template parameter {@link #_FieldType_} that contains the field's
	 * type (also defined as a type parameter) and the {@link #_Field_} that
	 * contains the name of the field with the first letter uppercased.
	 */
	@Setter
	public void set_Field_(_FieldType_ _field_) {
		this._field_ = _field_;
	}

	/**
	 * This template code defines the getter of a field {@link #_field_}. Note
	 * the template parameter {@link #_FieldType_} that contains the field's
	 * type (also defined as a type parameter) and the {@link #_Field_} that
	 * contains the name of the field with the first letter uppercased.
	 */
	@Getter
	public _FieldType_ get_Field_() {
		return _field_;
	}

	/**
	 * This template code defines the invocation of a setter for a field
	 * {@link #_field_}. The set expression is stored in the
	 * {@link #_setExpression_} template parameter. The {@link #_target_} can be
	 * null:
	 */
	@Local
	void setterInvocation() {
		_target_.S().set_Field_(_setExpression_.S());
	}

	/**
	 * This template code defines the invocation of a getter for a field
	 * {@link #_field_}. The {@link #_target_} can be null:
	 */
	@Local
	void getterInvocation() {
		_target_.S().get_Field_();
	}

}