public class CollisionCircleCircle implements CollisionCallback {

    public static final CollisionCircleCircle instance = new CollisionCircleCircle();

    @Override
    public void resolveCollision(Shape shape1, Shape shape2, Manifold manifold) {
        Circle a = (Circle) shape1;
        Circle b = (Circle) shape2;

        Vector normal = Vector.difference(new Vector(b.getPosition()), new Vector(a.getPosition()));

        double distance = normal.getMagnitude();
        long radius = a.getRadius() + b.getRadius();

        // No collision
        if (distance >= radius) {
            manifold.contactCount = 0;
            return;
        }

        manifold.contactCount = 1;

        if (distance == 0) {
            manifold.penetration = a.getRadius();
            manifold.normal.setXComponent(1.0d);
            manifold.normal.setYComponent(0.0d);
        } else {
            manifold.penetration = radius - distance;
            normal = Vector.scalarDivision(normal, distance);
            manifold.normal.setXComponent(normal.getXComponent());
            manifold.normal.setYComponent(normal.getYComponent());
        }

    }

}