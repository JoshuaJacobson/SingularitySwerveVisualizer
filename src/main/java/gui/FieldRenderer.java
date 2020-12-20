package gui;

import data.ULine;
import javafx.beans.binding.Bindings;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class FieldRenderer {
    private ULine[] lines;
    public FieldRenderer() {
        lines = new ULine[0];
    } 
    private FieldRenderer(ULine[] lines) {
        this.lines = lines;
    }
    public FieldRenderer addLines(ULine[] lines) {
        ULine[] newLines = new ULine[lines.length + this.lines.length];
        int i = 0;
        for (ULine line : this.lines) {
            newLines[i] = line;
            i++;
        }
        for (ULine line : lines) {
            newLines[i] = line;
            i++;
        }
        return new FieldRenderer(newLines);
    }
    private Pane graphLines() {
        Line[] array = new Line[this.lines.length];
        
        int i = 0;
        for (ULine line : this.lines){
            array[i] = graphLine(line);
            i++;
        }

        return new Pane(array);
    }

    public Pane render() {
        Axes axes = new Axes(
                500, 500,
                -100, 100, 10,
                -100, 100, 10
        );

        Pane lines = graphLines();

        Pane graph = new Pane(axes, lines);
        return graph;
    }

    private static Line graphLine(ULine line) {
        double startX = (line.getSource().getX() * 2.5) + 250;
        double startY = (-line.getSource().getY() * 2.5) + 250;
        double stopX = (line.getDest().getX() * 2.5) + 250;
        double stopY = (-line.getDest().getY() * 2.5) + 250;

        Line res = new Line(startX, startY, stopX, stopY);
        res.setStroke(line.getColor());
        return res;
    }

    class Axes extends Pane {
        private NumberAxis xAxis;
        private NumberAxis yAxis;

        public Axes(
                int width, int height,
                double xLow, double xHi, double xTickUnit,
                double yLow, double yHi, double yTickUnit
        ) {
            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            setPrefSize(width, height);
            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            xAxis = new NumberAxis(xLow, xHi, xTickUnit);
            xAxis.setSide(Side.BOTTOM);
            xAxis.setMinorTickVisible(false);
            xAxis.setPrefWidth(width);
            xAxis.setLayoutY(height / 2);

            yAxis = new NumberAxis(yLow, yHi, yTickUnit);
            yAxis.setSide(Side.LEFT);
            yAxis.setMinorTickVisible(false);
            yAxis.setPrefHeight(height);
            yAxis.layoutXProperty().bind(
                Bindings.subtract(
                    (width / 2) + 1,
                    yAxis.widthProperty()
                )
            );

            getChildren().setAll(xAxis, yAxis);
        }

        public NumberAxis getXAxis() {
            return xAxis;
        }

        public NumberAxis getYAxis() {
            return yAxis;
        }
    }
}
