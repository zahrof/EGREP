public class Pose{
    public int page, line, col;

    public Pose(int page, int line, int col){
        this.page = page;
        this.line = line;
        this.col = col;
    }

    public Pose() {
        page=0;line=0; col=0;
    }

    public Pose copy(){
        return new Pose(page, line, col);
    }

    public String toString(){
        return "(" + page + ", " + line + ", " + col + ")";
    }

}