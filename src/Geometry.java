import java.util.Arrays;

class Point{
    double x,y;
    Point(double _x, double _y){
        x = _x;y = _y;
    }
    Point(double[] d){
        x = d[0];y = d[1];
    }
    double magnitude(){
        return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

}

class Triangular{
    Point[] vert;
    Triangular(Point[] vertexes){
        // assert length should == 3
        vert = vertexes;
    }
    double area(){
        Point a = vert[0],b = vert[1],c = vert[2];
        Point v1 = new Point(new double[] {b.x-c.x,b.y-c.y}),v2 = new Point(new double[] {b.x-a.x,b.y-a.y});
        return Math.abs((v1.x * v2.y - v1.y*v2.x)/2);
    }
    double perimeter(){
        Point a = vert[0],b = vert[1],c = vert[2];
        Point bc = new Point(new double[] {b.x-c.x,b.y-c.y}),
                ba= new Point(new double[] {b.x-a.x,b.y-a.y}),
                ac = new Point(new double[] {c.x-a.x,c.y-a.y});
        double[] sides = {bc.magnitude(),ba.magnitude(),ac.magnitude()};
        return sides[0]+sides[1]+sides[2];
    }
}
class Circle extends Ellipse{
    Point origin;
    double radius;
    Circle(Point o,double r){
        super(o,r,r);
    }

}
class Square extends Quadrilateral{
    Square(Point[] vertexes) {
        super(vertexes);
    }
}
class Quadrilateral{
    Point[] vert;
    Quadrilateral(Point[] vertexes){
        // assert length should == 3
        vert = vertexes;
    }
    double perimeter(){
        Point a = vert[0],b = vert[1],c = vert[2],d = vert[3];
        Point bc = new Point(new double[] {b.x-c.x,b.y-c.y}),
                ba= new Point(new double[] {b.x-a.x,b.y-a.y}),
                dc = new Point(new double[] {c.x-d.x,c.y-d.y}),
                da = new Point(new double[] {a.x-d.x,a.y-d.y});
        return bc.magnitude()+ ba.magnitude() + dc.magnitude() + da.magnitude();
    }
    double area(){
        // using Brahmagupta's formulas
        Point a = vert[0],b = vert[1],c = vert[2],d = vert[3];
        Point bc = new Point(new double[] {b.x-c.x,b.y-c.y}),
                ba= new Point(new double[] {b.x-a.x,b.y-a.y}),
                dc = new Point(new double[] {c.x-d.x,c.y-d.y}),
                da = new Point(new double[] {a.x-d.x,a.y-d.y});
        double p = this.perimeter()/2;
        return Math.sqrt((p-bc.magnitude())*(p-ba.magnitude())*(p-dc.magnitude())*(p-da.magnitude()));
    }
}
class Ellipse{
    double major_l,minor_l;
    Point origin;
    Ellipse(Point o,double a,double b){
        origin = o;
        major_l= a;
        minor_l = b;
    }
    double area(){
        return Math.PI*major_l*minor_l;
    }
    double perimeter(){
        return 2*Math.PI*Math.sqrt((Math.pow(major_l,2)+Math.pow(minor_l,2))/2);
    }

}
