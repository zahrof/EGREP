public class Word {
    public Book book;
    public Position start;
    public Position end;

    public Word(Book book, Position start, Position end) {
        this.book = book;
        this.start = start;
        this.end = end;
    }

    public String toString(){
        String s = "Début: "+this.start + " \nFin: "+this.end+"\nMot: ";
        Position cursor = start;
        while(!cursor.equals(end)) {
            s = s + book.getCharacter(cursor.page, cursor.line, cursor.col);
            cursor = Position.move(book, cursor, 1);
        }
        return s + book.getCharacter(cursor.page, cursor.line, cursor.col)+" \n";
    }

    public String line(){
        return book.getLine(start.page, start.line);
    }

}
