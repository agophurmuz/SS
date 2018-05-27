package itba.edu.generators;

import java.awt.*;

public class ColorGenerator {
    /**
     * Remaps x from old-interval to new-interval.
     * DoubleInterval just wraps double values a, b.
     */
    public float remap(float x, DoubleInterval oldDomain, DoubleInterval newDomain) {
        float oldRange = oldDomain.size(); // oldDomain.b - oldDomain.a
        float oldRangeValue = x - oldDomain.a; // lowerBound a is just an offset
        float percentile = oldRangeValue / oldRange;

        float newRange = newDomain.size(); // newDomain.b - newDomain.a
        float newRangeValue = percentile * newRange;
        float newVal = newRangeValue + newDomain.a;
        return newVal;
    }

    /**
     * Returns color from specified color-interval that matches normValue <0,1>.
     * If normValue = 0, angleFrom = 0 (red), angleTo = 120 (green) then color = red.
     */
    public Color intervalColor(float normValue, float angleFrom, float angleTo) {
        float angleValue = remap(normValue, new DoubleInterval(0, 1.59f), new DoubleInterval(angleFrom, angleTo));
        return Color.getHSBColor(angleValue, 1.0f, 1.0f);
    }
}
