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
package org.pomo.toasterfx.demo.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * <h2>进度消息控制器</h2>
 *
 * <p>用于进度消息控制</p>
 * <br/>
 *
 * <p>创建时间：2020-09-28 08:59:21</p>
 * <p>更新时间：2020-09-28 08:59:21</p>
 *
 * @author Mr.Po
 * @version 1.0
 */
@Slf4j
public class ProgressToastController {

    @FXML
    private Label title;

    @FXML
    private ProgressBar progress;

    @FXML
    private Button button;

    private Consumer<ActionEvent> onAction;
    private BooleanBinding progressBarDisableProperty;

    /**
     * <h2>绑定</h2>
     *
     * @param titleProperty      标题属性
     * @param progressProperty   进度属性
     * @param buttonTextProperty 按钮文本属性
     * @param stateProperty      状态属性
     * @param onAction           执行操作
     */
    public void bind(@NonNull ReadOnlyStringProperty titleProperty,
                     @NonNull ReadOnlyDoubleProperty progressProperty,
                     @NonNull ReadOnlyStringProperty buttonTextProperty,
                     @NonNull ReadOnlyObjectProperty<Worker.State> stateProperty,
                     @NonNull Consumer<ActionEvent> onAction) {

        this.onAction = onAction;

        this.title.textProperty().bind(titleProperty);
        this.progress.progressProperty().bind(progressProperty);
        this.button.textProperty().bind(buttonTextProperty);

        this.progressBarDisableProperty = Bindings.createBooleanBinding(() -> {

            boolean flag;
            switch (stateProperty.get()) {
                case FAILED:
                case CANCELLED:
                    flag = true;
                    break;
                default:
                    flag = false;
            }
            return flag;
        }, stateProperty);

        this.progress.disableProperty().bind(this.progressBarDisableProperty);

        log.debug("ProgressToastController is binding.");
    }

    /**
     * <h2>解除绑定</h2>
     */
    public void unBind() {

        this.onAction = null;

        this.progress.disableProperty().unbind();

        this.progressBarDisableProperty.dispose();
        this.progressBarDisableProperty = null;

        this.title.textProperty().unbind();
        this.progress.progressProperty().unbind();
        this.button.textProperty().unbind();

        log.debug("ProgressToastController is unbinding.");
    }

    @FXML
    private void onAction(ActionEvent event) {
        this.onAction.accept(event);
    }
}
