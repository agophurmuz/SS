import models.Particle;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class FileGenerator {

    public static FileOutputStream createOutputFilePoints(String fileName){
        File file = new File(fileName);
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static void addParticle(FileOutputStream fileOutputStream, Particle particle) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append(particle.getId() + "\t" +  particle.getX() + "\t" + particle.getY() + "\t"
                    + particle.getVx() + "\t" + particle.getVy() + "\t" + particle.getRadius() + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addWalls(FileOutputStream fileOutputStream, int cant, double mass, double L, double W) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append(cant + "\t" +  0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\n");
            sb.append(cant + "\t" +  0 + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\n");
            sb.append(cant + "\t" +  W + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\n");
            sb.append(cant + "\t" +  W + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addHeader(FileOutputStream fileOutputStream, int cantParticles) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append((cantParticles + 4)+ "\n");
            sb.append("id" + "\t" +  "x" + "\t" + "y" + "\t" + "vx" + "\t" + "vy" + "\t" + "radius" + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCorners(FileOutputStream fileOutputStream, int cantParticles, int L) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(cantParticles + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\n");
            sb.append((cantParticles + 1) + "\t" + 0 + "\t" + L + "\t" + 0 + "\t" + 0 + "\n");
            sb.append((cantParticles + 2) + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\n");
            sb.append((cantParticles + 3) + "\t" + L + "\t" + L + "\t" + 0 + "\t" + 0 + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPointsToFile(FileOutputStream fileOutputStream, Map<Particle, Set<Particle>> particles, Particle particle) {

        try {

            StringBuilder sb = new StringBuilder();
            Set<Particle> neighbors = particles.get(particle);
            sb.append(particles.keySet().size() + "\n");
            sb.append("id" + "\t" +  "x" + "\t" + "y" + "\t" + "R" + "\t" + "G" + "\t" + "B" + "\n");
            //id x y R G B
            for (Particle p : particles.keySet()) {
                if(p.equals(particle)){
                    sb.append(printLine(p, Color.YELLOW));
                    //neighbors = particles.get(p);
                    for (Particle n : neighbors) {
                        sb.append(printLine(n, Color.RED));
                    }
                } else if(!neighbors.contains(p)){
                    sb.append(printLine(p, Color.GREEN));
                }
            }

            fileOutputStream.write(sb.toString().getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String printLine(Particle particle, Color color) {
        return particle.toString() + "\t" + color.getRed() + "\t" + color.getGreen() + "\t" + color.getBlue() + "\n";
    }

    public static void addCaudal(FileOutputStream fileOutputStreamCaudal, double time, int caudal) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(time + "\t" +  caudal + "\n");
            fileOutputStreamCaudal.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
