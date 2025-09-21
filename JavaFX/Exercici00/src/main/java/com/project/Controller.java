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
    private String currentOperation = "+";

    @FXML
    private void addNumber(ActionEvent event) {
        currentNumber += ((Button) event.getSource()).getText();
        textNumbers.setText(String.valueOf(currentNumber));
    }

    @FXML
    private void operate(ActionEvent event) {
        try {
            double number = Double.parseDouble(currentNumber);
            calculateCurrentResult(currentOperation, number);
            currentOperation = ((Button) event.getSource()).getText();
            currentNumber = "";
        } catch (NumberFormatException ex) {
            textNumbers.setText("ERROR");
        }
    }

    @FXML
    private void cleanOperations(ActionEvent event) {
        currentNumber = "0";
        textNumbers.setText(String.valueOf(currentNumber));

        result = 0;
    }

    @FXML
    private void result(ActionEvent event) {
        try {
            double number = Double.parseDouble(currentNumber);
            calculateCurrentResult(currentOperation, number);
            currentNumber = "";
            currentOperation = "+";
            result = 0;
        } catch (NumberFormatException ex) {
            textNumbers.setText("ERROR");
        }
    }

    private void calculateCurrentResult(String operation, double number) {
        switch (operation) {
            case "+":
                result += number;
                break;
            case "-":
                result -= number;
                break;
            case "*":
                result *= number;
                break;
            case "/":
                result /= number;
                break;
            default:
                break;
        }

        textNumbers.setText(String.valueOf(result));
    }

    @FXML
    private void adddDecimal(ActionEvent event) {

        if (currentNumber.contains("."))
            textNumbers.setText("ERROR");

        currentNumber += ".";
    }
}
