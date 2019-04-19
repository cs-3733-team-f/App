package images;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class ArrowConnectorShape {
    public enum arrowConnectorEnum {
        BEGINNING, MIDDLE, END
    }
    private Color color;
    private double height;
    private double ratio;
    private double width;
    private Polygon p;
    private arrowConnectorEnum type;
    public ArrowConnectorShape(double height, double width, double ratio, arrowConnectorEnum type, int x_offset, int y_offset) {
        if(ratio > 1 || ratio < 0) return;
        this.type = type;
        this.height = height;
        this.width = width;
        this.ratio = ratio;
        ArrayList<Double> points = new ArrayList<Double>();
        points.add(0.0); // A
        points.add(0.0);
        if(type == arrowConnectorEnum.BEGINNING) { // ACDFH
            points.add(width); // C
            points.add(0.0);
            points.add(width); // D
            points.add(ratio * height + (1 - ratio) * height * 0.5);
            points.add(width/2); // F
            points.add(height);
            points.add(0.0); // H
            points.add(ratio * height + (1 - ratio) * height * 0.5);
        } else if(type == arrowConnectorEnum.MIDDLE) { // ABCDFH
            points.add(width/2); // B
            points.add((1-ratio) * height * 0.5);
            points.add(width); // C
            points.add(0.0);
            points.add(width); // D
            points.add(ratio * height + (1 - ratio) * height * 0.5);
            points.add(width/2); // F
            points.add(height);
            points.add(0.0); // H
            points.add(ratio * height + (1 - ratio) * height * 0.5);
        } else if(type == arrowConnectorEnum.END) { // ABCEG
            points.add(width/2); // B
            points.add((1-ratio) * height * 0.5);
            points.add(width); // C
            points.add(0.0);
            points.add(width); // E
            points.add(height);
            points.add(0.0); // G
            points.add(height);
        }
        for(int i = 0; i < points.size(); i++) {
            if(i % 2 == 0) {
                points.set(i, points.get(i) + x_offset);
            }
            if(i % 2 == 1) {
                points.set(i, points.get(i) + y_offset);
            }
        }

        Polygon p = new Polygon();
        p.getPoints().addAll(points);
        this.p = p;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        p.setFill(color);
        p.setStroke(color);

    }
    public Polygon getPolygon() {
        return p;
    }
}
