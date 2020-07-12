package org.geek8080.physics2d;

public class Polygon extends Shape {

	public static final int MAX_POLY_VERTEX_COUNT = 64;

	public int vertexCount;
	public Vector2D[] vertices = Vector2D.arrayOf(MAX_POLY_VERTEX_COUNT);
	public Vector2D[] normals = Vector2D.arrayOf(MAX_POLY_VERTEX_COUNT);

	public Polygon() {
	}

	public Polygon(Vector2D... verts) {
		set(verts);
	}

	public Polygon(float hw, float hh) {
		setBox(hw, hh);
	}

	@Override
	public Shape clone() {
		Polygon p = new Polygon();
		p.u.set(u);
		for (int i = 0; i < vertexCount; i++) {
			p.vertices[i].set(vertices[i]);
			p.normals[i].set(normals[i]);
		}
		p.vertexCount = vertexCount;

		return p;
	}

	@Override
	public void initialize() {
		computeMass(1.0f);
	}

	@Override
	public void computeMass(float density) {
		Vector2D c = new Vector2D(0.0f, 0.0f);
		float area = 0.0f;
		float I = 0.0f;
		final float k_inv3 = 1.0f / 3.0f;

		for (int i = 0; i < vertexCount; ++i) {
			Vector2D p1 = vertices[i];
			Vector2D p2 = vertices[(i + 1) % vertexCount];

			float D = Vector2D.cross(p1, p2);
			float triangleArea = 0.5f * D;

			area += triangleArea;

			float weight = triangleArea * k_inv3;
			c.addsi(p1, weight);
			c.addsi(p2, weight);

			float intx2 = p1.x * p1.x + p2.x * p1.x + p2.x * p2.x;
			float inty2 = p1.y * p1.y + p2.y * p1.y + p2.y * p2.y;
			I += (0.25f * k_inv3 * D) * (intx2 + inty2);
		}

		c.muli(1.0f / area);

		for (int i = 0; i < vertexCount; ++i) {
			vertices[i].subi(c);
		}

		body.mass = density * area;
		body.invMass = (body.mass != 0.0f) ? 1.0f / body.mass : 0.0f;
		body.inertia = I * density;
		body.invInertia = (body.inertia != 0.0f) ? 1.0f / body.inertia : 0.0f;
	}

	@Override
	public void setOrient(float radians) {
		u.set(radians);
	}

	@Override
	public Type getType() {
		return Type.Polygon;
	}

	public void setBox(float hw, float hh) {
		vertexCount = 4;
		vertices[0].set(-hw, -hh);
		vertices[1].set(hw, -hh);
		vertices[2].set(hw, hh);
		vertices[3].set(-hw, hh);
		normals[0].set(0.0f, -1.0f);
		normals[1].set(1.0f, 0.0f);
		normals[2].set(0.0f, 1.0f);
		normals[3].set(-1.0f, 0.0f);
	}

	public void set(Vector2D... verts) {
		int rightMost = 0;
		float highestXCoord = verts[0].x;
		for (int i = 1; i < verts.length; ++i) {
			float x = verts[i].x;

			if (x > highestXCoord) {
				highestXCoord = x;
				rightMost = i;
			} else if (x == highestXCoord) {
				if (verts[i].y < verts[rightMost].y) {
					rightMost = i;
				}
			}
		}

		int[] hull = new int[MAX_POLY_VERTEX_COUNT];
		int outCount = 0;
		int indexHull = rightMost;

		for (;;) {
			hull[outCount] = indexHull;

			int nextHullIndex = 0;
			for (int i = 1; i < verts.length; ++i) {
				if (nextHullIndex == indexHull) {
					nextHullIndex = i;
					continue;
				}

				Vector2D e1 = verts[nextHullIndex].sub(verts[hull[outCount]]);
				Vector2D e2 = verts[i].sub(verts[hull[outCount]]);
				float c = Vector2D.cross(e1, e2);
				if (c < 0.0f) {
					nextHullIndex = i;
				}

				if (c == 0.0f && e2.lengthSq() > e1.lengthSq()) {
					nextHullIndex = i;
				}
			}

			++outCount;
			indexHull = nextHullIndex;

			if (nextHullIndex == rightMost) {
				vertexCount = outCount;
				break;
			}
		}

		for (int i = 0; i < vertexCount; ++i) {
			vertices[i].set(verts[hull[i]]);
		}

		for (int i = 0; i < vertexCount; ++i) {
			Vector2D face = vertices[(i + 1) % vertexCount].sub(vertices[i]);

			normals[i].set(face.y, -face.x);
			normals[i].normalize();
		}
	}

	public Vector2D getSupport(Vector2D dir) {
		float bestProjection = -Float.MAX_VALUE;
		Vector2D bestVertex = null;

		for (int i = 0; i < vertexCount; ++i) {
			Vector2D v = vertices[i];
			float projection = Vector2D.dot(v, dir);

			if (projection > bestProjection) {
				bestVertex = v;
				bestProjection = projection;
			}
		}

		return bestVertex;
	}

}
