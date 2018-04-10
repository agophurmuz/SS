package itba.edu.ar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileGenerator {


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
}
