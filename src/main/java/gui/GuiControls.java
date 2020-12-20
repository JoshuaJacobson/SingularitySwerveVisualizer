package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import singularity.SingularitySwerveVisualizer;

public class GuiControls {
    private Button submit;
    private TextField robotLength;
    private TextField robotWidth;
    private TextField robotX;
    private TextField robotY;
    private TextField robotRotate;
    private Label labelLength;
    private Label labelWidth;
    private Label labelX;
    private Label labelY;
    private Label labelRotate;

    public GuiControls() {
        submit = new Button("Simulate Robot");
        robotLength = new TextField();
        robotWidth = new TextField();
        robotX = new TextField();
        robotY = new TextField();
        robotRotate = new TextField();
        labelLength = new Label("Length: ");
        labelWidth = new Label("Width: ");
        labelX = new Label("X Target: ");
        labelY = new Label("Y Target: ");
        labelRotate = new Label("Rotation: ");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                SingularitySwerveVisualizer.customCodeHere();
            }
        });
    }

    public double getLength() {
        return Double.parseDouble(robotLength.getText());
    }

    public double getWidth() {
        return Double.parseDouble(robotWidth.getText());
    }

    public double getX() {
        return Double.parseDouble(robotX.getText());
    }

    public double getY() {
        return Double.parseDouble(robotY.getText());
    }

    public double getRotate() {
        return Math.toRadians(Double.parseDouble(robotRotate.getText()));
    }

    public Pane getPane() {
        GridPane pane = new GridPane();

        pane.add(labelLength,0,0);
        pane.add(robotLength,1,0);

        pane.add(labelWidth,0,1);
        pane.add(robotWidth,1,1);

        pane.add(labelX,0,2);
        pane.add(robotX,1,2);
        
        pane.add(labelY,0,3);
        pane.add(robotY,1,3);
        
        pane.add(labelRotate,0,4);
        pane.add(robotRotate,1,4);

        pane.add(submit,1,5);

        return pane;
    }
}
