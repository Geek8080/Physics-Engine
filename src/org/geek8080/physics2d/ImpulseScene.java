package org.geek8080.physics2d;

import java.util.ArrayList;

public class ImpulseScene {

	public float dt;
	public int iterations;
	public ArrayList<Body> bodies = new ArrayList<Body>();
	public ArrayList<Manifold> contacts = new ArrayList<Manifold>();

	public ImpulseScene(float dt, int iterations) {
		this.dt = dt;
		this.iterations = iterations;
	}

	public void step() {
		// Generate new collision info
		contacts.clear();
		for (int i = 0; i < bodies.size(); ++i) {
			Body A = bodies.get(i);

			for (int j = i + 1; j < bodies.size(); ++j) {
				Body B = bodies.get(j);

				if (A.invMass == 0 && B.invMass == 0) {
					continue;
				}

				Manifold m = new Manifold(A, B);
				m.solve();

				if (m.contactCount > 0) {
					contacts.add(m);
				}
			}
		}

		// Integrate forces
		for (int i = 0; i < bodies.size(); ++i) {
			integrateForces(bodies.get(i), dt);
		}

		// Initialize collision
		for (int i = 0; i < contacts.size(); ++i) {
			contacts.get(i).initialize();
		}

		// Solve collisions
		for (int j = 0; j < iterations; ++j) {
			for (int i = 0; i < contacts.size(); ++i) {
				contacts.get(i).applyImpulse();
			}
		}

		// Integrate velocities
		for (int i = 0; i < bodies.size(); ++i) {
			integrateVelocity(bodies.get(i), dt);
		}

		// Correct positions
		for (int i = 0; i < contacts.size(); ++i) {
			contacts.get(i).positionalCorrection();
		}

		// Clear all forces
		for (int i = 0; i < bodies.size(); ++i) {
			Body b = bodies.get(i);
			b.force.set(0, 0);
			b.torque = 0;
		}
	}

	public Body add(Shape shape, int x, int y) {
		Body b = new Body(shape, x, y);
		bodies.add(b);
		return b;
	}

	public void clear() {
		contacts.clear();
		bodies.clear();
	}

	// see http://www.niksula.hut.fi/~hkankaan/Homepages/gravity.html
	public void integrateForces(Body b, float dt) {

		if (b.invMass == 0.0f) {
			return;
		}

		float dts = dt * 0.5f;

		b.velocity.addsi(b.force, b.invMass * dts);
		b.velocity.addsi(ImpulseMath.GRAVITY, dts);
		b.angularVelocity += b.torque * b.invInertia * dts;
	}

	public void integrateVelocity(Body b, float dt) {

		if (b.invMass == 0.0f) {
			return;
		}

		b.position.addsi(b.velocity, dt);
		b.orient += b.angularVelocity * dt;
		b.setOrient(b.orient);

		integrateForces(b, dt);
	}

}
