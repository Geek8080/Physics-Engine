package org.geek8080.physics2d;

public class CollisionPolygonPolygon implements CollisionCallback {

	public static final CollisionPolygonPolygon instance = new CollisionPolygonPolygon();

	@Override
	public void handleCollision(Manifold m, Body a, Body b) {
		Polygon A = (Polygon) a.shape;
		Polygon B = (Polygon) b.shape;
		m.contactCount = 0;

		int[] faceA = { 0 };
		float penetrationA = findAxisLeastPenetration(faceA, A, B);
		if (penetrationA >= 0.0f) {
			return;
		}

		int[] faceB = { 0 };
		float penetrationB = findAxisLeastPenetration(faceB, B, A);
		if (penetrationB >= 0.0f) {
			return;
		}

		int referenceIndex;
		boolean flip;

		Polygon RefPoly;
		Polygon IncPoly;

		if (ImpulseMath.gt(penetrationA, penetrationB)) {
			RefPoly = A;
			IncPoly = B;
			referenceIndex = faceA[0];
			flip = false;
		} else {
			RefPoly = B;
			IncPoly = A;
			referenceIndex = faceB[0];
			flip = true;
		}

		Vector2D[] incidentFace = Vector2D.arrayOf(2);

		findIncidentFace(incidentFace, RefPoly, IncPoly, referenceIndex);

		Vector2D v1 = RefPoly.vertices[referenceIndex];
		referenceIndex = referenceIndex + 1 == RefPoly.vertexCount ? 0 : referenceIndex + 1;
		Vector2D v2 = RefPoly.vertices[referenceIndex];

		v1 = RefPoly.u.mul(v1).addi(RefPoly.body.position);
		v2 = RefPoly.u.mul(v2).addi(RefPoly.body.position);

		Vector2D sidePlaneNormal = v2.sub(v1);
		sidePlaneNormal.normalize();

		Vector2D refFaceNormal = new Vector2D(sidePlaneNormal.y, -sidePlaneNormal.x);

		float refC = Vector2D.dot(refFaceNormal, v1);
		float negSide = -Vector2D.dot(sidePlaneNormal, v1);
		float posSide = Vector2D.dot(sidePlaneNormal, v2);

		if (clip(sidePlaneNormal.neg(), negSide, incidentFace) < 2) {
			return;
		}

		if (clip(sidePlaneNormal, posSide, incidentFace) < 2) {
			return;
		}

		// Flip
		m.normal.set(refFaceNormal);
		if (flip) {
			m.normal.negi();
		}

		int cp = 0;
		float separation = Vector2D.dot(refFaceNormal, incidentFace[0]) - refC;
		if (separation <= 0.0f) {
			m.contacts[cp].set(incidentFace[0]);
			m.penetration = -separation;
			++cp;
		} else {
			m.penetration = 0;
		}

		separation = Vector2D.dot(refFaceNormal, incidentFace[1]) - refC;

		if (separation <= 0.0f) {
			m.contacts[cp].set(incidentFace[1]);

			m.penetration += -separation;
			++cp;

			m.penetration /= cp;
		}

		m.contactCount = cp;
	}

	public float findAxisLeastPenetration(int[] faceIndex, Polygon A, Polygon B) {
		float bestDistance = -Float.MAX_VALUE;
		int bestIndex = 0;

		for (int i = 0; i < A.vertexCount; ++i) {
			Vector2D nw = A.u.mul(A.normals[i]);

			Mat22 buT = B.u.transpose();
			Vector2D n = buT.mul(nw);

			Vector2D s = B.getSupport(n.neg());

			Vector2D v = buT.muli(A.u.mul(A.vertices[i]).addi(A.body.position).subi(B.body.position));

			float d = Vector2D.dot(n, s.sub(v));

			if (d > bestDistance) {
				bestDistance = d;
				bestIndex = i;
			}
		}

		faceIndex[0] = bestIndex;
		return bestDistance;
	}

	public void findIncidentFace(Vector2D[] v, Polygon RefPoly, Polygon IncPoly, int referenceIndex) {
		Vector2D referenceNormal = RefPoly.normals[referenceIndex];

		referenceNormal = RefPoly.u.mul(referenceNormal);
		referenceNormal = IncPoly.u.transpose().mul(referenceNormal);

		int incidentFace = 0;
		float minDot = Float.MAX_VALUE;
		for (int i = 0; i < IncPoly.vertexCount; ++i) {
			float dot = Vector2D.dot(referenceNormal, IncPoly.normals[i]);

			if (dot < minDot) {
				minDot = dot;
				incidentFace = i;
			}
		}

		v[0] = IncPoly.u.mul(IncPoly.vertices[incidentFace]).addi(IncPoly.body.position);
		incidentFace = incidentFace + 1 >= (int) IncPoly.vertexCount ? 0 : incidentFace + 1;
		v[1] = IncPoly.u.mul(IncPoly.vertices[incidentFace]).addi(IncPoly.body.position);
	}

	public int clip(Vector2D n, float c, Vector2D[] face) {
		int sp = 0;
		Vector2D[] out = { new Vector2D(face[0]), new Vector2D(face[1]) };

		float d1 = Vector2D.dot(n, face[0]) - c;
		float d2 = Vector2D.dot(n, face[1]) - c;

		if (d1 <= 0.0f)
			out[sp++].set(face[0]);
		if (d2 <= 0.0f)
			out[sp++].set(face[1]);

		if (d1 * d2 < 0.0f) {
			float alpha = d1 / (d1 - d2);

			out[sp++].set(face[1]).subi(face[0]).muli(alpha).addi(face[0]);
		}

		face[0] = out[0];
		face[1] = out[1];

		return sp;
	}

}
