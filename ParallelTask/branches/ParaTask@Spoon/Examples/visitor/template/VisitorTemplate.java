package spoon.examples.visitor.template;

import spoon.template.Local;
import spoon.template.Parameter;
import spoon.template.Template;

/**
 * The template {@link VisitorTemplate} defines an {@link #accept(_Visitor_)}
 * template method to be introduced in a target visited class. It takes two
 * parameters: {@link #_target_} is the simple name of the target visited class,
 * and {@link #_Visitor_} is the actual type of the visitor.
 */
public class VisitorTemplate implements Template {

    /**
     * The simple name of the target visited class.
     */
    @Parameter
    String _target_;

    /**
     * The actual type of the visitor.
     */
    @Parameter
    Class<?> _Visitor_;

    /**
     * The template's constructor that binds its parameters.
     */
    @Local
    public VisitorTemplate(String target, Class<?> visitorType) {
        _target_ = target;
        _Visitor_ = visitorType;
    }

    /**
     * The <code>accept</code> template method is the core of this template.
     * It is simple since it just calls back the right visitation method on the
     * passed visitor.
     */
    public void accept(_Visitor_ visitor) {
        visitor.visit_target_(this);
    }

}