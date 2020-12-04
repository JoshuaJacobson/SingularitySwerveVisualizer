
import data.Point;
import data.WheelLocation;
import gui.FieldRenderer;
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

    @Override
    public void start(Stage stage) {
        GridPane plane = new GridPane();

        Button simulate = new Button("Simulate Robot");
        plane.add(simulate, 0, 0);

        Robot robot = new Robot(20.0, 10.0);
        RobotLocation originalLocation = new RobotLocation(robot)
            .setWheel(WheelLocation.BackLeft, new Point(-5, -10))
            .setWheel(WheelLocation.BackRight, new Point(5, -10))
            .setWheel(WheelLocation.FrontRight, new Point(5, 10))
            .setWheel(WheelLocation.FrontLeft, new Point(-5, 10));

        FieldRenderer renderer = new FieldRenderer()
            .addLines(originalLocation.render());
        plane.add(renderer.render(), 1, 0);

        Scene scene = new Scene(plane, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Singularity Swerve Visualizer");
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    

}