import models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
            sb.append(particle.getId() + "\t" +  particle.getPosition().getX() + "\t" + particle.getPosition().getY() + "\t"
                    + particle.getVx() + "\t" + particle.getVy() + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addWalls(FileOutputStream fileOutputStream, int cant, double mass, int L) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append(cant + "\t" +  0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\n");
            sb.append(cant + "\t" +  0 + "\t" + L + "\t" + 0 + "\t" + 0 + "\n");
            sb.append(cant + "\t" +  L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\n");
            sb.append(cant + "\t" +  L + "\t" + L + "\t" + 0 + "\t" + 0 + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addHeader(FileOutputStream fileOutputStream, int cantParticles) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append((cantParticles)+ "\n");
            sb.append("id" + "\t" +  "x" + "\t" + "y" + "\t" + "vx" + "\t" + "vy" + "\n");
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
}
