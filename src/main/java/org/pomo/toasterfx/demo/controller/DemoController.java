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

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pomo.toasterfx.MultiToastFactory;
import org.pomo.toasterfx.ToastBarToasterService;
import org.pomo.toasterfx.ToasterFactory;
import org.pomo.toasterfx.component.SimpleListToastSupplier;
import org.pomo.toasterfx.control.impl.ToastBar;
import org.pomo.toasterfx.controller.TableViewListToastController;
import org.pomo.toasterfx.demo.task.ProgressTask;
import org.pomo.toasterfx.model.ReferenceType;
import org.pomo.toasterfx.model.ToastParameter;
import org.pomo.toasterfx.model.ToastType;
import org.pomo.toasterfx.model.impl.RandomAudio;
import org.pomo.toasterfx.model.impl.SingleAudio;
import org.pomo.toasterfx.model.impl.SingleToast;
import org.pomo.toasterfx.model.impl.ToastTypes;
import org.pomo.toasterfx.strategy.PopupStrategy;
import org.pomo.toasterfx.strategy.impl.RightTopPopupStrategy;
import org.pomo.toasterfx.transition.ToasterTransition;
import org.pomo.toasterfx.transition.impl.RightTopToasterTransitionIn;
import org.pomo.toasterfx.util.FXMessages;
import org.pomo.toasterfx.util.FXUtils;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <h2>Demo 控制器</h2>
 *
 * <p></p>
 * <br/>
 *
 * <p>创建时间：2020-09-27 19:26:51</p>
 * <p>更新时间：2020-09-27 19:26:51</p>
 *
 * @author Mr.Po
 * @version 1.0
 */
@Slf4j
public class DemoController {

    // region {FXML属性}
    @FXML
    private Label labLanguage;

    @FXML
    private Label labOperate;

    @FXML
    private ChoiceBox<Locale> cbxLanguage;

    @FXML
    private Label labPopup;

    @FXML
    private Label labTheme;

    @FXML
    private RadioButton radioThemeDefault;

    @FXML
    private ToggleGroup radioTheme;

    @FXML
    private RadioButton radioThemeDark;

    @FXML
    private ToggleGroup radioPopup;

    @FXML
    private RadioButton radioRightTop;

    @FXML
    private RadioButton radioTightBottom;

    @FXML
    private Button btnSuccess;

    @FXML
    private Button btnFail;

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnLong;

    @FXML
    private Button btnEvent;

    @FXML
    private Button btnBatch;

    @FXML
    private Button btnNoAudio;

    @FXML
    private Button btnCustomFXML;

    @FXML
    private Button btnCustomType;

    @FXML
    private Button btnCustomAudio;

    @FXML
    private Button btnNoClose;

    @FXML
    private Button btnNoProgress;

    @FXML
    private Button btnWarn;

    @FXML
    private Button btnNoTitle;

    @FXML
    private Button btnConcurrent;
    // endregion

    // region {成员组件}
    @NonNull
    private FXMessages messages;

    @Setter
    @NonNull
    private ToastBarToasterService service;
    // endregion

    // region {成员属性}
    @Setter
    @NonNull
    private Stage primaryStage;

    /**
     * 地区Property
     */
    @Setter
    @NonNull
    private ObjectProperty<Locale> localeProperty;

    /**
     * 支持的地区
     */
    @Setter
    @NonNull
    private Locale[] supportLocale;

    /**
     * 批量弹窗角标
     */
    private int batchIndex = 1;

    /**
     * 并发弹窗角标
     */
    private int concurrentIndex = 1;

    private InvalidationListener popupInvalidationListener;
    private InvalidationListener themeInvalidationListener;

    private final ToastType customToastType = new ToastType() {

        private final String[] styleClass = new String[]{"toast-type-custom"};

        @Override
        public String[] getStyleClass() {
            return this.styleClass;
        }

        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public String getName() {
            return "demo.toastTypeCustom";
        }
    };

    /**
     * 随机气泡音
     */
    @Setter
    private RandomAudio randomBubbleAudio;

    /**
     * 默认的消息存在时间
     */
    @Setter
    private Duration defaultDuration;

    private ToastParameter noAudioParameter;

    private ToastParameter customAudioParameter;

    public ToastParameter randomAudioIndefiniteParameter;
    // endregion

