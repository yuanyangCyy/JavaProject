module com.fxexample.rentalcarsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.fxexample.rentalcarsystem to javafx.fxml;
    exports com.fxexample.rentalcarsystem;
}