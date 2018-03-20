package itba.edu.ar;

import itba.edu.ar.models.Particle;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileGenerator {

    public static void createOutputFileNeighbors(String fileName, HashMap<Particle, Set<Particle>> hm) {
        File file = new File(fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            for (Particle particle : hm.keySet()) {
                sb.append(particle.getId());
                sb.append(": ");
                for (Particle neighbor : hm.get(particle)) {
                    sb.append(neighbor.getId());
                    sb.append(" ");
                }
                sb.append("\n");
            }

            out.write(sb.toString().getBytes());
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileOutputStream createOutputFilePoints(String fileName){
        File file = new File(fileName);
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
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
}
