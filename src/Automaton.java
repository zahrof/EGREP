public class Automaton {
    public int id;
    public boolean terminal=false;

    public Automaton(int id, boolean terminal) {
        this.id = id;
        this.terminal = terminal;
    }

    public Automaton(int id) {
        this.id = id;
    }
}
