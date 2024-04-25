package it.polimi.ingsw.codexnaturalis.view.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiApp extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showInitialStage();
    }
}
