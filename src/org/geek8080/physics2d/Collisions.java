package org.geek8080.physics2d;

/**
 * The class for holding the jump table for generating Manifolds for collisions
 */
public class Collisions {

    public static CollisionCallback[][] dispatch = {
            { CollisionCircleCircle.instance, CollisionCirclePolygon.instance },
            { CollisionPolygonCircle.instance, CollisionPolygonPolygon.instance } };
}