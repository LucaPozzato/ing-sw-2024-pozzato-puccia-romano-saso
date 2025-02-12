module it.polimi.ingsw.codexnaturalis {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;
    requires java.desktop;
    requires javafx.base;
    requires java.sql;

    exports it.polimi.ingsw.codexnaturalis;
    exports it.polimi.ingsw.codexnaturalis.controller;
    exports it.polimi.ingsw.codexnaturalis.model.chat;
    exports it.polimi.ingsw.codexnaturalis.model.enumerations;
    exports it.polimi.ingsw.codexnaturalis.model.exceptions;
    exports it.polimi.ingsw.codexnaturalis.model.game;
    exports it.polimi.ingsw.codexnaturalis.model.game.components;
    exports it.polimi.ingsw.codexnaturalis.model.game.components.cards;
    exports it.polimi.ingsw.codexnaturalis.model.game.components.structure;
    exports it.polimi.ingsw.codexnaturalis.model.game.parser;
    exports it.polimi.ingsw.codexnaturalis.model.game.player;
    exports it.polimi.ingsw.codexnaturalis.model.game.strategies;
    exports it.polimi.ingsw.codexnaturalis.network;
    exports it.polimi.ingsw.codexnaturalis.network.client;
    exports it.polimi.ingsw.codexnaturalis.network.commands;
    exports it.polimi.ingsw.codexnaturalis.network.events;
    exports it.polimi.ingsw.codexnaturalis.network.server;
    exports it.polimi.ingsw.codexnaturalis.utils;
    exports it.polimi.ingsw.codexnaturalis.view;
    exports it.polimi.ingsw.codexnaturalis.view.gui;
    exports it.polimi.ingsw.codexnaturalis.view.gui.controllers;
    exports it.polimi.ingsw.codexnaturalis.view.tui;

    opens it.polimi.ingsw.codexnaturalis to javafx.fxml;
    opens it.polimi.ingsw.codexnaturalis.view.gui.controllers to javafx.fxml;
    opens it.polimi.ingsw.codexnaturalis.model.game.parser;
    opens it.polimi.ingsw.codexnaturalis.model.game.components.structure;
    opens it.polimi.ingsw.codexnaturalis.controller;
    opens it.polimi.ingsw.codexnaturalis.model.game;
    opens it.polimi.ingsw.codexnaturalis.model.game.components;
    opens it.polimi.ingsw.codexnaturalis.model.chat;
}