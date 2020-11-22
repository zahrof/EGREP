package AhoUllmann_Method;

import java.util.*;

public class AdjacencyMatrix {

    protected ArrayList<State>[][] matrix;

    final static int ASCCI = 128;
    final static int EPSILON = ASCCI;

    public AdjacencyMatrix(EAutomata a){
        matrix = new ArrayList[EAutomata.getCounter()][ASCCI + 1];
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = new ArrayList<>();
        boolean[] checked = new boolean[EAutomata.getCounter()];
        Stack<EAutomata> process = new Stack<>();
        process.push(a);
        checked[a.id] = true;
        while(!process.empty()){
            EAutomata p = process.pop();
            for(Integer w : p.sons.keySet())
                for (EAutomata e : p.sons.get(w)) {
                    if (!checked[e.id]) {
                        checked[e.id] = true;
                        process.push(e);
                    }
                    if (w == -1) // epsilon transition
                        matrix[p.id][EPSILON].add(new State(e.id, e.terminal));
                    else
                        matrix[p.id][w].add(new State(e.id, e.terminal));
                }
        }
    }

    public AdjacencyMatrix(ArrayList<Transition> ways){
        matrix = new ArrayList[ways.size()][ASCCI + 1];
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = new ArrayList<>();
        for(Transition t : ways)
            matrix[t.source][t.character].add(
                    new State(t.destination, t.terminal));
    }

    public AdjacencyMatrix(int size) {
        this.matrix = new ArrayList[size][ASCCI+1];
    }


    public Set<State> epsilonCLosure(State s){
        Set<State> ec = new HashSet<>();
        ec.add(s);
        Stack<State> process = new Stack<>();
        process.push(s);
        while (!process.isEmpty()){
            State p = process.pop();
            if (matrix[p.id][EPSILON].isEmpty()) continue;
            for (State e : matrix[p.id][EPSILON]){
                ec.add(e);
                process.push(e);
            }
        }
        return ec;
    }

    public AdjacencyMatrix creationMinAutomata(Set<States> min) {

        AdjacencyMatrix res = new AdjacencyMatrix(min.size());
        for (States rs: min) {
            ArrayList<Integer> tab = new ArrayList<>();
            for(State s2: rs.states){
                for(int i=97; i <=99; i++){
                    if(this.matrix[s2.id][i]!=null) tab.add(i);
                }
            }
            for(int n: tab){
                if(res.matrix[rs.id][n]==null){
                    ArrayList<State> al = new ArrayList<>();
                    al.add(find(min,rs,n));
                    res.matrix[rs.id][n] = al;
                }
            }
        }
        return res;
    }

    private State find(Set<States> newAut, States s, int n) {
        Iterator<State> i = s.states.iterator();
        State a = null;
        if(i.hasNext()) a =  i.next();
        State res = this.matrix[a.id][n].get(0);

        for(States rs : newAut){
            for(State s3 : rs.states){
                if(s3.id ==res.id) {
                    return new State(rs.id,rs.terminal);
                }
            }
        }
        return res;
    }

    public class Transition{
        int source;
        int character;
        int destination;
        boolean terminal;

        public Transition(int s, int c, int d, boolean t){
            source = s;
            character = c;
            destination = d;
            terminal = t;
        }
    }

    // transformation from nfa to dfa
    public AdjacencyMatrix dfa(){
        ArrayList<Transition> ways = new ArrayList<>();
        Stack<States> process = new Stack<>();
        // 0 est toujours l'état initial, on l'ajoute donc en premier dans
        // la pile
        process.push(new States(epsilonCLosure(
                new State(0, false))));
        while(!process.isEmpty()){
            States p = process.pop();
            for (int i = 0; i < ASCCI; i++){
                Set<State> hs = new HashSet<>();
                for (State e : p.states) hs.addAll(
                        new HashSet<>(matrix[e.id][i]));
                for (State e: hs) hs.addAll(epsilonCLosure(e));
                if (hs.equals(p.states)){
                    ways.add(new Transition(p.id,i,p.id,p.terminal));
                    continue;
                }
                if (!hs.isEmpty()){
                    States s = new States(hs);
                    ways.add(new Transition(p.id, i, s.id, s.terminal));
                    process.push(s);
                }

            }
        }
        return new AdjacencyMatrix(ways);

    }

