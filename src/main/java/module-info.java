module com.mycompany.assignment2_youssefnagih {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.assignment2_youssefnagih to javafx.fxml;
    exports com.mycompany.assignment2_youssefnagih;
}
