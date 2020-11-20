import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class States {

    private static int counter = 0;
    public Set<State> states = new HashSet<>();
    public int id;
    public boolean terminal = false;
    public Set<Integer> ways= new HashSet<>();

    public States(int id, boolean terminal) {
        this.id=id;
        this.terminal=terminal;
    }

    public static void reset(){
        counter = 0;
    }

    public States(){
        this.id = counter;
        counter++;
    }

    public States(Set<State> states){
        this.id = counter;
        counter++;
        this.states = states;
        for(State s : states) {
            if (s.terminal) terminal=true;
        }
    }

    public States add(State s,int way){
        states.add(s);
        this.ways.add(way);
        if (s.terminal) terminal = true;
        return this;
    }
    public States add(State s, Set<Integer> ways) {
        states.add(s);
        this.ways=ways;
        if (s.terminal) terminal = true;
        return this;
    }


    public boolean present(Set<States> set){
        for(States s : set) if(s.states.containsAll(states)) return true;
        return false;
    }

    public boolean contains (State s){
        for (State e : states) if (s.equals(e)) return true;
        return false;
    }

    public boolean containsAll(Collection<State> c){
        for (State s : c) if (!contains(s)) return false;
        return true;
    }

    public String toString(){
        if (terminal)
            return "T" + id + " " + states.toString();
        else
            return id + " " + states.toString();
    }

    protected static boolean equals(States a, States s) {
        if(a.terminal !=s.terminal) return false;
        if(a.states.size()!=s.states.size()) return false;
        for (State b: a.states) {
            boolean present = false;
            for (State c: s.states)
                if(c.id ==b.id && c.terminal ==b.terminal) present=true;
            if(!present) return false;
        }
        return true;

    }


}
