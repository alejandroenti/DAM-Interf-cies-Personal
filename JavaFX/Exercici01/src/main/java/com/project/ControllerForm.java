package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ControllerForm extends Controller {
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldAge;
    @FXML
    private Button buttonNextView;

    @Override
    public void initialize() {
        textFieldName.setText("");
        textFieldAge.setText("");

        buttonNextView.setDisable(true);
    }

    @FXML
    private void updateName(KeyEvent event) {
        Main.name = textFieldName.getText();
        checkDisableButton();
    }

    @FXML
    private void updateAge(KeyEvent event) {
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
            Main.age = 0;
            checkDisableButton();
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
