package org.geek8080.physics2d;

public class Manifold {

	public Body a;
	public Body b;
	public float penetration;
	public final Vector2D normal = new Vector2D();
	public final Vector2D[] contacts = { new Vector2D(), new Vector2D() };
	public int contactCount;
	public float e;
	public float sf;
	public float df;

	public Manifold(Body a, Body b) {
		this.a = a;
		this.b = b;
	}

	public void solve() {
		int ia = a.shape.getType().ordinal();
		int ib = b.shape.getType().ordinal();

		Collisions.dispatch[ia][ib].handleCollision(this, a, b);
	}

	public void initialize() {
		e = StrictMath.min(a.restitution, b.restitution);

		sf = (float) StrictMath.sqrt(a.staticFriction * a.staticFriction + b.staticFriction * b.staticFriction);
		df = (float) StrictMath.sqrt(a.dynamicFriction * a.dynamicFriction + b.dynamicFriction * b.dynamicFriction);

		for (int i = 0; i < contactCount; i++) {
			Vector2D ra = Vector2D.differenceV(contacts[i], a.position);
			Vector2D rb = Vector2D.differenceV(contacts[i], b.position);

			Vector2D rv = Vector2D.differenceV(Vector2D
					.differenceV(Vector2D.sumV(b.velocity, Vector2D.crossProduct(b.angularVelocity, rb)), a.velocity),
					Vector2D.crossProduct(a.angularVelocity, ra));

			if (rv.lengthSq() < ImpulseMath.RESTING) {
				e = 0.0f;
			}
		}
	}

	public void applyImpulse() {
		if (ImpulseMath.equal(a.invMass + b.invMass, 0)) {
			infinitemassCorrection();
			return;
		}

		for (int i = 0; i < contactCount; i++) {
			Vector2D ra = Vector2D.differenceV(contacts[i], a.position);
			Vector2D rb = Vector2D.differenceV(contacts[i], b.position);

			Vector2D rv = Vector2D.differenceV(Vector2D
					.differenceV(Vector2D.sumV(b.velocity, Vector2D.crossProduct(b.angularVelocity, rb)), a.velocity),
					Vector2D.crossProduct(a.angularVelocity, ra));

			float contactVel = Vector2D.dotProduct(rv, normal);

			if (contactVel > 0) {
				return;
			}

			float raCrossN = Vector2D.crossProduct(ra, normal);
			float rbCrossN = Vector2D.crossProduct(rb, normal);
			float invMassSum = a.invMass + b.invMass + (raCrossN * raCrossN) * a.invInertia
					+ (rbCrossN * rbCrossN) * b.invInertia;

			float j = -(1.0f + e) * contactVel;
			j /= invMassSum;
			j /= contactCount;

			Vector2D impulse = Vector2D.scaledMultiplicationN(normal, j);
			a.applyImpulse(Vector2D.scaledMultiplicationN(impulse, -1), ra);
			b.applyImpulse(impulse, rb);

			rv = Vector2D.differenceV(Vector2D
					.differenceV(Vector2D.sumV(b.velocity, Vector2D.crossProduct(b.angularVelocity, rb)), a.velocity),
					Vector2D.crossProduct(a.angularVelocity, ra));

			Vector2D t = new Vector2D(rv);
			t.difference(Vector2D.scaledMultiplicationN(normal, Vector2D.dotProduct(rv, normal)));
			t.normalize();

			float jt = -Vector2D.dotProduct(rv, t);
			jt /= invMassSum;
			jt /= contactCount;

			if (ImpulseMath.equal(jt, 0.0f)) {
				return;
			}

			Vector2D tangentImpulse;
			if (StrictMath.abs(jt) < j * sf) {
				tangentImpulse = Vector2D.scaledMultiplicationN(t, jt);
			} else {
				tangentImpulse = Vector2D.scaledMultiplicationN(t, (-j) * df);
			}

			a.applyImpulse(Vector2D.scaledMultiplicationN(tangentImpulse, -1), ra);
			b.applyImpulse(tangentImpulse, rb);
		}
	}

	public void positionalCorrection() {
		float correction = StrictMath.max(penetration - ImpulseMath.PENETRATION_ALLOWANCE, 0.0f)
				/ (a.invMass + b.invMass) * ImpulseMath.PENETRATION_CORRETION;

		a.position.sum(Vector2D.scaledMultiplicationN(normal, -a.invMass * correction));
		b.position.sum(Vector2D.scaledMultiplicationN(normal, -b.invMass * correction));
	}

	public void infinitemassCorrection() {
		a.velocity.set(0, 0);
		b.velocity.set(0, 0);
	}

}
