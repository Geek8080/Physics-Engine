package org.geek8080.physics2d;

/**
 * This represents bodies in physical world(world space)
 */
public class Body {

    // These three are final to prevent NullPointerException
    public final Vector2D position = new Vector2D();
    public final Vector2D velocity = new Vector2D();
    public final Vector2D force = new Vector2D();

    // Rotational properties
    public float orient;
    public float angularVelocity;
    public float torque;

    // Mass data
    public float mass;
    public float invMass;
    public float inertia;
    public float invInertia;

    // Friction data
    public float staticFriction;
    public float dynamicFriction;

    // Restitution
    public float restitution;

    // Finally the geometric shape
    // shape can't be re-initialized as shape won't change mid-way
    public final Shape shape;

    public Body(Shape shape, int x, int y) {
        this.shape = shape;

        this.position.set(x, y);
        this.velocity.set(0, 0);
        this.force.set(0, 0);

        this.orient = ImpulseMath.random(-ImpulseMath.PI, ImpulseMath.PI);
        this.angularVelocity = 0;
        this.torque = 0;

        this.staticFriction = 0.5f;
        this.dynamicFriction = 0.3f;

        this.restitution = 0.2f;

        this.shape.body = this;
        this.shape.initialize();
    }

    public void applyForce(Vector2D f) {
        this.force.sum(f);
    }

    public void applyImpulse(Vector2D impulse, Vector2D contactVector) {
        velocity.sum(Vector2D.scaledMultiplicationN(impulse, invMass));
        angularVelocity += invInertia * Vector2D.crossProduct(contactVector, impulse);
    }

    /**
     * Set the body unmovable or permanent
     */
    public void setStatic() {
        this.inertia = 0.0f;
        this.invInertia = 0.0f;
        this.mass = 0.0f;
        this.invMass = 0.0f;
    }

    public void setOrient(float radians){
        orient = radians;
        shape.setOrient(radians);
    }

}