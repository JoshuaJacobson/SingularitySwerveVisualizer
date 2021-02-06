package robot;

import java.util.ArrayList;

import data.Point;
import data.ULine;
import data.Wheel;
import data.WheelLocation;

public class Robot {
    private final double width;
    private final double length;
    private Point accelerationVector;
    private Point velocityVector;
    private double rotationalVelocity;
    private double rotationalAcceleration;
    private Point location;
    private double rotation;
    private final RobotLocation defaults;

    private static final double MAX_VELOCITY = 50;
    private static final double MAX_ROTATION = Math.PI / 20;
    private static final double VELOCITY_DECEL = 25;
    private static final double ROTATION_DECEL = Math.PI / 40;

    public Robot(double width, double length) {
        this.width = width;
        this.length = length;
        velocityVector = new Point(0, 0);
        accelerationVector = new Point(0, 0);
        rotationalVelocity = 0;
        rotationalAcceleration = 0;
        location = new Point(0, 0);
        rotation = 0;
        defaults = new RobotLocation(this)
            .setWheel(new Wheel(WheelLocation.BackLeft, new Point(-width / 2.0, -length / 2.0)))
            .setWheel(new Wheel(WheelLocation.FrontLeft, new Point(-width / 2.0, length / 2.0)))
            .setWheel(new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, length / 2.0)))
            .setWheel(new Wheel(WheelLocation.BackRight, new Point(width / 2.0, -length / 2.0)));
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

    //These wheels need to be robot centric, not field centric
    public ULine[] applyWheels(Wheel[] wheels) {
        accelerationVector = new Point(0,0);
        rotationalAcceleration = 0;
        for (Wheel wheel : wheels) {
            accelerationVector = accelerationVector.add(wheel.getPosition());
            rotationalAcceleration += (Math.PI/2) * Math.sin(wheel.getPosition().rotation() - defaults.getWheel(wheel.getLocation()).getPosition().rotation());
        }
        rotationalAcceleration *= accelerationVector.length();
        return renderWheels(wheels);
    }

    public void move(double time) {
        if (Math.abs(accelerationVector.length()) < 0.001) {
            //Decelerate
            double scalar = VELOCITY_DECEL * time;
            velocityVector = new Point(velocityVector.getX() * scalar, velocityVector.getY() * scalar);
        } else {
            accelerationVector = new Point(accelerationVector.getX() * time * 10, accelerationVector.getY() * time * 10);
            accelerationVector = accelerationVector.rotate(rotation);
            velocityVector = velocityVector.add(accelerationVector);
            if (velocityVector.length() > MAX_VELOCITY) {
                double scalar = MAX_VELOCITY / velocityVector.length();
                velocityVector = new Point(velocityVector.getX() * scalar, velocityVector.getY() * scalar);
            }
        }

        if (Math.abs(rotationalAcceleration) < 0.01) {
            //Decelerate
            double scalar = ROTATION_DECEL * time;
            rotationalVelocity *= scalar;
        } else {
            rotationalAcceleration *= time;
            rotationalVelocity += rotationalAcceleration;
            if (Math.abs(rotationalVelocity) > MAX_ROTATION) {
                double scalar = MAX_ROTATION / rotationalVelocity;
                rotationalVelocity *= scalar;
            }
        }

        rotation += rotationalVelocity * time;
        location = location.add(new Point(velocityVector.getX() * time, velocityVector.getY() * time));
    }

    public RobotLocation render() {
        RobotLocation res = new RobotLocation(this);
        for (Wheel wheel : defaults.getWheels()) {
            res = res.setWheel(wheel.rotate(rotation).add(location));
        }
        return res;
    }

    public ULine[] renderWheels(Wheel[] wheels) {
        RobotLocation iowa = defaults;
        RobotLocation post_move = defaults;
        RobotLocation truth = render();
        RobotLocation post_truth = defaults;
        for (Wheel wheel : wheels) {
            iowa = iowa.setWheel(wheel.getLocation(),iowa.getWheel(wheel.getLocation()).getPosition().add(new Point(130, -30)));
            double scalar = 5.0 / wheel.getPosition().length();
            if (wheel.getPosition().length() == 0) {
                scalar = 1;
            }
            Wheel offset = new Wheel(wheel.getLocation(), new Point(wheel.getPosition().getX() * scalar, wheel.getPosition().getY()*scalar));
            post_move = post_move.setWheel(iowa.getWheel(wheel.getLocation()).add(offset.getPosition()));
            post_truth = post_truth.setWheel(truth.getWheel(wheel.getLocation()).add(offset.getPosition().rotate(rotation)));
        }
        ArrayList<ULine> lines = new ArrayList<>();
        for (ULine uLine : iowa.renderPaths(post_move)) {
            lines.add(uLine);
        }
        for (ULine uLine : truth.renderPaths(post_truth)) {
            lines.add(uLine);
        }
        return lines.toArray(new ULine[8]);
    }

    public String toString() {
        return String.format("ROBOT POSITION: (%6.2f, %6.2f) ROTATION: %6.2f", location.getX(), location.getY(), rotation);
    }
}
