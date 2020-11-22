package javaPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class EAutomaton extends Automaton {

    public static int counter;
    public HashMap<Integer, ArrayList<EAutomaton>> sons;

    public EAutomaton(HashMap<Integer, ArrayList<EAutomaton>> sons,
                      boolean isFinal){
        super(counter,isFinal);
        this.sons = sons;
        counter++;
    }

    public static EAutomaton determine(EAutomaton root){
        EAutomaton[] ta = getAll(root);
        for (EAutomaton a : ta)
            ta[a.id] = absorb(a);

        for (EAutomaton a : ta)
            for(Integer key : a.sons.keySet()) {
                ArrayList<EAutomaton> value = new ArrayList<>();
                for (EAutomaton s : a.sons.get(key)) {
                    value.add(ta[s.id]);
                }
                a.sons.put(key, value);
            }
        return renameAll(ta[0]);
    }

    public static EAutomaton absorb(EAutomaton a){
        ArrayList<EAutomaton> eps = a.sons.getOrDefault(-1, null);
        if(eps == null) return a;
        a.sons.remove(-1);
        for(EAutomaton e : eps){
            if(e.terminal) a.terminal = true;
            for(Integer key : e.sons.keySet()) {
                a.merge(key, e.sons.get(key));
            }
        }
        return absorb(a);
    }

    public static EAutomaton renameAll(EAutomaton root){
        EAutomaton[] tab = getAll(root);
        int counter = 0;
        Stack<EAutomaton> s = new Stack<>();
        s.push(root);
        while(!s.isEmpty()){
            EAutomaton a = s.pop();
            if (a.id < 0) continue;
            a.id = -counter;
            counter++;
            for(Integer key : a.sons.keySet())
                for(EAutomaton e : a.sons.get(key))
                    s.push(e);
        }
        for(EAutomaton e : tab)
            if (e != null && e.id < 0)
                e.id = -e.id;
        EAutomaton.counter = counter;

        return root;
    }

    public static EAutomaton[] getAll(EAutomaton root){
        EAutomaton[] ta = new EAutomaton[EAutomaton.getCounter()];
        Stack<EAutomaton> s = new Stack<>();
        s.push(root);
        while(!s.isEmpty()){
            EAutomaton a = s.pop();
            if (ta[a.id] != null) continue;
            ta[a.id] = a;
            for(Integer key : a.sons.keySet())
                for(EAutomaton e : a.sons.get(key))
                    s.push(e);
        }
        return ta;
    }

    public void put(Integer key, EAutomaton a) {
        if (a == null) return;
        ArrayList<EAutomaton> value =
                (ArrayList<EAutomaton>) sons.getOrDefault(key,
                        new ArrayList<>()).clone();
        value.add(a);
        sons.put(key, value);
    }

    public void initialize(RegExTree ret){
        switch (ret.root){
            case RegEx.CONCAT:
                // Creation de la deuxieme partie du chemin
                EAutomaton s = new EAutomaton(
                        (HashMap<Integer, ArrayList<EAutomaton>>)
                                sons.clone(),false);

                // Modification des chemins du noeud courant
                sons.clear(); this.put(-1, s);

                // Calcul java.RegEx R1 dans this puis R2 dans s
                this.initialize(ret.subTrees.get(0));
                s.initialize(ret.subTrees.get(1));
                break;
            case RegEx.ALTERN:
                // Creation chemin alternatif R1
                EAutomaton s1 = new EAutomaton(new HashMap<>(),false);
                EAutomaton e1 = new EAutomaton(
                        (HashMap<Integer, ArrayList<EAutomaton>>)
                                sons.clone(),false);
                // Liaison + Calcul java.RegEx R1
                s1.put(-1, e1); s1.initialize(ret.subTrees.get(0));

                // Creation chemin alternatif R2
                EAutomaton s2 = new EAutomaton(new HashMap<>(),false);
                EAutomaton e2 = new EAutomaton(
                        (HashMap<Integer, ArrayList<EAutomaton>>)
                                sons.clone(),false);
                // Liaison + Calcul java.RegEx R2
                s2.put(-1, e2); s2.initialize(ret.subTrees.get(1));

                // Modification des chemins du noeud courant
                sons.clear(); this.put(-1, s1); this.put(-1, s2);
                break;
            case RegEx.ETOILE:
                // Creation du chemin optionnel
                EAutomaton so = new EAutomaton(new HashMap<>(),false);
                EAutomaton eo = new EAutomaton(
                        (HashMap<Integer, ArrayList<EAutomaton>>)
                                sons.clone(),false);

                so.put(-1, eo);
                eo.put(-1, so);
                this.put(-1, so);
                so.initialize(ret.subTrees.get(0));
                break;
            default:
                sons.put(ret.root, sons.remove(-1));
        }
    }

    public void merge(Integer key, ArrayList<EAutomaton> l){
        ArrayList<EAutomaton> value = sons.getOrDefault(key, new ArrayList<>());
        for (EAutomaton e : l){
            if(!value.contains(e)) value.add(e);
        }
        sons.put(key, value);

    }

    public static int getCounter(){
        return counter;
    }

    public String toString(){
        if (terminal) return "T" + id + sons.toString();
        else return id + sons.toString();
    }


}
