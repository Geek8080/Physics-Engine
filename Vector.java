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

    public Vector(Point p2) {
        Point p1 = new Point(0, 0);
        this.p1 = p1;
        this.p2 = p2;
        this.xComponent = this.p2.getX() - this.p1.getX();
        this.yComponent = this.p2.getY() - this.p1.getY();
        this.angle = Math.atan2(this.yComponent, this.xComponent);
        this.magnitude = Math.sqrt(this.xComponent * this.xComponent + this.yComponent * this.yComponent);
    }

    public Vector(double magnitude, Vector unitVector) {
        scaledProduct(magnitude, unitVector);
    }

    public Vector() {
    }

    private void scaledProduct(double magnitude, Vector unitVector) {
        Point point = new Point(magnitude * unitVector.getXComponent(), magnitude * unitVector.getYComponent());
        new Vector(point);
    }

    public static Vector scalarProduct(double magnitude, Vector unitVector) {
        Point point = new Point(magnitude * unitVector.getXComponent(), magnitude * unitVector.getYComponent());
        return new Vector(point);
    }

    public static double dotProduct(Vector v1, Vector v2) {
        return (v1.xComponent * v2.xComponent + v1.yComponent * v2.yComponent);
    }

    public static Vector crossProduct(Vector v1, Vector v2) {
        return new Vector(new Point(v1.getXComponent() * v2.getYComponent(), -v1.getYComponent() * v2.getXComponent()));
    }

    public static Vector sum(Vector v1, Vector v2) {
        return new Vector(new Point(v1.getXComponent() + v2.getXComponent(), v1.getYComponent() + v2.getYComponent()));
    }

    public static Vector difference(Vector from, Vector vector) {
        return new Vector(new Point(from.getXComponent() - vector.getXComponent(),
                from.getYComponent() - vector.getYComponent()));
    }

    public static Vector scalarDivision(Vector vector, double by) {
        return new Vector(new Point(vector.getXComponent() / by, vector.getYComponent() / by));
    }

    public static double angleBetween(Vector v1, Vector v2) {
        return Math.acos(Vector.dotProduct(v1, v2) / (v1.getMagnitude() * v2.getMagnitude()));
    }

    public double getXComponent() {
        return this.xComponent;
    }

    public void setXComponent(double d) {
        this.xComponent = d;
        update();
    }

    public double getYComponent() {
        return this.yComponent;
    }

    public void setYComponent(double d) {
        this.yComponent = d;
        update();
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        update();
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public void update() {
        this.angle = Math.atan2(this.yComponent, this.xComponent);
        this.magnitude = Math.sqrt(this.xComponent * this.xComponent + this.yComponent * this.yComponent);
    }

    @Override
    public boolean equals(Object v) {
        Vector vector = (Vector) v;
        return (ImpulseMath.equal(this.xComponent, vector.xComponent)
                && ImpulseMath.equal(this.yComponent, vector.yComponent));
    }

    @Override
    public String toString() {
        return String.format("(%.2f)i + (%.2f)j {%.2f(%.2f)}", this.xComponent, this.yComponent, this.magnitude,
                this.angle);
    }

}