package itba.edu.ar;

import itba.edu.ar.voyager.Body;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FileGenerator {

    private static final double AU = 149598073;


    public static FileOutputStream createFile(String fileName){
        File file = new File(fileName);
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static void addTitle(FileOutputStream fileOutputStream) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Calculated R" + "\t" + "Analytic R" + "\t" + "Time" + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    public static void addLine(double calculatedR, double analyticR, double time, FileOutputStream fileOutputStream) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(calculatedR + "\t" + analyticR + "\t" + time + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCuadraticError(double error, FileOutputStream fileOutputStream) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Error" + "\t" + error + "\n");
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPlanets(Body body, List<Body> bodies, FileOutputStream fileOutputStream) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(bodies.size() + 1 + "\n");
            sb.append("x" + "\t" + "y" + "\t" + "vx" + "\t" + "vy" + "\n");
            printLine(body, sb);
            for (Body b: bodies) {
                printLine(b, sb);
            }
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printLine(Body body, StringBuilder sb) {
        double posX = body.getPosition().getX() / 1000;
        double posY = body.getPosition().getY() / 1000;
        sb.append((posX/AU) + "\t" + (posY/AU) + "\t" + (body.getvX()/1000) + "\t" + (body.getvY()/1000) + "\n");
    }
}
