module baller.example.sexyvanialoanclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.mariadb.jdbc;

    opens baller.example.sexyvanialoanclient to javafx.fxml;
    exports baller.example.sexyvanialoanclient;
}