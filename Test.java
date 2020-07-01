public class Test {
    public static void main(String[] args) {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(1, 1);
        Vector v = new Vector(p1, p2);
        System.out.println(v);
    }
}