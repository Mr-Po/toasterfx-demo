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
package org.pomo.toasterfx.demo.task;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pomo.toasterfx.model.Toast;
import org.pomo.toasterfx.util.FXMessages;

import java.util.concurrent.TimeUnit;

/**
 * <h2>进度任务</h2>
 *
 * <p>进度任务具体执行的过程</p>
 * <br/>
 *
 * <p>创建时间：2020-09-28 09:00:59</p>
 * <p>更新时间：2020-09-28 09:00:59</p>
 *
 * @author Mr.Po
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class ProgressTask extends Task<Void> {

    private boolean pause;

    @NonNull
    private final FXMessages messages;

    private final StringProperty buttonTextProperty = new SimpleStringProperty();

    public ReadOnlyStringProperty buttonTextProperty() {
        return buttonTextProperty;
    }

    @Override
    protected void scheduled() {

        messages.bindProperty(this,
                ((StringProperty) this.titleProperty()), () -> {

                    String title;
                    switch (this.getState()) {
                        case CANCELLED:
                            title = messages.get("demo.toastTitleCanceled");
                            break;
                        case SUCCEEDED:
                            title = messages.get("demo.toastTitleSucceed");
                            break;
                        default:
                            title = messages.get("demo.toastTitleExecuting");
                    }

                    return title;

                }, this.stateProperty());

        messages.bindProperty(this, buttonTextProperty, () -> {

            String text;
            switch (this.getState()) {
                case SUCCEEDED:
                case CANCELLED:
                case FAILED:
                    text = messages.get("demo.btnOK");
                    break;
                default:
                    text = messages.get("demo.btnCancel");
            }

            return text;

        }, this.stateProperty());
    }

    @Override
    protected void cancelled() {
        log.debug("Task of Progress is cancelled.");
    }

    @Override
    protected Void call() throws Exception {

        for (int i = 1; i <= 100; ) {

            TimeUnit.MILLISECONDS.sleep(100);

            // 处于暂停中
            if (this.pause) continue;

            // 底层就是调用的Platform.runLater()
            // 不过其内部有一个判定，保证其会等待上一个run被执行后，才执行下一个
            // 所以，会丢失一部分update
            this.updateProgress(i, 100);

            i++;
        }

        log.debug("--->：ProgressToast，执行完毕！");

        return null;
    }

    /**
     * <h2>暂停</h2>
     */
    public void pause() {
        this.pause = true;
    }

    /**
     * <h2>开始</h2>
     */
    public void start() {
        this.pause = false;
    }

    /**
     * <h2>销毁</h2>
     */
    public void destroy() {
        messages.disposeBinging(this);
    }

    /**
     * <h2>执行</h2>
     *
     * @param toast 消息体
     */
    public void action(Toast toast) {

        switch (this.getState()) {
            case SUCCEEDED:
            case CANCELLED:
            case FAILED:
                toast.close();
                break;
            default:
                this.cancel();
        }
    }
}
