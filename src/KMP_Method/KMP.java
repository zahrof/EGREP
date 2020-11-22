package KMP_Method;

import FileReadingUtilitaryClasses.Book;
import FileReadingUtilitaryClasses.Position;

import java.util.ArrayList;


public class KMP {

    public KMP(){}

    public ArrayList<Position> KMP(String motif, Book book){
        ArrayList<Position> result = new ArrayList<>();
        Position current = new Position(0,0,0);
        int[] carryover = carryover(motif);
        
        Position start = current.copy();
        int index = 0;
        while (current.page < book.getSize()){
            if(motif.charAt(index) == book.getCharacter(current.page, current.line, current.col)) {
                index++;
                if (index == motif.length()) {
                    result.add(start.copy());

                    current = Position.move(book, start, carryover[index] + 1);
                    index = 0;
                }
                else{
                    current = Position.move(book, current, 1);
                }
            } else {
                current = Position.move(book, start, carryover[index] + 1);
                index = 0;
            }
            if (index == 0) start = current.copy();
        }
        return result;
    }

    public int[] carryover(String motif){
        return new int[motif.length() + 1];
    }



}

