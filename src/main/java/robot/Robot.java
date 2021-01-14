package robot;

import data.Point;
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

    private static final double MAX_VELOCITY = 5;
    private static final double MAX_ROTATION = Math.PI / 20;

    public Robot(double width, double length) {
        this.width = width;
        this.length = length;
        velocityVector = new Point(0,0);
        rotationMoment = 0;
        location = new Point(0, 0);
        rotation = 0;
        defaults = new Wheel[4];
        defaults[0] = new Wheel(WheelLocation.BackLeft, new Point(-width/2.0,-length/2.0));
        defaults[1] = new Wheel(WheelLocation.FrontLeft, new Point(-width/2.0,length/2.0));
        defaults[2] = new Wheel(WheelLocation.FrontRight, new Point(width/2.0,length/2.0));
        defaults[3] = new Wheel(WheelLocation.BackRight, new Point(width/2.0,-length/2.0));
    }
    public double getWidth() {
        return width;
    }
    public double getLength() {
        return length;
    }

    public void applyWheels(Wheel[] wheels) {
        //Calculate Acceleration for all wheels
        Point resultantVector = new Point(0,0);
        for (Wheel wheel: wheels) {
            resultantVector = resultantVector.add(wheel.getPosition());
        }
        resultantVector = new Point(resultantVector.getX() / 4.0, resultantVector.getY() / 4.0);

        //Calculate the rotational moment 
        double rotation = 0;
        for (Wheel wheel: wheels) {
            rotation += wheel.getPosition().rotation() - resultantVector.rotation();
        }
        rotation /= 4.0;

        //Calculate our new velocity
        resultantVector = new Point(resultantVector.getX() + velocityVector.getX(), resultantVector.getY() + velocityVector.getY());
        if (Math.abs(resultantVector.length()) > MAX_VELOCITY) {
            double scalar = MAX_VELOCITY / Math.abs(resultantVector.length());
            resultantVector = new Point(resultantVector.getX() * scalar, resultantVector.getY() * scalar);
        }
        velocityVector = resultantVector;

        //Calculate resultant rotation
        rotationMoment = rotation;
        if (Math.abs(rotationMoment) > MAX_ROTATION) {
            double scalar = MAX_ROTATION / Math.abs(resultantVector.length());
            rotationMoment *= scalar;
        }
    }

    public void move(double time) {
        location = new Point(location.getX() + (velocityVector.getX() * time), location.getY() + (velocityVector.getY() * time));
        rotation += rotationMoment;
    }

    public RobotLocation render() {
        RobotLocation res = new RobotLocation(this);
        for (Wheel wheel: defaults) {
            res = res.setWheel(wheel.rotate(rotation).add(location));
            //System.out.println(wheel.rotate(rotation).add(location).getPosition().getX() + "  " + wheel.rotate(rotation).add(location).getPosition().getY());
        }
        return res;
    }
}
