module it.polimi.ingsw.codexnaturalis {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens it.polimi.ingsw.codexnaturalis to javafx.fxml;

    exports it.polimi.ingsw.codexnaturalis;
}