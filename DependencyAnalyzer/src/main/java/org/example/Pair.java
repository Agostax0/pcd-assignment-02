package org.example;

import java.util.Objects;

public class Pair<A, B> {
    final A a;
    final B b;

    public Pair(A a1, B b1){
        this.a = a1;
        this.b = b1;
    }


    @Override
    public String toString() {
        return "Pair{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return a.equals(pair.a) && b.equals(pair.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
