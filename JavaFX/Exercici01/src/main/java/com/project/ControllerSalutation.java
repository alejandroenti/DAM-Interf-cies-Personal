package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javafx.scene.control.TextField;

import com.project.Main;
import com.project.UtilsViews;

import javafx.event.ActionEvent;

public class ControllerSalutation {
    @FXML
    private Text textMessage;
    @FXML
    private Button buttonPreviousView;

    @FXML
    public void initialize() {
        System.out.println(Main.name + " - " + Main.age);
        textMessage.setText(String.format("Hola %s, tens %d anys!", Main.name, Main.age));
    }

    @FXML
    private void previousView(ActionEvent event) {
        Main.name = "";
        Main.age = 0;

        UtilsViews.setView("FormView");
    }
}