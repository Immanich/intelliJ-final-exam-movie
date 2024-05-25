module com.javafx.final_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.javafx.final_project to javafx.fxml;
    exports com.javafx.final_project;
}