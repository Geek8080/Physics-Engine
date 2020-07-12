package org.geek8080.physics2d;

public interface CollisionCallback {

	public void handleCollision(Manifold m, Body a, Body b);
}
