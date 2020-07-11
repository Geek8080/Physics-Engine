package org.geek8080.physics2d;

public class CollisionPolygonCircle implements CollisionCallback {

    public static final CollisionPolygonCircle instance = new CollisionPolygonCircle();

    @Override
    public void handleCollision(Manifold m, Body body1, Body body2) {
        CollisionCirclePolygon.instance.handleCollision(m, body2, body1);

        if (m.contactCount > 0) {
            m.normal.set(Vector2D.scaledMultiplicationN(m.normal, -1));
        }
    }
}