package itba.edu.ar;

import itba.edu.ar.models.Particle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class FileGenerator {

    public static void createOutputFile(String fileName, HashMap<Particle, Set<Particle>> hm, int cantPoints) {
        File file = new File(fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            for (Particle particle : hm.keySet()) {
                sb.append(particle.getId());
                sb.append(": ");
                for (Particle neighbor: hm.get(particle)) {
                    sb.append(" ,");
                    sb.append(neighbor.getId());
                }
                sb.append("\n");
            }
            out.write(sb.toString().getBytes());
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
