package org.bell.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.bell.entity.DayName;
import org.bell.entity.FileNameConstants;
import org.bell.entity.SchoolDay;
import org.bell.framework.IntegerRangeValidator;
import org.bell.framework.LocalTimeConverter;
import org.bell.framework.TimeHHMMValidator;
import org.bell.framework.TimeToStringConverter;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;

public class MainController implements Initializable {
    //private static String BELL_TIMES_FILE_NAME = "BellPlan.json";
    public static final Type LOCAL_TIME_TYPE = new TypeToken<LocalTime>() {
    }.getType();
    @FXML
    public ComboBox<String> cbDayList;
    //    @FXML
//    public ListView lvTimes;
    @FXML
    public Button btnSelectMp3;
    @FXML
    public GridPane gridPane;
    @FXML
    public TextField startTimeField;
    @FXML
    public TextField lectureTimeField;
    @FXML
    public TextField breakTimeField;
    @FXML
    public TextField lectureCountBeforeLunchField;
    @FXML
    public TextField lunchBreakTimeField;
    @FXML
    public TextField lectureCountAfterLunch;
    private File file;
    private ValidationSupport validationSupport;
    private ArrayList<SchoolDay> schoolDays;
    private Timer tmr;
    private ObservableList<String> dayList;
    @FXML
    private Button btnClick;

    //    private StringProperty startTime;
//    private StringProperty lectureTime;
//    private StringProperty breakTime;
//    private IntegerProperty lectureCountBeforeLunch;
//    private StringProperty lunchBreakTime;
    public void btnClicked(Event event) {
        if (!validationSupport.isInvalid()) {
            DailyBellCalculator dailyBellCalculator = new DailyBellCalculator();
            dailyBellCalculator.setStartTime(LocalTime.parse(startTimeField.getText()));
            dailyBellCalculator.setLectureTime(LocalTime.parse(lectureTimeField.getText()));
            dailyBellCalculator.setLectureCountBeforeLunch(Integer.parseInt(lectureCountBeforeLunchField.getText()));
            dailyBellCalculator.setLunchBreakTime(LocalTime.parse(lunchBreakTimeField.getText()));
            dailyBellCalculator.setLectureCountAfterLunch(Integer.parseInt(lectureCountAfterLunch.getText()));
            dailyBellCalculator.setBreakTime(LocalTime.parse(breakTimeField.getText()));
            SchoolDay sd = new SchoolDay();
            if (schoolDays.size() < 6) {
                String selectedItem = cbDayList.getSelectionModel().getSelectedItem();
                sd.setDayName(selectedItem);
                sd.setBellTimes(dailyBellCalculator.calculateBellTime());
                schoolDays.add(sd);
                cbDayList.getItems().remove(selectedItem);
            }
            ObservableList<LocalTime> list = FXCollections.observableArrayList();
            list.addAll(sd.getBellTimes());
//            lvTimes.setItems(list);
            jsonProcesses();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            Stage alartStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alartStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("bell1.png")));
            alert.setTitle("Dikkat");
            alert.setHeaderText("Doğrulama Hatası!");
            alert.setContentText("Lütfen bütün alanların doğru girildiğine emin olunuz.");
            alert.show();
        }
    }


    private void jsonProcesses() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(LOCAL_TIME_TYPE, new LocalTimeConverter())
                .serializeNulls().create();
        String wdir = System.getProperty("user.dir");
        SchoolDay readedBellTimes[];
        try {
            generateFile(wdir + FileNameConstants.JSON_FILE_NAME, gson.toJson(schoolDays).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFile(String name, byte[] bytes) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(name);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DropShadow shadow = new DropShadow();
        validationSupport = new ValidationSupport();
        // cbDayList.setItems(FXCollections.observableArrayList());
        schoolDays = new ArrayList<>();
        setDays();
        buttonDropShadowEffect(shadow);

        btnSelectMp3.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Bir mp3 seçiniz");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio Files", "*.mp3"));
            file = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                //FileWriter fileWriter = new FileWriter(.getAbsoluteFile() + file.getName());
                String p = System.getProperty("user.dir");
                generateFile(p + FileNameConstants.MP3_FILE_NAME, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        setFormatters();
        setValidations();
    }

    private void setDays() {
        dayList = FXCollections.observableArrayList();
//        lvTimes.setItems(FXCollections.observableArrayList());
        dayList.add(DayName.MONDAY);
        dayList.add(DayName.TUESDAY);
        dayList.add(DayName.WEDNESDAY);
        dayList.add(DayName.THURSDAY);
        dayList.add(DayName.FRIDAY);

        cbDayList.setItems(dayList);
    }

    private void setFormatters() {
        TextFormatter<LocalTime> tf1 = new TextFormatter<>(new TimeToStringConverter());
        TextFormatter<LocalTime> tf2 = new TextFormatter<>(new TimeToStringConverter());
        TextFormatter<LocalTime> tf3 = new TextFormatter<>(new TimeToStringConverter());
        TextFormatter<LocalTime> tf4 = new TextFormatter<>(new TimeToStringConverter());

        startTimeField.setTextFormatter(tf1);
        lectureTimeField.setTextFormatter(tf2);
        lectureCountBeforeLunchField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        lectureCountAfterLunch.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        breakTimeField.setTextFormatter(tf3);
        lunchBreakTimeField.setTextFormatter(tf4);
    }

    private void setValidations() {
        validationSupport.registerValidator(startTimeField, new TimeHHMMValidator());
        validationSupport.registerValidator(lectureTimeField, new TimeHHMMValidator());
        validationSupport.registerValidator(lectureCountBeforeLunchField, new IntegerRangeValidator(3, 6));
        validationSupport.registerValidator(lectureCountAfterLunch, new IntegerRangeValidator(1, 5));
        validationSupport.registerValidator(breakTimeField, new TimeHHMMValidator());
        validationSupport.registerValidator(lunchBreakTimeField, new TimeHHMMValidator());
        validationSupport.registerValidator(cbDayList, Validator.createEmptyValidator("Lütfen bir sçim yapınız"));
    }

    private void buttonDropShadowEffect(DropShadow shadow) {
        btnClick.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            btnClick.setEffect(shadow);
        });

        btnClick.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            btnClick.setEffect(null);
        });
    }

    public void showAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage alartStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alartStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("bell1.png")));
        alert.setTitle("Okul zili");
        alert.setHeaderText("Hakkında");
        alert.setContentText("Bu yazılım Ermenek Mustafa Demirok Mesleki ve " +
                "Teknik Eğitim Merkezi Elektrik-Elektronik Teknolojileri Alanı/" +
                "Elektrik Öğretmeni Hakan GÜLEN tarafından açık kaynak kodlu olarak geliştirimiştir." +
                "Her türlü görüş ve önerilerinizi hgulen33@gmail.com adresine iletebilrsiniz.");
        alert.show();
    }
}


