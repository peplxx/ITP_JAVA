import java.io.File;
import java.util.Scanner;

public class S {
    public static void main(String[] args) {
        System.out.println("fsgdggdf");
        try(Scanner s = new Scanner(new File("input.txt")))
        {
            System.out.println("fsgdggdf");
            String a = s.next(),b = s.next();
            System.out.println("fsgdggdf");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