    /**
     * <h2>初始化</h2>
     */
    public void init() {

        Objects.requireNonNull(localeProperty);
        Objects.requireNonNull(primaryStage);
        Objects.requireNonNull(supportLocale);
        Objects.requireNonNull(service);
        Objects.requireNonNull(randomBubbleAudio);
        Objects.requireNonNull(defaultDuration);

        this.messages = this.service.getMessages();

        // region {语言下拉列表}
        ObservableList<Locale> languages = FXCollections.observableArrayList(this.supportLocale);
        cbxLanguage.setConverter(new StringConverter<Locale>() {
            @Override
            public String toString(Locale object) {
                return object.getDisplayLanguage(object);
            }

            @Override
            public Locale fromString(String string) {
                return null;
            }
        });
        cbxLanguage.setItems(languages);
        cbxLanguage.valueProperty().bindBidirectional(localeProperty);
        // endregion

        // region {主题}
        ObservableList<String> toasterStylesheets = this.service.getToasterFactory().getStylesheets();
        toasterStylesheets.add("/org/pomo/toasterfx/demo/types/CustomType.css");

        List<String> darkThemeStylesheets = new ArrayList<>(this.service.getDarkThemeStylesheets());
        darkThemeStylesheets.add("/org/pomo/toasterfx/demo/fxml/dark/ProgressToastNode.css");
        darkThemeStylesheets.add("/org/pomo/toasterfx/demo/types/dark/CustomType.css");

        SimpleListToastSupplier multiToastSupplier
                = (SimpleListToastSupplier) this.service.getMultiToastFactory().getMultiToastSupplier();
        ObservableList<String> windowStylesheets = multiToastSupplier.getWindowStylesheets();
        windowStylesheets.add("/org/pomo/toasterfx/demo/types/CustomType.css");

        this.themeInvalidationListener = theme -> {

            @SuppressWarnings("unchecked")
            RadioButton radioButton = ((ReadOnlyObjectProperty<RadioButton>) theme).get();

            ToasterFactory toasterFactory = this.service.getToasterFactory();
            toasterFactory.clear();

            ObservableList<String> stylesheets = this.primaryStage.getScene().getStylesheets();
            stylesheets.clear();

            if (radioButton == this.radioThemeDark) {

                stylesheets.add("/org/pomo/toasterfx/demo/fxml/dark/Demo.css");
                toasterStylesheets.addAll(darkThemeStylesheets);
                windowStylesheets.add(TableViewListToastController.DARK_THEME_STYLESHEETS);
                windowStylesheets.add("/org/pomo/toasterfx/demo/types/dark/CustomType.css");

            } else if (radioButton == this.radioThemeDefault) {

                toasterStylesheets.removeAll(darkThemeStylesheets);
                windowStylesheets.remove(TableViewListToastController.DARK_THEME_STYLESHEETS);
                windowStylesheets.remove("/org/pomo/toasterfx/demo/types/dark/CustomType.css");

            } else throw new IllegalArgumentException("unknown theme : " + radioButton.getText());

            this.service.success(
                    msg("demo.toastTitleSuccess"),
                    msg("demo.toastContentThemeSwitchedSuccess"));
        };
        this.radioTheme.selectedToggleProperty().addListener(this.themeInvalidationListener);
        // endregion

        // region {弹出策略}
        AtomicReference<PopupStrategy> popupStrategyReference = new AtomicReference<>();
        AtomicReference<Supplier<ToasterTransition>> transitionInSupplierReference = new AtomicReference<>();

        this.popupInvalidationListener = strategy -> {

            @SuppressWarnings("unchecked")
            RadioButton radioButton = ((ReadOnlyObjectProperty<RadioButton>) strategy).get();

            ToasterFactory toasterFactory = this.service.getToasterFactory();
            MultiToastFactory multiToastFactory = this.service.getMultiToastFactory();

            PopupStrategy popupStrategy
                    = popupStrategyReference.getAndSet(toasterFactory.getPopupStrategy());
            Supplier<ToasterTransition> transitionSupplier
                    = transitionInSupplierReference.getAndSet(toasterFactory.getTransitionInSupplier());

            if (radioButton == this.radioRightTop) {

                if (popupStrategy == null)
                    popupStrategy = new RightTopPopupStrategy(multiToastFactory, Duration.seconds(0.35));

                if (transitionSupplier == null)
                    transitionSupplier = () -> new RightTopToasterTransitionIn(Duration.seconds(0.4), 0.87);

            } else if (radioButton != this.radioTightBottom) {

                throw new IllegalArgumentException("unknown popup strategy : " + radioButton.getText());
            }

            toasterFactory.setTransitionInSupplier(transitionSupplier);
            toasterFactory.setPopupStrategy(popupStrategy);

            toasterFactory.clear();

            this.service.success(
                    msg("demo.toastTitleSuccess"),
                    msg("demo.toastContentPopupSwitchedSuccess"));
        };
        this.radioPopup.selectedToggleProperty().addListener(this.popupInvalidationListener);

        if (FXUtils.getVisualBounds().getMinY() > 0) {// 任务栏在上，切换默认弹出
            this.radioPopup.selectToggle(this.radioRightTop);
        } else this.radioPopup.selectToggle(this.radioTightBottom);
        // endregion

        this.bindTextProperty(
                labLanguage, labTheme, labPopup, labOperate,
                radioRightTop, radioTightBottom, radioThemeDefault, radioThemeDark,
                btnSuccess, btnFail, btnInfo, btnWarn,
                btnNoProgress, btnNoClose, btnNoTitle, btnNoAudio,
                btnCustomFXML, btnCustomType, btnCustomAudio,
                btnLong, btnEvent, btnBatch, btnConcurrent
        );

        this.noAudioParameter = ToastParameter.builder().timeout(defaultDuration).build();
        SingleAudio customAudio
                = new SingleAudio(this.getClass().getResource("/org/pomo/toasterfx/demo/audio/custom.mp3"));
        this.customAudioParameter
                = ToastParameter.builder().audio(customAudio).timeout(defaultDuration).build();
        this.randomAudioIndefiniteParameter
                = ToastParameter.builder().audio(randomBubbleAudio).timeout(Duration.INDEFINITE).build();
    }

