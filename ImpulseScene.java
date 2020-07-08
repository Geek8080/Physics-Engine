import java.util.ArrayList;

public class ImpulseScene {
    public double dt;
    public int iterations;
    public ArrayList<Shape> bodies = new ArrayList<>();
    public ArrayList<Manifold> contacts = new ArrayList<>();

    public ImpulseScene(double dt, int iterations) {
        this.iterations = iterations;
    }

    public void step() {

        // Generate new collision info
        contacts.clear();
        for (int i = 0; i < bodies.size(); ++i) {
            Shape A = bodies.get(i);

            for (int j = i + 1; j < bodies.size(); ++j) {
                Shape B = bodies.get(j);

                if (A.getInvMass() == 0 && B.getInvMass() == 0) {
                    continue;
                }

                Manifold m = new Manifold(A, B);
                m.solve();

                if (m.contactCount > 0) {
                    contacts.add(m);
                }
            }

        }

        // Integrate forces
        for (int i = 0; i < bodies.size(); ++i) {
            integrateForces(bodies.get(i), dt);
        }

        // Initialize collision
        for (int i = 0; i < contacts.size(); ++i) {
            contacts.get(i).initialize();
        }

        // Solve collisions
        for (int j = 0; j < iterations; ++j) {
            for (int i = 0; i < contacts.size(); ++i) {
                contacts.get(i).applyImpulse();
            }
        }

        // Integrate velocities
        for (int i = 0; i < bodies.size(); ++i) {
            integrateVelocity(bodies.get(i), dt);
        }

        // apply positional corrections
        for (int i = 0; i < contacts.size(); ++i) {
            contacts.get(i).positionalCorrection();
        }

        // Clear all forces
        for (int i = 0; i < bodies.size(); ++i) {
            Shape b = bodies.get(i);
            b.setForce(new Vector(new Point(0, 0)));
        }
    }

    private void integrateVelocity(Shape shape, double dt2) {
        if (shape.getInvMass() == 0) {
            return;
        }

        Vector positionVector = Vector.sum(new Vector(shape.getPosition()),
                Vector.scalarProduct(dt2, shape.getVelocity()));
        shape.setPosition(new Point(positionVector.getXComponent(), positionVector.getYComponent()));

        integrateForces(shape, dt2);
    }

    private void integrateForces(Shape shape, double dt2) {
        if (shape.getInvMass() == 0) {
            return;
        }

        dt2 = dt2 * 0.5d;

        Vector newForce = Vector.scalarProduct(shape.getInvMass() * dt2, shape.getForce());
        shape.setVelocity(Vector.sum(shape.getVelocity(), newForce));
    }

    public void clear() {
        contacts.clear();
        bodies.clear();
    }
}