module com.example.bpower2notifications {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.prefs;
    requires java.net.http;
    requires org.apache.commons.codec;
    requires java.desktop;
    requires com.google.gson;

    opens com.example.bpower2notifications to javafx.fxml;
    exports com.example.bpower2notifications;
}