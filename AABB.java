/*
 * An Axis Aligned Bounding Box (AABB) is a box that has its four axes 
 * aligned with the coordinate system in which it resides. 
 * This means it is a box that cannot rotate, and is always 
 * squared off at 90 degrees (usually aligned with the screen). 
 * In general it is referred to as a "bounding box" because 
 * AABBs are used to bound other more complex shapes.
 */

public class AABB extends Shape {
    private Point min;
    private Point max;

    public AABB(Point min, Point max) {
        this.min = min;
        this.max = max;
        this.setVelocity(new Vector(new Point(0, 0), new Point(0, 0)));
        this.setMass(0);
        this.setRestitution(1);
    }

    public Point getMin() {
        return this.min;
    }

    /*
     * Checks if the two AABBs are colliding by finding overlapping Axis.
     */
    public static boolean isColliding(AABB box1, AABB box2) {
        // Exit with no intersection if found separated along an axis
        if (box1.getMax().getX() < box2.getMin().getX() || box1.getMin().getX() > box2.getMax().getX())
            return false;
        if (box1.getMax().getY() < box2.getMin().getY() || box1.getMin().getY() > box2.getMax().getY())
            return false;

        // No separating axis found, therefor there is at least one overlapping axis
        return true;
    }

    public void setMin(Point min) {
        this.min = min;
    }

    public Point getMax() {
        return this.max;
    }

    public void setMax(Point max) {
        this.max = max;
    }

}