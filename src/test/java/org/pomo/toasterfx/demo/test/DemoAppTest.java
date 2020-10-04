package org.pomo.toasterfx.demo.test;

import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.pomo.toasterfx.demo.DemoApplication;
import org.testfx.framework.junit.ApplicationTest;

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
        this.application.start(stage);
    }

    @Override
    public void stop() {
        this.application.stop();
    }

    @Test
    public void doBtnSuccess() {
        clickOn("#btnSuccess");
    }

    @Test
    public void doBtnFail() {
        clickOn("#btnFail");
    }
}
