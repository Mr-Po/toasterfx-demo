package org.pomo.toasterfx.demo.test;

import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

public class ATest {

    @Test
    public void t01() {
        System.out.println(ResourceBundle.getBundle("org.pomo.toasterfx.demo.language.message", Locale.ENGLISH));
    }

    @Test
    public void t02() {
        System.out.println(ResourceBundle.getBundle("org.pomo.toasterfx.demo.language.message", Locale.US));
    }
}
