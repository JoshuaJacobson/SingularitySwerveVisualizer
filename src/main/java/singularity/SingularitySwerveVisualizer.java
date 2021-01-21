package singularity;

import data.Point;
import data.ULine;
import data.Wheel;
import data.WheelLocation;
import gui.FieldRenderer;
import gui.GuiControls;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import robot.Robot;
import robot.RobotLocation;

public class SingularitySwerveVisualizer extends Application {

    private static Stage stage;
    private static GridPane plane;
    private static GuiControls controls;
    private static Robot robot;
    private static FieldRenderer renderer;
    private static Pane lines;
    private static long startNanoTime;
    private static final double ROBOT_LENGTH = 15.0;
    private static final double ROBOT_WIDTH = 15.0;
    private static double currentX;
    private static double currentY;
    private static double currentRotate;

    @Override
    public void start(Stage stage) {
        SingularitySwerveVisualizer.stage = stage;
        plane = new GridPane();

        controls = new GuiControls();
        //plane.add(controls.getPane(), 0, 0);

        currentX = 1;
        currentY = 0;
        currentRotate = 0;

        robot = new Robot(ROBOT_WIDTH, ROBOT_LENGTH);

        renderer = new FieldRenderer().addLines(robot.render().render());

        lines = renderer.render();
        Pane pane = new Pane(lines);
        plane.add(pane, 1, 0);

        Scene scene = new Scene(plane, 740, 480);
        stage.setScene(scene);
        stage.setTitle("Singularity Swerve Visualizer");

        startNanoTime = System.nanoTime();

        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() { 
                @Override 
                public void handle(KeyEvent event) {
                        boolean pressed = event.getEventType() == KeyEvent.KEY_PRESSED;
                        switch (event.getCode()) {
                                case W:
                                        currentY=pressed?1:0.1;
                                        break;
                                case A:
                                        currentX=pressed?-1:0.1;
                                        break;
                                case S:
                                        currentY=pressed?-1:0.1;
                                        break;
                                case D:
                                        currentX=pressed?1:0.1;
                                        break;
                                case K:
                                        currentRotate=pressed?Math.PI/8.0:0;
                                        break;
                                case L:
                                        currentRotate=pressed?-Math.PI/8.0:0;
                                        break;
                                default:
                                        break;
                        }
                }           
        };
        stage.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        stage.addEventHandler(KeyEvent.KEY_RELEASED, eventHandler);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0; 
                startNanoTime = currentNanoTime;
                Wheel[] wheels = customCodeHere();
                ULine[] wheelLines = robot.applyWheels(wheels);
                robot.move(t);
                System.out.println(robot);
                renderer = new FieldRenderer().addLines(robot.render().render()).addLines(wheelLines);
                lines = renderer.render();
                pane.getChildren().clear();
                pane.getChildren().addAll(lines);
            }
        }.start();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return (Math.sqrt((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2))));
    }

    private static double getAngleIfStill(double xNext, double yNext, double xCurr, double yCurr, double height, double width, double angleAdd){
        if(xNext == xCurr && yNext == yCurr){ //check to see if robot is stationary
            return Math.toDegrees(Math.atan(width / height)) + angleAdd;
        }
        else{ //if the robot isn't stationary
            return Math.toDegrees(Math.atan((yNext - yCurr) / (xNext - xCurr)));
        }
    }

    
    public static Wheel[] customCodeHere() {

        double rotationSpeedConstant = 1;

        double horizontal = currentX;
        double vertical = currentY;
        double rotation = 0;
        System.out.println(currentX);
        //double horizontal = 10;
        //double vertical = -10;


        double halfRobotWidth = ROBOT_WIDTH / 2;
        double halfRobotHeight = ROBOT_WIDTH / 2;

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

        double mBR_Offset_Angle = Math.atan(ROBOT_LENGTH / ROBOT_WIDTH); // Angle for offsetting wheel positions along the
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
        //double mFL_Angle = Math.atan((mFL_YPos_Next - mFL_YPos_Curr) / (mFL_XPos_Next - mFL_XPos_Curr));
        double mFL_Angle = getAngleIfStill(horizontal, vertical, 0, 0, ROBOT_WIDTH, ROBOT_LENGTH, 180);
        double mFR_Angle = getAngleIfStill(horizontal, vertical, 0, 0, ROBOT_LENGTH, ROBOT_WIDTH, 90);
        double mBL_Angle = getAngleIfStill(horizontal, vertical, 0, 0, ROBOT_LENGTH, ROBOT_WIDTH, 270);
        double mBR_Angle = getAngleIfStill(horizontal, vertical, 0, 0, ROBOT_WIDTH, ROBOT_LENGTH, 0);

        double mFR_Distance = distance(mFR_XPos_Curr, mFR_YPos_Curr, mFR_XPos_Next, mFR_YPos_Next);
        double mFL_Distance = distance(mFL_XPos_Curr, mFL_YPos_Curr, mFL_XPos_Next, mFL_YPos_Next);
        double mBL_Distance = distance(mBL_XPos_Curr, mBL_YPos_Curr, mBL_XPos_Next, mBL_YPos_Next);
        double mBR_Distance = distance(mBR_XPos_Curr, mBR_YPos_Curr, mBR_XPos_Next, mBR_YPos_Next);
        
        /*Wheel[] wheels = new Wheel[4];
        wheels[0] = new Wheel(WheelLocation.BackLeft, mBL_Angle, mBL_Distance);
        wheels[1] = new Wheel(WheelLocation.FrontLeft, mFL_Angle, mFL_Distance);
        wheels[2] = new Wheel(WheelLocation.FrontRight, mFR_Angle, mFR_Distance);
        wheels[3] = new Wheel(WheelLocation.BackRight, mBR_Angle, mBR_Distance);*/

        Wheel[] wheels = new Wheel[4];
        wheels[0] = new Wheel(WheelLocation.BackLeft, mBL_Angle, mBL_Distance);
        wheels[1] = new Wheel(WheelLocation.FrontLeft, mFL_Angle, mFL_Distance);
        wheels[2] = new Wheel(WheelLocation.FrontRight, mFR_Angle, mFR_Distance);
        wheels[3] = new Wheel(WheelLocation.BackRight, mBL_Angle, mBR_Distance);

        /*System.out.println(mFR_Angle);
        System.out.println(mFL_Angle);
        System.out.println(mBR_Angle);
        System.out.println(mBL_Angle);*/
        //System.out.println();
        /*System.out.println(mFR_Distance);
        System.out.println(mFL_Distance);
        System.out.println(mBR_Distance);
        System.out.println(mBL_Distance);*/

        return wheels;


    }

}