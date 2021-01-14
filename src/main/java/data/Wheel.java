package data;

import javafx.scene.paint.Color;

public class Wheel {
    private final WheelLocation location;
    private final Point position;
    public Wheel(WheelLocation location, Point position) {
        this.location = location;
        this.position = position;
    }
    public Wheel(WheelLocation location, double angle, double distance) {
        this.location = location;
        this.position = new Point(
            distance * Math.cos(angle),
            distance * Math.sin(angle)
        );
    }
    public boolean isSameLocation(Wheel wheel) {
        return this.location == wheel.location;
    }
    public boolean isLocation(WheelLocation location) {
        return this.location == location;
    }
    public ULine lineToWheel(Wheel wheel, Color color) {
        return new ULine(
            this.position,
            wheel.position,
            color
        );
    }
    public Point getPosition() {
        return this.position;
    }
    public WheelLocation getLocation() {
        return this.location;
    }
    public Wheel add(Point partner) {
        return new Wheel(location, position.add(partner));
    }
    public Wheel rotate(double radians) {
        return new Wheel(location, position.rotate(radians));
    }
}