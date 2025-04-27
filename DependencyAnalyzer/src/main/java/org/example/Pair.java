package org.example;

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
}
