/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignment2_youssefnagih;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Youssef Nagih
 * 6372635
 */
public class CarModel extends Group {
    
    private final Group frontWheel;
    private final Group rearWheel;

    public CarModel(CarData car) {
        Color color = Color.web(car.getColorHex());

        // body and roof
        Polygon body = new Polygon(0, 30, 0, 18, 12, 14, 88, 14, 100, 20, 100, 30);
        body.setFill(color);
        body.setStroke(Color.BLACK);

        Polygon roof = new Polygon(25, 14, 35, 0, 70, 0, 80, 14);
        roof.setFill(color.darker());
        roof.setStroke(Color.BLACK);

        // windows
        Rectangle window1 = new Rectangle(38, 3, 14, 10);
        Rectangle window2 = new Rectangle(56, 3, 14, 10);
        window1.setFill(Color.LIGHTSKYBLUE);
        window2.setFill(Color.LIGHTSKYBLUE);

        // car number
        Circle badge = new Circle(50, 23, 7, Color.WHITE);
        String num = String.valueOf(car.getNumber());
        Text number = new Text(50 - num.length() * 3, 26.5, num);
        number.setFont(new Font("Arial Bold", 10));
        number.setFill(Color.BLACK);
        
        rearWheel = createWheel(24, 31);
        frontWheel = createWheel(77, 31);
        
        getChildren().addAll(body, roof, window1, window2, badge, number, rearWheel, frontWheel);

    }
    
    /*
    helper method to create the wheel and its design
    */
    private static Group createWheel(double cx, double cy) {
        Circle wheel = new Circle(cx, cy, 8, Color.DIMGRAY);
        wheel.setStroke(Color.BLACK);
        wheel.setStrokeWidth(2);
        
        Line spoke = new Line(cx, cy, cx, cy - 7);
        spoke.setStroke(Color.WHITE);
        spoke.setStrokeWidth(2);
        
        return new Group(wheel, spoke);
    }
    
    public Group getFrontWheel() { 
        return frontWheel; 
    }
    
    public Group getRearWheel() { 
        return rearWheel; 
    }
}
