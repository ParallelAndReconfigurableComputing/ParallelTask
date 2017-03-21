package annotatedHouse;


public class Complex {
    private final double re;

    private final double im;

    public Complex(double real ,double imag) {
        this.re = real;
        this.im = imag;
    }

    public java.lang.String toString() {
        if ((im) == 0)
            return (re) + "";
        
        if ((re) == 0)
            return (im) + "i";
        
        if ((re) < 0)
            return (((re) + " - ") + (-(im))) + "i";
        
        return (((re) + " + ") + (im)) + "i";
    }

    public double abs() {
        return java.lang.Math.hypot(re, im);
    }

    public double phase() {
        return java.lang.Math.atan2(im, re);
    }

    public annotatedHouse.Complex plus(annotatedHouse.Complex b) {
        double real = (annotatedHouse.Complex.this.re) + (b.re);
        double imag = (annotatedHouse.Complex.this.im) + (b.im);
        return new annotatedHouse.Complex(real , imag);
    }

    public annotatedHouse.Complex plus(annotatedHouse.Complex a, annotatedHouse.Complex b) {
        double real = (a.re) + (b.re);
        double imag = (a.im) + (b.im);
        return new annotatedHouse.Complex(real , imag);
    }

    public annotatedHouse.Complex minus(annotatedHouse.Complex b) {
        double real = (annotatedHouse.Complex.this.re) - (b.re);
        double imag = (annotatedHouse.Complex.this.im) - (b.im);
        return new annotatedHouse.Complex(real , imag);
    }

    public annotatedHouse.Complex times(annotatedHouse.Complex b) {
        double real = ((annotatedHouse.Complex.this.re) * (b.re)) - ((annotatedHouse.Complex.this.im) * (b.im));
        double imag = ((annotatedHouse.Complex.this.re) * (b.im)) + ((annotatedHouse.Complex.this.im) * (b.re));
        return new annotatedHouse.Complex(real , imag);
    }

    public annotatedHouse.Complex conjugate() {
        return new annotatedHouse.Complex(re , (-(im)));
    }

    public annotatedHouse.Complex reciprocal() {
        double scale = ((re) * (re)) + ((im) * (im));
        return new annotatedHouse.Complex(((re) / scale) , ((-(im)) / scale));
    }

    public double re() {
        return re;
    }

    public double im() {
        return im;
    }

    public annotatedHouse.Complex divides(annotatedHouse.Complex b) {
        return times(b.reciprocal());
    }

    public annotatedHouse.Complex exp() {
        return new annotatedHouse.Complex(((java.lang.Math.exp(re)) * (java.lang.Math.cos(im))) , ((java.lang.Math.exp(re)) * (java.lang.Math.sin(im))));
    }

    public annotatedHouse.Complex sin() {
        return new annotatedHouse.Complex(((java.lang.Math.sin(re)) * (java.lang.Math.cosh(im))) , ((java.lang.Math.cos(re)) * (java.lang.Math.sinh(im))));
    }

    public annotatedHouse.Complex cos() {
        return new annotatedHouse.Complex(((java.lang.Math.cos(re)) * (java.lang.Math.cosh(im))) , ((-(java.lang.Math.sin(re))) * (java.lang.Math.sinh(im))));
    }

    public annotatedHouse.Complex tan() {
        return sin().divides(cos());
    }
}

