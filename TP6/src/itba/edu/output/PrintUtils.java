package itba.edu.output;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PrintUtils {

    //TODO: MAKE IT GENERIC
    private static List<String> proccesSlidingWindow(ArrayList<Integer> caudals, double window, double delta2) {
        int deltasInWindow = (int) (window/delta2);
        double average;
        int sum = 0;
        ArrayList<Integer> averagedCaudals = new ArrayList<>();
        List<String> caudalFileLog = new ArrayList<>();
        caudalFileLog.add("Tiempo"+ "\t" +"Caudal promediado");
        for(int i=0;i<caudals.size()-deltasInWindow;i++){
            for (int j=i;j<i+deltasInWindow;j++){
                sum+=caudals.get(j);
            }
            average = (double)sum/deltasInWindow;
            caudalFileLog.add(i*delta2+ "\t" +average);
            sum = 0;
        }
        return caudalFileLog;
    }

    public static void writeLogFileFromList(List<String> fileLog,String filename) {
        Path file = Paths.get(filename);
        try {
            Files.write(file, fileLog, Charset.forName("UTF-8"));
        } catch (Exception e) {

        }
    }
}
