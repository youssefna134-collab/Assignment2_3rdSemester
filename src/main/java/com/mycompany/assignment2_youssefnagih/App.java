package com.mycompany.assignment2_youssefnagih;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/*
@author Youssef Nagih
6372635
*/
public class App extends Application {

    private CarRaceController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CarRaceFXML.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        stage.setTitle("Campus Grand Prix");
        stage.setScene(new Scene(root, 1000, 720));
        stage.show();

        controller.start();
    }

    

    public static void main(String[] args) {
        launch(args);
    }
}