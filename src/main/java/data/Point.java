package data;

public class Point {
    private final double x;
    private final double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public static Point Unitialized = new Point(0,0);
    public double rotation() {
        double r = Math.atan(getY()/getX());
        if (getX() < 0) {
            r += Math.PI;
        }
        if (getX() > 0 && getY() < 0) {
            r += 2*Math.PI;
        }
        return r;
    }
    public double length() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }
    public Point add(Point partner) {
        return new Point(this.getX()+partner.getX(), this.getY()+partner.getY());
    }
    public Point rotate(double radians) {
        double rotation = this.rotation() + radians;
        double magnitude = this.length();
        return new Point(magnitude*Math.cos(rotation), magnitude*Math.sin(rotation));
    }
}
