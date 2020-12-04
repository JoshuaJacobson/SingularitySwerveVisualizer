package robot;

import java.util.Arrays;

import javax.swing.text.Position;

import data.Point;
import data.ULine;
import data.Wheel;
import data.WheelLocation;

public class RobotLocation {
    private Robot robot;
    private Wheel[] wheels;

    public RobotLocation(Robot robot) {
        this.robot = robot;
        this.wheels = new Wheel[4];
        this.wheels[0] = new Wheel(WheelLocation.BackLeft, Point.Unitialized);
        this.wheels[1] = new Wheel(WheelLocation.BackRight, Point.Unitialized);
        this.wheels[2] = new Wheel(WheelLocation.FrontRight, Point.Unitialized);
        this.wheels[3] = new Wheel(WheelLocation.FrontLeft, Point.Unitialized);
    }

    private RobotLocation(Robot robot, Wheel[] wheels) {
        this.robot = robot;
        this.wheels = wheels;
    }

    public RobotLocation setWheel(WheelLocation location, Point position) {
        return new RobotLocation(this.robot, this.updateWheel(new Wheel(location, position)));
    }

    public RobotLocation setWheel(Wheel wheel) {
        return new RobotLocation(this.robot, this.updateWheel(wheel));
    }
    public ULine[] renderPaths(RobotLocation target) {
        ULine[] lines = new ULine[4];
        try {
            lines[0] = renderLinePath(WheelLocation.FrontLeft, target);
            lines[1] = renderLinePath(WheelLocation.BackLeft, target);
            lines[2] = renderLinePath(WheelLocation.BackRight, target);
            lines[3] = renderLinePath(WheelLocation.FrontRight, target);
        } catch (Exception e) {
            System.err.println("Could not render, internal issue with Wheels");
        }
        return lines;
    }

    public ULine[] render() {
        ULine[] lines = new ULine[4];
        try {
            lines[0] = renderLine(WheelLocation.FrontLeft, WheelLocation.BackLeft);
            lines[1] = renderLine(WheelLocation.BackLeft, WheelLocation.BackRight);
            lines[2] = renderLine(WheelLocation.BackRight, WheelLocation.FrontRight);
            lines[3] = renderLine(WheelLocation.FrontRight, WheelLocation.FrontLeft);
        } catch (Exception e) {
            System.err.println("Could not render, internal issue with Wheels");
        }
        return lines;
    }
    public boolean isValidPosition() {
        return
            isWheelDistance(WheelLocation.FrontLeft, WheelLocation.BackLeft, robot.getLength()) &&
            isWheelDistance(WheelLocation.BackLeft, WheelLocation.BackRight, robot.getWidth()) &&
            isWheelDistance(WheelLocation.BackRight, WheelLocation.FrontRight, robot.getLength()) &&
            isWheelDistance(WheelLocation.FrontRight, WheelLocation.FrontLeft, robot.getWidth());
    }
    private ULine renderLine(WheelLocation source, WheelLocation dest) throws Exception {
        return getWheel(source).lineToWheel(getWheel(dest));
    }
    private ULine renderLinePath(WheelLocation source, RobotLocation target) throws Exception {
        return getWheel(source).lineToWheel(target.getWheel(source));
    }
    private boolean isWheelDistance(WheelLocation source, WheelLocation dest, double length) {
        try {
            return renderLine(source, dest).equalsLength(robot.getLength());
        } catch (Exception e) {
            System.err.println("WheelLocation not present in Wheels array");
            return false;
        }
    }
    private Wheel[] updateWheel(Wheel wheel) {
        Wheel[] wheels = new Wheel[this.wheels.length];
        int i = 0;
        for (Wheel w : this.wheels) {
            if (w.isSameLocation(wheel)) {
                wheels[i] = wheel;
            } else {
                wheels[i] = w;
            }
            i++;
        }
        return wheels;
    }
    private Wheel getWheel(WheelLocation location) throws Exception {
        for (Wheel w: wheels) {
            if (w.isLocation(location)) {
                return w;
            }
        }
        throw new Exception("Wheel not found");
    }
}
