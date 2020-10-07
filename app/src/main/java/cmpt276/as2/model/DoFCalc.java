package cmpt276.as2.model;

/**
 * Provide the functions able to calculate data entered from the user
 */

public class DoFCalc {
    private Lens ls;
    private double aperture;
    private double distance;
    private double COC;

    public DoFCalc(Lens ls, double aperture, double distance, double COC) {
        this.ls = ls;
        this.aperture = aperture;
        this.distance = distance;
        this.COC = COC;
    }

    public double hyperFocalDis() {
        int focalSquare = ls.getFocalLength() * ls.getFocalLength();
        double calc = aperture * COC;

        return (focalSquare / calc);
    }

    public double nearFocalPoint() {
        double calcOne = (hyperFocalDis() * distance);
        double calcTwo = (hyperFocalDis() + (distance - ls.getFocalLength()));

        return (calcOne / calcTwo);
    }

    public double farFocalPoint() {

        if(distance > hyperFocalDis()) {
            return Double.POSITIVE_INFINITY;
        }

        double calcOne = (hyperFocalDis() * distance);
        double calcTwo = (hyperFocalDis() - (distance - ls.getFocalLength()));

        return (calcOne / calcTwo);
    }

    public double depthOfField() {
        return (farFocalPoint() - nearFocalPoint());
    }
}
