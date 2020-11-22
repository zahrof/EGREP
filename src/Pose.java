public class Pose{
    public int page, line, col;

    public Pose(int page, int line, int col){
        this.page = page;
        this.line = line;
        this.col = col;
    }

    public Pose() {
        page=0; line=0; col=0;
    }

    public Pose copy(){
        return new Pose(page, line, col);
    }

    public String toString(){
        return "(Page:" + page + ", Ligne:" + line + ", Colonne: " + col + ")";
    }

    public static Pose move(Book book, Pose start, int gap){
        if (gap == 0) return start;
        if (start.col < book.get(start.page, start.line).length() - 1)
            return move(book, new Pose(start.page, start.line, start.col + 1), gap - 1);
        else if (start.line < book.size(start.page) - 1)
            return move (book, new Pose(start.page, start.line + 1, 0), gap - 1);
        else if (start.page < book.size() - 1)
            return move (book, new Pose(start.page + 1, 0, 0), gap - 1);
        return null;
    }

    public boolean equals(Object o){
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof Pose)) return false;
        Pose p = (Pose) o;
        return page == p.page && line == p.line && col == p.col;
    }
}