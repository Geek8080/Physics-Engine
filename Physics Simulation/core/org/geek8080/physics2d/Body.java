package org.geek8080.physics2d;

public class Body {

	public final Vector2D position = new Vector2D();
	public final Vector2D velocity = new Vector2D();
	public final Vector2D force = new Vector2D();
	public float angularVelocity;
	public float torque;
	public float orient;
	public float mass, invMass, inertia, invInertia;
	public float staticFriction;
	public float dynamicFriction;
	public float restitution;
	public final Shape shape;

	public Body(Shape shape, int x, int y) {
		this.shape = shape;

		position.set(x, y);
		velocity.set(0, 0);
		angularVelocity = 0;
		torque = 0;
		orient = ImpulseMath.random(-ImpulseMath.PI, ImpulseMath.PI);
		force.set(0, 0);
		staticFriction = 0.2f;
		dynamicFriction = 0.3f;
		restitution = 0.8f;

		shape.body = this;
		shape.initialize();
	}

	public void applyForce(Vector2D f) {
		force.addi(f);
	}

	public void applyImpulse(Vector2D impulse, Vector2D contactVector) {
		velocity.addsi(impulse, invMass);
		angularVelocity += invInertia * Vector2D.cross(contactVector, impulse);
	}

	public void setStatic() {
		inertia = 0.0f;
		invInertia = 0.0f;
		mass = 0.0f;
		invMass = 0.0f;
	}

	public void setOrient(float radians) {
		orient = radians;
		shape.setOrient(radians);
	}

}
