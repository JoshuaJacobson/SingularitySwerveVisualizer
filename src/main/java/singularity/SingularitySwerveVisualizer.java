package singularity;

import data.Point;
import data.WheelLocation;
import gui.FieldRenderer;
import gui.GuiControls;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import robot.Robot;
import robot.RobotLocation;

public class SingularitySwerveVisualizer extends Application {

    private static Stage stage;
    private static GridPane plane;
    private static GuiControls controls;
    private static Robot robot;
    private static RobotLocation originalLocation;
    private static RobotLocation newLocation;
    private static FieldRenderer renderer;

    @Override
    public void start(Stage stage) {
        SingularitySwerveVisualizer.stage = stage;
        plane = new GridPane();

        controls = new GuiControls();
        plane.add(controls.getPane(), 0, 0);

        robot = new Robot(20.0, 10.0);
        originalLocation = new RobotLocation(robot)
            .setWheel(WheelLocation.BackLeft, new Point(-5, -10))
            .setWheel(WheelLocation.BackRight, new Point(5, -10))
            .setWheel(WheelLocation.FrontRight, new Point(5, 10))
            .setWheel(WheelLocation.FrontLeft, new Point(-5, 10));
        
        newLocation = new RobotLocation(robot)
            .setWheel(WheelLocation.BackLeft, new Point(15, 10))
            .setWheel(WheelLocation.BackRight, new Point(25, 10))
            .setWheel(WheelLocation.FrontRight, new Point(25, 30))
            .setWheel(WheelLocation.FrontLeft, new Point(15, 30));

        renderer = new FieldRenderer()
            .addLines(originalLocation.render())
            .addLines(newLocation.render())
            .addLines(originalLocation.renderPaths(newLocation));
        
        plane.add(renderer.render(), 1, 0);

        Scene scene = new Scene(plane, 740, 480);
        stage.setScene(scene);
        stage.setTitle("Singularity Swerve Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return (Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2))));
    }

    public static void customCodeHere() {

        double size = 1.0;
        double rotationSpeedConstant = 1;

        double robotWidth = controls.getWidth();
        double robotHeight = controls.getLength();
        double horizontal = controls.getX();
        double vertical = controls.getY();
        double rotation = controls.getRotate();

        double halfRobotWidth = robotWidth / 2;
        double halfRobotHeight = robotHeight / 2;

        double midToOutSize = distance(halfRobotWidth, halfRobotHeight, 0, 0); // distance between robot center and any
                                                                               // of the wheels

        double mFL_XPos_Curr = -halfRobotWidth;
        double mFL_YPos_Curr = halfRobotHeight;

        double mFR_XPos_Curr = halfRobotWidth;
        double mFR_YPos_Curr = halfRobotHeight;

        double mBL_XPos_Curr = -halfRobotWidth;
        double mBL_YPos_Curr = -halfRobotHeight;

        double mBR_XPos_Curr = halfRobotWidth;
        double mBR_YPos_Curr = -halfRobotHeight;

        double mBR_Offset_Angle = Math.atan(robotHeight / robotWidth); // Angle for offsetting wheel positions along the
                                                                       // circle with radius midToOutSize
        double mFR_Offset_Angle = -mBR_Offset_Angle; // " " but negative
        double mFL_Offset_Angle = mBR_Offset_Angle + Math.PI; // complement ( +180 degrees ) of the first one
        double mBL_Offset_Angle = mFR_Offset_Angle + Math.PI; // " " but of the second one

        double mFL_XPos_Next = horizontal
                + (midToOutSize * Math.cos(-(rotation * rotationSpeedConstant) - mFL_Offset_Angle));
        double mFL_YPos_Next = vertical
                + (midToOutSize * Math.sin(-(rotation * rotationSpeedConstant) - mFL_Offset_Angle));

        double mFR_XPos_Next = horizontal
                + (midToOutSize * Math.cos(-(rotation * rotationSpeedConstant) - mFR_Offset_Angle));
        double mFR_YPos_Next = vertical
                + (midToOutSize * Math.sin(-(rotation * rotationSpeedConstant) - mFR_Offset_Angle));

        double mBL_XPos_Next = horizontal
                + (midToOutSize * Math.cos(-(rotation * rotationSpeedConstant) - mBL_Offset_Angle));
        double mBL_YPos_Next = vertical
                + (midToOutSize * Math.sin(-(rotation * rotationSpeedConstant) - mBL_Offset_Angle));

        double mBR_XPos_Next = horizontal
                + (midToOutSize * Math.cos(-(rotation * rotationSpeedConstant) - mBR_Offset_Angle));
        double mBR_YPos_Next = vertical
                + (midToOutSize * Math.sin(-(rotation * rotationSpeedConstant) - mBR_Offset_Angle));

        // Angle adjusting motors will set the wheels to be pointed to the angle of
        // these slopes:
        double mFL_Angle = Math.atan((mFL_YPos_Next - mFL_YPos_Curr) / (mFL_XPos_Next - mFL_XPos_Curr));
        double mFR_Angle = Math.atan((mFR_YPos_Next - mFR_YPos_Curr) / (mFR_XPos_Next - mFR_XPos_Curr));
        double mBL_Angle = Math.atan((mBL_YPos_Next - mBL_YPos_Curr) / (mBL_XPos_Next - mBL_XPos_Curr));
        double mBR_Angle = Math.atan((mBR_YPos_Next - mBR_YPos_Curr) / (mBR_XPos_Next - mBR_XPos_Curr));

        double mFR_Distance = distance(mFR_XPos_Curr, mFR_YPos_Curr, mFR_XPos_Next, mFR_YPos_Next);
        double mFL_Distance = distance(mFL_XPos_Curr, mFL_YPos_Curr, mFL_XPos_Next, mFL_YPos_Next);
        double mBL_Distance = distance(mBL_XPos_Curr, mBL_YPos_Curr, mBL_XPos_Next, mBL_YPos_Next);
        double mBR_Distance = distance(mBR_XPos_Curr, mBR_YPos_Curr, mBR_XPos_Next, mBR_YPos_Next);

        plane = new GridPane();

        plane.add(controls.getPane(), 0, 0);

        robot = new Robot(controls.getLength(), controls.getWidth());
        originalLocation = new RobotLocation(robot)
            .setWheel(WheelLocation.BackLeft, new Point(mBL_XPos_Curr, mBL_YPos_Curr))
            .setWheel(WheelLocation.BackRight, new Point(mBR_XPos_Curr, mBR_YPos_Curr))
            .setWheel(WheelLocation.FrontRight, new Point(mFR_XPos_Curr, mFR_YPos_Curr))
            .setWheel(WheelLocation.FrontLeft, new Point(mFL_XPos_Curr, mFL_YPos_Curr));
        
        newLocation = new RobotLocation(robot)
            .setWheel(WheelLocation.BackLeft, new Point(mBL_XPos_Next, mBL_YPos_Next))
            .setWheel(WheelLocation.BackRight, new Point(mBR_XPos_Next, mBR_YPos_Next))
            .setWheel(WheelLocation.FrontRight, new Point(mFR_XPos_Next, mFR_YPos_Next))
            .setWheel(WheelLocation.FrontLeft, new Point(mFL_XPos_Next, mFL_YPos_Next));

        renderer = new FieldRenderer()
            .addLines(originalLocation.render())
            .addLines(newLocation.render())
            .addLines(originalLocation.renderPaths(newLocation));
        
        plane.add(renderer.render(), 1, 0);

        Scene scene = new Scene(plane, 740, 480);
        stage.setScene(scene);
    }

}