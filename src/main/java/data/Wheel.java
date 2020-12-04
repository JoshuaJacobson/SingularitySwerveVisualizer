package data;

public class Wheel {
    private WheelLocation location;
    private Point position;
    public Wheel(WheelLocation location, Point position) {
        this.location = location;
        this.position = position;
    }
    public boolean isSameLocation(Wheel wheel) {
        return this.location == wheel.location;
    }
    public boolean isLocation(WheelLocation location) {
        return this.location == location;
    }
    public ULine lineToWheel(Wheel wheel) {
        return new ULine(
            this.position,
            wheel.position
        );
    }
    public Point getPosition() {
        return this.position;
    }
    public WheelLocation getLocation() {
        return this.location;
    }
}