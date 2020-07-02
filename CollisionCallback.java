public interface CollisionCallback {
    public void resolveCollision(Shape a, Shape b, Manifold manifold);
}