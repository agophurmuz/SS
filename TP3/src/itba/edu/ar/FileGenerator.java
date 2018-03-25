package itba.edu.ar;

import itba.edu.ar.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

    public static void addPointsToFile(FileOutputStream fileOutputStream, List<Particle> particles) {

        try {

            StringBuilder sb = new StringBuilder();
            sb.append(particles.size()+2 + "\n");
            sb.append("id" + "\t" + "x" + "\t" + "y" + "\t" + "Vx" + "\t" + "Vy" + "\t" + "Masa" + "\t" + "Radio" + "\n");
            //id x y R G B
            for (Particle p : particles) {
                sb.append(printLine(p));
            }
            // Limits
            sb.append(999 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0.05 + "\n");
            sb.append(998 + "\t" + 0.5 + "\t" + 0.5 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0.05 + "\n");
            fileOutputStream.write(sb.toString().getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String printLine(Particle particle) {
        return particle.toString() + "\n";
    }


}
