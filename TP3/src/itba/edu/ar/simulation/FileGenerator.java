package itba.edu.ar.simulation;

import itba.edu.ar.simulation.methods.BrownianMovement;
import itba.edu.ar.simulation.models.Particle;

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


    public static void addBigParticlePath(FileOutputStream fileBigParticleOutputStream, Particle particle, double initX, double initY, double v, double mass) {
        try {
            StringBuilder sb = new StringBuilder();
            double zX = Math.pow(particle.getX() - initX, 2);
            double zY = Math.pow(particle.getY() - initY, 2);
            double z2 = zX + zY;
            double T = 0.5 * mass * Math.pow(v, 2);
            //Desplazamiento Cuadrático (z2) - Temperatura
            sb.append(z2 + "\t" + T);
            fileBigParticleOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addBigParticleEventTime(FileOutputStream fileBigParticleTimeOutputStream, Particle particle, double bigParticleInitX, double bigParticleInitY, double v, double time) {
        try {
            StringBuilder sb = new StringBuilder();
            double x = particle.getX();
            double y = particle.getY();
            // datos de la particula grande
            // x y initX initY tiempo
            sb.append(x + "\t" + y + "\t" + bigParticleInitX + "\t" + bigParticleInitY + "\t" + time + "\n");
            fileBigParticleTimeOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
