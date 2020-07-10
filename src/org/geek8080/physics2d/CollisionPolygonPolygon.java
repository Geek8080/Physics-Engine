package org.geek8080.physics2d;

public class CollisionPolygonPolygon implements CollisionCallback {

	public static final CollisionPolygonPolygon instance = new CollisionPolygonPolygon();

	@Override
	public void handleCollision(Manifold m, Body body1, Body body2) {
		Polygon a = (Polygon) body1.shape;
		Polygon b = (Polygon) body2.shape;
		m.contactCount = 0;

		int[] faceA = { 0 };
		float penetrationA = findLeastPenetrationAxis(faceA, a, b);
		if (penetrationA >= 0.0f) {
			return;
		}

		int[] faceB = { 0 };
		float penetrationB = findLeastPenetrationAxis(faceB, b, a);
		if (penetrationB >= 0.0f) {
			return;
		}

		int referenceIndex;
		boolean flip;

		Polygon refPoly;
		Polygon incPoly;

		if (ImpulseMath.gt(penetrationA, penetrationB)) {
			refPoly = a;
			incPoly = b;
			referenceIndex = faceA[0];
			flip = false;
		} else {
			refPoly = b;
			incPoly = a;
			referenceIndex = faceB[0];
			flip = true;
		}

		Vector2D[] incidentFace = Vector2D.arrayOf(2);

		findIncidentFace(incidentFace, refPoly, incPoly, referenceIndex);

		Vector2D v1 = refPoly.vertices[referenceIndex];
		referenceIndex = referenceIndex + 1 == refPoly.vertexCount ? 0 : referenceIndex + 1;
		Vector2D v2 = refPoly.vertices[referenceIndex];

		v1 = Vector2D.sumV(refPoly.u.mul(v1), refPoly.body.position);
		v2 = Vector2D.sumV(refPoly.u.mul(v2), refPoly.body.position);

		Vector2D sidePlaneNormal = Vector2D.differenceV(v2, v1);
		sidePlaneNormal.normalize();

		// Orthogonalize
		Vector2D refFaceNormal = new Vector2D(sidePlaneNormal.y, -sidePlaneNormal.x);

		float refC = Vector2D.dotProduct(refFaceNormal, v1);
		float negSide = -Vector2D.dotProduct(sidePlaneNormal, v1);
		float posSide = Vector2D.dotProduct(sidePlaneNormal, v2);

		if (clip(Vector2D.scaledMultiplicationN(sidePlaneNormal, -1), negSide, incidentFace) < 2) {
			return;
		}

		if (clip(sidePlaneNormal, posSide, incidentFace) < 2) {
			return;
		}

		m.normal.set(refFaceNormal);
		if (flip) {
			m.normal.scaledMultiplication(-1);
		}

		int cp = 0;
		float seperation = Vector2D.dotProduct(refFaceNormal, incidentFace[0]) - refC;
		if (seperation <= 0.0f) {
			m.contacts[cp].set(incidentFace[0]);
			m.penetration = -seperation;
			++cp;
		} else {
			m.penetration = 0;
		}

		seperation = Vector2D.dotProduct(refFaceNormal, incidentFace[1]) - refC;
		if (seperation <= 0.0f) {
			m.contacts[cp].set(incidentFace[1]);

			m.penetration += -seperation;
			++cp;

			m.penetration /= cp;
		}

		m.contactCount = cp;

	}

	private int clip(Vector2D n, float c, Vector2D[] face) {

		int sp = 0;
		Vector2D[] out = { new Vector2D(face[0]), new Vector2D(face[1]) };

		float d1 = Vector2D.dotProduct(n, face[0]) - c;
		float d2 = Vector2D.dotProduct(n, face[1]) - c;

		if (d1 <= 0.0f) {
			out[sp++].set(face[0]);
		}

		if (d2 <= 0.0f) {
			out[sp++].set(face[1]);
		}

		if (d1 * d2 < 0.0f) {
			float alpha = d1 / (d1 - d2);
			out[sp] = Vector2D.sumV(face[0],
					Vector2D.scaledMultiplicationN(Vector2D.differenceV(face[1], face[0]), alpha));
			++sp;
		}

		face[0] = out[0];
		face[1] = out[1];

		return sp;
	}

	private void findIncidentFace(Vector2D[] v, Polygon refPoly, Polygon incPoly, int referenceIndex) {

		Vector2D referenceNormal = refPoly.normals[referenceIndex];

		referenceNormal = refPoly.u.mul(referenceNormal);
		referenceNormal = incPoly.u.transpose().mul(referenceNormal);

		int incidentFace = 0;
		float minDot = Float.MAX_VALUE;

		for (int i = 0; i < incPoly.vertexCount; i++) {
			float dot = Vector2D.dotProduct(referenceNormal, incPoly.normals[i]);

			if (dot < minDot) {
				minDot = dot;
				incidentFace = i;
			}
		}

		v[0] = Vector2D.sumV(incPoly.u.mul(incPoly.vertices[incidentFace]), incPoly.body.position);
		incidentFace = incidentFace + 1 >= (int) incPoly.vertexCount ? 0 : incidentFace + 1;
		v[1] = Vector2D.sumV(incPoly.u.mul(incPoly.vertices[incidentFace]), incPoly.body.position);

	}

	private float findLeastPenetrationAxis(int[] faceIndex, Polygon A, Polygon B) {
		float bestDistance = -Float.MAX_VALUE;
		int bestIndex = 0;

		for (int i = 0; i < A.vertexCount; i++) {
			Vector2D nw = A.u.mul(A.normals[i]);

			Mat22 buT = B.u.transpose();
			Vector2D n = buT.mul(nw);

			Vector2D s = B.getSupport(Vector2D.scaledMultiplicationN(n, -1));

			Vector2D v = A.vertices[i];
			v = Vector2D.sumV(A.u.mul(v), A.body.position);
			v.difference(B.body.position);
			v = buT.mul(v);

			float d = Vector2D.dotProduct(n, Vector2D.differenceV(s, v));

			if (d > bestDistance) {
				bestDistance = d;
				bestIndex = i;
			}
		}

		faceIndex[0] = bestIndex;
		return bestDistance;
	}
}