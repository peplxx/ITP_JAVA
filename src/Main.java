import java.io.File;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.


public class Main {
    public static void main(String[] args) {
        System.out.println("fsgdggdf");
        try(Scanner s = new Scanner(new File("input.txt")))
        {
            System.out.println("fsgdggdf");
            String a = s.next(),b = s.next();
            int aa = Integer.parseInt(a),bb = Integer.parseInt(b);
            System.out.println(aa+bb);
            System.out.println("fsgdggdf");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}