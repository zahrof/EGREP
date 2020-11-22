import AhoUllmann_Method.*;
import FileReadingUtilitaryClasses.Book;
import FileReadingUtilitaryClasses.Position;
import FileReadingUtilitaryClasses.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static AhoUllmann_Method.RegEx.regEx;


public class Main {


/*    public static void main(String arg[]){
        FileReadingUtilitaryClasses.Book book =
        new FileReadingUtilitaryClasses.
        Book("/home/zahrof/Documents/Master2/DAAR/Projet1/EGREP/src/Babylone.txt");
        String motif = "Sargon";

        ArrayList<FileReadingUtilitaryClasses.Pose> kmp =
         (new KMP_Method.KMP()).KMP_Method.KMP(motif, book);
        System.out.print(kmp );
    }*/

    private static void egrep(Book b, MinState ms){
        Position cursor = new Position();
        ArrayList<Word> positions = new ArrayList<>();
        Position end;
        while(cursor!=null){
            end = app(b, cursor, ms);
            if(end!=null) positions.add(new Word(b, cursor, end));
            cursor = Position.move (b,cursor,1);
        }
        for(Word w : positions) System.out.println(w.toString());
    }

    // return null
    private static Position app(Book b, Position cursor, MinState a) {
        int i = b.getCharacter(cursor.page, cursor.line, cursor.col);
        MinState son = a.sons.getOrDefault(i, null);
        if(son!=null){
            if(son.terminal) return cursor;
            return app(b, Position.move(b,cursor,1),son);
        }
        else
        if (a.terminal)
            return cursor;
        else
            return null;
    }

    //MAIN
    public static void main(String args[]) {
        RegEx re = new RegEx();
        if (args.length!=0) {
            regEx = args[0];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("  >> Please enter a regEx: ");
            regEx = scanner.next();
        }
        if (regEx.length()<1) System.err.println("  >> ERROR: empty regEx.");
        else {
            RegExTree ret = null;
            try {
                ret = re.parse();
            } catch (Exception e) {
                System.err.println("  >> ERROR: syntax error for" +
                        " regEx \""+regEx+"\".");
            }
            EAutomata s = new EAutomata(new HashMap<>(),false);
            s.put(-1, new EAutomata(new HashMap<>(), true));
            // add final state with epsilon
            s.fromRegExTree(ret);
            EAutomata ndfa = DFA.dfa(s);
            MinState ms = MinState.minimisation(ndfa);
            Book b = new Book(args[1]);
            egrep(b,ms);

        }

    }
}
