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
package org.pomo.toasterfx.demo.test;

import com.sun.media.jfxmedia.MediaException;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Throwables;
import org.junit.Test;
import org.pomo.toasterfx.demo.DemoApplication;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Locale;
import java.util.Set;

/**
 * <h2>样例程序测试</h2>
 *
 * <p>用于远端测试</p>
 * <br/>
 *
 * <p>创建时间：2020-10-02 10:58:23</p>
 * <p>更新时间：2020-10-02 10:58:23</p>
 *
 * @author Mr.Po
 * @version 1.0
 */
@Slf4j
public class DemoAppTest extends ApplicationTest {

    private DemoApplication application;

    @Override
    public void init() {
        this.application = new DemoApplication();
    }

    @Override
    public void start(Stage stage) {

        Thread javafxThread = Thread.currentThread();

        final Thread.UncaughtExceptionHandler handler
                = javafxThread.getUncaughtExceptionHandler();

        if (handler != null) {

            javafxThread.setUncaughtExceptionHandler((t, e) -> {

                Throwable rootCause = Throwables.getRootCause(e);

                if (!rootCause.getClass().isAssignableFrom(MediaException.class)
                        || !"Could not create player!".equals(rootCause.getMessage())) {

                    handler.uncaughtException(t, e);

                } else log.debug(rootCause.getMessage());
            });
        }

        this.application.start(stage);
    }

    @Override
    public void stop() {
        this.application.stop();
    }

    /**
     * <h2>点击全部按钮</h2>
     */
    @Test
    @SneakyThrows
    public void execute() {

        Set<Node> nodes = lookup(".button").queryAll();
        nodes.forEach(this::clickOn);

        clickOn("#radioThemeDark");
        clickOn("#radioRightTop");

        ChoiceBox<Locale> cbxLanguage = lookup("#cbxLanguage").query();

        clickOn(cbxLanguage);
        key(KeyCode.ENTER);

        clickOn(cbxLanguage);
        key(KeyCode.DOWN);
        key(KeyCode.ENTER);

        clickOn(cbxLanguage);
        key(KeyCode.DOWN);
        key(KeyCode.ENTER);

        nodes.forEach(this::clickOn);
    }

    /**
     * <h2>模拟键盘点击</h2>
     *
     * @param code 键位
     */
    private void key(KeyCode code) {
        press(code);
        release(code);
    }
}
