/*
 * Copyright © 2020 Mr.Po (ldd_live@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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