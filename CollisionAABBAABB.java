public class CollisionAABBAABB implements CollisionCallback {
    public static final CollisionAABBAABB instance = new CollisionAABBAABB();

    @Override
    public void resolveCollision(Shape shape1, Shape shape2, Manifold manifold) {
        AABB a = (AABB) shape1;
        AABB b = (AABB) shape2;

        Vector normal = Vector.difference(new Vector(b.getPosition()), new Vector(a.getPosition()));

        double a_xExtent = (a.getMax().getX() - a.getMin().getX()) / 2;
        double b_xExtent = (b.getMax().getX() - b.getMin().getX()) / 2;

        double xOverlap = a_xExtent + b_xExtent - StrictMath.abs(normal.getXComponent());

        if (xOverlap > 0) {

            double a_yExtent = (a.getMin().getY() - a.getMax().getY()) / 2;
            double b_yExtent = (b.getMin().getY() - b.getMax().getY()) / 2;

            double yOverlap = a_yExtent + b_yExtent - StrictMath.abs(normal.getYComponent());

            if (yOverlap > 0) {
                manifold.contactCount = 1;

                if (xOverlap < yOverlap) {
                    if (normal.getXComponent() < 0) {
                        manifold.normal.setXComponent(-1.0d);
                    } else {
                        manifold.normal.setXComponent(1.0d);
                    }
                    manifold.penetration = xOverlap;
                } else {
                    if (normal.getYComponent() < 0) {
                        manifold.normal.setYComponent(-1.0d);
                    } else {
                        manifold.normal.setYComponent(1.0d);
                    }
                    manifold.penetration = yOverlap;
                }
            }
        }
    }
}