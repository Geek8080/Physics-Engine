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

}