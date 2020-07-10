package org.geek8080.physics2d;

public class CollisionCircleCircle implements CollisionCallback {

	public static final CollisionCircleCircle instance = new CollisionCircleCircle();

	@Override
	public void handleCollision(Manifold m, Body body1, Body body2) {
		Circle a = (Circle) body1.shape;
		Circle b = (Circle) body2.shape;

		Vector2D normal = Vector2D.differenceV(body2.position, body1.position);

		float dist_sqr = normal.lengthSq();
		float radius = a.radius + b.radius;

		if (dist_sqr >= radius * radius) {
			m.contactCount = 0;
			return;
		}

		float distance = (float) StrictMath.sqrt(dist_sqr);

		m.contactCount = 1;

		if (distance == 0.0f) {
			m.penetration = a.radius;
			m.normal.set(1.0f, 1.0f);
			m.contacts[0].set(body1.position);
		} else {
			m.penetration = radius - distance;
			// Faster than using Normalized since we already performed sqrt
			m.normal.set(normal);
			m.normal.scaledDivision(distance);
			m.contacts[0].set(Vector2D.sumV(Vector2D.scaledMultiplicationN(m.normal, a.radius), body1.position));
		}
	}

}