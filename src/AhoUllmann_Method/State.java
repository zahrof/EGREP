package AhoUllmann_Method;

public class State {

    public int id;
    public boolean terminal;

    public State(int id, boolean terminal){
        this.id = id;
        this.terminal = terminal;
    }

    public String toString(){
        if (terminal)
            return "T" + id;
        else
            return "" + id;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof State)) return false;
        State s = (State) o;
        return id == s.id && terminal == s.terminal;
    }
}
