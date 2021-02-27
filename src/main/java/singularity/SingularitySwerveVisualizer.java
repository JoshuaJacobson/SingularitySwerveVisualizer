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
    private static final double ROBOT_LENGTH = 2;
    private static final double ROBOT_WIDTH = 1;
    private static double currentX;
    private static double currentY;
    private static double currentRotate;

    @Override
    public void start(Stage stage) {
        SingularitySwerveVisualizer.stage = stage;
        plane = new GridPane();

        controls = new GuiControls();
        //plane.add(controls.getPane(), 0, 0);

        currentX = 0;
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
                                        currentY=pressed?1:0;
                                        break;
                                case A:
                                        currentX=pressed?-1:0;
                                        break;
                                case S:
                                        currentY=pressed?-1:0;
                                        break;
                                case D:
                                        currentX=pressed?1:0;
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
                Wheel[] wheels = customCodeHere(robot);
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

    private static double getAngleIfStill(double horizontal, double vertical, double xNext, double yNext, double xCurr, double yCurr, double height, double width, double angleAdd, double localToGlobal){ //LOCALTOGLOBAL IN DEGREES
        if( (horizontal <= .001 && horizontal >= -.001) && (vertical <= .001 && vertical >= -.001) ){ //check to see if robot is stationary
                //System.out.println(Math.toDegrees(Math.atan(width / height)) + angleAdd);
            return Math.toDegrees(Math.atan(width / height)) + angleAdd;
        }
        else{ //if the robot isn't stationary
            if(horizontal == 0){ //no horizontal movement
                if(vertical < 0){
                        return 270 - localToGlobal;
                }//:)
                else{
                        return 90 - localToGlobal;
                }
            }
            else if(xNext < xCurr){
                return 180 + Math.toDegrees(Math.atan((yNext - yCurr) / (xNext - xCurr))) - localToGlobal;
            }
            else if(xNext > xCurr){ 
                return Math.toDegrees(Math.atan((yNext - yCurr) / (xNext - xCurr))) - localToGlobal;
            }
            else{ // xNext == xCurr gives atan divide by zero error
                if(yNext > yCurr){ //going up
                        return 90 - localToGlobal;
                }
                else{
                        return 270 - localToGlobal;
                }
            }
            //return Math.toDegrees(Math.atan((yNext - yCurr) / (xNext - xCurr)));
                
        }
    }
    

    private static double getDistanceIfStill(double horizontal, double vertical, double xNext, double yNext, double xCurr, double yCurr, double rotationSpeed ){
        if( (horizontal <= .001 && horizontal >= -.001) && (vertical <= .001 && vertical >= -.001) ){ //if they're basically zero
                return rotationSpeed;
        }
        else{
                return distance(xNext, yNext, xCurr, yCurr);
                //return 1;
        }
        
    }

    
    public static Wheel[] customCodeHere(Robot robot) {

        double rotationSpeedConstant = 1;

        double horizontal = currentX;
        double vertical = currentY;
        //double rotation = currentRotate;
        
        
        //double rotate = (robot.getRotate());
        double rotate = -.1;
        double rotation = rotate + .1;
        //double rotate = -0.5;
        //System.out.println(currentX);
        //double horizontal = 10;
        //double vertical = -10;


        double halfRobotWidth = ROBOT_WIDTH / 2;
        double halfRobotHeight = ROBOT_LENGTH / 2;

        double midToOutSize = distance(halfRobotWidth, halfRobotHeight, 0, 0); // distance between robot center and any
                                                                               // of the wheels

        double mBR_Offset_Angle = Math.atan(ROBOT_LENGTH / ROBOT_WIDTH); // Angle for offsetting wheel positions along the
                                                                               // circle with radius midToOutSize
        double mFR_Offset_Angle = -mBR_Offset_Angle; // 
        double mFL_Offset_Angle = mBR_Offset_Angle + Math.PI; // 
        double mBL_Offset_Angle = -1.0 * Math.atan(ROBOT_LENGTH/ROBOT_WIDTH); // 


        double mFL_XPos_Curr = (midToOutSize * Math.cos(-(rotate) - mFL_Offset_Angle));
        double mFL_YPos_Curr = (midToOutSize * Math.sin(-(rotate) - mFL_Offset_Angle));

        double mFR_XPos_Curr = (midToOutSize * Math.cos(-(rotate) - mFR_Offset_Angle));
        double mFR_YPos_Curr = (midToOutSize * Math.sin(-(rotate) - mFR_Offset_Angle));

        double mBL_XPos_Curr = (midToOutSize * Math.cos(-(rotate) - mBL_Offset_Angle));
        double mBL_YPos_Curr = (midToOutSize * Math.sin(-(rotate) - mBL_Offset_Angle));

        double mBR_XPos_Curr = (midToOutSize * Math.cos(-(rotate) - mBR_Offset_Angle));
        double mBR_YPos_Curr = (midToOutSize * Math.sin(-(rotate) - mBR_Offset_Angle));

        /*double mFL_XPos_Curr = -.597;
        double mFL_YPos_Curr = .945;

        double mFR_XPos_Curr = .398;
        double mFR_YPos_Curr = 1.045;

        double mBL_XPos_Curr = -.398; //THESE ARE NOT BEING CALCULATED AS NEGATIVE VALUES: TO BE FIXED ON 27/FEB/2021 
        double mBL_YPos_Curr = -1.045;

        double mBR_XPos_Curr = .597;
        double mBR_YPos_Curr = -.945;*/

        System.out.println(mFL_XPos_Curr);
        System.out.println(mFL_YPos_Curr);

        System.out.println(mFR_XPos_Curr);
        System.out.println(mFR_YPos_Curr);

        System.out.println(mBL_XPos_Curr);
        System.out.println(mBL_YPos_Curr);

        System.out.println(mBR_XPos_Curr);
        System.out.println(mBR_YPos_Curr);


        /*double mFL_XPos_Curr_Rotate = ;
        double mFR_XPos_Curr_Rotate = (midToOutSize * Math.cos(-(rotate) - mFR_Offset_Angle));
        double mBL_XPos_Curr_Rotate = (midToOutSize * Math.cos(-(rotate) - mBL_Offset_Angle));
        double mBR_XPos_Curr_Rotate = (midToOutSize * Math.cos(-(rotate) - mBR_Offset_Angle));

        double mFL_YPos_Curr_Rotate = (midToOutSize * Math.sin(-(rotate) - mFL_Offset_Angle));
        double mFR_YPos_Curr_Rotate = (midToOutSize * Math.sin(-(rotate) - mFR_Offset_Angle));
        double mBL_YPos_Curr_Rotate = (midToOutSize * Math.sin(-(rotate) - mBL_Offset_Angle));
        double mBR_YPos_Curr_Rotate = (midToOutSize * Math.sin(-(rotate) - mBR_Offset_Angle));*/






        /*double mFL_XPos_Next = horizontal
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
                + (midToOutSize * Math.sin(-(rotation * rotationSpeedConstant) - mBR_Offset_Angle));*/




        double mFL_XPos_Next = 2.1;

        double mFL_YPos_Next = 3.07;

        double mFR_XPos_Next = 3.1;
        double mFR_YPos_Next = 3.07;

        double mBL_XPos_Next = 2.1;
        double mBL_YPos_Next = 1.07;

        double mBR_XPos_Next = 3.1;
        double mBR_YPos_Next = 1.07;

        //System.out.println(mBR_XPos_Next);
        //System.out.println(mBR_YPos_Next);

        


        

        // Angle adjusting motors will set the wheels to be pointed to the angle of
        // these slopes:
        //double mFL_Angle = Math.atan((mFL_YPos_Next - mFL_YPos_Curr) / (mFL_XPos_Next - mFL_XPos_Curr)); ROBOT_WIDTH

        double mFR_Angle = getAngleIfStill(horizontal, vertical, mFR_XPos_Next, mFR_YPos_Next, mFR_XPos_Curr, mFR_YPos_Curr, ROBOT_LENGTH, ROBOT_WIDTH, 90, Math.toDegrees(rotate));
        double mFL_Angle = getAngleIfStill(horizontal, vertical, mFL_XPos_Next, mFL_YPos_Next, mFL_XPos_Curr, mFL_YPos_Curr, ROBOT_WIDTH, ROBOT_LENGTH, 180, Math.toDegrees(rotate));
        double mBL_Angle = getAngleIfStill(horizontal, vertical, mBL_XPos_Next, mBL_YPos_Next, mBL_XPos_Curr,mBL_YPos_Curr, ROBOT_LENGTH, ROBOT_WIDTH, 270, Math.toDegrees(rotate));
        double mBR_Angle = getAngleIfStill(horizontal, vertical, mBR_XPos_Next, mBR_YPos_Next, mBR_XPos_Curr, mBR_YPos_Curr, ROBOT_WIDTH, ROBOT_LENGTH, 0, Math.toDegrees(rotate));

        //System.out.println(Math.atan((mBR_YPos_Next-)/()) );
        //double testAngle = getAngleIfStill(1, 1, -1, .5, .5, .5, ROBOT_WIDTH, ROBOT_LENGTH, 0, 0);

        
        /*System.out.println(mFR_Angle);
        System.out.println(mFL_Angle);
        System.out.println(mBL_Angle);
        System.out.println(mBR_Angle);*/

        /*double mFL_Angle = getAngleIfStill(0, 0, 1, 1, 2, 2, ROBOT_WIDTH, ROBOT_LENGTH, 0, 0);
        double mFR_Angle = getAngleIfStill(0, 0, 1, 1, 2, 2, ROBOT_WIDTH, ROBOT_LENGTH, 0, 0);
        double mBL_Angle = getAngleIfStill(0, 0, 1, 1, 2, 2, ROBOT_WIDTH, ROBOT_LENGTH, 0, 0);
        double mBR_Angle = getAngleIfStill(10, 10, 17.5, 2.5, 10.178, -2.986, ROBOT_WIDTH, ROBOT_LENGTH, 0, 0);*/
        //System.out.println(Math.toDegrees(Math.atan((-15.55-(-2.986))/(9.45-10.178))) );

        double mFR_Distance = getDistanceIfStill(horizontal, vertical,  mFR_XPos_Next, mFR_YPos_Next, mFR_XPos_Curr, mFR_YPos_Curr, currentRotate);
        double mFL_Distance = getDistanceIfStill(horizontal, vertical,  mFL_XPos_Next, mFL_YPos_Next, mFL_XPos_Curr, mFL_YPos_Curr, currentRotate);
        double mBL_Distance = getDistanceIfStill(horizontal, vertical,  mBL_XPos_Next, mBL_YPos_Next, mBL_XPos_Curr, mBL_YPos_Curr, currentRotate);
        double mBR_Distance = getDistanceIfStill(horizontal, vertical,  mBR_XPos_Next, mBR_YPos_Next, mBR_XPos_Curr, mBR_YPos_Curr, currentRotate);
        //System.out.println(Math.atan( (.5-mBR_YPos_Curr)/(-1.0-mBR_YPos_Curr) ));

        //double mFR_Distance = 1;
        //double mFL_Distance = 1;
        //double mBL_Distance = 1;
        //double mBR_Distance = 1;

        /*Wheel[] wheels = new Wheel[4];
        wheels[0] = new Wheel(WheelLocation.BackLeft, mBL_Angle, mBL_Distance);
        wheels[1] = new Wheel(WheelLocation.FrontLeft, mFL_Angle, mFL_Distance);
        wheels[2] = new Wheel(WheelLocation.FrontRight, mFR_Angle, mFR_Distance);
        wheels[3] = new Wheel(WheelLocation.BackRight, mBR_Angle, mBR_Distance);*/

        Wheel[] wheels = new Wheel[4];
        wheels[0] = new Wheel(WheelLocation.BackLeft,Math.toRadians(mBL_Angle), mBL_Distance);
        wheels[1] = new Wheel(WheelLocation.FrontLeft, Math.toRadians(mFL_Angle), mFL_Distance);
        wheels[2] = new Wheel(WheelLocation.FrontRight, Math.toRadians(mFR_Angle), mFR_Distance);
        wheels[3] = new Wheel(WheelLocation.BackRight, Math.toRadians(mBR_Angle), mBR_Distance);

        //System.out.printf("(%6.2f)%n" , mFL_Angle);
        //System.out.printf("(%6.2f)%n" , mFR_Angle);
        //System.out.printf("(%6.2f)%n" , mBL_Angle);
        //System.out.printf("(%6.2f)%n" , mBR_Angle);
//
        //System.out.printf("(%6.2f)%n" , mFL_Distance);
        //System.out.printf("(%6.2f)%n" , mFR_Distance);
        //System.out.printf("(%6.2f)%n" , mBL_Distance);
        //System.out.printf("(%6.2f)%n" , mBR_Distance);

        return wheels;


    }

}