import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MinimalizedAutomaton extends Automaton {
    public HashMap<Integer, MinimalizedAutomaton> sons= new HashMap<>();
    public boolean terminal=false;
    public HashMap<Integer,ArrayList<MinimalizedAutomaton>> father= new HashMap<>();

    public MinimalizedAutomaton(int id) {
        super(id);
    }

    public MinimalizedAutomaton(int id, boolean terminal) {
       super(id,terminal);
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MinimalizedAutomaton)) return false;
        MinimalizedAutomaton s = (MinimalizedAutomaton) o;
        return id == s.id && terminal == s.terminal;
    }

    public MinimalizedAutomaton merge(MinimalizedAutomaton ms){
        if(ms==this) return this;
        if(ms==null) return this;
        if(ms.terminal!=this.terminal) return null;
        for (Integer i:this.sons.keySet()) {
            if(!ms.sons.containsKey(i)) return null;
            if(this.sons.get(i).equals(this)&&
                    ms.sons.get(i).equals(ms)) continue;
            if (!this.sons.get(i).equals(ms.sons.get(i))) return null;
        }
        MinimalizedAutomaton res;
        if(ms.id>this.id) res  = new MinimalizedAutomaton(this.id,this.terminal);
        else res = new MinimalizedAutomaton(ms.id, this.terminal);
        res.sons.putAll(this.sons);
        res.sons.putAll(ms.sons);

        res.father.putAll(this.father);
        res.father.putAll(ms.father);

        for(Integer key : res.sons.keySet()) {
            if (res.sons.get(key).equals(this) ||
                    res.sons.get(key).equals(ms))
                res.sons.put(key, res);
            else{
                res.sons.get(key).father.get(key).remove(this);
                res.sons.get(key).father.get(key).remove(ms);
                res.sons.get(key).father.get(key).add(res);
            }
        }

        MinimalizedAutomaton old;
        for(Integer key : res.father.keySet())
            for (MinimalizedAutomaton f : res.father.get(key)){
                if (f.equals(this) || f.equals(ms)){
                    ArrayList<MinimalizedAutomaton> value = new ArrayList<>();
                    for(MinimalizedAutomaton e : res.father.get(key))
                        if(!e.equals(this) && !e.equals(ms))
                            value.add(e);
                    value.add(res);
                    res.father.put(key, value);
                } else if (f.sons.containsKey(key)) {
                    old = f.sons.get(key);
                    f.sons.put(key, res);
                }
            }


        return res;
    }

    public static ArrayList<MinimalizedAutomaton> fromEAutomata(EAutomaton a){
        MinimalizedAutomaton[] tab = new MinimalizedAutomaton[EAutomaton.counter];
        for(int i = 0; i < tab.length; i++) tab[i] = new MinimalizedAutomaton(i);
        for(EAutomaton e : EAutomaton.getAll(a)){
            tab[e.id].terminal = e.terminal;
            for(Integer key : e.sons.keySet())
                for(EAutomaton s : e.sons.get(key)) {
                    tab[e.id].sons.put(key, tab[s.id]);
                    ArrayList<MinimalizedAutomaton> value =
                            tab[s.id].father.getOrDefault(
                                    key, new ArrayList<>());
                    value.add(tab[e.id]);
                    tab[s.id].father.put(key, value);
                }
        }

        return new ArrayList<>(Arrays.asList(tab));
    }

    public static MinimalizedAutomaton minimize(EAutomaton a) {
        ArrayList<MinimalizedAutomaton> automate = fromEAutomata(a);
        int sizeAutomata = automate.size();
        int i=0;
        while(i<sizeAutomata){
            int j=1;
            MinimalizedAutomaton ms;
            while(j<sizeAutomata){
                ms=automate.get(i).merge(automate.get(j));
                if(ms!=null && i!=j){
                    automate.add(ms); // CAREFULL Risque d'erreur
                    if(i<j){
                        automate.remove(j);
                        automate.remove(i);
                    }else {
                        automate.remove(i);
                        automate.remove(j);
                    }
                    sizeAutomata--;
                    i=0;
                    j=0;
                }
                j++;
            }i++;
        }
        MinimalizedAutomaton min = null;
        for (MinimalizedAutomaton ms: automate) {
            if(ms.id==0){
                min=ms;
                break;
            }
        }
        return min;
    }

    public MinimalizedAutomaton clone(){
        MinimalizedAutomaton res = new MinimalizedAutomaton(id);
        res.terminal=terminal;
        res.sons= (HashMap<Integer, MinimalizedAutomaton>) this.sons.clone();
        res.father = (HashMap<Integer, ArrayList<MinimalizedAutomaton>>)
                this.father.clone();
        return res;
    }
}
