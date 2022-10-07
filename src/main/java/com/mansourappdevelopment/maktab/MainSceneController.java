package com.mansourappdevelopment.maktab;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.controlsfx.control.textfield.TextFields;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class MainSceneController implements Initializable {


    @FXML
    private Button btnPrint;
    @FXML
    private Button btnIncMok;
    @FXML
    private Button btnDecMok;
    @FXML
    private Button btnDeleteData;

    @FXML
    private TextField txtMok;
    @FXML
    private TextField txtProj;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtProjName;
    @FXML
    private TextField txtCompanyName;
    @FXML
    private TextField txtValue;
    @FXML
    private TextField txtProjYear;


    @FXML
    private ComboBox<String> cbEshtratat;
    @FXML
    private ComboBox<String> cbEshtratatType;
    @FXML
    private ComboBox<String> cbKorasaType;

    @FXML
    private DatePicker datePicker;
    @FXML
    private Label txtStatus;
    @FXML
    private TextField txtMokawelName;

    private String dayOfWeek;
    private String dayOfMonth;
    private String month;
    private String year;

    private String mok;
    private String proj;
    private String location;
    private String projName;
    private String companyName;
    private String mokawelName;
    private String value;
    private String projYear;

    private String korasaType;
    private String eshtratatType;
    private String eshtratat;

    private SortedSet<String> companies;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkForUpdates();

        try {
            //companies = Utils.readFileUsingFileReader("src/main/resources/com/mansourappdevelopment/maktab/data/SharekatNames.txt");
            companies = Utils.readFileUsingFileReader("Data/SharekatNames.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextFields.bindAutoCompletion(txtCompanyName, companies);
        addComboBoxesItems();

        txtMok.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtMok.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        txtProj.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtProj.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        txtValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^-?\\d*\\.?\\d*$")) {
                txtValue.setText(oldValue);
            }
        });
        txtProjYear.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtProjYear.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        //Increase Mokaysa Number
        btnIncMok.setOnAction(event -> {
            String mokNumberStr = txtMok.getText();
            if (!mokNumberStr.equals("")) {
                int mokNumber = Integer.parseInt(mokNumberStr);
                txtMok.setText(String.valueOf(++mokNumber));
            }
        });

        //Decrease Mokaysa Number
        btnDecMok.setOnAction(event -> {
            String mokNumberStr = txtMok.getText();
            if (!mokNumberStr.equals("")) {
                int mokNumber = Integer.parseInt(mokNumberStr);
                txtMok.setText(String.valueOf(--mokNumber));
            }
        });


        btnDeleteData.setOnAction(event -> deleteValues());


        btnPrint.setOnAction(event -> {
            if (validData()) {
                getTextFieldValues();
                getComboBoxesValues();
                getDate();
                prepareKorasa();
                try {
                    printKorasa();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validData() {
        if (txtMok.getText().equals("")) {
            txtStatus.setText("من فضلك ادخل رقم المقايسه");
            txtStatus.getStyleClass().add("label-error");
            return false;
        } else if (txtProj.getText().equals("")) {
            txtStatus.setText("من فضلك ادخل رقم المشروع");
            txtStatus.getStyleClass().add("label-error");
            return false;
        } else if (txtValue.getText().equals("")) {
            txtStatus.setText("من فضلك ادخل قيمة المقايسه");
            txtStatus.getStyleClass().add("label-error");
            return false;
        } else if (txtProjName.getText().equals("")) {
            txtStatus.setText("من فضلك ادخل اسم المشروع");
            txtStatus.getStyleClass().add("label-error");
            return false;
        } else if (txtLocation.getText().equals("")) {
            txtStatus.setText("من فضلك ادخل جهة تنفيذ المشروع");
            txtStatus.getStyleClass().add("label-error");
            return false;
        } else if (txtCompanyName.getText().equals("")) {
            txtStatus.setText("من فضلك ادخل اسم الشركه");
            txtStatus.getStyleClass().add("label-error");
            return false;
        } else if (txtProjYear.getText().equals("")) {
            txtStatus.setText("من فضلك ادخل سنة المشروع");
            txtStatus.getStyleClass().add("label-error");
            return false;
        } else {
            txtStatus.setText("");
            txtStatus.getStyleClass().add("label-correct");
            return true;
        }
    }

    private void getComboBoxesValues() {
        korasaType = cbKorasaType.getValue();
        eshtratatType = cbEshtratatType.getValue();
        eshtratat = cbEshtratat.getValue();
    }

    private void getTextFieldValues() {
        mok = txtMok.getText();
        proj = txtProj.getText();
        location = txtLocation.getText();
        projName = txtProjName.getText();
        companyName = txtCompanyName.getText();
        value = txtValue.getText();
        projYear = txtProjYear.getText();
        mokawelName = txtMokawelName.getText();
    }

    private void getDate() {
        try {
            dayOfWeek = Utils.getDaysNamesInArabic(datePicker.getValue().getDayOfWeek().getValue());
            month = Utils.getArabicNumbers(String.valueOf(datePicker.getValue().getMonth().getValue()));
            dayOfMonth = Utils.getArabicNumbers(String.valueOf(datePicker.getValue().getDayOfMonth()));
            year = Utils.getArabicNumbers(String.valueOf(datePicker.getValue().getYear()));
            txtStatus.setText("");
        } catch (Exception e) {
            txtStatus.setText("من فضلك ادخل التاريخ");
            txtStatus.getStyleClass().add("label-error");
        }
    }

    private void deleteValues() {
        txtMok.setText("");
        txtProj.setText("");
        txtValue.setText("");
        txtCompanyName.setText("");
        txtMokawelName.setText("");
        txtLocation.setText("");
        txtProjName.setText("");
        txtProjYear.setText("");
        datePicker.setValue(null);
        datePicker.getEditor().setText("");
        datePicker.setPromptText("اختر التاريخ");
    }

    private void addComboBoxesItems() {
        cbKorasaType.getItems().addAll("أعمال", "مطارات");
        cbEshtratatType.getItems().addAll("شاملة الضريبه", "غير شاملة الضريبه");
        cbEshtratat.getItems().addAll("سنه معدات", "سنه بدون معدات",
                "سنه خصم اسمنت بمعدات", "سنه خصم اسمنت بدون معدات", "ابتدائى نهائى", "بيتومين");
    }

    private void prepareKorasa() {
        XWPFDocument korasaDoc = null;
        XWPFDocument eshtratatDoc = null;
        try {
            if (korasaType.equals("أعمال")) {
                setKorasaData("Data/KorasetA3mal.docx");
                korasaDoc = new XWPFDocument(new FileInputStream("Data/KorasetA3mal.docx"));
                eshtratatDoc = switch (eshtratat) {
                    case "سنه معدات" -> eshtratatDoc = new XWPFDocument(new FileInputStream("Data/A3mal/sanaMo3dat.docx"));
                    case "سنه بدون معدات" -> eshtratatDoc = new XWPFDocument(new FileInputStream("Data/A3mal/sanaWithoutMo3dat.docx"));
                    case "سنه خصم اسمنت بمعدات" -> eshtratatDoc = new XWPFDocument(new FileInputStream("Data/A3mal/sana5asmAsmntMo3dat.docx"));
                    case "سنه خصم اسمنت بدون معدات" -> eshtratatDoc = new XWPFDocument(new FileInputStream("Data/A3mal/sana5asmAsmntWithoutMo3dat.docx"));
                    case "ابتدائى نهائى" -> eshtratatDoc = new XWPFDocument(new FileInputStream("Data/A3mal/ebtdaieNhaie.docx"));
                    case "بيتومين" -> eshtratatDoc = new XWPFDocument(new FileInputStream("Data/A3mal/betomen.docx"));
                    default -> null;
                };
            } else if (korasaType.equals("مطارات")) {
                setKorasaData("Data/KorasetMatarat.docx");
                korasaDoc = new XWPFDocument(new FileInputStream("Data/KorasetMatarat.docx"));
                eshtratatDoc = switch (eshtratat) {
                    case "سنه معدات" -> new XWPFDocument(new FileInputStream("Data/Matarat/sanaMo3dat.docx"));
                    case "سنه بدون معدات" -> new XWPFDocument(new FileInputStream("Data/Matarat/sanaWithoutMo3dat.docx"));
                    case "سنه خصم اسمنت بمعدات" -> new XWPFDocument(new FileInputStream("Data/Matarat/sana5asmAsmntMo3dat.docx"));
                    case "سنه خصم اسمنت بدون معدات" -> new XWPFDocument(new FileInputStream("Data/Matarat/sana5asmAsmntWithoutMo3dat.docx"));
                    case "ابتدائى نهائى" -> new XWPFDocument(new FileInputStream("Data/Matarat/ebtdaieNhaie.docx"));
                    case "بيتومين" -> new XWPFDocument(new FileInputStream("Data/Matarat/betomen.docx"));
                    default -> null;
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (XWPFParagraph p : korasaDoc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains("MOK_NUMBER")) {
                        text = text.replace("MOK_NUMBER", mok);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("PROJ_NUMBER")) {
                        text = text.replace("PROJ_NUMBER", proj);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("PROJ_YEAR")) {
                        text = text.replace("PROJ_YEAR", projYear);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("PROJ_NAME")) {
                        text = text.replace("PROJ_NAME", projName);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("VALUE")) {
                        text = text.replace("VALUE", value);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("COMPANY_NAME")) {
                        text = text.replace("COMPANY_NAME", companyName);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("DAY_OF_WEEK")) {
                        text = text.replace("DAY_OF_WEEK", dayOfWeek);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("DAY_OF_MONTH")) {
                        text = text.replace("DAY_OF_MONTH", dayOfMonth);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("MONTH")) {
                        text = text.replace("MONTH", month);
                        r.setText(text, 0);
                    }
                }
            }
        }


        for (XWPFParagraph p : eshtratatDoc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains("MOK_NUMBER")) {
                        text = text.replace("MOK_NUMBER", mok);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("PROJ_NUMBER")) {
                        text = text.replace("PROJ_NUMBER", proj);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("PROJ_YEAR")) {
                        text = text.replace("PROJ_YEAR", projYear);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("PROJ_NAME")) {
                        text = text.replace("PROJ_NAME", projName);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("PROJ_LOCATION")) {
                        text = text.replace("PROJ_LOCATION", location);
                        r.setText(text, 0);
                    } else if (text != null && text.contains("ESHTRATAT_TYPE")) {
                        text = text.replace("ESHTRATAT_TYPE", eshtratatType);
                        r.setText(text, 0);
                    }
                }
            }
        }

        try (FileOutputStream korasa = new FileOutputStream("Data/Output/KorasaOutput.docx");
             FileOutputStream eshtratat = new FileOutputStream("Data/Output/EshtratatOutput.docx")) {
            korasaDoc.write(korasa);
            eshtratatDoc.write(eshtratat);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setKorasaData(String path) {
        OPCPackage opcPackage = null;
        try {
            opcPackage = OPCPackage.open(path);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        PackageProperties packageProperties = null;
        try {
            packageProperties = opcPackage.getPackageProperties();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        packageProperties.setTitleProperty(companyName);
        packageProperties.setCategoryProperty(year);
        if (!mokawelName.equals(""))
            packageProperties.setSubjectProperty("  -  " + mokawelName);
        else
            packageProperties.setSubjectProperty(" ");

        try {
            opcPackage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printKorasa() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.print(new File("Data/Output/KorasaOutput.docx"));
        desktop.print(new File("Data/Output/EshtratatOutput.docx"));
    }

    private void checkForUpdates(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date updateDate = dateFormat.parse("09/10/2022");
            Date today = new Date();
            long diff = updateDate.getTime() - today.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            if (days <= 0) {
                btnPrint.setDisable(true);
                btnIncMok.setDisable(true);
                btnDecMok.setDisable(true);
                btnDeleteData.setDisable(true);
                txtMok.setDisable(true);
                txtProj.setDisable(true);
                txtLocation.setDisable(true);
                txtProjName.setDisable(true);
                txtCompanyName.setDisable(true);
                txtValue.setDisable(true);
                txtProjYear.setDisable(true);
                cbEshtratat.setDisable(true);
                cbEshtratatType.setDisable(true);
                cbKorasaType.setDisable(true);
                datePicker.setDisable(true);
                txtMokawelName.setDisable(true);
                txtStatus.setText("يوجد تحديث جديد، برجاء الاتصال بمطور البرنامج.");
            }
            System.out.println(days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}




