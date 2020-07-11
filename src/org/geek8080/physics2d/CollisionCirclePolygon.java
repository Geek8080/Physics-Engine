package org.geek8080.physics2d;

public class CollisionCirclePolygon implements CollisionCallback {

	public static final CollisionCirclePolygon instance = new CollisionCirclePolygon();

	@Override
	public void handleCollision(Manifold m, Body body1, Body body2) {

		Circle a = (Circle) body1.shape;
		Polygon b = (Polygon) body2.shape;

		m.contactCount = 0;

		Vector2D center = body1.position;
		center = b.u.transpose().muli(Vector2D.differenceV(center, body2.position));

		float separation = -Float.MAX_VALUE;
		int faceNormal = 0;

		for (int i = 0; i < b.vertexCount; i++) {
			float s = Vector2D.dotProduct(b.normals[i], Vector2D.differenceV(center, b.vertices[i]));

			if (s > a.radius) {
				return;
			}

			if (s > separation) {
				separation = s;
				faceNormal = i;
			}
		}

		Vector2D v1 = b.vertices[faceNormal];
		int i2 = faceNormal + 1 < b.vertexCount ? faceNormal + 1 : 0;
		Vector2D v2 = b.vertices[i2];

		if (separation < ImpulseMath.EPSILON) {
			m.contactCount = 1;
			Vector2D n = Vector2D.scaledMultiplicationN(b.u.mul(b.normals[faceNormal]), -1);
			m.normal.set(n);
			m.contacts[0].set(Vector2D.sumV(Vector2D.scaledMultiplicationN(m.normal, a.radius), body1.position));
			m.penetration = a.radius;
			return;
		}

		float dot1 = Vector2D.dotProduct(Vector2D.differenceV(center, v1), Vector2D.differenceV(v2, v1));
		float dot2 = Vector2D.dotProduct(Vector2D.differenceV(center, v2), Vector2D.differenceV(v1, v2));
		m.penetration = a.radius - separation;

		if (dot1 <= 0.0f) {
			if (Vector2D.distanceSq(center, v1) > a.radius * a.radius) {
				return;
			}

			m.contactCount = 1;
			Vector2D n = Vector2D.differenceV(v1, center);
			n = b.u.mul(n);
			n.normalize();
			m.normal.set(n);
			v1.set(Vector2D.sumV(b.u.mul(v1), body2.position));
			m.contacts[0].set(v1);
		} else if (dot2 <= 0.0f) {
			if (Vector2D.distanceSq(center, v2) > a.radius * a.radius) {
				return;
			}

			m.contactCount = 1;
			Vector2D n = Vector2D.differenceV(v2, center);
			v2.set(Vector2D.sumV(b.u.mul(v2), body2.position));
			m.contacts[0].set(v2);
			n = b.u.mul(n);
			n.normalize();
			m.normal.set(n);

		} else {
			Vector2D n = b.normals[faceNormal];

			if (Vector2D.dotProduct(Vector2D.differenceV(center, v1), n) > a.radius) {
				return;
			}

			n = b.u.mul(n);
			m.normal.set(Vector2D.scaledMultiplicationN(n, -1));
			m.contacts[0].set(Vector2D.sumV(Vector2D.scaledMultiplicationN(m.normal, a.radius), body1.position));
			m.contactCount = 1;

		}
	}
}