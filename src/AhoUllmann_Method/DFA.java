package AhoUllmann_Method;

import java.util.ArrayList;
import java.util.Stack;

public class DFA {

    public static EAutomata dfa(EAutomata root){
        EAutomata[] ta = getAll(root);
        for (EAutomata a : ta)
            ta[a.id] = absorb(a);

        for (EAutomata a : ta)
            for(Integer key : a.sons.keySet()) {
                ArrayList<EAutomata> value = new ArrayList<>();
                for (EAutomata s : a.sons.get(key)) {
                    value.add(ta[s.id]);
                }
                a.sons.put(key, value);
            }

        EAutomata res = ta[0];
        return renameAll(ta[0]);
    }

    public static EAutomata absorb(EAutomata a){
        ArrayList<EAutomata> eps = a.sons.getOrDefault(-1, null);
        if(eps == null) return a;
        a.sons.remove(-1);
        for(EAutomata e : eps){
            if(e.terminal) a.terminal = true;
            for(Integer key : e.sons.keySet()) {
                a.merge(key, e.sons.get(key));
            }
        }
        return absorb(a);
    }

    public static EAutomata renameAll(EAutomata root){
        EAutomata[] tab = getAll(root);
        int counter = 0;
        Stack<EAutomata> s = new Stack<>();
        s.push(root);
        while(!s.isEmpty()){
            EAutomata a = s.pop();
            if (a.id < 0) continue;
            a.id = -counter;
            counter++;
            for(Integer key : a.sons.keySet())
                for(EAutomata e : a.sons.get(key))
                    s.push(e);
        }
        for(EAutomata e : tab)
            if (e != null && e.id < 0)
                e.id = -e.id;
        EAutomata.counter = counter;

        return root;
    }

    public static EAutomata[] getAll(EAutomata root){
        EAutomata[] ta = new EAutomata[EAutomata.getCounter()];
        Stack<EAutomata> s = new Stack<>();
        s.push(root);
        while(!s.isEmpty()){
            EAutomata a = s.pop();
            if (ta[a.id] != null) continue;
            ta[a.id] = a;
            for(Integer key : a.sons.keySet())
                for(EAutomata e : a.sons.get(key))
                    s.push(e);
        }
        return ta;
    }
}