package data;

public class ULine {
    public static double MAX_LENGTH_DIFF = 0.001;
    private Point source;
    private Point destination;
    public ULine(Point source, Point destination) {
        this.source = source;
        this.destination = destination;
    }
    public boolean equalsLength(double length) {
        return Math.abs(this.getLength() - length) < MAX_LENGTH_DIFF;
    }
    private double getLength() {
        return Math.sqrt(
            Math.pow(source.getX() - destination.getX(), 2) +
            Math.pow(source.getY() - destination.getY(), 2)
        );
    }
    public Point getSource() {
        return this.source;
    }
    public Point getDest() {
        return this.destination;
    }
}
