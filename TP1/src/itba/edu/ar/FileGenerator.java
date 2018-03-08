package itba.edu.ar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class FileGenerator {

    public static void createOutputFile(String fileName, HashMap<Integer, Set<Integer>> hm, int cantPoints) {
        File file = new File(fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            for (Integer key : hm.keySet()) {
                sb.append(key);
                sb.append(": ");
                for (Integer particle: hm.get(key)) {
                    sb.append(" ,");
                    sb.append(particle);
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
