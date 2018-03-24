package itba.edu.ar;

import itba.edu.ar.models.Particle;
import itba.edu.ar.models.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    private int N;
    private int L;
    private List<Particle> particles;

    public FileParser(String staticName, String dynamicName) {

        try {
            File staticFile = new File(staticName);
            FileReader staticReader = new FileReader(staticFile);
            BufferedReader staticBufferReader = new BufferedReader(staticReader);

            File dynamicFile = new File(dynamicName);
            FileReader dynamicReader = new FileReader(dynamicFile);
            BufferedReader dynamicBufferReader = new BufferedReader(dynamicReader);

            if(staticBufferReader.ready() && dynamicBufferReader.ready()) {
                this.N = Integer.parseInt(staticBufferReader.readLine().trim());
                this.L = Integer.parseInt(staticBufferReader.readLine().trim());

                this.particles = new ArrayList<>();
                dynamicBufferReader.readLine();

                for (int i = 0; i < N; i++) {
                    String[] radiusIds = staticBufferReader.readLine().trim().split(" ");
                    String[] positions = dynamicBufferReader.readLine().trim().split(" ");
                    particles.add(new Particle(new Position(Double.parseDouble(positions[0]), Double.parseDouble(positions[1])),
                            Integer.parseInt(radiusIds[0]) ,Double.parseDouble(radiusIds[1])));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
