public class Word {
    public Pose start;
    public Pose end;

    public Word(Pose start, Pose end) {
        this.start = start;
        this.end = end;
    }

    public String toString(){
       return  "Start "+start + " End "+ end+"\n";
    }

}
