import java.util.ArrayList;
import java.util.HashSet;

public class Generics {
    public static void main(String[] args) {
        Exercise1.test_library();
        Exercise2.showcase();
    }
}

abstract class Media{
    String author,name;

    Media(String a,String n){
        author = a;
        name = n;
    }
    public String getAuthor(){
        return author;
    }
    public String getName(){
        return name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }
    abstract void display();
}

class Book extends Media{

    Book(String a, String n) {
        super(a, n);
    }

    @Override
    void display() {
        System.out.println("Book: "+ author + ' ' + name);
    }
}
class Video extends Media{

    Video(String a, String n) {
        super(a, n);
    }

    @Override
    void display() {
        System.out.println("Video: "+ author + ' ' + name);
    }
}
class Newspaper extends Media{

    Newspaper(String a, String n) {
        super(a, n);
    }

    @Override
    void display() {
        System.out.println("Newspaper: "+ author + ' ' + name);
    }
}
class Library<T extends Media>{
    ArrayList<T> array;
    Library(){
        array = new ArrayList<T>();
    }
    public void add(T e){
        array.add(e);
    }
    public void display(){
        for (T e: array){
            e.display();
        }
    }
}
class Exercise1{
    public static void test_library(){
        Library<Book> l = new Library<Book>();
        l.add(new Book("Sergey","Java for Beginners"));
        l.add(new Book("Platon","Dialogs"));
        l.display();


    }
}

class Animal{
    String nickname;
    int loudness;
    Animal(String name){
        nickname = name;
        loudness = 5;
    }
    void voice(){
        System.out.println("[Animal] " + nickname +  " voice");
    }
    public boolean equals(Animal obj) {
        return nickname.equals(obj.nickname) && loudness == obj.loudness;
    }

    @Override
    public int hashCode() {
        return nickname.hashCode()+ Integer.toString(loudness).hashCode();
    }
}
class Cat extends Animal{
    Cat(String name) {
        super(name);
    }

    public void purLoudness(int value){
        loudness = value;
    }

    @Override
    void voice() {
        System.out.println("[Cat] " + nickname +  " pur...");
    }
}
class Dog extends Animal{
    Dog(String name) {
        super(name);
    }

    public void barkingLoudness(int value){
        loudness = value;
    }

    @Override
    void voice() {
        System.out.println("[Dog] " + nickname +  " bark!");
    }
}

class Exercise2{
    public static void showcase(){
        System.out.println("Exersice 2");
        HashSet<Cat> cats = new HashSet<>();
        Cat c1 = new Cat("Kitty");
        c1.purLoudness(3);
        cats.add(c1);
        cats.add(new Cat("Meow"));
        Cat c2 = new Cat("Kitty");
        c2.purLoudness(3);
        cats.add(c2);
        System.out.println(cats.size());
        System.out.println(c1.equals(c2));
    }
}