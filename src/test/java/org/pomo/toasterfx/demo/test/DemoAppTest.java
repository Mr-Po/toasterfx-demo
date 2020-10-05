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
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Throwables;
import org.junit.Assert;
import org.junit.Test;
import org.pomo.toasterfx.demo.DemoApplication;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

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
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.printException = false;

        this.application = new DemoApplication();
    }

    @Override
    public void start(Stage stage) {
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
    public void execute() {

        Set<Node> nodes = lookup(".button").queryAll();
        nodes.forEach(this::click);

        this.click(lookup("#radioThemeDark").query());
        this.click(lookup("#radioRightTop").query());

        ChoiceBox<Locale> cbxLanguage = lookup("#cbxLanguage").query();

        this.click(cbxLanguage);
        key(KeyCode.ENTER);
        this.handleException(cbxLanguage);

        this.click(cbxLanguage);
        key(KeyCode.DOWN);
        key(KeyCode.ENTER);
        this.handleException(cbxLanguage);

        this.click(cbxLanguage);
        key(KeyCode.DOWN);
        key(KeyCode.ENTER);
        this.handleException(cbxLanguage);

        nodes.forEach(this::click);
    }

    /**
     * <h2>点击</h2>
     *
     * @param node 节点
     */
    private void click(Node node) {

        this.clickOn(node);
        this.handleException(node);
    }

    /**
     * <h2>处理异常</h2>
     *
     * @param node 节点
     */
    private void handleException(Node node) {

        try {

            WaitForAsyncUtils.checkException();

        } catch (Throwable throwable) {

            Throwable rootCause = Throwables.getRootCause(throwable);

            // 排除音频无法播放的
            if (!rootCause.getClass().isAssignableFrom(MediaException.class)
                    && !"Could not create player!".equals(rootCause.getMessage())) {

                log.error("handle: " + node, rootCause);

                Assert.fail(Throwables.getRootCause(throwable).getMessage());
            }
        }
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