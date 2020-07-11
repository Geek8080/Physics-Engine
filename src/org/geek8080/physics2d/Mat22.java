package org.geek8080.physics2d;

/**
 * The class handles Vector rotation
 */
public class Mat22 {

	public float m00, m01;
	public float m10, m11;

	public Mat22() {

	}

	public Mat22(float a, float b, float c, float d) {
		this.set(a, b, c, d);
	}

	public Mat22(float radians) {
		this.set(radians);
	}

	/**
	 * Sets rotation matrix to rotate a vector by radians.
	 * 
	 * @param radians
	 */
	public void set(float radians) {
		float c = (float) StrictMath.cos(radians);
		float s = (float) StrictMath.sin(radians);

		this.m00 = c;
		this.m01 = -s;
		this.m10 = s;
		this.m11 = c;
	}

	/**
	 * Sets the value for m00, m01, m10, m11
	 * 
	 * @param a: m00
	 * @param b: m01
	 * @param c: m10
	 * @param d: m11
	 */
	public void set(float a, float b, float c, float d) {
		this.m00 = a;
		this.m01 = b;
		this.m10 = c;
		this.m11 = d;
	}

	/**
	 * sets values for m00, m01, m10, m11 of this matrix as that of m
	 * 
	 * @param m
	 */
	public void set(Mat22 m) {
		this.m00 = m.m00;
		this.m01 = m.m01;
		this.m10 = m.m10;
		this.m11 = m.m11;
	}

	/**
	 * Sets out to the transformation of v by this matrix.
	 */
	public Vector2D mul(Vector2D v, Vector2D out) {
		return mul(v.x, v.y, out);
	}

	/**
	 * Returns a new vector that is the transformation of v by this matrix.
	 */
	public Vector2D mul(Vector2D v) {
		return mul(v.x, v.y, new Vector2D());
	}

	/**
	 * Transforms v by this matrix.
	 */
	public Vector2D muli(Vector2D v) {
		return mul(v.x, v.y, v);
	}

	/**
	 * Sets out the to transformation of {x,y} by this matrix.
	 */
	public Vector2D mul(float x, float y, Vector2D out) {
		out.x = m00 * x + m01 * y;
		out.y = m10 * x + m11 * y;
		return out;
	}

	/**
	 * Multiplies this matrix by x.
	 */
	public void muli(Mat22 x) {
		set(m00 * x.m00 + m01 * x.m10, m00 * x.m01 + m01 * x.m11, m10 * x.m00 + m11 * x.m10, m10 * x.m01 + m11 * x.m11);
	}

	/**
	 * Sets out to the multiplication of this matrix and x.
	 */
	public Mat22 mul(Mat22 x, Mat22 out) {
		out.m00 = m00 * x.m00 + m01 * x.m10;
		out.m01 = m00 * x.m01 + m01 * x.m11;
		out.m10 = m10 * x.m00 + m11 * x.m10;
		out.m11 = m10 * x.m01 + m11 * x.m11;
		return out;
	}

	/**
	 * Returns a new matrix that is the multiplication of this and x.
	 */
	public Mat22 mul(Mat22 x) {
		return mul(x, new Mat22());
	}

	/**
	 * Sets the matrix to it's transpose.
	 */
	public void transposei() {
		float t = m01;
		m01 = m10;
		m10 = t;
	}

	/**
	 * Sets out to the transpose of this matrix.
	 */
	public Mat22 transpose(Mat22 out) {
		out.m00 = m00;
		out.m01 = m10;
		out.m10 = m01;
		out.m11 = m11;
		return out;
	}

	/**
	 * Returns a new matrix that is the transpose of this matrix.
	 */
	public Mat22 transpose() {
		return transpose(new Mat22());
	}

}