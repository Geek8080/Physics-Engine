package org.geek8080.physics2d;

public class CollisionCirclePolygon implements CollisionCallback {

	public static final CollisionCirclePolygon instance = new CollisionCirclePolygon();

	@Override
	public void handleCollision(Manifold m, Body a, Body b) {
		Circle A = (Circle) a.shape;
		Polygon B = (Polygon) b.shape;

		m.contactCount = 0;

		Vector2D center = B.u.transpose().muli(a.position.sub(b.position));

		float separation = -Float.MAX_VALUE;
		int faceNormal = 0;
		for (int i = 0; i < B.vertexCount; ++i) {
			float s = Vector2D.dot(B.normals[i], center.sub(B.vertices[i]));

			if (s > A.radius) {
				return;
			}

			if (s > separation) {
				separation = s;
				faceNormal = i;
			}
		}

		Vector2D v1 = B.vertices[faceNormal];
		int i2 = faceNormal + 1 < B.vertexCount ? faceNormal + 1 : 0;
		Vector2D v2 = B.vertices[i2];

		if (separation < ImpulseMath.EPSILON) {
			m.contactCount = 1;
			B.u.mul(B.normals[faceNormal], m.normal).negi();
			m.contacts[0].set(m.normal).muli(A.radius).addi(a.position);
			m.penetration = A.radius;
			return;
		}

		float dot1 = Vector2D.dot(center.sub(v1), v2.sub(v1));
		float dot2 = Vector2D.dot(center.sub(v2), v1.sub(v2));
		m.penetration = A.radius - separation;

		if (dot1 <= 0.0f) {
			if (Vector2D.distanceSq(center, v1) > A.radius * A.radius) {
				return;
			}

			m.contactCount = 1;
			B.u.muli(m.normal.set(v1).subi(center)).normalize();
			B.u.mul(v1, m.contacts[0]).addi(b.position);
		}

		else if (dot2 <= 0.0f) {
			if (Vector2D.distanceSq(center, v2) > A.radius * A.radius) {
				return;
			}

			m.contactCount = 1;
			B.u.muli(m.normal.set(v2).subi(center)).normalize();
			B.u.mul(v2, m.contacts[0]).addi(b.position);
		}

		else {
			Vector2D n = B.normals[faceNormal];

			if (Vector2D.dot(center.sub(v1), n) > A.radius) {
				return;
			}

			m.contactCount = 1;
			B.u.mul(n, m.normal).negi();
			m.contacts[0].set(a.position).addsi(m.normal, A.radius);
		}
	}

}
