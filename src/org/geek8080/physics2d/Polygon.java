package org.geek8080.physics2d;

public class Polygon extends Shape {

    public static final int MAX_POLY_VERTEX_COUNT = 64;

    public int vertexCount;
    /**
     * array of all the vertices of the polygon.
     */
    public Vector2D[] vertices = Vector2D.arrayOf(MAX_POLY_VERTEX_COUNT);
    /**
     * array of normals to corresponding faces of the polygon
     */
    public Vector2D[] normals = Vector2D.arrayOf(MAX_POLY_VERTEX_COUNT);

    public Polygon() {
    }

    public Polygon(Vector2D... verts) {
        set(vertices);
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

        // Finding COM and MOI
        Vector2D c = new Vector2D(0.0f, 0.0f);
        float area = 0.0f;
        float I = 0.0f;
        final float k_inv3 = 1.0f / 3.0f;

        for (int i = 0; i < vertexCount; i++) {
            // Triangle vertices, third vertex implied as (0, 0)
            Vector2D p1 = vertices[i];
            Vector2D p2 = vertices[(i + 1) % vertexCount];

            float D = Vector2D.crossProduct(p1, p2);
            float triangleArea = 0.5f * D;

            area += triangleArea;

            // Use area to weight the centroid average, not just vertex position
            float weight = triangleArea * k_inv3;
            c.sum(Vector2D.scaledSumN(p1, weight));
            c.sum(Vector2D.scaledSumN(p2, weight));

            float intx2 = p1.x * p1.x + p1.x * p2.x + p2.x * p2.x;
            float inty2 = p1.y * p1.y + p1.y * p2.y + p2.y * p2.y;
            I += (0.25 * k_inv3 * D) * (intx2 + inty2);
        }

        c.scaledMultiplication(1.0f / area);
        
        // Translate vertices to centroid (make the centroid (0, 0)
        // for the polygon in model space)
        for (int i = 0; i < vertexCount; i++) {
            vertices[i].difference(c);
        }

        body.mass = density * area;
        body.invMass = (body.mass != 0) ? 1.0f / body.mass : 0.0f;
        body.inertia = I * density;
        body.invInertia = (body.inertia != 0) ? 1.0f / body.inertia : 0.0f;
    }

    @Override
    public void setOrient(float radians) {
        u.set(radians);
    }

    @Override
    public Type getType() {
        return Type.Polygon;
    }

    private void set(Vector2D... verts) {

        // Find the right most point on the hull
        int rightMost = 0;
        float highestXCoord = verts[0].x;

        for (int i = 1; i < verts.length; i++) {
            float x = verts[i].x;

            if (x > highestXCoord) {
                highestXCoord = x;
                rightMost = i;
            } else if (x == highestXCoord) {
                // If matching x then take farthest negative y
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

            // Search for next index that wraps around the hull
            // by computing cross products to find the most counter-clockwise
            // vertex in the set, given the previous hull index
            int nextHullIndex = 0;

            for (int i = 1; i < verts.length; i++) {
                // Skip if same coordinate as we need three unique
                // points in the set to perform a cross product
                if (nextHullIndex == indexHull) {
                    nextHullIndex = i;
                    continue;
                }

                // Cross every set of three unique vertices
                // Record each counter clockwise third vertex and add
                // to the output hull
                // See : http://www.oocities.org/pcgpe/math2d.html
                Vector2D e1 = Vector2D.differenceV(verts[nextHullIndex], verts[hull[outCount]]);
                Vector2D e2 = Vector2D.differenceV(verts[i], verts[hull[outCount]]);
                float c = Vector2D.crossProduct(e1, e2);
                if (c < 0.0f) {
                    nextHullIndex = i;
                    // this i correspond to a concave surface
                }

                // Cross product is zero then e vectors are on same line
                // therefore want to record vertex farthest along that line
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

        // copy vertices into shape's vertices
        for (int i = 0; i < vertexCount; i++) {
            vertices[i].set(verts[hull[i]]);
        }

        // compute face normals
        for (int i = 0; i < vertexCount; i++) {
            Vector2D face = Vector2D.differenceV(vertices[(i + 1) % vertexCount], vertices[i]);

            // Calculate normal with 2D cross product between vector and scalar
            normals[i].set(face.y, -face.x);
            normals[i].normalize();
        }
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

}