    public ArrayList<MinState> getMinAutomate(){
        MinState[] tab = new MinState[this.matrix.length];
       for (int i =0; i < this.matrix.length; i++) tab[i]= new MinState(i);
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < ASCCI; j++) {
                if (!matrix[i][j].isEmpty())
                    for (State s : matrix[i][j]) {
                        tab[s.id].terminal = s.terminal;
                        tab[i].sons.put(j, tab[s.id]);
                        ArrayList<MinState> value =
                                tab[s.id].father.getOrDefault(j,new ArrayList<>());
                        value.add(tab[i]);
                        tab[s.id].father.put(j,value);
                    }
            }
        }
        return new ArrayList<>(Arrays.asList(tab)) ;

    }

    public boolean present(Set<States> set, Collection<State> c){
        for (States s : set) if (s.containsAll(c)) return true;
        return false;
    }

    public boolean present(Set<States> set, State c){
        for (States s : set) if (s.contains(c)) return true;
        return false;
    }

    public boolean sameStateSet(State elt1, State elt2, Set<States> fnf){
        for (States s : fnf)
            if (s.contains(elt1) && s.contains(elt2)) return true;
        return false;
    }


    public Set<States> minimisation2() {
        getMinAutomate();
        Set<States> fnf = null;
        boolean changes = true;
        States.reset();
        do{
            Set<States> nouv = new HashSet<>();
            for(States s : fnf){
                if(s.states.size() == 1){
                    nouv.add(new States(s.states));
                    continue;
                }
                Set<Couple> couples = new HashSet<>();
                for(State e1 : s.states)
                    for(State e2 : s.states){
                        if(e1.equals(e2)) continue;
                        boolean into = false;
                        for (Couple c: couples) if(e1 == c.a && e2 == c.b
                                || e1 == c.b && e2 == c.a) into = true;
                        if (!into) couples.add(new Couple(e1, e2));
                    }
                for (Couple c : couples){
                    boolean equal = true;
                    Set<Integer> ways = new HashSet<>();
                    for(int i = 0; i < ASCCI; i++){
                        if(!matrix[c.a.id][i].isEmpty()
                                && matrix[c.b.id][i].isEmpty()
                        || matrix[c.a.id][i].isEmpty() &&
                                !matrix[c.b.id][i].isEmpty()){
                            equal = false;
                            if(!present(nouv, c.a))
                                nouv.add((new States()).add(c.a,i));
                            if(!present(nouv,c.b))
                                nouv.add((new States()).add(c.b,i));
                            break;
                        }
                        if(matrix[c.a.id][i].isEmpty()
                                && matrix[c.b.id][i].isEmpty()) continue;
                        if(!sameStateSet(c.a, c.b, fnf)){
                            equal = false;
                            break;
                        }
                        ways.add(i);
                    }
                    if(equal) nouv = ajout(c.a,c.b,nouv,ways);
                }
            }
            if(equals(nouv,fnf)) changes = false;
            fnf = nouv;
        } while(changes);
        return fnf;
    }

    private Set<States> ajout(State a, State b, Set<States> nouv,
                              Set<Integer> ways) {
        for (States s: nouv)
            if(s.contains(a)&&s.contains(b)) return nouv;
        Set<States> res = new HashSet<>();
        boolean ajoutAB=false;
        for (States s1: nouv) {
            States aux = new States();
            for (State s2 : s1.states) {
                if ((s2.equals(a))||(s2.equals(b))){
                    aux.add(a,ways).add(b,ways);
                    ajoutAB=true;
                }
                else aux.add(s2,ways);
            }
            if(aux.states.size()>0)res.add(aux);
        }
        if(!ajoutAB) res.add(new States().add(a,ways).add(b,ways));
        return res;
    }

    private boolean equals(Set<States> newStates, Set<States> fnf) {
        if(newStates.size()!=fnf.size()) return false;
        for (States rs: newStates) {
            boolean contains = false;
            for (States rs2: fnf) {
                if(States.equals(rs,rs2)) contains = true;
            }
            if(!contains) return false;
        }
        return true;
    }


    public String toString() {
        String res = "MATRIX : \n";
        for(int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                if (!matrix[i][j].isEmpty())
                    res = res + i + "(" + (char)j + ") -> " +
                            matrix[i][j] + "\n";

        return res;
    }

}
