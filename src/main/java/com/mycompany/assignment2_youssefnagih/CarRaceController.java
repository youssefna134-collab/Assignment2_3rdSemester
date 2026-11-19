/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.assignment2_youssefnagih;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.animation.ParallelTransition;

/**
 * FXML Controller class
 *
 * @author Youssef Nagih
 * 6372635
 */
public class CarRaceController implements Initializable {

    private static final double SLIDE_SECONDS = 2.5;
    private static final double WHEEL_RADIUS = 8;
    private static final String INTRO = "Press start to begin the race";
            

    private enum State { 
        IDLE, RUNNING, PAUSED, FINISHED 
    }
    
    @FXML
    private BorderPane racePane;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label speedLabel;
    @FXML
    private Slider speedSlider;
    @FXML
    private TextArea messageArea;
    @FXML
    private Pane track;
    @FXML
    private VBox slidePane;
    @FXML
    private StackPane carStage;
    @FXML
    private Label carInfoLabel;
    @FXML
    private Button skipButton;
    
     private final CarRaceModel model = new CarRaceModel();
     private State state = State.IDLE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

     @FXML
    private void onStart() {
        if (state == State.IDLE) {
            launchRace();
            state = State.RUNNING;
        } else if (state == State.PAUSED) {
            for (ParallelTransition race : races) {
                if (race.getStatus() == Animation.Status.PAUSED) {
                    race.play();
                }
            }
            state = State.RUNNING;
        }
        updateButtons();
    }

    @FXML
    private void onPause() {
        if (state != State.RUNNING) {
            return;
        }
        for (ParallelTransition race : races) {
            if (race.getStatus() == Animation.Status.RUNNING) {
                race.pause();
            }
        }
        state = State.PAUSED;
        updateButtons();
    }

    @FXML
    private void onReset() {
        for (ParallelTransition race : races) {
            race.stop();
        }
        races.clear();
        for (CarModel shape : carModels) {
            shape.setTranslateX(0);
            shape.getFrontWheel().setRotate(0);
            shape.getRearWheel().setRotate(0);
            shape.setWinnerGlow(false);
        }
        model.resetRace();
        state = State.IDLE;
        messageArea.setText(INTRO);
        updateButtons();
    }
    
       private void updateButtons() {
        startButton.setDisable(state == State.RUNNING || state == State.FINISHED);
        pauseButton.setDisable(state != State.RUNNING);
       }


    @FXML
    private void onSkip(ActionEvent event) {
    }
    
}
