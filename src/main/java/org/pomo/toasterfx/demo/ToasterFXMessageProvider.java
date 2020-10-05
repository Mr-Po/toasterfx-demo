package org.pomo.toasterfx.demo;

import org.pomo.toasterfx.ToasterService;
import org.pomo.toasterfx.util.FXMessageProvider;

import java.util.Locale;
import java.util.ResourceBundle;

public class ToasterFXMessageProvider implements FXMessageProvider {

    @Override
    public ResourceBundle getBundle(String baseName, Locale locale) {

        if (ToasterService.DEFAULT_BASE_NAME.equals(baseName) && Locale.JAPAN == locale)
            return ResourceBundle.getBundle("org.pomo.toasterfx.demo.extend.language.Message", locale);

        return null;
    }
}
