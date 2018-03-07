package itba.edu.ar;

import itba.edu.ar.models.Particle;

import java.util.List;

/**
 * Created by marlanti on 3/6/18.
 */
public class app {

    public static void main(String[] args) {
        List<Particle> particles = ParticleSystemGenerator.generateRandomParticleSystem(5,30,0.25);
        System.out.println(particles);
    }

}
