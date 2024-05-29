package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * WaitStage class is a javaFx controller used to manage waitStage stage
 */
public class WaitStage implements Initializable {

    @FXML
    private Text timer;

    private int seconds = 0;

    /**
     * Used to update the timer from seconds to minutes
     */
    private void updateTimerText() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        timer.setText(String.format("%d:%02d", minutes, remainingSeconds));
    }

    /**
     * Initialized the controller class
     * Used to set up the timer
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTimerText();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds++;
            updateTimerText();
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
