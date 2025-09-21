package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

public class ControllerForm {
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldAge;
    @FXML
    private Button buttonNextView;

    @FXML
    public void initialize() {
        textFieldName.setText("");
        textFieldAge.setText("");

        buttonNextView.setDisable(true);
    }

    @FXML
    private void updateName(ActionEvent event) {
        Main.name = textFieldName.getText();
        checkDisableButton();
    }

    @FXML
    private void updateAge(ActionEvent event) {
        try {
            int age = Integer.parseInt(textFieldAge.getText());

            if (age < 0 || age > 100) {
                textFieldAge.setText("");
            } else {
                Main.age = age;
                checkDisableButton();
            }
        } catch (NumberFormatException ex) {
            textFieldAge.setText("");
        }
    }

    @FXML
    private void nextView(ActionEvent event) {
        UtilsViews.setView("SalutationView");
    }

    private void checkDisableButton() {
        if (Main.name != "" && Main.age != 0) {
            buttonNextView.setDisable(false);
        } else {
            buttonNextView.setDisable(true);
        }
    }
}
