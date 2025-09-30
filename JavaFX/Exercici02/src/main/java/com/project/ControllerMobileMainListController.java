package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ControllerMobileMainListController {

    @FXML
    private Label mobileMainListItemText;
    @FXML
    private AnchorPane mobileMainListItemBackground;

    private int objectIndex;
    private ControllerMobileMain mainController;

    public void setMainController(ControllerMobileMain mainController) {
        this.mainController = mainController;
    }

    public void setIndex(int objectIndex) {
        this.objectIndex = objectIndex;
    }

    public void setTitle(String title) {
        this.mobileMainListItemText.setText(title);
    }

    @FXML
    private void onMouseEntered() {
        mobileMainListItemBackground.setStyle("-fx-background-color: #696969;");
        mobileMainListItemBackground.setStyle("-fx-border-color: #fefefe;");
    }

    @FXML
    private void onMouseExited() {
        mobileMainListItemBackground.setStyle("-fx-background-color: #fefefe;");
        mobileMainListItemBackground.setStyle("-fx-border-color: #B5B5B5;");
    }

    @FXML
    public void onMouseClicked() {
        Main.currentObject = objectIndex;
        mainController.passNextView();
    }
}