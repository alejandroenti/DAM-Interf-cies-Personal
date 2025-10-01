package com.project;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ControllerScreen {

    @FXML
    private Label textScreen;
    @FXML
    private ImageView imageScreen;
    @FXML
    private HBox layoutScreen;

    private Boolean isQuestion = false;

    public void setText(String text) {
        textScreen.setText(text);
    }

    public Label getText() {
        return textScreen;
    }

    public void setIsQuestion(Boolean isQuestion) {
        this.isQuestion = isQuestion;
        setLayout();
    }

    public void setImage(Image image) {
        imageScreen.setImage(image);
    }

    private void setLayout() {

        if (isQuestion) {
            layoutScreen.setAlignment(Pos.CENTER_RIGHT);
            textScreen.setStyle("-fx-background-color: #5ED6BA");
        }
        else {
            layoutScreen.setAlignment(Pos.CENTER_LEFT);
            textScreen.setStyle("-fx-background-color: #9eaca8ff");
        }
    }
}
