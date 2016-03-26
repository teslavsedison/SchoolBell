package org.bell.app;

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
import org.bell.entity.BellTime;
import org.bell.entity.FileNameConstants;
import org.bell.entity.SchoolDay;
import org.bell.framework.IntegerRangeValidator;
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
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class MainController implements Initializable {
    //private static String BELL_TIMES_FILE_NAME = "BellPlan.json";
    public static final Type LOCAL_TIME_TYPE = new TypeToken<LocalTime>() {
    }.getType();
    @FXML
    public ComboBox<SchoolDay> cbDayList;
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
    @FXML
    public TabPane tabMain;
    @FXML
    public ListView lvTimes;
    @FXML
    public ComboBox cbDayList2;

    @FXML
    private Button btnClick;

    private File file;
    private ValidationSupport validationSupport;
    private Set<SchoolDay> schoolDays;
    private ObservableList<SchoolDay> dayList;

    public void btnClicked(Event event) {
        if (!validationSupport.isInvalid()) {
            DailyBellCalculator dailyBellCalculator = setCalculator();
            SchoolDay sd = new SchoolDay();
            if (schoolDays.size() < 6) {
                SchoolDay selectedItem = cbDayList.getSelectionModel().getSelectedItem();
                sd.setDayName(selectedItem.getDayName());
                sd.setBellTimes(dailyBellCalculator.calculateBellTime());
                schoolDays.add(sd);
                cbDayList.getItems().remove(selectedItem);
                cbDayList2.getItems().add(selectedItem);
            }
            ObservableList<BellTime> list = FXCollections.observableArrayList();
            list.addAll(sd.getBellTimes());
//            lvTimes.setItems(list);
//            jsonProcesses(schoolDays);
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

    private DailyBellCalculator setCalculator() {
        DailyBellCalculator dailyBellCalculator = new DailyBellCalculator();
        dailyBellCalculator.setStartTime(LocalTime.parse(startTimeField.getText()));
        dailyBellCalculator.setLectureTime(LocalTime.parse(lectureTimeField.getText()));
        dailyBellCalculator.setLectureCountBeforeLunch(Integer.parseInt(lectureCountBeforeLunchField.getText()));
        dailyBellCalculator.setLunchBreakTime(LocalTime.parse(lunchBreakTimeField.getText()));
        dailyBellCalculator.setLectureCountAfterLunch(Integer.parseInt(lectureCountAfterLunch.getText()));
        dailyBellCalculator.setBreakTime(LocalTime.parse(breakTimeField.getText()));
        return dailyBellCalculator;
    }


    private void generateFile(String name, byte[] bytes) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(name);
        //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8");
//        RandomAccessFile randomAccessFile = new RandomAccessFile(name, "rw");
//        randomAccessFile.seek(randomAccessFile.);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DropShadow shadow = new DropShadow();
        validationSupport = new ValidationSupport();
        cbDayList2.setItems(FXCollections.observableArrayList());
        schoolDays = new HashSet<>();
        buttonDropShadowEffect(shadow);

        btnSelectMp3.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Bir mp3 seçiniz");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio Files", "*.mp3"));
            file = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
            try {
                if (file != null) {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    generateFile(FileNameConstants.MP3_FILE_NAME, bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tabMain.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                });

        cbDayList2.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
//                    ArrayList<SchoolDay> schoolDays = JsonFileHelper.getSchoolDays();
                    for (SchoolDay sd : schoolDays) {
                        ObservableList<BellTime> sds = null;
                        if (sd.getDayName().equals(newValue)) {
                            sds = FXCollections.observableArrayList(sd.getBellTimes());
                            lvTimes.setItems(sds);
                        }
                    }
                });

        setFormatters();
        setValidations();
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

    private void setDays2() {
        ObservableList<String> dayList2 = FXCollections.observableArrayList();
        ArrayList<SchoolDay> schoolDays = null;
        //dayList.addAll(DayName.MONDAY, DayName.TUESDAY, DayName.WEDNESDAY, DayName.THURSDAY, DayName.FRIDAY);
        if (schoolDays != null) {
            for (SchoolDay sd : schoolDays) {
                dayList2.add(sd.getDayName());
            }
            cbDayList2.setItems(dayList2);
        }
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

    public void deleteSelectedSchoolDay(Event event) {
        String selected = (String) cbDayList2.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ArrayList<SchoolDay> schoolDays = null;// = JsonFileHelper.getSchoolDays();
            for (int index = 0; index < schoolDays.size(); index++) {
                if (schoolDays.get(index).getDayName().equals(selected)) {
                    SchoolDay sd = schoolDays.remove(index);
//                    jsonProcesses(schoolDays);
                    setDays2();
                    //cbDayList.getItems().add(sd.getDayName());
                }
            }
        }
    }
}


