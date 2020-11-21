import java.util.ArrayList;


public class KMP {


    public KMP(){

    }

    public ArrayList<Pose> KMP(String motif, Book book){
        ArrayList<Pose> result = new ArrayList<>();
        Pose current = new Pose(0,0,0);
        int[] carryover = carryover(motif);
        
        Pose start = current.copy();
        int index = 0;
        while (current.page < book.size()){
            if(motif.charAt(index) == book.get(current.page, current.line, current.col)) {
                index++;
                if (index == motif.length()) {
                    result.add(start.copy());

                    current = move(book, start, carryover[index] + 1);
                    index = 0;
                }
                else{
                    current = move(book, current, 1);
                }
            } else {
                current = move(book, start, carryover[index] + 1);
                index = 0;
            }
            if (index == 0) start = current.copy();

        }

        return result;
    }



    private Pose move(Book book, Pose start, int gap){
        if (gap == 0) return start;
        if (start.col < book.get(start.page, start.line).length() - 1)
            return move(book, new Pose(start.page, start.line, start.col + 1), gap - 1);
        else if (start.line < book.size(start.page) - 1)
            return move (book, new Pose(start.page, start.line + 1, 0), gap);
        else if (start.page < book.size() - 1)
            return move (book, new Pose(start.page + 1, 0, 0), gap - 1);
        return null;
    }

    public int[] carryover(String motif){
        int[] carryover = new int[motif.length() + 1];

        return carryover;
    }
    public class Pose{
        public int page, line, col;

        public Pose(int page, int line, int col){
            this.page = page;
            this.line = line;
            this.col = col;
        }

        public Pose copy(){
            return new Pose(page, line, col);
        }

        public String toString(){
            return "(" + Integer.toString(page) + ", "
                    + Integer.toString(line) + ", "
                    + Integer.toString(col) + ")";
        }
    }


}

