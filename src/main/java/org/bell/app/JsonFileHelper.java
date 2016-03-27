//package org.bell.app;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//import org.bell.entity.FileNameConstants;
//import org.bell.entity.SchoolDay;
//import org.bell.framework.LocalTimeConverter;
//
//import java.io.*;
//import java.lang.reflect.Type;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//
//public class JsonFileHelper {
//    private static final Type LOCAL_TIME_TYPE = new TypeToken<LocalTime>() {
//    }.getType();
//
//    public  static ArrayList<SchoolDay> getSchoolDays(){
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.registerTypeAdapter(LOCAL_TIME_TYPE, new LocalTimeConverter())
//                .serializeNulls().create();
//
//        ArrayList<SchoolDay> readedSchoolDays = null;
//        File f = new File(FileNameConstants.JSON_FILE_NAME);
//        if (!(f.exists() && !f.isDirectory())) {
////            Alert alert = new Alert(Alert.AlertType.WARNING);
////            alert.setTitle("Dikkat");
////            alert.setHeaderText("Zil dosyası yok");
////            alert.setContentText("Lütfen bütün alanların doğru girildiğine emin olunuz.");
////            alert.show();
//            System.out.println("Zil dosyası yok!");
//        } else {
//            try {
//                FileInputStream fis = new FileInputStream(FileNameConstants.JSON_FILE_NAME);
//                InputStreamReader isr = new InputStreamReader(fis);
//                BufferedReader bufferedReader = new BufferedReader(isr);
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    sb.append(line);
//                }
//                String json = sb.toString();
//                Type t = new TypeToken<ArrayList<SchoolDay>>(){}.getType();
//                readedSchoolDays = gson.fromJson(json, t);
//                if (readedSchoolDays != null) {
//                    for (SchoolDay sd :
//                            readedSchoolDays) {
//                        System.out.println(sd.toString());
//                    }
//                }
//                System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return readedSchoolDays;
//    }
//}