package org.geek8080.physics2d;

public class CollisionCircleCircle implements CollisionCallback {

	public static final CollisionCircleCircle instance = new CollisionCircleCircle();

	@Override
	public void handleCollision(Manifold m, Body a, Body b) {
		Circle A = (Circle) a.shape;
		Circle B = (Circle) b.shape;

		Vector2D normal = b.position.sub(a.position);

		float dist_sqr = normal.lengthSq();
		float radius = A.radius + B.radius;

		if (dist_sqr >= radius * radius) {
			m.contactCount = 0;
			return;
		}

		float distance = (float) StrictMath.sqrt(dist_sqr);

		m.contactCount = 1;

		if (distance == 0.0f) {
			m.penetration = A.radius;
			m.normal.set(1.0f, 0.0f);
			m.contacts[0].set(a.position);
		} else {
			m.penetration = radius - distance;
			m.normal.set(normal).divi(distance);
			m.contacts[0].set(m.normal).muli(A.radius).addi(a.position);
		}
	}

}
