package AhoUllmann_Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class EAutomata {

    public int id;
    public static int counter;
    public boolean terminal;
    public HashMap<Integer, ArrayList<EAutomata>> sons;

    public EAutomata(HashMap<Integer, ArrayList<EAutomata>> sons, boolean isFinal){
        this.sons = sons;
        this.terminal = isFinal;
        id = counter;
        counter++;

    }

    public EAutomata(Set<States> min, AdjacencyMatrix dfa) {
        this.sons = new HashMap<>();
        id = counter;
        counter++;
        boolean initialized = false;
        for(States s : min){
            if(!initialized){
                this.terminal = s.terminal;
                initialized=true;
            }
            for (int i = 0; i < AdjacencyMatrix.ASCCI; i++){
                if(dfa.matrix[s.id][i].size()>1){

                   // this.put(i);
                }
            }

        }
    }

    public void put(Integer key, EAutomata a) {
        if (a == null) return;
        ArrayList<EAutomata> value = (ArrayList<EAutomata>) sons.getOrDefault(key, new ArrayList<>()).clone();
        value.add(a);
        sons.put(key, value);
    }

    public void fromRegExTree(RegExTree ret){
        switch (ret.root){
            case RegEx.CONCAT:
                // Creation de la deuxieme partie du chemin
                EAutomata s = new EAutomata((HashMap<Integer, ArrayList<EAutomata>>) sons.clone(),false);

                // Modification des chemins du noeud courant
                sons.clear(); this.put(-1, s);

                // Calcul AhoUllmann_Method.RegEx R1 dans this puis R2 dans s
                this.fromRegExTree(ret.subTrees.get(0));
                s.fromRegExTree(ret.subTrees.get(1));
                break;
            case RegEx.ALTERN:
                // Creation chemin alternatif R1
                EAutomata s1 = new EAutomata(new HashMap<>(),false);
                EAutomata e1 = new EAutomata((HashMap<Integer, ArrayList<EAutomata>>) sons.clone(),false);
                s1.put(-1, e1); s1.fromRegExTree(ret.subTrees.get(0)); // Liaison + Calcul AhoUllmann_Method.RegEx R1

                // Creation chemin alternatif R2
                EAutomata s2 = new EAutomata(new HashMap<>(),false);
                EAutomata e2 = new EAutomata((HashMap<Integer, ArrayList<EAutomata>>) sons.clone(),false);
                s2.put(-1, e2); s2.fromRegExTree(ret.subTrees.get(1)); // Liaison + Calcul AhoUllmann_Method.RegEx R2

                // Modification des chemins du noeud courant
                sons.clear(); this.put(-1, s1); this.put(-1, s2);
                break;
            case RegEx.ETOILE:
                // Creation du chemin optionnel
                EAutomata so = new EAutomata(new HashMap<>(),false);
                EAutomata eo = new EAutomata((HashMap<Integer, ArrayList<EAutomata>>) sons.clone(),false);

                so.put(-1, eo);
                eo.put(-1, so);
                this.put(-1, so);
                so.fromRegExTree(ret.subTrees.get(0));
                break;
            default:
                sons.put(ret.root, sons.remove(-1));
        }
    }

    public void merge(Integer key, ArrayList<EAutomata> l){
        ArrayList<EAutomata> value = sons.getOrDefault(key, new ArrayList<>());
        for (EAutomata e : l){
            if(!value.contains(e)) value.add(e);
        }
        sons.put(key, value);

    }

    public static int getCounter(){
        return counter;
    }

    public String toString(){
        if (terminal)
            return "T" + id + sons.toString();
        else
            return id + sons.toString();
    }


}