    @FXML
    public void doNoAudio() {
        this.service.bomb(msg("demo.toastTitleInfo"), msg("demo.toastContentNoAudio"),
                noAudioParameter, ToastTypes.INFO);
    }

    @FXML
    public void doCustomAudio() {
        this.service.bomb(msg("demo.toastTitleInfo"), msg("demo.toastContentCustomAudio"),
                this.customAudioParameter, ToastTypes.INFO);
    }

    @FXML
    public void doCustomType() {

        SingleToast toast = new SingleToast(this.service.getDefaultToastParameter(), this.customToastType,
                msg("demo.toastContentCustomType"), (it) -> {
            Pane pane = new Pane();
            pane.getStyleClass().add("graphic");
            return new ToastBar(msg("demo.toastTitleCustomType"), msg("demo.toastContentCustomType"), pane);
        });

        this.service.push(toast);
    }

    @FXML
    public void doCustomFXML() {

        ProgressTask task = new ProgressTask(messages);

        CompletableFuture.runAsync(task);

        SingleToast singleToast = new SingleToast(this.randomAudioIndefiniteParameter,
                ToastTypes.INFO,
                msg("demo.customDigest"), toast -> {

            URL url = DemoController.class
                    .getResource("/org/pomo/toasterfx/demo/fxml/ProgressToastNode.fxml");

            return FXUtils.load(url,
                    (BiConsumer<VBox, ProgressToastController>) Node::setUserData);
        });

        singleToast
                .setOnClose((it, toast, node) -> {

                    if (task.isRunning()) {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle(msg("demo.alertTitleConfirmationClose"));
                        alert.setHeaderText(msg("demo.alertHeardTaskRunning"));
                        alert.setContentText(msg("demo.alertContentConfirmationClose"));
                        this.initDialog(alert);

                        task.pause();

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.orElse(ButtonType.CANCEL) == ButtonType.OK) {

                            // 再次判断是否仍在运行
                            if (task.isRunning()) task.cancel();

                            return true;

                        } else {

                            task.start();
                            return false;
                        }
                    }

                    return true;
                })
                .setOnDock((toast, node) -> {

                    ProgressToastController controller = (ProgressToastController) node.getUserData();
                    controller.bind(
                            task.titleProperty(), task.progressProperty(), task.buttonTextProperty(),
                            task.stateProperty(),
                            it -> task.action(toast)
                    );
                })
                .setOnUnDock((toast, node) -> {

                    ProgressToastController controller = (ProgressToastController) node.getUserData();
                    controller.unBind();
                })
                .setOnNodeDestroy((it, node) -> {
                    log.debug("Node of progress is destroyed.");
                    node.setUserData(null);
                })
                .setOnArchive((toast, node) -> ReferenceType.WEAK)
                .setOnNodeRecycle(it -> log.debug("Node of progress is recovery."))
                .setOnDestroy(it -> {

                    if (task.isRunning()) task.cancel();
                    task.destroy();
                });

