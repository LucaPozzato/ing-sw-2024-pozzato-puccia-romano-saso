module it.polimi.ingsw.codexnaturalis {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;
    requires java.desktop;
    requires javafx.base;
    requires java.sql;
    // requires org.junit.jupiter.api;

    exports it.polimi.ingsw.codexnaturalis;
    exports it.polimi.ingsw.codexnaturalis.model.game.components;
    exports it.polimi.ingsw.codexnaturalis.model.enumerations;
    exports  it.polimi.ingsw.codexnaturalis.model.game.player;
    exports  it.polimi.ingsw.codexnaturalis.model.game.components.structure;
    exports  it.polimi.ingsw.codexnaturalis.model.chat;
    exports  it.polimi.ingsw.codexnaturalis.model.game.components.cards;

    opens it.polimi.ingsw.codexnaturalis to javafx.fxml;

    exports it.polimi.ingsw.codexnaturalis.view.gui;
    exports it.polimi.ingsw.codexnaturalis.view.gui.controllers;

    opens it.polimi.ingsw.codexnaturalis.view.gui.controllers to javafx.fxml;

    exports it.polimi.ingsw.codexnaturalis.network;
}

//My last module info:
//module it.polimi.ingsw.codexnaturalis {
//        requires javafx.controls;
//        requires javafx.graphics;
//        requires javafx.fxml;
//        requires com.google.gson;
//        requires java.rmi;
//        requires java.desktop;
//        requires javafx.base;
//        // requires org.junit.jupiter.api;
//
//
//        exports it.polimi.ingsw.codexnaturalis.model.game.components;
//        exports it.polimi.ingsw.codexnaturalis.model.enumerations;
//        exports  it.polimi.ingsw.codexnaturalis.model.game.player;
//        exports  it.polimi.ingsw.codexnaturalis.model.game.components.structure;
//        exports  it.polimi.ingsw.codexnaturalis.model.chat;
//        exports  it.polimi.ingsw.codexnaturalis.model.game.components.cards;
//
//        exports it.polimi.ingsw.codexnaturalis;
//
//
//        opens it.polimi.ingsw.codexnaturalis to javafx.fxml;
//
//        exports it.polimi.ingsw.codexnaturalis.view.gui;
//        exports it.polimi.ingsw.codexnaturalis.view.gui.controllers;
//
//        opens it.polimi.ingsw.codexnaturalis.view.gui.controllers to javafx.fxml;
//
//        exports it.polimi.ingsw.codexnaturalis.network;
//        }