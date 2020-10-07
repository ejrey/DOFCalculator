package cmpt276.as2.model;

/**
 * Represent a single lens info such as it's make, max aperture, and focal length. Getter functions.
 */

public class Lens {
    private String cameraMake;
    private double maxAperture;
    private int focalLength;

    public Lens(String cameraMake, double maxAperture, int focalLength) {
        this.cameraMake = cameraMake;
        this.maxAperture = maxAperture;
        this.focalLength = focalLength;
    }

    public String getCameraMake() {
        return cameraMake;
    }

    public double getMaxAperture() {
        return maxAperture;
    }

    public int getFocalLength() {
        return focalLength;
    }

    @Override
    public String toString() {
        return cameraMake + " " + focalLength + "mm " + "F" + maxAperture;
    }
}
