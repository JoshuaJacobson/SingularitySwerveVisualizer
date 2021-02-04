package robot;

import java.io.PrintStream;
import java.util.Formatter;

import data.Point;
import data.ULine;
import data.Wheel;
import data.WheelLocation;

public class Robot {
    private final double width;
    private final double length;
    private Point velocityVector;
    private double rotationMoment;
    private Point location;
    private double rotation;
    private final Wheel[] defaults;

    private static final double MAX_VELOCITY = 50;
    private static final double MAX_ROTATION = Math.PI / 20;

    public Robot(double width, double length) {
        this.width = width;
        this.length = length;
        velocityVector = new Point(0, 0);
        rotationMoment = 0;
        location = new Point(0, 0);
        rotation = 0;
        defaults = new Wheel[4];
        defaults[0] = new Wheel(WheelLocation.BackLeft, new Point(-width / 2.0, -length / 2.0));
        defaults[1] = new Wheel(WheelLocation.FrontLeft, new Point(-width / 2.0, length / 2.0));
        defaults[2] = new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, length / 2.0));
        defaults[3] = new Wheel(WheelLocation.BackRight, new Point(width / 2.0, -length / 2.0));
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    public double getRotate() {
        return rotation;
    }

    public ULine[] applyWheels(Wheel[] wheels) {
        // Calculate Acceleration for all wheels
        Point resultantVector = new Point(0, 0);
        for (Wheel wheel : wheels) {
            if(wheel.getPosition().length() >= 0.001){
                resultantVector = resultantVector.add(wheel.getPosition());
            } 
        }
        resultantVector = new Point(resultantVector.getX() / 4.0, resultantVector.getY() / 4.0);
        if (Math.abs(resultantVector.getX()) < 0.001)
            resultantVector = new Point(0, resultantVector.getY());
        if (Math.abs(resultantVector.getY()) < 0.001)
            resultantVector = new Point(resultantVector.getX(), 0);
        if (resultantVector.getX() == 0 && resultantVector.getY() == 0) {
            resultantVector = new Point(-velocityVector.getX() * .1, -velocityVector.getY() * .1);
        }

        // Calculate the rotational moment
        double rotation = 0;
        for (Wheel wheel : wheels) {
            double local_rotation = 0;
            switch (wheel.getLocation()) {
                case BackLeft:
                    local_rotation = spinningDifferential(defaults[0], wheel);
                    break;
                case FrontLeft:
                    local_rotation = spinningDifferential(defaults[1], wheel);
                    break;
                case FrontRight:
                    local_rotation = spinningDifferential(defaults[2], wheel);
                    break;
                case BackRight:
                    local_rotation = spinningDifferential(defaults[3], wheel);
                    break;
                default:
                    break;
            }
            rotation += local_rotation;
        }
        rotation /= 4.0;
        

        // Calculate our new velocity
        resultantVector = new Point(resultantVector.getX() + velocityVector.getX(),
                resultantVector.getY() + velocityVector.getY());
        if (Math.abs(resultantVector.length()) > MAX_VELOCITY) {
            double scalar = MAX_VELOCITY / Math.abs(resultantVector.length());
            resultantVector = new Point(resultantVector.getX() * scalar, resultantVector.getY() * scalar);
        }
        velocityVector = resultantVector;

        // Calculate resultant rotation
        rotationMoment = rotation;
        if (Math.abs(rotationMoment) > MAX_ROTATION) {
            double scalar = MAX_ROTATION / Math.abs(rotationMoment);
            rotationMoment *= scalar;
        }

        return renderWheels(wheels);
    }

    public double spinningDifferential(Wheel defaultWheel, Wheel wheel) {
        double defaultRot = defaultWheel.getPosition().rotate(Math.PI / 2).rotation();
        double res = Math.PI / 2
            - Math.abs(defaultRot - (wheel.getPosition().rotation()));
        System.out.printf("DEFAULT: {%4.1f}, WHEEL: {%4.1f} RES: {%4.1f}%n", defaultRot, Math.toDegrees(wheel.getPosition().rotation()), Math.toDegrees(res));
        return res;
    }

    public void move(double time) {
        location = new Point(location.getX() + (velocityVector.getX() * time),
                location.getY() + (velocityVector.getY() * time));
        rotation += rotationMoment;
    }

    public RobotLocation render() {
        RobotLocation res = new RobotLocation(this);
        for (Wheel wheel : defaults) {
            res = res.setWheel(wheel.rotate(rotation).add(location));
            // System.out.println(wheel.rotate(rotation).add(location).getPosition().getX()
            // + " " + wheel.rotate(rotation).add(location).getPosition().getY());
        }
        return res;
    }

    public ULine[] renderWheels(Wheel[] wheels) {
        RobotLocation res = new RobotLocation(this);
        for (Wheel wheel : defaults) {
            res = res.setWheel(wheel.rotate(rotation).add(location));
            // System.out.println(wheel.rotate(rotation).add(location).getPosition().getX()
            // + " " + wheel.rotate(rotation).add(location).getPosition().getY());
        }
        for (Wheel wheel : wheels) {
            double scalar = 5.0 / wheel.getPosition().length();
            wheel = new Wheel(wheel.getLocation(), new Point(wheel.getPosition().getX()*scalar,wheel.getPosition().getY()*scalar));
            try {
                res = res.setWheel(res.getWheel(wheel.getLocation()).add(wheel.getPosition()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.render().renderPaths(res);
    }

    public String toString() {
        return String.format("ROBOT POSITION: (%6.2f, %6.2f) ROTATION: %6.2f", location.getX(), location.getY(), rotation);
    }
}
