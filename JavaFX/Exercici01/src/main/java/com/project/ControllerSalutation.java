package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class ControllerSalutation extends Controller {
    @FXML
    private Text textMessage;
    @FXML
    private Button buttonPreviousView;

    public void initialize() {
        textMessage.setText(String.format("Hola %s, tens %d anys!", Main.name, Main.age));
    }

    @FXML
    private void previousView(ActionEvent event) {
        Main.name = "";
        Main.age = 0;

        UtilsViews.setView("FormView");
    }
}