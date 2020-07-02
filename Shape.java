public class Shape {
    private Vector velocity;
    private Vector acceleration;
    private long mass;
    private double invMass;
    private float restitution;
    private Type type;
    private Point position;

    public enum Type {
        Circle, AABB
    }

    public Vector getAcceleration() {
        return this.acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public long getMass() {
        return this.mass;
    }

    public void setMass(long mass) {
        this.mass = mass;
        this.setInvMass(mass);
    }

    public Vector getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public double getInvMass() {
        return this.invMass;
    }

    private void setInvMass(long mass) {
        if (mass == 0) {
            invMass = Long.MAX_VALUE;
            return;
        }
        this.invMass = 1.0d / (double) mass;
    }

    public float getRestitution() {
        return this.restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}