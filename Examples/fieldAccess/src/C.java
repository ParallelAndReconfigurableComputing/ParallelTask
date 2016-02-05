package spoon.examples.fieldaccess.src;

import spoon.examples.fieldaccess.annotation.Access;

public class C {

    @Access
    int i;

    public void m() {
        i = i + 12;
    }

}