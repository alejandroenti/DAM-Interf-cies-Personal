package com.project;

import javafx.scene.control.Label;
import java.util.Objects;
import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ControllerDesktopListController {

    @FXML
    private ImageView desktopListItemImage;
    @FXML
    private Label desktopListItemText;
    @FXML
    private AnchorPane desktopListItemBackground;

    private int objectIndex;
    private ControllerDesktop mainController;

    public void setMainController(ControllerDesktop mainController) {
        this.mainController = mainController;
    }

    public void setIndex(int objectIndex) {
        this.objectIndex = objectIndex;
    }

    public void setTitle(String title) {
        this.desktopListItemText.setText(title);
    }

    public void setImatge(String imagePath) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            this.desktopListItemImage.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
    }

    @FXML
    private void onMouseEntered() {
        desktopListItemBackground.setStyle("-fx-background-color: #696969;");
        desktopListItemBackground.setStyle("-fx-border-color: #fefefe;");
    }

    @FXML
    private void onMouseExited() {
        desktopListItemBackground.setStyle("-fx-background-color: #fefefe;");
        desktopListItemBackground.setStyle("-fx-border-color: #B5B5B5;");
    }

    @FXML
    public void onMouseClicked() {
        Main.currentObject = objectIndex;
        mainController.showData();
    }
}