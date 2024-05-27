module module_name {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.base;
    requires java.desktop;

    opens pl.github.korbecki.krzysciak to javafx.graphics;
}