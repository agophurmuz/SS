package itba.edu.ar;

import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marlanti on 3/6/18.
 */
public class ParticleSystemGenerator {

    /**
     * @param N      Amount of particles in the system.
     * @param L      Domain's size is L*L.
     * @param radius of the particles. All particles will have the same radius
     * @return List of particles with random positions in L*L domain.
     */
    public static final List<Particle> generateRandomParticleSystem(final int N, final int L, final Double radius) {
        ArrayList<Particle> list = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            list.add(new Particle(getRandomPosition(L), i, radius));
        }

        return list;
    }

    public static final List<Particle> generateRandomParticleWithSpeedSystem(final int N, final int L, final Double radius, final Double speed) {
        ArrayList<Particle> list = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            list.add(new Particle(getRandomPosition(L), i, radius, Math.random() * 2 * Math.PI, speed));
        }

        return list;
    }


    private static final Position getRandomPosition(final int L) {

        double x = L * Math.random();
        double y = L * Math.random();

        return new Position(x, y);
    }

    public static final List<Particle> generateParticleSystemFromFile() {
        //TODO
        return null;
    }


}
