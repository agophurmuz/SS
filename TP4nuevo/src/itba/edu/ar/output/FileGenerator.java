package itba.edu.ar.output;

import itba.edu.ar.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileGenerator {

    private static final double AU = 149598073;

    public static FileOutputStream createOutputFile(String fileName){
        File file = new File(fileName);
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static void addHeader(FileOutputStream fileOutputStream, int cantParticles) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(cantParticles + "\n");
            sb.append("id" + "\t" +  "x" + "\t" + "y" + "\t" + "vx" + "\t" + "vy" + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void addParticle(FileOutputStream fileOutputStream, Particle particle) {
        try {

            StringBuilder sb = new StringBuilder();
            // id x y vx vy
            double x = particle.getX()/1000;
            double y = particle.getY()/1000;
            sb.append(particle.getId() + "\t" + (x/AU)  + "\t" + (y/AU) + "\t"
                    + (particle.getVx()/1000) + "\t" + (particle.getVy()/1000) + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
