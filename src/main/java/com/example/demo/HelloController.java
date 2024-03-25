package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HelloController implements Initializable {
    @FXML
    public AreaChart temp_chart;
    public ToggleButton window_toggle;
    public Slider slider_R;
    public Slider slider_G;
    public Slider slider_B;
    public Label ppl_label;
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
    private Button json_get;
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

    boolean window_flag=HelloApplication.dBmanager.getDataBool("switches","switches","state");

    @FXML
    protected void window_toggle_click(){
        if(!HelloApplication.dBmanager.getDataBool("switches","switches","state")){
            window_toggle.setSelected(false);
            HelloApplication.dBmanager.editData("switches","switches","state",true);
            window_toggle.setText("Окно открыто");
        }else{
            window_toggle.setSelected(true);
            HelloApplication.dBmanager.editData("switches","switches","state",false);
            window_toggle.setText("Окно закрыто");
        }
    }
    @FXML
    protected void json_get_click(){

    }
    @FXML
    protected void db_update_click()
    {
        HelloApplication.dBmanager.Ping();
    }
    @FXML
    public void db_send_click(ActionEvent actionEvent) {
HelloApplication.dBmanager.sendData(DBmanager.SENSORS,DBmanager.TEMP,(int)(Math.random()*20));
    }
    @FXML
    protected void bedroom_click(){
        bedroom_toggle.setSelected(bedroom_toggle.isSelected());
        HelloApplication.dBmanager.editData("switches","led","enabled",!HelloApplication.dBmanager.getDataBool("switches","led","enabled"));
    }
    @FXML

    public void db_get_click(ActionEvent actionEvent) {
      temp_label.setText("  Температура: "+HelloApplication.dBmanager.getLatestData("sensors","temperature","data")+ " °C");
      humidity_label.setText("  Влажность: "+HelloApplication.dBmanager.getLatestData("sensors","humidity","data")+ "%");
        XYChart.Series seriesTemp= new XYChart.Series();
        seriesTemp.setName("Температура");
        int data[] = HelloApplication.dBmanager.getLatestN("sensors","temperature","data",10);
        for (int i = 0; i < 10; i++){
            seriesTemp.getData().add(new XYChart.Data(1,data[i]));
        }
        //temp_chart.getData().add(seriesTemp);
        temp_chart.getData().add(seriesTemp);
    }


    private void refresh(){
        if(HelloApplication.dBmanager.getDataBool("switches","switches","state")){
            window_toggle.setSelected(false);
            window_toggle.setText("Окно открыто");
        }else{
            window_toggle.setSelected(true);
            window_toggle.setText("Окно закрыто");
        }
        temp_label.setText("  Температура: "+HelloApplication.dBmanager.getLatestData("sensors","temperature","data")+ " °C");
        humidity_label.setText("  Влажность: "+HelloApplication.dBmanager.getLatestData("sensors","humidity","data")+ "%");
        System.out.println("Refreshed!");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        slider_R.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                HelloApplication.dBmanager.editDataInt("switches","led","red",(int)slider_R.getValue());
            }
        });
        slider_G.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                HelloApplication.dBmanager.editDataInt("switches","led","green",(int)slider_G.getValue());
            }
        });
        slider_B.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                HelloApplication.dBmanager.editDataInt("switches","led","blue",(int)slider_B.getValue());
            }
        });
        refresh();
       Timeline refreshTimline = new Timeline(new KeyFrame(Duration.seconds(5),actionEvent ->{
           System.out.println("autorefreshing..");
           refresh();
        } ));
        refreshTimline.setCycleCount(Animation.INDEFINITE);
        refreshTimline.play();
        Timeline detection = new Timeline(new KeyFrame(Duration.millis(100),event ->{
            Object obj = null; // Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
            try {
                obj = new JSONParser().parse(new FileReader("src/main/detect.json"));
                JSONObject jo = (JSONObject) obj;
                ppl_label.setText("Людей обнаружено: " + jo.get("persons"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }));
        detection.setCycleCount(Animation.INDEFINITE);
        detection.play();
    }
}
