package itba.edu.ar;

import itba.edu.ar.models.Particle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FileGenerator {

    public static void  addToOutputFile(FileOutputStream fileOutputStream, List<Particle> set, long time, int L) throws IOException {

        // id x y vx vy colorR colorG colorB
        StringBuilder sb = new StringBuilder();
        sb.append(set.size() + 4);
        sb.append("\n");
        sb.append(time);
        sb.append("\n");
        for (Particle particle : set) {
            sb.append(particle.getId());
            sb.append("\t");
            sb.append(particle.getPosition().getX());
            sb.append("\t");
            sb.append(particle.getPosition().getY());
            sb.append("\t");
            sb.append(particle.getSpeed() * Math.cos(particle.getAngle()));
            sb.append("\t");
            sb.append(particle.getSpeed() * Math.cos(particle.getAngle()));
            sb.append("\t");
            sb.append(1 + (Math.cos(particle.getAngle())) /2 );
            sb.append("\t");
            sb.append(1 + (Math.sin(particle.getAngle())) / 2);
            sb.append("\n");
        }
        sb.append("999 0 0 0 0 0 0 0");
        sb.append("\n");
        sb.append("999 0 " + L + " 0 0 0 0 0");
        sb.append("\n");
        sb.append("999 " + L + " 0 0 0 0 0 0");
        sb.append("\n");
        sb.append("999 " + L + " " + L + " 0 0 0 0 0");
        sb.append("\n");
        fileOutputStream.write(sb.toString().getBytes());
        //fileOutputStream.close();

    }

    public static FileOutputStream createOutputFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        //fileOutputStream.close();
        return fileOutputStream;
    }
}
