package data;

import javafx.scene.paint.Color;

public class ULine {
    public static double MAX_LENGTH_DIFF = 0.001;
    private Point source;
    private Point destination;
    private Color color;
    public ULine(Point source, Point destination, Color color) {
        this.source = source;
        this.destination = destination;
        this.color = color;
    }
    public boolean equalsLength(double length) {
        return Math.abs(this.getLength() - length) < MAX_LENGTH_DIFF;
    }
    public double getLength() {
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
    public Color getColor() {
        return this.color;
    }
}
