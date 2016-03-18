package org.bell.app;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.bell.framework.TimeHHMMValidator;
import org.bell.framework.TimeToStringConverter;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    public GridPane gridPane;

    @FXML
    public TextField startTime;

    @FXML
    public TextField lectureTime;

    @FXML
    public TextField breakTime;

    @FXML
    public TextField lectureCountBeforeLunch;

    @FXML
    public TextField lunchBreakTime;

    @FXML
    public TextField lectureCountAfterLunch;

    @FXML
    public TextField endTime;
    DropShadow shadow = new DropShadow();
    ValidationSupport validationSupport = new ValidationSupport();
    @FXML
    private Button btnClick;

    public void btnClicked(Event event) {
//        btnClick.setText("Hahahah");
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Hello Şeko");
//        alert.setHeaderText("TEST");
//        alert.showAndWait();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnClick.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            btnClick.setEffect(shadow);
        });

        btnClick.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            btnClick.setEffect(null);
        });


        TextFormatter<LocalTime> tf1 = new TextFormatter<>(new TimeToStringConverter());
        TextFormatter<LocalTime> tf2 = new TextFormatter<>(new TimeToStringConverter());
        TextFormatter<LocalTime> tf3 = new TextFormatter<>(new TimeToStringConverter());
        startTime.setTextFormatter(tf1);
        endTime.setTextFormatter(tf3);
        lectureTime.setTextFormatter(tf2);


        validationSupport.registerValidator(startTime, Validator.createEmptyValidator("Boş bırakılamaz."));
        validationSupport.registerValidator(endTime, Validator.createEmptyValidator("Boş bırakılamaz."));
        validationSupport.registerValidator(startTime, new TimeHHMMValidator());
        validationSupport.registerValidator(lectureTime, new TimeHHMMValidator());

    }
}


