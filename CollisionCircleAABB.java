public class CollisionCircleAABB implements CollisionCallback {
    public static final CollisionCircleAABB instance = new CollisionCircleAABB();

    @Override
    public void resolveCollision(Shape a, Shape b, Manifold manifold) {
        CollisionAABBCircle.instance.resolveCollision(b, a, manifold);

        if (manifold.contactCount>0) {
            manifold.normal.setXComponent(-manifold.normal.getXComponent());
            manifold.normal.setYComponent(-manifold.normal.getYComponent());
        }
    }
}