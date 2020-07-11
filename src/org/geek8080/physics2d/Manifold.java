package org.geek8080.physics2d;

public class Manifold {

	public Body A;
	public Body B;
	public float penetration;
	public final Vector2D normal = new Vector2D();
	public final Vector2D[] contacts = { new Vector2D(), new Vector2D() };
	public int contactCount;
	public float e;
	public float df;
	public float sf;

	public Manifold(Body a, Body b) {
		A = a;
		B = b;
	}

	public void solve() {
		int ia = A.shape.getType().ordinal();
		int ib = B.shape.getType().ordinal();

		Collisions.dispatch[ia][ib].handleCollision(this, A, B);
	}

	public void initialize() {
		e = StrictMath.min(A.restitution, B.restitution);

		sf = (float) StrictMath.sqrt(A.staticFriction * A.staticFriction + B.staticFriction * B.staticFriction);
		df = (float) StrictMath.sqrt(A.dynamicFriction * A.dynamicFriction + B.dynamicFriction * B.dynamicFriction);

		for (int i = 0; i < contactCount; ++i) {
			Vector2D ra = contacts[i].sub(A.position);
			Vector2D rb = contacts[i].sub(B.position);

			Vector2D rv = B.velocity.add(Vector2D.cross(B.angularVelocity, rb, new Vector2D())).subi(A.velocity)
					.subi(Vector2D.cross(A.angularVelocity, ra, new Vector2D()));

			if (rv.lengthSq() < ImpulseMath.RESTING) {
				e = 0.0f;
			}
		}
	}

	public void applyImpulse() {
		if (ImpulseMath.equal(A.invMass + B.invMass, 0)) {
			infiniteMassCorrection();
			return;
		}

		for (int i = 0; i < contactCount; ++i) {
			Vector2D ra = contacts[i].sub(A.position);
			Vector2D rb = contacts[i].sub(B.position);

			Vector2D rv = B.velocity.add(Vector2D.cross(B.angularVelocity, rb, new Vector2D())).subi(A.velocity)
					.subi(Vector2D.cross(A.angularVelocity, ra, new Vector2D()));

			float contactVel = Vector2D.dot(rv, normal);

			if (contactVel > 0) {
				return;
			}

			float raCrossN = Vector2D.cross(ra, normal);
			float rbCrossN = Vector2D.cross(rb, normal);
			float invMassSum = A.invMass + B.invMass + (raCrossN * raCrossN) * A.invInertia
					+ (rbCrossN * rbCrossN) * B.invInertia;

			float j = -(1.0f + e) * contactVel;
			j /= invMassSum;
			j /= contactCount;

			Vector2D impulse = normal.mul(j);
			A.applyImpulse(impulse.neg(), ra);
			B.applyImpulse(impulse, rb);

			rv = B.velocity.add(Vector2D.cross(B.angularVelocity, rb, new Vector2D())).subi(A.velocity)
					.subi(Vector2D.cross(A.angularVelocity, ra, new Vector2D()));

			Vector2D t = new Vector2D(rv);
			t.addsi(normal, -Vector2D.dot(rv, normal));
			t.normalize();

			float jt = -Vector2D.dot(rv, t);
			jt /= invMassSum;
			jt /= contactCount;

			if (ImpulseMath.equal(jt, 0.0f)) {
				return;
			}

			Vector2D tangentImpulse;
			if (StrictMath.abs(jt) < j * sf) {
				tangentImpulse = t.mul(jt);
			} else {
				tangentImpulse = t.mul(j).muli(-df);
			}

			A.applyImpulse(tangentImpulse.neg(), ra);
			B.applyImpulse(tangentImpulse, rb);
		}
	}

	public void positionalCorrection() {

		float correction = StrictMath.max(penetration - ImpulseMath.PENETRATION_ALLOWANCE, 0.0f)
				/ (A.invMass + B.invMass) * ImpulseMath.PENETRATION_CORRETION;

		A.position.addsi(normal, -A.invMass * correction);
		B.position.addsi(normal, B.invMass * correction);
	}

	public void infiniteMassCorrection() {
		A.velocity.set(0, 0);
		B.velocity.set(0, 0);
	}

}
