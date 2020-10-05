import org.pomo.toasterfx.demo.ToasterFXMessageProvider;
import org.pomo.toasterfx.util.FXMessageProvider;

module org.pomo.toasterfx.demo {

    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires java.desktop;
    requires org.pomo.toasterfx;
    requires org.pomo.toasterfx.resource.audio;
    requires org.slf4j;

    opens org.pomo.toasterfx.demo to javafx.graphics;
    opens org.pomo.toasterfx.demo.controller to javafx.fxml;

    opens org.pomo.toasterfx.demo.image;

    opens org.pomo.toasterfx.demo.language;
    opens org.pomo.toasterfx.demo.extend.language;

    opens org.pomo.toasterfx.demo.types;
    opens org.pomo.toasterfx.demo.types.dark;

    opens org.pomo.toasterfx.demo.fxml;
    opens org.pomo.toasterfx.demo.fxml.dark;

    provides FXMessageProvider with ToasterFXMessageProvider;
}