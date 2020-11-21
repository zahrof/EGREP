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
            if(!ms.sons.containsKey(i)) return null;
            if(this.sons.get(i).equals(this)&& ms.sons.get(i).equals(ms)) continue;
            if (!this.sons.get(i).equals(ms.sons.get(i))) return null;
        }
        MinState res;
        if(ms.id>this.id) res  = new MinState(this.id,this.terminal);
        else res = new MinState(ms.id, this.terminal);

        //System.out.println("new fusion " + this.id + " with " + ms.id);
        res.sons.putAll(this.sons);
        res.sons.putAll(ms.sons);

        res.father.putAll(this.father);
        res.father.putAll(ms.father);

        for(Integer key : res.sons.keySet()) {
            if (res.sons.get(key).equals(this) || res.sons.get(key).equals(ms))
                res.sons.put(key, res);
            else{
                res.sons.get(key).father.get(key).remove(this);
                res.sons.get(key).father.get(key).remove(ms);
                res.sons.get(key).father.get(key).add(res);
            }
        }

        MinState old;
        for(Integer key : res.father.keySet())
            for (MinState f : res.father.get(key)){
                if (f.equals(this) || f.equals(ms)){
                    res.father.get(key).remove(this);
                    res.father.get(key).remove(ms);
                    res.father.get(key).add(res);
                } else if (f.sons.containsKey(key)) {
                    old = f.sons.get(key);
                    f.sons.put(key, res);
                    System.out.println(old);
                }
            }


        return res;
    }


    public MinState clone(){
        MinState res = new MinState(id);
        res.terminal=terminal;
        res.sons= (HashMap<Integer, MinState>) this.sons.clone();
        res.father = (HashMap<Integer, ArrayList<MinState>>) this.father.clone();
        return res;
    }
}
