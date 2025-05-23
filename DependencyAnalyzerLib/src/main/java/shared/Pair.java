package shared;

import java.util.Objects;

public class Pair<A, B> {
    final public A a;
    final public B b;

    public Pair(A a1, B b1){
        this.a = a1;
        this.b = b1;
    }


    @Override
    public String toString() {
        return "Pair{" +
                " start= " + a +
                ", end= " + b +
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
