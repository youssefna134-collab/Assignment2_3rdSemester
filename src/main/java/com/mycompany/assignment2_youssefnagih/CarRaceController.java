/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.assignment2_youssefnagih;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
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
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


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
    private final List<CarModel> carModels = new ArrayList<>();
    private final CarRaceModel model = new CarRaceModel();
    private State state = State.IDLE;
    private final List<ParallelTransition> races = new ArrayList<>();
    private MediaPlayer player;
    private Timeline slideshow;

    private enum State { 
        IDLE, RUNNING, PAUSED, FINISHED 
    }
    
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
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
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        speedLabel.textProperty().bind(speedSlider.valueProperty().asString("Speed: %.1fx"));

        for (CarData car : model.getCars()) {
            CarModel shape = new CarModel(car);
            shape.setLayoutX(CarRaceModel.START_X);
            shape.setLayoutY(car.getY());
            shape.translateXProperty().addListener((observable, oldValue, newValue) ->
                    model.updatePosition(car, CarRaceModel.START_X + newValue.doubleValue())
            );
            carModels.add(shape);
            track.getChildren().add(shape);
        }

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            for (ParallelTransition race : races) {
                race.setRate(newValue.doubleValue());
            }
        });

        messageArea.setText(INTRO);
        updateButtons();
    }    
    
    /*
    SLIDESHOW, called by app once the window is showing
    */
    public void start() {
        startSound();
        int n = model.getCars().size();
        slideshow = new Timeline();
        for (int i = 0; i < n; i++) {
            final int idx = i;
            slideshow.getKeyFrames().add(new KeyFrame(Duration.seconds(i * 
                    SLIDE_SECONDS), e -> showSlide(idx, n)));
        }
        slideshow.getKeyFrames().add(new KeyFrame(Duration.seconds(n * 
                SLIDE_SECONDS), e -> endSlideshow()));
        slideshow.play();
    }

    /*
    ---------- BUTTONS
    */
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
        }
        
        model.resetRace();
        state = State.IDLE;
        messageArea.setText(INTRO);
        updateButtons();
    }
    
    @FXML
    private void onSkip(ActionEvent event) {
        endSlideshow();
    }
    
    private void updateButtons() {
        startButton.setDisable(state == State.RUNNING || state == State.FINISHED);
        pauseButton.setDisable(state != State.RUNNING);
    }
    

    /*
    -------- SOUND MEDIA
    */
    private void startSound() {
        try {
            Media media = new Media(getClass().getResource("race.wav").toExternalForm());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setAutoPlay(true);
        } catch (Exception ex) {
            System.out.println("Could not play sound");
        }
    }

    public void stopSound() {
        if (player != null) {
            player.stop();
        }
    }
    
    
    /*
    ---------- SHOWS SHAPE CAR
    */
    private void showSlide(int idx, int total) {
        CarData car = model.getCars().get(idx);
        CarModel carModel = new CarModel(car);
        carModel.setScaleX(2.6);
        carModel.setScaleY(2.6);
        Node picture = carModel;
        
        carStage.getChildren().setAll(picture);
        carInfoLabel.setText(String.format("Car #%d  -  %s %s   (%d of %d)",
                car.getNumber(), car.getColorName(), car.getType(), idx + 1, total));

        FadeTransition fade = new FadeTransition(Duration.millis(600), carStage);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    
    private void endSlideshow() {
        if (slideshow != null) {
            slideshow.stop();
        }
        stopSound();
        slidePane.setVisible(false);
        slidePane.setManaged(false);
    }
    
    /*
    -------- RACE LOGIC AND RESULTS SHOWCASE
    */
    private void launchRace() {
        model.randomizeSpeeds();
        races.clear();
        double distance = model.getFinishDistance();
        double wheelDegrees = Math.toDegrees(distance / WHEEL_RADIUS);
        messageArea.setText("The race is on!\n");

        for (int i = 0; i < model.getCars().size(); i++) {
            final int idx = i;
            CarData car = model.getCars().get(i);
            CarModel shape = carModels.get(i);
            Duration time = Duration.seconds(distance / car.getSpeed());

            TranslateTransition move = new TranslateTransition(time, shape);
            move.setFromX(0);
            move.setToX(distance);
            move.setInterpolator(Interpolator.LINEAR);

            RotateTransition frontSpin = new RotateTransition(time, shape.getFrontWheel());
            frontSpin.setByAngle(wheelDegrees);
            frontSpin.setInterpolator(Interpolator.LINEAR);

            RotateTransition rearSpin = new RotateTransition(time, shape.getRearWheel());
            rearSpin.setByAngle(wheelDegrees);
            rearSpin.setInterpolator(Interpolator.LINEAR);

            ParallelTransition race = new ParallelTransition(move, frontSpin, rearSpin);
            race.setRate(speedSlider.getValue());
            race.setOnFinished(e -> carFinished(idx));
            races.add(race);
        }

        for (ParallelTransition race : races) {
            race.play();
        }
    }
    
    /*
    Identifies each car's position and puts it inside the text area
    */
    private void carFinished(int idx) {
        CarData car = model.getCars().get(idx);
        model.recordFinish(car);

        int place = model.getFinishOrder().size();
        if (place == 1) {
            messageArea.appendText("Car #" + car.getNumber()  + car.getColorName()
                    + "crosses the finish line first\n");
        } else {
            messageArea.appendText("Car #" + car.getNumber() + car.getColorName()
                    + "finishes in place " + place + "\n");
        }

        if (model.isRaceOver()) {
            state = State.FINISHED;
            messageArea.setText(buildResults());
            updateButtons();
        }
    }
    
    /* 
    Helper method to build a string for the results
    */
    private String buildResults() {
        CarData winner = model.getWinner();
        StringBuilder sb = new StringBuilder();
        sb.append("Congratulations! Car #").append(winner.getNumber()) 
          .append(winner.getColorName()).append(" ").append(winner.getType())
          .append("wins the race\n\n");
        sb.append("Final results:\n");
        int place = 1;
        for (CarData car : model.getFinishOrder()) {
            sb.append(place++).append(" Car #").append(car.getNumber())
              .append(car.getColorName()).append(" ").append(car.getType()).append("\n");
        }

        return sb.toString();
    }
}
    