import java.util.HashMap;

public class Automata {

    public int id;
    public static int counter;
    public boolean terminal;
    public HashMap<String, Automata> sons;

    public Automata(HashMap<String, Automata> sons, boolean isFinal){
        this.sons = sons;
        this.terminal = isFinal;
        counter++;
        this.id = counter;
    }

    public Automata(AdjacencyMatrix AM){
        
    }

    public String toString(){
        if (terminal)
            return id + " : TERMINAL\n" + sons.toString();
        else
            return id + ":\n" + sons.toString();
    }


}
