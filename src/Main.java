import java.util.ArrayList;

public class Main {

    public static void main(String arg[]){
        Book book = new Book("/home/zahrof/Documents/Master2/DAAR/Projet1/EGREP/src/Babylone.txt");
        String motif = "Sargon";

        ArrayList<KMP.Pose> kmp = (new KMP()).KMP(motif, book);
        System.out.print(kmp );
    }
}
