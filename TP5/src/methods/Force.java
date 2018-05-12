package methods;

import models.Particle;

import java.util.HashMap;
import java.util.Set;

public class Force {


    private double k;
    private double gama;
    private double prevSup;
    private double sup;
    private double deltaTime;
    private static final double G = -10;
    private HashMap<String,Double> prevSuperpositions;

    public Force(double k, double gama, double deltaTime) {
        this.k = k;
        this.gama = gama;
        this.prevSup = 0;
        this.sup = 0;
        this.deltaTime = deltaTime;
        prevSuperpositions = new HashMap<>();
    }

    private String getSuperpositionKey(Particle p1, Particle p2){
        /*String invertedKey = p2.getId()+"-"+p1.getId();

        if(prevSuperpositions.containsKey(invertedKey)){
            return invertedKey;
        }*/

        return p1.getId()+"-"+p2.getId();
    }

    private void addSuperposition(Particle p1, Particle p2, double sup){
        String key = getSuperpositionKey(p1,p2);
        this.prevSuperpositions.put(key, sup);
    }

    private double getPrevSup(Particle p1, Particle p2){
        String key = getSuperpositionKey(p1,p2);
        Double sup = prevSuperpositions.get(key);

        return sup == null ? 0 : sup;
    }

    public void setForces(Particle particle, Set<Particle> neighbors) {

        try {
            double totalX = 0;
            double totalY = particle.getMass() * G;
            for (Particle p : neighbors) {
                if (particle.getId() != p.getId()) {
                    sup = superposition(particle, p);
                    totalX += getForceReceivedFrom(particle, p, k, gama) * getNormalXVector(particle,p);
                    totalY += getForceReceivedFrom(particle, p, k, gama) * getNormalYVector(particle,p);
                    addSuperposition(particle,p,sup);
                }
            }
            particle.setFx(totalX);
            particle.setFy(totalY);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private double getNormalXVector(Particle particle, Particle p){
        return (p.getX() - particle.getX()) / getDistance(particle, p);
    }

    private double getNormalYVector(Particle particle, Particle p){
        return (p.getY() - particle.getY()) / getDistance(particle, p);
    }

    private double getForceReceivedFrom(Particle particle, Particle p, double k, double gama) {
        //System.out.println("Particle x: " + particle.getX() +" y: "+ particle.getY() + " sup: " + sup );
        if (sup < 0) {
            sup = 0;
            return 0;
        }
        double prev = getPrevSup(particle,p);
        return (-k * sup) - (gama * ((sup - prev) / deltaTime));
        //return (-k * sup) - gama * relativeSpeed(particle,p);
    }

    private double relativeSpeed(Particle p1, Particle p2){
        return (p2.getVx() - p1.getVx()) * getNormalXVector(p2,p1) + (p2.getVy() - p1.getVy()) * getNormalYVector(p2,p1);
    }

    private double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) +
                Math.pow(p1.getY() - p2.getY(), 2));
    }

    private double superposition(Particle p1, Particle p2) {
        return p1.getRadius() + p2.getRadius() - getDistance(p1, p2);
    }

    private double superpositionPrime(Particle p1, Particle p2) {
        return -(Math.sqrt(Math.pow(p1.getVx() - p2.getVx(), 2) + Math.pow(p1.getVy() - p2.getVy(), 2)));
    }


}
