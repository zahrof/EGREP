import java.util.ArrayList;
import java.util.HashMap;

public class MinState {
    public HashMap<Integer,MinState> sons= new HashMap<>();
    public int id;
    public boolean terminal=false;
    public HashMap<Integer,ArrayList<MinState>> father= new HashMap<>();

    public MinState(int id) {
        this.id = id;
    }

    public MinState(int id, boolean terminal) {
        this.id = id;
        this.terminal = terminal;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MinState)) return false;
        MinState s = (MinState) o;
        return id == s.id && terminal == s.terminal;
    }

    public MinState fusion(MinState ms){
        if(ms==this) return this;
        if(ms==null) return this;
        if(ms.terminal!=this.terminal) return null;
        for (Integer i:this.sons.keySet()) {
            if(!ms.sons.containsKey(i)) continue;
            if(this.sons.get(i).equals(this)&& ms.sons.get(i).equals(ms)) continue;
            if(this.sons.get(i).equals(this)&& ms.sons.get(i).equals(this)) continue;
            if(this.sons.get(i).equals(ms)&& ms.sons.get(i).equals(ms)) continue;
            if (!this.sons.get(i).equals(sons.get(i))) return null;
        }
        MinState res;
        if(ms.id>this.id) res  = new MinState(this.id,this.terminal);
        else res = new MinState(ms.id);
        for(Integer i : this.father.keySet())
            for(MinState f : this.father.get(i))
                f.sons.put(i,res);

        for(Integer i : ms.father.keySet())
            for(MinState f : ms.father.get(i))
                f.sons.put(i,res);
        res.sons.putAll(this.sons);
        res.sons.putAll(ms.sons);
        return res;
    }

    public MinState clone(){
        MinState res = new MinState(id);
        res.terminal=terminal;
        res.sons= (HashMap<Integer, MinState>) this.sons.clone();
        return res;
    }
}
