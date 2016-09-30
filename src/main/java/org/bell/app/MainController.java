package org.bell.app;

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
import javazoom.jl.decoder.JavaLayerException;
import org.bell.dao.SchoolBellDao;
import org.bell.entity.BellTime;
import org.bell.entity.FileNameConstants;
import org.bell.entity.SchoolDay;
import org.bell.framework.BellControl;
import org.bell.framework.IntegerRangeValidator;
import org.bell.framework.TimeHHMMValidator;
import org.bell.framework.TimeToStringConverter;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType;

public class MainController implements Initializable {

    @FXML
    public ComboBox<SchoolDay> cbDayList;
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
    public ListView<BellTime> lvTimes;
    @FXML
    public ComboBox<SchoolDay> cbDayList2;
    @FXML
    public Button btnStart;
    @FXML
    public Button btnStop;
    BellControl bellControl;
    @FXML
    private Button btnClick;
    private SchoolBellDao dao = null;
    private ValidationSupport validationSupport;
    private List<SchoolDay> schoolDaysComputed;
    private List<SchoolDay> schoolDaysNotComputed;
    private JobDetail job;
    private Scheduler scheduler;
    private SimpleTrigger trigger;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DropShadow shadow = new DropShadow();
        validationSupport = new ValidationSupport();
        //bellControl = new BellControl();
        btnStop.setDisable(true);

        dao = new SchoolBellDao();
        setCbDataSources();
        buttonDropShadowEffect(shadow);

        tabMain.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    cbDayList2.getSelectionModel().clearSelection();
                    cbDayList.getSelectionModel().clearSelection();
                    lvTimes.getItems().clear();
                    setCbDataSources();
                });

        cbDayList2.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    ObservableList<BellTime> sds = null;
                    if (newValue != null) {
                        List<BellTime> bellTimes = newValue.getBellTimes();
                        sds = FXCollections.observableArrayList(bellTimes);
                        lvTimes.setItems(sds);
                    }
                });

        setFormatter();
        setValidations();
    }

    public void btnClicked(Event event) {
        if (!validationSupport.isInvalid()) {
            DailyBellCalculator dailyBellCalculator = setCalculator();
            SchoolDay selectedItem = cbDayList.getSelectionModel().getSelectedItem();
            selectedItem.getBellTimes().addAll(dailyBellCalculator.calculateBellTime());
            selectedItem.getBellTimes().forEach(bellTime -> bellTime.setSchoolDay(selectedItem));
            dao.save(selectedItem);
            setCbDataSources();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("bell1.png")));
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
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    private void setCbDataSources() {
        schoolDaysNotComputed = dao.getSchoolDaysNotComputed();
        schoolDaysComputed = dao.getSchoolDays();

        if (schoolDaysNotComputed != null && !schoolDaysNotComputed.isEmpty()) {
            cbDayList.setItems(FXCollections.observableArrayList(schoolDaysNotComputed));
        } else {
            cbDayList.setItems(FXCollections.observableArrayList());
        }
        if (schoolDaysComputed != null && !schoolDaysComputed.isEmpty()) {
            cbDayList2.setItems(FXCollections.observableArrayList(schoolDaysComputed));
        } else {
            cbDayList2.setItems(FXCollections.observableArrayList());
        }
    }

    private void setFormatter() {
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
        Alert alert = new Alert(AlertType.INFORMATION);
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
        SchoolDay selected = cbDayList2.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Dikkat");
            alert.setHeaderText("Silme uyarısı");
            alert.setContentText(selected.getDayName() + " gününe ait zil tanımlamamaları silinecek onaylıyor musunuz?");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
                if (selected != null) {
                    dao.clearSchoolDaysBellTime(selected);
                    lvTimes.getItems().clear();
                    setCbDataSources();
                }
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Dikkat");
            alert.setHeaderText("Gün seçiniz");
            alert.setContentText("Simek için önce bir gün seçimi yapınız");
            alert.show();
        }
    }

    public void selectMp3(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Bir mp3 seçiniz");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio Files", "*.mp3"));
        File file = fileChooser.showOpenDialog(gridPane.getScene().getWindow());
//        file.getPath().replace("\\", "/");
        try {
            if (file != null) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                generateFile(FileNameConstants.MP3_FILE_NAME, bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnStartClicked(Event event) throws SchedulerException, IOException, JavaLayerException {
        if (Files.exists(Paths.get(FileNameConstants.MP3_FILE_NAME))) {

            
            btnStart.setDisable(true);
            btnStop.setDisable(false);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Dikkat");
            alert.setHeaderText("Mp3 Seçimi");
            alert.setContentText("Zil için gereken Mp3 dosyasını seçiniz");
            alert.show();
        }
    }

    public void btnStopClicked(Event event) throws SchedulerException {
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        bellControl.stop();
        //SchedulerUtil.pause();
    }
}


