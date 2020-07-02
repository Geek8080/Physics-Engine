public class Manifold {
    public Shape a;
    public Shape b;
    public double penetration;
    public final Vector normal = new Vector(new Point(0.0d, 0.0d));
    public int contactCount;
    public double e;

    public Manifold(Shape a, Shape b) {
        this.a = a;
        this.b = b;
    }

    public void solve() {
        int ia = a.getType().ordinal();
        int ib = b.getType().ordinal();

        Collision.dispatch[ia][ib].resolveCollision(a, b, this);
    }

    public void initialize() {
        e = Math.min(a.getRestitution(), b.getRestitution());
    }

    public void applyImpulse() {
        Vector relativeVelocity = Vector.difference(b.getVelocity(), a.getVelocity());
        double velocityAlongNormal = Vector.dotProduct(relativeVelocity, this.normal);

        if (velocityAlongNormal > 0) {
            return;
        }

        double restitution = Math.min(a.getRestitution(), b.getRestitution());

        double j = -(1 + restitution) * velocityAlongNormal;

        j /= (a.getInvMass() + b.getInvMass());

        Vector impulse = Vector.scalarProduct(j, this.normal);
        a.setVelocity(Vector.difference(a.getVelocity(), Vector.scalarProduct(a.getInvMass(), impulse)));
        b.setVelocity(Vector.sum(b.getVelocity(), Vector.scalarProduct(b.getInvMass(), impulse)));
    }

    public void positionalCorrection() {
        double correction = Math.max(this.penetration - ImpulseMath.PENETRATION_ALLOWANCE, 0.0d)
                / (a.getInvMass() + b.getInvMass()) * ImpulseMath.PENETRATION_CORRETION;
        Point posA = a.getPosition();
        Point posB = b.getPosition();
        Point newPosA = new Point(posA.getX() - (correction * a.getInvMass()),
                posA.getY() - (correction * a.getInvMass()));
        Point newPosB = new Point(posB.getX() + (correction * b.getInvMass()),
                posB.getY() + (correction * b.getInvMass()));
        a.setPosition(newPosA);
        b.setPosition(newPosB);
    }

}