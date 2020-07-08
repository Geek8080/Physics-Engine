public class CollisionAABBCircle implements CollisionCallback {
    public static final CollisionAABBCircle instance = new CollisionAABBCircle();

    @Override
    public void resolveCollision(Shape shape1, Shape shape2, Manifold manifold) {
        AABB a = (AABB) shape1;
        Circle c = (Circle) shape2;

        Vector normal = Vector.difference(new Vector(c.getPosition()), new Vector(a.getPosition()));

        Vector closest = new Vector(new Point(normal.getXComponent(), normal.getYComponent()));

        double xExtent = (a.getMax().getX() - a.getMin().getX()) / 2.0d;
        double yExtent = (a.getMin().getY() - a.getMax().getY()) / 2.0d;

        closest.setXComponent(ImpulseMath.clamp(-xExtent, xExtent, closest.getXComponent()));
        closest.setYComponent(ImpulseMath.clamp(-yExtent, yExtent, closest.getYComponent()));

        boolean inside = false;

        if (normal.equals(closest)) {
            inside = true;

            if (Math.abs(normal.getXComponent()) > Math.abs(normal.getYComponent())) {
                if (closest.getXComponent() > 0) {
                    closest.setXComponent(xExtent);
                } else {
                    closest.setXComponent(-xExtent);
                }
            } else {
                if (closest.getYComponent() > 0) {
                    closest.setYComponent(yExtent);
                } else {
                    closest.setYComponent(-yExtent);
                }
            }
        }

        Vector n = Vector.difference(normal, closest);
        double d = n.getMagnitude();
        double r = c.getRadius();

        if (d > r && !inside) {
            manifold.contactCount = 0;
            return;
        }

        manifold.contactCount = 1;

        if (inside) {
            manifold.normal.setXComponent((-n.getXComponent()) / n.getMagnitude());
            manifold.normal.setYComponent((-n.getYComponent()) / n.getMagnitude());
        } else {
            manifold.normal.setXComponent((normal.getXComponent()) / normal.getMagnitude());
            manifold.normal.setYComponent((normal.getYComponent()) / normal.getMagnitude());
        }

        manifold.penetration = r - d;
    }
}