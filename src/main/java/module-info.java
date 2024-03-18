module it.polimi.ingsw.codexnaturalis {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsw.codexnaturalis to javafx.fxml;
    exports it.polimi.ingsw.codexnaturalis;
}