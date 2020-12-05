package singularity;

import data.Point;
import data.WheelLocation;
import gui.FieldRenderer;
import gui.GuiControls;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Side;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
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

    public static void customCodeHere() {
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
            .setWheel(WheelLocation.BackLeft, new Point(25, 10))
            .setWheel(WheelLocation.BackRight, new Point(35, 10))
            .setWheel(WheelLocation.FrontRight, new Point(35, 30))
            .setWheel(WheelLocation.FrontLeft, new Point(25, 30));

        renderer = new FieldRenderer()
            .addLines(originalLocation.render())
            .addLines(newLocation.render())
            .addLines(originalLocation.renderPaths(newLocation));
        
        plane.add(renderer.render(), 1, 0);

        Scene scene = new Scene(plane, 740, 480);
        stage.setScene(scene);
    }

}