        this.service.push(singleToast);
    }

    @FXML
    public void doConcurrent() {
        for (int i = 0; i < 20; i++) {

            final int index = concurrentIndex++;

            CompletableFuture.runAsync(() -> {

                String title = format("demo.toastTitleConcurrent", index);
                String content = format("demo.toastContentConcurrent", Thread.currentThread().getName());

                this.service.bomb(title, content, ToastTypes.INFO);
            });
        }
    }

    @FXML
    public void doBatch() {

        List<SingleToast> toasts = IntStream.range(0, 20).mapToObj(it -> {

            String title = format("demo.toastTitleBatch", batchIndex++);
            String content = format("demo.toastContentBatch", it + 1, 20);

            return this.service.born(title, content, ToastTypes.INFO);

        }).collect(Collectors.toList());

        this.service.push(toasts);
    }

    @FXML
    public void doEvent() {

        this.service.bomb(
                msg("demo.toastTitleEvent"), msg("demo.toastContentEvent"),
                ToastTypes.INFO,
                (event1, toast, node) -> {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, msg("demo.alertContentEvent"));
                    this.initDialog(alert);

                    alert.show();
                    return true;
                });
    }

    @FXML
    public void doFail() {
        this.service.bomb(msg("demo.toastTitleFail"), msg("demo.toastContentFail"),
                ToastTypes.FAIL);
    }

    @FXML
    public void doLong() {
        this.service.bomb(msg("demo.toastTitleLong"), msg("demo.toastContentLong"),
                ToastTypes.INFO);
    }

    @FXML
    public void doWarn() {
        this.service.bomb(msg("demo.toastTitleWarn"), msg("demo.toastContentWarn"),
                ToastTypes.WARN);
    }

    @FXML
    public void doInfo() {
        this.service.bomb(msg("demo.toastTitleInfo"), msg("demo.toastContentInfo"),
                ToastTypes.INFO);
    }

    @FXML
    public void doSuccess() {
        this.service.bomb(msg("demo.toastTitleSuccess"), msg("demo.toastContentSuccess"),
                ToastTypes.SUCCESS);
    }

    @FXML
    public void doNoClose() {
        this.service.bomb(msg("demo.toastTitleSuccess"), msg("demo.toastContentSuccess"),
                ToastTypes.SUCCESS, toast -> toast.setIsShowClose(false));
    }

    @FXML
    public void doNoProgress() {

        this.service.bomb(msg("demo.toastTitleFail"), msg("demo.toastContentNoProgress"),
                this.randomAudioIndefiniteParameter, ToastTypes.FAIL);
    }

    @FXML
    public void doNoTitle() {
        this.service.bomb(null, msg("demo.toastContentNoTitle"),
                ToastTypes.INFO);
    }

    /**
     * <h2>得到国际化文本</h2>
     *
     * @param key 键值
     * @return 国际化文本
     */
    private String msg(String key) {
        return this.messages.get(key);
    }

    /**
     * <h2>得到 格式化值</h2>
     *
     * @param key  键值
     * @param args 参数
     * @return 格式化值
     */
    private String format(String key, Object... args) {
        return this.messages.format(key, args);
    }

    /**
     * <h2>初始化对话框</h2>
     *
     * @param alert 对话框
     */
    private void initDialog(Alert alert) {

        Scene scene = alert.getDialogPane().getScene();

        ((Stage) scene.getWindow()).getIcons().addAll(primaryStage.getIcons());

        if (this.radioTheme.getSelectedToggle() == this.radioThemeDark) {
            scene.getStylesheets().add("/org/pomo/toasterfx/demo/fxml/dark/DialogPane.css");
        }
    }

    /**
     * <h2>绑定文本属性</h2>
     *
     * @param labeledArray labeled 数组
     */
    private void bindTextProperty(Labeled... labeledArray) {
        for (Labeled labeled : labeledArray) {
            this.messages.bindProperty(this, labeled.textProperty(), "demo." + labeled.getId());
        }
    }

    /**
     * <h2>销毁</h2>
     */
    public void destroy() {

        this.radioTheme.selectedToggleProperty().removeListener(this.themeInvalidationListener);
        this.themeInvalidationListener = null;

        this.radioPopup.selectedToggleProperty().removeListener(this.popupInvalidationListener);
        this.popupInvalidationListener = null;

        this.messages.disposeBinging(this);
    }
}
