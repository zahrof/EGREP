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

                    current = Pose.move(book, start, carryover[index] + 1);
                    index = 0;
                }
                else{
                    current = Pose.move(book, current, 1);
                }
            } else {
                current = Pose.move(book, start, carryover[index] + 1);
                index = 0;
            }
            if (index == 0) start = current.copy();

        }

        return result;
    }



    public int[] carryover(String motif){
        int[] carryover = new int[motif.length() + 1];

        return carryover;
    }



}

