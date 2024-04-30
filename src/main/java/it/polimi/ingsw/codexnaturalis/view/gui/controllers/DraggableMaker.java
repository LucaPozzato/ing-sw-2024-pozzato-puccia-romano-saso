package it.polimi.ingsw.codexnaturalis.view.gui.controllers;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class DraggableMaker {

    private double mouseX;
    private double mouseY;

    private double initialPositionX;
    private double initialPositionY;

    private double initialWidth;
    private double initialHeight;



    public void makeDraggable(Node node, ImageView node1){

        initialPositionX = node.getLayoutX();
        initialPositionY = node.getLayoutY();

        initialHeight = node1.getFitHeight();
        initialWidth = node1.getFitWidth();


        node.setOnMousePressed(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
        });

        node.setOnMouseDragged(mouseEvent -> {
            node1.setFitWidth(144);
            node1.setFitHeight(98);
            node.setLayoutX(mouseEvent.getSceneX() - mouseX);
            node.setLayoutY(mouseEvent.getSceneY() - mouseY);
        });

        node.setOnMouseReleased(mouseEvent -> {
            node.setLayoutX(initialPositionX);
            node.setLayoutY(initialPositionY);
            node1.setFitHeight(initialHeight);
            node1.setFitWidth(initialWidth);
        });



    }
}
