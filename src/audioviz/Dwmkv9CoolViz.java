/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audioviz;

import static java.lang.Integer.min;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author darrellmartin
 */
public class Dwmkv9CoolViz implements Visualizer{
    
    private final String name = "Dwmkv9CoolViz";
    
    private String vizPaneInitialStyle = "";
    private Integer numBands;
    private AnchorPane vizPane;
    private Double height = 0.0;
    private Double width = 0.0;
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private final Double bandHeightPercentage = 1.3;
    private Double halfBandHeight = 0.0;
    
    private Ellipse[] ellipses;
    private final Double minEllipseRadius = 10.0;
    private final Double startHue = 260.0;
    private Double magnitudeOffset = 70.0;
    
    private Rectangle[] rectangles1;
    private Rectangle[] rectangles2;

    
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane) {
        end();
        
        vizPaneInitialStyle = vizPane.getStyle();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane.setClip(clip);
        
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        rectangles1 = new Rectangle[numBands];
        rectangles2 = new Rectangle[numBands];
        
        for(int i = 0; i<numBands; i++) {
            Rectangle rectangle1 = new Rectangle();
            Rectangle rectangle2 = new Rectangle();

            rectangle1.setY(height / 2 + 25);
            rectangle1.setX(bandWidth / 2 + bandWidth * i);
            rectangle1.setWidth(width / numBands);
            rectangle1.setHeight(bandWidth / 2);
            
            rectangle2.setY(height / 2 - 25);
            rectangle2.setX(bandWidth / 2 + bandWidth * i);
            rectangle2.setWidth(width / numBands);
            rectangle2.setHeight(bandWidth / 2);
            
            rectangle1.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            rectangle2.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            
            vizPane.getChildren().add(rectangle1);
            vizPane.getChildren().add(rectangle2);
            
            rectangles1[i] = rectangle1;
            rectangles2[i] = rectangle2;
        }    
        
    }

    @Override
    public void end() {
        if (rectangles1 != null) {
            for (Rectangle rectangle1 : rectangles1) {
                vizPane.getChildren().remove(rectangle1);
            }
            rectangles1 = null;
            vizPane.setClip(null);
            vizPane.setStyle(vizPaneInitialStyle);
        }        
        
        if (rectangles2 != null) {
            for (Rectangle rectangle2 : rectangles2) {
                vizPane.getChildren().remove(rectangle2);
            }
            rectangles2 = null;
            vizPane.setClip(null);
            vizPane.setStyle(vizPaneInitialStyle);
        }        
        
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if(rectangles1 == null){
            return;
        }
        
        Integer num = min(rectangles1.length, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            rectangles1[i].setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
            rectangles2[i].setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
            
            
            rectangles1[i].setHeight(magnitudeOffset + magnitudes[i]);
            rectangles2[i].setHeight(magnitudeOffset + magnitudes[i]);
            
            rectangles1[i].setTranslateY( (Math.abs(magnitudes[i]) * 5) - 300);
            rectangles2[i].setTranslateY( -((Math.abs(magnitudes[i]) * 5) - 300));
            
        }
        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        vizPane.setStyle("-fx-background-color: hsb(" + hue + ", 20%, 100%)" );
        
    }
    
    
}
