package com.fnranked.ranked.util;

import java.util.Objects;

public class Quadruple<A,B,C,D> {

    A a;
    B b;
    C c;
    D d;

    public Quadruple(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }

    public D getD() {
        return d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadruple<?, ?, ?, ?> quadruple = (Quadruple<?, ?, ?, ?>) o;
        return Objects.equals(a, quadruple.a) &&
                Objects.equals(b, quadruple.b) &&
                Objects.equals(c, quadruple.c) &&
                Objects.equals(d, quadruple.d);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c, d);
    }
}
