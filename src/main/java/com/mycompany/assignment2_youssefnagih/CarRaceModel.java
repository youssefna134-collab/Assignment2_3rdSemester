/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.assignment2_youssefnagih;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarRaceModel {

    public static final double START_X = 60;
    public static final double FINISH_X = 860;
    public static final double CAR_WIDTH = 100;
    public static final double LANE_TOP = 40;
    public static final double LANE_SPACING = 72;
    
    private final List<CarData> cars = new ArrayList<>();
    private final List<CarData> finishOrder = new ArrayList<>();
    private final Random random = new Random();

    private void addCar(String type, int number, String colorName, String colorHex) {
        double y = LANE_TOP + cars.size() * LANE_SPACING;
        cars.add(new CarData(type, number, colorName, colorHex, START_X, y));
    }
    
    public CarRaceModel() {
        addCar("Sports Coupe", 7, "Red", "#E53935");
        addCar("Muscle Car", 22, "Blue", "#1E88E5");
        addCar("Rally Hatch", 5, "Green", "#43A047");
        addCar("Roadster", 88, "Yellow", "#FDD835");
        addCar("Turbo Sedan", 13, "Purple", "#8E24AA");
    }
    
    public List<CarData> getCars() { 
        return cars; 
    }
    
    // the pixels a car travels from the start position until its front hits the 
    // finish line
    public double getFinishDistance() { 
        return FINISH_X - (START_X + CAR_WIDTH); 
    }
    
    // Give every car a new random speed in pixels per second
    public void randomizeSpeeds() {
        for (CarData c : cars) {
            c.setSpeed(65 + random.nextDouble() * 30);
        }
    }
}