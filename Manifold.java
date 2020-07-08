public class Manifold {
    public Shape a;
    public Shape b;
    public double penetration;
    public final Vector normal = new Vector(new Point(0.0d, 0.0d));
    public int contactCount;
    public double e;
    public double sf;
    public double df;

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
        // Calculate average restitution
        e = Math.min(a.getRestitution(), b.getRestitution());

        // Calculate static and dynamic friction
        sf = StrictMath
                .sqrt(a.getStaticFriction() * a.getStaticFriction() + b.getStaticFriction() * b.getStaticFriction());
        df = StrictMath.sqrt(
                a.getDynamicFriction() * a.getDynamicFriction() + b.getDynamicFriction() * b.getDynamicFriction());
    }

    public void applyImpulse() {
        // Early out and positional correct if both objects have infinite mass
        if (ImpulseMath.equal(a.getInvMass() + b.getInvMass(), 0)) {
            infiniteMassCorrection();
            return;
        }

        Vector relativeVelocity = Vector.difference(b.getVelocity(), a.getVelocity());
        double velocityAlongNormal = Vector.dotProduct(relativeVelocity, this.normal);

        if (velocityAlongNormal > 0) {
            return;
        }

        double j = -(1 + e) * velocityAlongNormal;

        j /= (a.getInvMass() + b.getInvMass());

        Vector impulse = Vector.scalarProduct(j, this.normal);
        a.setVelocity(Vector.difference(a.getVelocity(), Vector.scalarProduct(a.getInvMass(), impulse)));
        b.setVelocity(Vector.sum(b.getVelocity(), Vector.scalarProduct(b.getInvMass(), impulse)));

        // Re-calculate relative velocity after normal impulse
        // is applied (impulse from first article, this code comes
        // directly thereafter in the same resolve function)
        relativeVelocity = Vector.difference(b.getVelocity(), a.getVelocity());

        // Solve for the tangent vector
        Vector tangent = Vector.difference(relativeVelocity,
                Vector.scalarProduct(Vector.dotProduct(relativeVelocity, normal), normal));
        if (ImpulseMath.equal(tangent.getMagnitude(), 0)) {
            return;
        }
        tangent.setXComponent(tangent.getXComponent() / tangent.getMagnitude());
        tangent.setYComponent(tangent.getYComponent() / tangent.getMagnitude());

        // magnitude of friction
        double jt = -Vector.dotProduct(relativeVelocity, tangent);
        jt /= (a.getInvMass() + b.getInvMass());

        // Clamp magnitude of friction and create impulse vector
        Vector frictionImpulse;

        if (Math.abs(jt) < j * sf) {
            frictionImpulse = Vector.scalarProduct(jt, tangent);
        } else {
            frictionImpulse = Vector.scalarProduct(-j * df, tangent);
        }

        // apply
        a.setVelocity(Vector.difference(a.getVelocity(), Vector.scalarProduct(a.getInvMass(), frictionImpulse)));
        b.setVelocity(Vector.sum(b.getVelocity(), Vector.scalarProduct(b.getInvMass(), frictionImpulse)));
    }

    private void infiniteMassCorrection() {
        a.getVelocity().setXComponent(0.0d);
        b.getVelocity().setYComponent(0.0d);
        a.getVelocity().setXComponent(0.0d);
        b.getVelocity().setYComponent(0.0d);
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