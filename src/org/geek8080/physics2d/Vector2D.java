package org.geek8080.physics2d;

public class Vector2D {

    public float x;
    public float y;

    public Vector2D() {

    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    // methods for scaling Vector2D objects starts here

    /**
     * multiplies the x and y of v by s
     * 
     * @param v
     * @param s
     */
    public static void scaledMultiplication(Vector2D v, float s) {
        v = Vector2D.scaledMultiplicationN(v, s);
    }

    /**
     * returns new Vector2D object with x and y of v multiplied by s
     * 
     * @param v
     * @param s
     */
    public static Vector2D scaledMultiplicationN(Vector2D v, float s) {
        return new Vector2D(s * v.x, s * v.y);
    }

    /**
     * multiplies x and y components by s
     * 
     * @param s
     */
    public void scaledMultiplication(float s) {
        this.x *= s;
        this.y *= s;
    }

    /**
     * divides the x and y of v by s
     * 
     * @param v
     * @param s
     */
    public static void scaledDivision(Vector2D v, float s) {
        v = Vector2D.scaledDivisionN(v, s);
    }

    /**
     * returns new Vector2D object with x and y of v divided by s
     * 
     * @param v
     * @param s
     */
    public static Vector2D scaledDivisionN(Vector2D v, float s) {
        return new Vector2D(v.x / s, v.y / s);
    }

    /**
     * divides x and y components by s
     * 
     * @param s
     */
    public void scaledDivision(float s) {
        this.x /= s;
        this.y /= s;
    }

    /**
     * increments the x and y of v by s
     * 
     * @param v
     * @param s
     */
    public static void scaledSum(Vector2D v, float s) {
        v = Vector2D.scaledSumN(v, s);
    }

    /**
     * returns new Vector2D object with x and y of v incremented by s
     * 
     * @param v
     * @param s
     */
    public static Vector2D scaledSumN(Vector2D v, float s) {
        return new Vector2D(v.x + s, v.y + s);
    }

    /**
     * increments x and y components by s
     * 
     * @param s
     */
    public void scaledSum(float s) {
        this.x += s;
        this.y += s;
    }

    /**
     * increments the x and y of v by s
     * 
     * @param v
     * @param s
     */
    public static void scaledDifference(Vector2D v, float s) {
        v = Vector2D.scaledDifferenceN(v, s);
    }

    /**
     * returns new Vector2D object with x and y of v incremented by s
     * 
     * @param v
     * @param s
     */
    public static Vector2D scaledDifferenceN(Vector2D v, float s) {
        return new Vector2D(v.x - s, v.y - s);
    }

    /**
     * increments x and y components by s
     * 
     * @param s
     */
    public void scaledDifference(float s) {
        this.x -= s;
        this.y -= s;
    }

    // methods for scaling Vector2D objects ends here

    // methods for vector operations on Vector2D objects starts here

    /**
     * sets v1 = v1 + v2
     * 
     * @param v1
     * @param v2
     */
    public static void sum(Vector2D v1, Vector2D v2) {
        v1 = sumV(v1, v2);
    }

    /**
     * @param v1
     * @param v2
     * @return v1 + v2
     */
    public static Vector2D sumV(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    /**
     * adds v to this
     * 
     * @param v
     */
    public void sum(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    /**
     * sets v1 = v1 - v2
     * 
     * @param v1
     * @param v2
     */
    public static void difference(Vector2D v1, Vector2D v2) {
        v1 = sumV(v1, v2);
    }

    /**
     * @param v1
     * @param v2
     * @return v1 - v2
     */
    public static Vector2D differenceV(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    /**
     * subtracts v from this
     * 
     * @param v
     */
    public void difference(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    /**
     * @param v1
     * @param v2
     * @return v1*v2
     */
    public static float dotProduct(Vector2D v1, Vector2D v2) {
        return (v1.x * v2.x + v1.y * v2.y);
    }

    /**
     * The 2D cross product, unlike the 3D version, does not return a vector but a
     * scalar. This scalar value actually represents the magnitude of the orthogonal
     * vector along the z-axis, if the cross product were to actually be performed
     * in 3D.
     * 
     * @param v1
     * @param v2
     * @return v1.x * v2.y - v1.y * v2.x
     */
    public static float crossProduct(Vector2D v1, Vector2D v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    /**
     * The 2D cross product, unlike the 3D version, does not return a vector but a
     * scalar. This scalar value actually represents the magnitude of the orthogonal
     * vector along the z-axis, if the cross product were to actually be performed
     * in 3D.
     * 
     * @param v
     * @param f
     * @return (f * v.y, -f * v.x)
     */
    public static Vector2D crossProduct(Vector2D v, float f) {
        return new Vector2D(f * v.y, -f * v.x);
    }

    /**
     * The 2D cross product, unlike the 3D version, does not return a vector but a
     * scalar. This scalar value actually represents the magnitude of the orthogonal
     * vector along the z-axis, if the cross product were to actually be performed
     * in 3D.
     * 
     * @param f
     * @param v
     * @return (-f * v.y, f * v.x)
     */
    public static Vector2D crossProduct(float f, Vector2D v) {
        return new Vector2D(-f * v.y, f * v.x);
    }

    // methods for vector operations on Vector2D objects ends here

    // methods for vector manipulation starts here

    /**
     * @return Squared length of this vector
     */
    public float lengthSq() {
        return (this.x * this.x + this.y * this.y);
    }

    /**
     * @return Length of this vector
     */
    public float length() {
        return (float) StrictMath.sqrt(this.lengthSq());
    }

    /**
     * @param v1
     * @param v2
     * @return Squared distance between two vectors
     */
    public static float distanceSq(Vector2D v1, Vector2D v2) {
        return (new Vector2D(v2.x - v1.x, v2.y - v1.y)).lengthSq();
    }

    /**
     * @param v
     * @return Squared distance between v and this vectors
     */
    public float distanceSq(Vector2D v) {
        return Vector2D.distanceSq(v, this);
    }

    /**
     * @param v1
     * @param v2
     * @return Distance between two vectors
     */
    public static float distance(Vector2D v1, Vector2D v2) {
        return (new Vector2D(v2.x - v1.x, v2.y - v1.y)).length();
    }

    /**
     * @param v
     * @return Distance between v and this vectors
     */
    public float distance(Vector2D v) {
        return Vector2D.distanceSq(v, this);
    }

    // methods for vector manipulation ends here

    // extra methods

    /**
     * 
     * Uses rotation matrix concept. In linear algebra, a rotation matrix is a
     * matrix that is used to perform a rotation in Euclidean space. Here's a link:
     * https://en.wikipedia.org/wiki/Rotation_matrix
     * 
     * @param radians: Rotates this vector by the given radians
     */
    public void rotate(float radians) {
        float c = (float) StrictMath.cos(radians);
        float s = (float) StrictMath.sin(radians);

        float xp = this.x * c - this.y * s;
        float yp = this.x * s + this.y * c;

        this.x = xp;
        this.y = yp;
    }

    /**
     * Normalizes this vector, hence making it an unit vector
     */
    public void normalize() {
        float lensq = this.lengthSq();

        if (lensq > ImpulseMath.EPSILON_SQ) {
            float invLen = 1.0f / (float) StrictMath.sqrt(lensq);
            this.x *= invLen;
            this.y *= invLen;
        }
    }

    @Override
    public boolean equals(Object v) {
        Vector2D vector = (Vector2D) v;
        return (vector.x == this.x && vector.y == this.y);
    }

    @Override
    public String toString() {
        return String.format(" {(%.2f)i + (%.2f)j} ", this.x, this.y);
    }

    /**
     * @param len
     * @return an array of len Vector2D objects
     */
    public static Vector2D[] arrayOf(int len) {
        Vector2D[] arr = new Vector2D[len];

        while (--len >= 0) {
            arr[len] = new Vector2D();
        }

        return arr;
    }
}