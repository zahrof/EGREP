public class Word {
    public Book book;
    public Pose start;
    public Pose end;

    public Word(Book book, Pose start, Pose end) {
        this.book = book;
        this.start = start;
        this.end = end;
    }

    public String toString(){
        String s = "Début: "+this.start + " \nFin: "+this.end+"\nMot:";
        Pose cursor = start;
        while(!cursor.equals(end)) {
            s = s + book.get(cursor.page, cursor.line, cursor.col);
            cursor = Pose.move(book, cursor, 1);
        }
        return s + book.get(cursor.page, cursor.line, cursor.col);
       /*return "Début: "+this.start + " \nFin: "+this.end+"\n";*/
    }

    public String line(){
        return book.get(start.page, start.line);
    }

}
