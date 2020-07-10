package org.geek8080.physics2d;

public class Manifold {

    public Body a;
    public Body b;
    public float penetration;
    public final Vector2D normal = new Vector2D();
    public Vector2D[] contacts = { new Vector2D(), new Vector2D() };
    public float contactCount;
    public float e;
    public float sf;
    public float df;

    public Manifold(Body a, Body b) {
        this.a = a;
        this.b = b;
    }

    public void solve() {

    }

    public void initialize() {

    }

    public void applyImpulse() {

    }

    public void positionalCorrection() {

    }

    public void infinitemassCorrection() {

    }

}
