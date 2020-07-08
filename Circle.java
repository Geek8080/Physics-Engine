public class Circle extends Shape {
    private long radius;

    public Circle(long radius, Point position) {
        this.radius = radius;
        this.setPosition(position);
        this.setVelocity(new Vector(new Point(0.0d, 0.0d), new Point(0, 0)));
        this.setMass(0);
        this.setRestitution(1);
        this.setForce(new Vector(new Point(0, 0), new Point(0, 0)));
        this.setType(Type.Circle);
    }

    /*
     * Testing for whether or not two circles intersect is very simple: take the
     * radii of the two circles and add them together, then check to see if this sum
     * is greater than the distance between the two circles.
     */
    /*
     * public boolean isColliding(Circle a, Circle b) { long dist = a.getRadius() +
     * b.getRadius();
     * 
     * // In general multiplication is a much cheaper operation than taking the
     * square // root of a value.
     * 
     * return ((dist * dist) < (a.getCentre().getX() - b.getCentre().getX())
     * (a.getCentre().getX() - b.getCentre().getX()) + (a.getCentre().getY() -
     * b.getCentre().getY()) * (a.getCentre().getY() - b.getCentre().getY())); }
     */

    public long getRadius() {
        return this.radius;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }

}