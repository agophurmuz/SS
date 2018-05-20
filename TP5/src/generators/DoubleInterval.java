package generators;

public class DoubleInterval {

    float a;
    float b;

    public DoubleInterval(float a, float b) {
        this.a = a;
        this.b = b;
    }

    public float size() {
        return b-a;
    }
}
