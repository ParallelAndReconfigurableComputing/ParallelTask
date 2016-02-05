package spoon.examples.visitor.template;

/**
 * This intermediate type is used to represent the visitor type in a generic
 * manner. Every reference to it in a template code will be substituted by a
 * reference to the type defined by a type template parameter named "_Visitor_".
 * 
 * <p>
 * When progamming templates, using intermediate types that act like shadows for
 * concrete type is often required. In several case, they can be avoided by
 * using a type parameter (aka generics) that stands for a concrete type and
 * that will be substituted. However, in this case, since we want to precise
 * that the type holds a parameterized method, we need to define it as an
 * intermediate interface.
 */
interface _Visitor_ {
    /**
     * This method stands for the visitation methods of the visitor. Here,
     * "_target_" is a string template parameter that contains the simple name
     * of a visited class. The visited element passed as a parameter is of an
     * unknown type and is hence set to <code>Object</code>.
     */
    void visit_target_(Object e);
}