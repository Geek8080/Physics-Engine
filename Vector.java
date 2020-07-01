public class Vector {
    private Point p1;
    private Point p2;
    private double xComponent;
    private double yComponent;
    private double angle;
    private double magnitude;

    /*
     * The constructor makes a vector crossing through two points p1 and p2.
     * 
     * @param p1 The source point(x1, x2)
     */
    public Vector(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.xComponent = this.p2.getX() - this.p1.getX();
        this.yComponent = this.p2.getY() - this.p1.getY();
        this.angle = Math.atan2(this.yComponent, this.xComponent);
        this.magnitude = Math.sqrt(this.xComponent * this.xComponent + this.yComponent * this.yComponent);
    }

    public Vector(Point p1) {
        Point p2 = new Point(0, 0);
        this.p1 = p1;
        this.p2 = p2;
        this.xComponent = this.p2.getX() - this.p1.getX();
        this.yComponent = this.p2.getY() - this.p1.getY();
        this.angle = Math.atan2(this.yComponent, this.xComponent);
        this.magnitude = Math.sqrt(this.xComponent * this.xComponent + this.yComponent * this.yComponent);
    }

    public static double dotProduct(Vector v1, Vector v2) {
        return (v1.xComponent * v2.xComponent + v1.yComponent * v2.yComponent);
    }

    public static double angleBetween(Vector v1, Vector v2) {
        return Math.acos(Vector.dotProduct(v1, v2) / (v1.getMagnitude() * v2.getMagnitude()));
    }

    public Point getP1() {
        return this.p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return this.p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public double getXComponent() {
        return this.xComponent;
    }

    public void setXComponent(long xComponent) {
        this.xComponent = xComponent;
    }

    public double getYComponent() {
        return this.yComponent;
    }

    public void setYComponent(long yComponent) {
        this.yComponent = yComponent;
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public void setMagnitude(double length) {
        this.magnitude = length;
    }

    public static Vector sum(Vector v1, Vector v2) {
        return new Vector(new Point(v1.getXComponent() + v2.getXComponent(), v1.getYComponent() + v2.getYComponent()));
    }

    @Override
    public boolean equals(Object v) {
        Vector vector = (Vector) v;
        return ((this.xComponent == vector.xComponent) && (this.yComponent == vector.yComponent));
    }

    @Override
    public String toString() {
        return String.format("(%.2f)i + (%.2f)j {%.2f(%.2f)}", this.xComponent, this.yComponent, this.magnitude,
                this.angle);
    }
}