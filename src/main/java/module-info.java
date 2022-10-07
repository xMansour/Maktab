module maktab {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;
    exports com.mansourappdevelopment.maktab;
    opens com.mansourappdevelopment.maktab to javafx.fxml;
}