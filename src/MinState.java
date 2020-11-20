import java.util.HashMap;

public class MinState {
    public HashMap<Integer,MinState> sons= new HashMap<>();
    public int id;
    public boolean terminal=false;

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
        for (Integer i:this.sons.keySet())
            if (!this.sons.get(i).equals(sons.getOrDefault(i, this.sons.get(i)))) return null;
        MinState res;
        if(ms.id>this.id) res  = new MinState(this.id,this.terminal);
        else res = new MinState(ms.id);
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
