module com.fxexample.rentalcarsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.fxexample.rentalcarsystem to javafx.fxml;
    exports com.fxexample.rentalcarsystem;
}