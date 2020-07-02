import java.util.ArrayList;

public class ImpulseScene {
    public double dt;
    public int iterations;
    public ArrayList<Shape> bodies = new ArrayList<>();
    public ArrayList<Manifold> contacts = new ArrayList<>();

    public ImpulseScene(int iterations) {
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

        for (int i = 0; i < contacts.size(); ++i) {
            contacts.get(i).positionalCorrection();
        }
    }

    public void clear() {
        contacts.clear();
        bodies.clear();
    }
}