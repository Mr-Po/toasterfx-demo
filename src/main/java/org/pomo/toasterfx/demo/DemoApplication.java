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
package org.pomo.toasterfx.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pomo.toasterfx.ToastBarToasterService;
import org.pomo.toasterfx.demo.controller.DemoController;
import org.pomo.toasterfx.model.ToastParameter;
import org.pomo.toasterfx.resource.audio.RandomBubbleAudio;
import org.pomo.toasterfx.util.FXMessages;
import org.pomo.toasterfx.util.FXUtils;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * <h2>演示程序</h2>
 *
 * <p>java9+：--add-opens ch.qos.logback.classic/ch.qos.logback.classic.model.processor=ch.qos.logback.core</p>
 * <br/>
 *
 * <p>创建时间：2020-09-27 19:26:10</p>
 * <p>更新时间：2020-09-27 19:26:10</p>
 *
 * @author Mr.Po
 * @version 1.0
 */
@Slf4j
public class DemoApplication extends Application {

    /**
     * 支持的地区
     */
    @Getter
    private final Locale[] supportLocales = new Locale[]{Locale.ENGLISH, Locale.CHINA, Locale.JAPAN};

    /**
     * 默认的持续时间
     */
    @Getter
    private final Duration defaultDuration = Duration.seconds(5);

    /**
     * 国际化基础包名
     */
    @Getter
    private final String basename = "org.pomo.toasterfx.demo.language.message";

    /**
     * 样例控制器
     */
    @Getter
    private DemoController controller;

    /**
     * 消息条-消息体服务
     */
    @Getter
    private ToastBarToasterService service;

    @Override
    public void start(Stage primaryStage) {

        ReadOnlyObjectWrapper<Locale> localeWrapper = this.createLocaleWrapper();

        this.service = this.createToastBarToasterService(localeWrapper);
        this.service.initialize();

        this.service.getMessages().bindProperty(this, primaryStage.titleProperty(), "demo.title");

        URL url = DemoApplication.class.getResource("/org/pomo/toasterfx/demo/fxml/Demo.fxml");

        Map.Entry<Parent, DemoController> entry = FXUtils.load(url);
        Parent parent = entry.getKey();

        this.controller = entry.getValue();
        this.controller.setLocaleProperty(localeWrapper);
        this.controller.setPrimaryStage(primaryStage);
        this.controller.setSupportLocale(this.getSupportLocales());
        this.controller.setService(this.service);
        this.controller.setRandomBubbleAudio(RandomBubbleAudio.DEFAULT);
        this.controller.setDefaultDuration(this.getDefaultDuration());
        this.controller.init();

        primaryStage.setOnCloseRequest(it -> Platform.exit());

        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);

        Image image = new Image("/org/pomo/toasterfx/demo/image/icon.png");
        primaryStage.getIcons().add(image);

        primaryStage.show();

        // 非无头环境执行
        if (!GraphicsEnvironment.isHeadless()) {
            SplashScreen splashScreen = SplashScreen.getSplashScreen();
            if (splashScreen != null) splashScreen.close();
        }
    }

    @Override
    public void stop() {

        this.controller.destroy();

        this.service.getMessages().disposeBinging(this);

        log.debug("DemoApplication is destroyed.");

        this.service.destroy();
    }

    /**
     * <h2>创建 消息条-消息体服务</h2>
     *
     * @param localeWrapper 地区Wrapper
     * @return 消息条-消息体服务
     */
    protected ToastBarToasterService createToastBarToasterService(ReadOnlyObjectWrapper<Locale> localeWrapper) {

        FXMessages messages = new FXMessages();
        messages.setLocaleProperty(localeWrapper.getReadOnlyProperty());
        messages.getBaseNames().add(this.getBasename());

        ToastParameter randomAudioParameter = ToastParameter.builder()
                .audio(RandomBubbleAudio.DEFAULT)
                .timeout(this.getDefaultDuration())
                .build();

        ToastBarToasterService service = new ToastBarToasterService(messages);
        service.setDefaultToastParameter(randomAudioParameter);

        return service;
    }

    /**
     * <h2>创建 地区Wrapper</h2>
     *
     * @return 地区Wrapper
     */
    private ReadOnlyObjectWrapper<Locale> createLocaleWrapper() {

        ReadOnlyObjectWrapper<Locale> localeWrapper = new ReadOnlyObjectWrapper<>();
        localeWrapper.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));

        Locale defaultLocale = Locale.getDefault();

        Locale[] supportLocales = this.getSupportLocales();

        Locale locale = Arrays.stream(supportLocales)
                .filter(it -> it.equals(defaultLocale))
                .findAny()
                .orElse(supportLocales[0]);

        localeWrapper.set(locale);

        return localeWrapper;
    }

    public static void main(String[] args) {
        Application.launch(DemoApplication.class);
    }
}
