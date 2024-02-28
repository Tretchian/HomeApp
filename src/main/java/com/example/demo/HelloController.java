package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

public class HelloController {
    @FXML
    private TextArea targetHumidity_textArea;
    @FXML
    private Label humidity_label;
    @FXML
    private TextArea targetTemp_textArea;
    @FXML
    private Button db_update;
    @FXML
    private Button db_send;
    @FXML
    private LineChart chart;
    @FXML
    private Pane main_pane;
    @FXML
    private Pane atmos_pane;
    @FXML
    private Pane door_pane;
    @FXML
    private Pane lights_pane;

    @FXML
    private ToggleButton bedroom_toggle;
    @FXML
    private ToggleButton garage_toggle;
    @FXML
    private ToggleButton canteen_toggle;
    @FXML
    private ToggleButton weird_toggle;

    @FXML
    private Label  temp_label;


    @FXML
    protected void db_update_click()
    {
        HelloApplication.dBmanager.Ping();
    }
    @FXML
    public void db_send_click(ActionEvent actionEvent) {
HelloApplication.dBmanager.sendData();
    }
    @FXML
    protected void bedroom_click(){
        bedroom_toggle.setSelected(bedroom_toggle.isSelected());
    }
    @FXML
    protected void garage_click(){
        garage_toggle.setSelected(garage_toggle.isSelected());

    }
    @FXML
    protected void canteen_click(){
        canteen_toggle.setSelected(canteen_toggle.isSelected());

    }
    @FXML
    protected void weird_click(){
        weird_toggle.setSelected(weird_toggle.isSelected());

    }


}