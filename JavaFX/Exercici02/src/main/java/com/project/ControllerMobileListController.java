package com.project;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ControllerMobileListController {

    @FXML
    private Label mobileListItemText;
    @FXML
    private AnchorPane mobileListItemBackground;
    @FXML
    private ImageView mobileListItemImage;

    private int objectIndex;
    private ControllerMobileItems mainController;

    public void setMainController(ControllerMobileItems mainController) {
        this.mainController = mainController;
    }

    public void setIndex(int objectIndex) {
        this.objectIndex = objectIndex;
    }

    public void setTitle(String title) {
        this.mobileListItemText.setText(title);
    }

    public void setImatge(String imagePath) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            this.mobileListItemImage.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
    }

    @FXML
    public void onMouseClicked() {
        Main.currentObject = objectIndex;
        mainController.passNextView();
    }
}