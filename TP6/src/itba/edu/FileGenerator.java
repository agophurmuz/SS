package itba.edu;


import itba.edu.models.Particle;

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
            // id x y vx vy radius R G B
            sb.append(particle.getId() + "\t" +  particle.getX() + "\t" + particle.getY() + "\t"
                    + particle.getVx() + "\t" + particle.getVy() + "\t" + particle.getRadius() + "\t"
                    //+ (int) particle.getPreasure()+ "\t"
                    + particle.getRed()+ "\t"
                    + particle.getGreen()+ "\t"
                    + particle.getBlue() + "\t" + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addWalls(FileOutputStream fileOutputStream, int cant, double L, double W) {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append(cant + "\t" +  0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0+ "\t" + 0+ "\t" + 0+ "\n");
            sb.append(cant + "\t" +  0 + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0+ "\t" + 0+ "\t" + 0+ "\n");
            sb.append(cant + "\t" +  W + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0+ "\t" + 0+ "\t" + 0+ "\n");
            sb.append(cant + "\t" +  W + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0+ "\t" + 0+ "\t" + 0+ "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addHeader(FileOutputStream fileOutputStream, int cantParticles) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append((cantParticles+4)+ "\n");
            sb.append("id" + "\t" +  "x" + "\t" + "y" + "\t" + "vx" + "\t" + "vy" + "\t" + "radius" + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
