module cr.ac.una.alquilervehiculos {
    requires javafx.controls;
    requires javafx.fxml;


    opens cr.ac.una.alquilervehiculos to javafx.fxml;
    exports cr.ac.una.alquilervehiculos;
}