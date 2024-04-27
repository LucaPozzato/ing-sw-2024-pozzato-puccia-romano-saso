module it.polimi.ingsw.codexnaturalis {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;
    requires java.desktop;


    exports it.polimi.ingsw.codexnaturalis.model.game.components;


    opens it.polimi.ingsw.codexnaturalis to javafx.fxml;
    exports it.polimi.ingsw.codexnaturalis.view.gui;
    exports it.polimi.ingsw.codexnaturalis.view.gui.controllers;
    opens it.polimi.ingsw.codexnaturalis.view.gui.controllers to javafx.fxml;
}