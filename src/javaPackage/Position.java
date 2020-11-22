package javaPackage;

public class Position {
    public int page, line, col;

    public Position(int page, int line, int col){
        this.page = page;
        this.line = line;
        this.col = col;
    }

    public Position() {
        page=0; line=0; col=0;
    }

    public Position copy(){
        return new Position(page, line, col);
    }

    public String toString(){
        return "(Page:" + page + ", Ligne:" + line + ", Colonne: " + col + ")";
    }

    public static Position move(Book book, Position start, int gap){
        if (gap == 0) return start;
        if (start.col < book.getLine(start.page, start.line).length() - 1)
            return move(book, new Position(start.page, start.line,
                            start.col + 1), gap - 1);
        else if (start.line < book.getSize(start.page) - 1)
            return move (book, new Position(start.page,
                    start.line + 1, 0), gap - 1);
        else if (start.page < book.getSize() - 1)
            return move (book, new Position(start.page + 1,
                    0, 0), gap - 1);
        return null;
    }

    public boolean equals(Object o){
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return page == p.page && line == p.line && col == p.col;
    }
}