package AhoUllmann_Method;

public class Couple {
    public State a;
    public State b;

    public Couple(State a, State b){
        this.a = a; this.b = b;
    }

    public boolean equals(Object o){
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof Couple)) return false;
        Couple t = (Couple) o;
        return a.equals(t.a) && b.equals(t.b) ||
                a.equals(t.b) && b.equals(t.a);
    }
}
