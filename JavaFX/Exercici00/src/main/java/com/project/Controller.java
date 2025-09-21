package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

public class Controller {
    @FXML
    private Text textNumbers;

    private double result = 0;
    private String currentNumber = "";
    private char currentOperation = '+';

    @FXML
    private void addNumber(ActionEvent event) {
        currentNumber = ((Button) event.getSource()).getText();
        textNumbers.setText(String.valueOf(currentNumber));
    }
}
